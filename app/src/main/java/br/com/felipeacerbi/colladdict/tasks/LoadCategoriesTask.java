package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.ListItem;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadCategoriesTask extends AsyncTask<Void, Void, Void> {

    private ListView categoryList;
    private NewCollectionActivity nca;
    private List<ListItem> categories;
    private ProgressDialog progress;


    public LoadCategoriesTask(NewCollectionActivity nca, ListView categotyList) {

        this.nca = nca;
        this.categoryList = categotyList;
        nca.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(nca);
        progress.setMessage("Loading Categories...");
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        CollectionsContract contract = new CollectionsContract(nca);
        categories = contract.getCategories();

        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {

        categoryList.setAdapter(new DialogListAdapter(nca, categories));

        progress.dismiss();
    }
}
