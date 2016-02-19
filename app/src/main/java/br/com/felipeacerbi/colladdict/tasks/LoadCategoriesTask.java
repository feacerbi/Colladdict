package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadCategoriesTask extends AsyncTask<Void, Void, Cursor> {

    private final TextView categoryField;
    private TaskManager tm;
    private ProgressDialog progress;
    private Category selectedCategory;
    private int selectedPosition;
    private CollectionsContract contract;

    public LoadCategoriesTask(TaskManager tm, TextView categoryField) {

        this.tm = tm;
        this.categoryField = categoryField;
        contract = new CollectionsContract(tm.getAppCompatActivity());
        tm.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(tm.getAppCompatActivity());
        progress.setMessage("Loading Categories...");
        progress.show();
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        return contract.getCategoriesCursor();
    }

    @Override
    protected void onPostExecute(final Cursor cursor) {
        if(categoryField.getTag() != null) {
            Category category = (Category) categoryField.getTag();
            Log.d("Acerbi", "Id: " + category.getId());
            if(cursor.moveToFirst()) {
                do {
                    if(category.getId() == cursor.getLong(cursor.getColumnIndex(CollectionsContract.Categories._ID))) {
                        selectedPosition = cursor.getPosition();
                    }
                } while(cursor.moveToNext());
            }
        }
        Log.d("Acerbi", "Pos: " + selectedPosition);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(tm.getAppCompatActivity());
        alertBuilder.setSingleChoiceItems(cursor, selectedPosition, CollectionsContract.Categories.COLUMN_NAME_TITLE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            }
        }).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedCategory = contract.getCategory(cursor, selectedPosition);
                categoryField.setText(selectedCategory.getTitle());
                categoryField.setTag(selectedCategory);
            }
        })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel Action
                    }
                })
                .setTitle("Select Category")
                .create()
                .show();

        tm.getApp().unregister(this);
        progress.dismiss();
    }
}
