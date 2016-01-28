package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class RemoveItemsTask extends AsyncTask<List<CollectionItem>, Void, Void> {

    AppCompatActivity aca;

    public RemoveItemsTask(AppCompatActivity aca) {

        this.aca = aca;

        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected Void doInBackground(List<CollectionItem>... items) {

        CollectionsContract contract = new CollectionsContract(aca);

        for(CollectionItem item : items[0]) {
            contract.deleteCollectionItem(item.getId());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((CollectionsApplication) aca.getApplication()).unregister(this);
    }
}
