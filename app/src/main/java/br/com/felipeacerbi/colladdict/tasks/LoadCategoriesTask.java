package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.adapters.DialogListAdapter;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.ListItem;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadCategoriesTask extends AsyncTask<Void, Void, Cursor> {

    private final TextView categoryField;
    private AppCompatActivity aca;
    private ProgressDialog progress;
    private Category selectedCategory;
    private int selectedPosition;
    private CollectionsContract contract;

    public LoadCategoriesTask(AppCompatActivity aca, TextView categoryField) {

        this.aca = aca;
        this.categoryField = categoryField;
        contract = new CollectionsContract(aca);
        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(aca);
        progress.setMessage("Loading Categories...");
        progress.show();
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        return contract.getCategoriesCursor();
    }

    @Override
    protected void onPostExecute(final Cursor cursor) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(aca);
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

        ((CollectionsApplication) aca.getApplication()).unregister(this);
        progress.dismiss();
    }
}
