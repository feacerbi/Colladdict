package br.com.felipeacerbi.colladdict.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.activities.NewItemActivity;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertCategoryTask;
import br.com.felipeacerbi.colladdict.tasks.LoadCategoriesTask;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class ItemUIHelper {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/Colladdict/";

    private NewItemActivity nia;
    private EditText titleField;
    private EditText descField;
    private ImageView photo;
    private String path;
    private String extPath;
    private long id;
    private boolean isModify;
    private TextView saveButton;
    private CollectionStorage storage;
    private LinearLayout photoButton;

    public ItemUIHelper(NewItemActivity nia) {

        this.nia = nia;

        getInfo();
        checkFolder();

        isModify = getModify();
        if(isModify) {
            saveButton.setText("UPDATE");
        }
    }

    public void getInfo() {

        saveButton = (TextView) nia.findViewById(R.id.save_button);
        titleField = (EditText) nia.findViewById(R.id.collection_title);
        descField = (EditText) nia.findViewById(R.id.collection_description);
        photo = (ImageView) nia.findViewById(R.id.collection_photo);

        photoButton = (LinearLayout) nia.findViewById(R.id.button_edit_item_image);
        nia.registerForContextMenu(photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nia.openContextMenu(v);
            }
        });
    }

    public boolean checkFolder() {
        File defaultFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), "Colladdict");

        if(!defaultFolder.exists()) {
            return defaultFolder.mkdir();
        }

        return false;
    }

    public boolean getModify() {

        CollectionItem item = (CollectionItem) nia.getIntent().getSerializableExtra("collection_item");
        storage = (CollectionStorage) nia.getIntent().getSerializableExtra("collection_storage");

        if(item != null) {

            id = item.getId();
            titleField.setText(item.getTitle());
            descField.setText(item.getDescription());

            if(item.getPhotoPath() != null) {
                setPhoto(item.getPhotoPath());
            }

            return true;
        }

        return false;
    }

    public CollectionItem getCollectionItem() {

        CollectionItem item = new CollectionItem();
        item.setId(id);
        item.setTitle(titleField.getText().toString());
        item.setDescription(descField.getText().toString());
        item.setPhotoPath(getPath());
        item.setStorageId(storage.getId());

        return item;

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
        Bitmap bmp = BitmapFactory.decodeFile(getPath());
        photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true));
    }

    public String getBitmapPath(Intent data){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = nia.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
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
