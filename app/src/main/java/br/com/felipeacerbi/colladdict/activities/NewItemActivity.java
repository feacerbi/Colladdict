package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertItemTask;
import br.com.felipeacerbi.colladdict.tasks.InsertStorageTask;
import br.com.felipeacerbi.colladdict.ui.InsertItemUIHelper;
import br.com.felipeacerbi.colladdict.ui.InsertStorageUIHelper;

public class NewItemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView saveButton;
    private InsertItemUIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        uiHelper = new InsertItemUIHelper(this);

        setToolbar();
    }

    @Override
    public void onContentChanged() {
        getWindow().setEnterTransition(new Slide());
        saveButton = (TextView) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionItem item = uiHelper.getCollectionItem();

                new InsertItemTask(NewItemActivity.this, uiHelper.isModify()).execute(item);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("collection_item", item);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
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
