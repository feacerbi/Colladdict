package br.com.felipeacerbi.colladdict.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertTask;
import br.com.felipeacerbi.colladdict.tasks.LoadCategoriesTask;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class StorageUIHelper {

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
    private LinearLayout photoButton;
    private String extPath;

    public StorageUIHelper(NewCollectionActivity nca) {

        this.nca = nca;

        getInfo();

        isModify = getModify();
        if(isModify) {
            saveButton.setText("UPDATE");
            categoryField.setText(category.getTitle());
        } else {
            categoryField.setText("Default");
        }
    }

    public void getInfo() {

        saveButton = (TextView) nca.findViewById(R.id.save_button);
        titleField = (EditText) nca.findViewById(R.id.collection_title);
        descField = (EditText) nca.findViewById(R.id.collection_description);
        photo = (ImageView) nca.findViewById(R.id.collection_photo);

        photoButton = (LinearLayout) nca.findViewById(R.id.button_edit_collection_image);
        nca.registerForContextMenu(photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nca.openContextMenu(v);
            }
        });
        
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
                newCategoryField.setHint(nca.getResources().getString(R.string.new_category_hint));
                newCategoryField.requestFocus();

                alertDialog.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newCategoryName = newCategoryField.getText().toString();
                        if (!newCategoryName.isEmpty()) {
                            category = new Category(newCategoryName);
                            new InsertTask(nca, false).execute(category);
                            categoryField.setText(category.getTitle());
                            categoryField.setTag(category);
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
            categoryField.setTag(category);

            if(storage.getPhotoPath() != null) {
                setPhoto(storage.getPhotoPath());
            }

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

    public String getExtPath() {
        return extPath;
    }

    public void setExtPath(String extPath) {
        this.extPath = extPath;
    }

    public void setPhoto(String path) {
        setPath(path);

        Glide.with(nca)
                .load(getPath())
                .centerCrop()
                .error(R.drawable.shells)
                .into(photo);
    }

    public String getBitmapPath(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = nca.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }

    public boolean isModify() {
        return isModify;
    }

}
