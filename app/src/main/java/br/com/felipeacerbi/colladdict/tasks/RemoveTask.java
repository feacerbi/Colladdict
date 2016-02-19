package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import java.util.List;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class RemoveTask extends AsyncTask<List, Void, Void> {

    TaskManager tm;

    public RemoveTask(TaskManager tm) {

        this.tm = tm;

        tm.getApp().register(this);
    }

    @Override
    protected Void doInBackground(List... objects) {

        CollectionsContract contract = new CollectionsContract(tm.getAppCompatActivity());

        for(Object remove : objects[0]) {
            if (remove instanceof CollectionStorage) {
                contract.deleteCollectionStorage(((CollectionStorage) remove).getId());
            } else if (remove instanceof CollectionItem) {
                contract.deleteCollectionItem(((CollectionItem) remove).getId());
            } else if (remove instanceof Category) {
                contract.deleteCategory(((Category) remove).getId());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        tm.getApp().unregister(this);
    }
}
