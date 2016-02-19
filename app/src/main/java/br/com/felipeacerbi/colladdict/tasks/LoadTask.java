package br.com.felipeacerbi.colladdict.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.adapters.CategoriesAdapter;
import br.com.felipeacerbi.colladdict.adapters.CollectionItemsAdapter;
import br.com.felipeacerbi.colladdict.adapters.CollectionStoragesAdapter;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.fragments.CollectionStorageFragment;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class LoadTask extends AsyncTask<Void, Void, RecyclerView.Adapter> {

    private final int type;
    private Object object;
    private TextView emptyText;
    private RecyclerView recyclerView;
    private TaskManager tm;
    private ProgressDialog progress;


    public LoadTask(TaskManager tm, RecyclerView recyclerView, TextView emptyText, int type, Object object) {

        this.tm = tm;
        this.recyclerView = recyclerView;
        this.emptyText = emptyText;
        this.type = type;
        this.object = object;
        tm.getApp().register(this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(tm.getAppCompatActivity());
        progress.setMessage("Loading Collections...");
        progress.show();
    }

    @Override
    protected RecyclerView.Adapter doInBackground(Void... voids) {

        CollectionsContract contract = new CollectionsContract(tm.getAppCompatActivity());
        RecyclerView.Adapter adapter = null;

        switch (type) {
            case Collections.LOAD_COLLECTION_STORAGES:
                List<CollectionStorage> storages = contract.getCollectionStorages();
                adapter = new CollectionStoragesAdapter(tm, storages);
                break;
            case Collections.LOAD_COLLECTION_ITEMS:
                if(object instanceof CollectionStorage) {
                    CollectionStorage storage = (CollectionStorage) object;
                    List<CollectionItem> items;
                    if (storage.getId() != -1) {
                        items = contract.getCollectionItems(storage.getId());
                    } else {
                        items = new ArrayList<>();
                    }
                    adapter = new CollectionItemsAdapter(tm, items, storage);
                }
                break;
            case Collections.LOAD_CATEGORIES:
                List<Category> categories = contract.getCategories();
                adapter = new CategoriesAdapter(tm, categories);
                break;
            default:

        }

        return adapter;
    }

    @Override
    protected void onPostExecute(RecyclerView.Adapter adapter) {

        if(adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);

        tm.getApp().unregister(this);
        progress.dismiss();
    }
}
