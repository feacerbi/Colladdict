package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertStorageTask extends AsyncTask<CollectionStorage, Void, Void> {

    AppCompatActivity aca;

    public InsertStorageTask(AppCompatActivity aca) {

        this.aca = aca;

        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected Void doInBackground(CollectionStorage... storages) {

        CollectionsContract contract = new CollectionsContract(aca);

        contract.insertCollectionStorage(storages[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((CollectionsApplication) aca.getApplication()).unregister(this);
    }
}
