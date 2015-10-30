package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertStorageTask;
import br.com.felipeacerbi.colladdict.ui.InsertStorageUIHelper;

public class NewCollectionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveButton;
    private InsertStorageUIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);

        uiHelper = new InsertStorageUIHelper(this);
    }

    @Override
    public void onContentChanged() {
        getWindow().setEnterTransition(new Slide());
        saveButton = (TextView) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionStorage storage = uiHelper.getCollectionStorage();
                new InsertStorageTask(NewCollectionActivity.this).execute(storage);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("collection_storage", storage);
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });
        super.onContentChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbar("New Collection");
    }

    public void setToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
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
            case R.id.action_edit_photo:
                return true;
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
