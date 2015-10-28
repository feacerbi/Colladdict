package br.com.felipeacerbi.colladdict.activities;

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

public class NewCollectionActivity extends AppCompatActivity {

    private Spinner categoriesSpinner;
    private EditText titleField;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
    }

    @Override
    public void onContentChanged() {
        getWindow().setEnterTransition(new Slide());
        categoriesSpinner = (Spinner) findViewById(R.id.collection_category);
        titleField = (EditText) findViewById(R.id.collection_title);
        titleField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toolbar.setTitle(titleField.getText().toString());
            }
        });
        categoriesSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_item));
        super.onContentChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbar("");
    }

    public void setToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Back!", Toast.LENGTH_SHORT).show();
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
