package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.adapters.CollectionStorageAdapter;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadStoragesTask extends AsyncTask<Void, Void, Void> {

    private TextView emptyText;
    private RecyclerView recyclerView;
    private CollectionStorageAdapter adapter;
    private Collections col;
    private List<CollectionStorage> storages;
    private ProgressDialog progress;


    public LoadStoragesTask(Collections col, RecyclerView recyclerView, TextView emptyText, List<CollectionStorage> storages, CollectionStorageAdapter adapter) {

        this.col = col;
        this.recyclerView = recyclerView;
        this.emptyText = emptyText;
        this.storages = storages;
        this.adapter = adapter;
        col.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(col);
        progress.setMessage("Loading Collections...");
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        CollectionsContract contract = new CollectionsContract(col);
        storages.clear();
        storages = contract.getCollectionStorages();
        adapter = new CollectionStorageAdapter(col, storages);

        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {

        if(adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);

        col.getApp().unregister(this);
        progress.dismiss();
    }
}
