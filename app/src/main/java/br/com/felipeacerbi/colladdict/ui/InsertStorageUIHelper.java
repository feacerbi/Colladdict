package br.com.felipeacerbi.colladdict.ui;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertCategoryTask;
import br.com.felipeacerbi.colladdict.tasks.LoadCategoriesTask;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertStorageUIHelper {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/Colladdict/";

    private NewCollectionActivity nca;
    private Category category;
    private EditText titleField;
    private EditText descField;
    private ImageView photo;
    private String path;
    private long id;
    private boolean isModify;
    private ImageView addCategoryButton;
    private TextView categoryField;
    private TextView saveButton;

    public InsertStorageUIHelper(NewCollectionActivity nca) {

        this.nca = nca;

        getInfo();

        isModify = getModify();
        if(isModify) {
            saveButton.setText("UPDATE");
            categoryField.setText(category.getTitle());
        }
    }

    public void getInfo() {

        saveButton = (TextView) nca.findViewById(R.id.save_button);
        titleField = (EditText) nca.findViewById(R.id.collection_title);
        descField = (EditText) nca.findViewById(R.id.collection_description);
        photo = (ImageView) nca.findViewById(R.id.collection_photo);
        categoryField = (TextView) nca.findViewById(R.id.collection_category);
        categoryField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LoadCategoriesTask(nca, categoryField).execute();

            }
        });
        addCategoryButton = (ImageView) nca.findViewById(R.id.add_category_icon);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(nca);
                View inputView = layoutInflater.inflate(R.layout.input_dialog_view, null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(nca)
                        .setView(inputView);
                alertDialog.create();

                final EditText newCategoryField = (EditText) inputView.findViewById(R.id.new_category);
                newCategoryField.setHint("Category Name");
                newCategoryField.requestFocus();

                alertDialog.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newCategoryName = newCategoryField.getText().toString();
                        if (!newCategoryName.isEmpty()) {
                            category = new Category(newCategoryName);

                            new InsertCategoryTask(nca).execute(category);
                            categoryField.setText(category.getTitle());
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Cancel Action
                            }
                        })
                        .setTitle("Add Category")
                        .show();
            }
        });

        category = null;
    }

    public boolean checkFolder() {

        File defaultFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), "Colladdict");

        if(!defaultFolder.exists()) {
            return defaultFolder.mkdir();
        }

        return false;
    }

    public boolean getModify() {

        CollectionStorage storage = (CollectionStorage) nca.getIntent().getSerializableExtra("collection_storage");

        if(storage != null) {

            id = storage.getId();
            titleField.setText(storage.getTitle());
            descField.setText(storage.getDescription());
            category = storage.getCategory();

            return true;
        }

        return false;
    }

    public CollectionStorage getCollectionStorage() {

        CollectionStorage storage = new CollectionStorage();
        storage.setId(id);
        storage.setTitle(titleField.getText().toString());
        if(categoryField.getTag() != null) {
            category = (Category) categoryField.getTag();
        }
        storage.setCategory(category);
        storage.setDescription(descField.getText().toString());
        storage.setPhotoPath(getPath());

        return storage;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // TODO Handle photo from camera/storage.
//    public void setPhoto() {
//        Ion.with(photo)
//                .placeholder(R.drawable.ic_contact_picture_big)
//                .error(R.drawable.ic_contact_picture_big)
//                .load(getPath());
//    }


    public boolean isModify() {
        return isModify;
    }

}
