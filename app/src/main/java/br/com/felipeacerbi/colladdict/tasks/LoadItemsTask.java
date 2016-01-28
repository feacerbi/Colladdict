package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.adapters.CollectionItemsAdapter;
import br.com.felipeacerbi.colladdict.adapters.CollectionStoragesAdapter;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.fragments.CollectionStorageFragment;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadItemsTask extends AsyncTask<Void, Void, CollectionItemsAdapter> {

    private TextView emptyText;
    private RecyclerView recyclerView;
    AppCompatActivity aca;
    private CollectionStorageFragment fragment;
    private List<CollectionItem> items;
    private ProgressDialog progress;
    private CollectionStorage storage;


    public LoadItemsTask(AppCompatActivity aca, RecyclerView recyclerView, TextView emptyText, CollectionStorage storage) {

        this.aca = aca;
        this.recyclerView = recyclerView;
        this.emptyText = emptyText;
        this.storage = storage;
        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(aca);
        progress.setMessage("Loading Items...");
        progress.show();
    }

    @Override
    protected CollectionItemsAdapter doInBackground(Void... voids) {

        if(storage.getId() != -1) {
            CollectionsContract contract = new CollectionsContract(aca);
            items = contract.getCollectionItems(storage.getId());
        } else {
            items = new ArrayList<>();
        }

        CollectionItemsAdapter adapter = new CollectionItemsAdapter(aca, items);

        return adapter;
    }

    @Override
    protected void onPostExecute(CollectionItemsAdapter adapter) {

        if(adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);

                ((CollectionsApplication) aca.getApplication()).unregister(this);
        progress.dismiss();
    }
}
