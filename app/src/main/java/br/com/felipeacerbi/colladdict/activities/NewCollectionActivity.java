package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.File;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertStorageTask;
import br.com.felipeacerbi.colladdict.ui.StorageUIHelper;

public class NewCollectionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveButton;
    private StorageUIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);

        setToolbar();

        uiHelper = new StorageUIHelper(this);
    }

    @Override
    public void onContentChanged() {
        getWindow().setEnterTransition(new Slide());
        saveButton = (TextView) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionStorage storage = uiHelper.getCollectionStorage();

                new InsertStorageTask(NewCollectionActivity.this, uiHelper.isModify()).execute(storage);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("collection_storage", storage);
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });
        super.onContentChanged();
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Collections.TAKE_PICTURE) {
                uiHelper.setPhoto(uiHelper.getExtPath());
            } else if(requestCode == Collections.BROWSE) {
                uiHelper.setPhoto(uiHelper.getBitmapPath(data));
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_select, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_camera:
                Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                uiHelper.setExtPath(getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg");
                cam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(uiHelper.getExtPath())));
                if(cam.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cam, Collections.TAKE_PICTURE);
                } else {
                    Snackbar.make(findViewById(R.id.coordinator), "No Camera app fuond", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_gallery:
                Intent gal = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(gal.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(gal, Collections.BROWSE);
                } else {
                    Snackbar.make(findViewById(R.id.coordinator), "No Gallery app fuond", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onBackPressed();
    }

    public CollectionsApplication getApp() {
        return (CollectionsApplication) getApplication();
    }
}
