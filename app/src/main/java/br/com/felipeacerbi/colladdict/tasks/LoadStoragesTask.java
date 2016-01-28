package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.adapters.CollectionStoragesAdapter;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.fragments.CollectionStorageFragment;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadStoragesTask extends AsyncTask<Void, Void, CollectionStoragesAdapter> {

    private TextView emptyText;
    private RecyclerView recyclerView;
    private Collections col;
    private CollectionStorageFragment fragment;
    private List<CollectionStorage> storages;
    private ProgressDialog progress;


    public LoadStoragesTask(CollectionStorageFragment fragment, RecyclerView recyclerView, TextView emptyText) {

        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.emptyText = emptyText;
        col = (Collections) fragment.getActivity();
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
    protected CollectionStoragesAdapter doInBackground(Void... voids) {

        CollectionsContract contract = new CollectionsContract(col);
        storages = contract.getCollectionStorages();
        CollectionStoragesAdapter adapter = new CollectionStoragesAdapter(fragment, storages);

        return adapter;
    }

    @Override
    protected void onPostExecute(CollectionStoragesAdapter adapter) {

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
