package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class RemoveStoragesTask extends AsyncTask<List<CollectionStorage>, Void, Void> {

    AppCompatActivity aca;

    public RemoveStoragesTask(AppCompatActivity aca) {

        this.aca = aca;

        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected Void doInBackground(List<CollectionStorage>... storages) {

        CollectionsContract contract = new CollectionsContract(aca);

        for(CollectionStorage storage : storages[0]) {
            contract.deleteCollectionStorage(storage.getId());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((CollectionsApplication) aca.getApplication()).unregister(this);
    }
}
