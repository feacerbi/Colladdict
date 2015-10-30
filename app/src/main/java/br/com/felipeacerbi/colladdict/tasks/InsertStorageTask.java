package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;

import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertStorageTask extends AsyncTask<CollectionStorage, Void, Void> {

    NewCollectionActivity nca;

    public InsertStorageTask(NewCollectionActivity nca) {

        this.nca = nca;

        nca.getApp().register(this);
    }

    @Override
    protected Void doInBackground(CollectionStorage... storages) {

        CollectionsContract contract = new CollectionsContract(nca);

        storages[0].setId(contract.insertCollectionStorage(storages[0]));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        nca.getApp().unregister(this);
    }
}
