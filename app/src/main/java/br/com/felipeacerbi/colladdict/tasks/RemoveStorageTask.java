package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class RemoveStorageTask extends AsyncTask<List<CollectionStorage>, Void, Void> {

    Collections col;

    public RemoveStorageTask(Collections col) {

        this.col = col;

        col.getApp().register(this);
    }

    @Override
    protected Void doInBackground(List<CollectionStorage>... storages) {

        CollectionsContract contract = new CollectionsContract(col);

        for(CollectionStorage storage : storages[0]) {
            Log.d("RemoveStorage", ""+storage.getId());
            contract.deleteCollectionStorage(storage.getId());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        col.getApp().unregister(this);
    }
}
