package br.com.felipeacerbi.colladdict.ui;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertStorageUIHelper {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/Colladdict/";

    private NewCollectionActivity nca;
    private EditText titleField;
    private EditText descField;
    private Spinner categoryField;
    private ImageView photo;
    private String path;
    private long id;

    public InsertStorageUIHelper(NewCollectionActivity nca) {

        this.nca = nca;

        getInfo();

        // TODO Modify Storage.
//        if(getModify()) {
//            ((TextView) nca.findViewById(R.id.save_button)).setText("MODIFY");
//        }
    }

    public void getInfo() {

        titleField = (EditText) nca.findViewById(R.id.collection_title);
        descField = (EditText) nca.findViewById(R.id.collection_description);
        categoryField = (Spinner) nca.findViewById(R.id.collection_category);
        categoryField.setAdapter(ArrayAdapter.createFromResource(nca, R.array.categories, R.layout.spinner_item));
        photo = (ImageView) nca.findViewById(R.id.collection_photo);
    }

    public boolean checkFolder() {

        File defaultFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), "Colladdict");

        if(!defaultFolder.exists()) {
            return defaultFolder.mkdir();
        }

        return false;
    }

    public boolean getModify() {

        CollectionStorage storage = (CollectionStorage) nca.getIntent().getSerializableExtra("storage");

        if(storage != null) {

            id = storage.getId();
            titleField.setText(storage.getTitle());
            descField.setText(storage.getDescription());
            categoryField.setSelection(storage.getCategory(), true);

            return true;
        }

        return false;
    }

    public CollectionStorage getCollectionStorage() {

        CollectionStorage storage = new CollectionStorage();
        storage.setId(id);
        storage.setTitle(titleField.getText().toString());
        storage.setCategory(categoryField.getSelectedItemPosition());
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

}
