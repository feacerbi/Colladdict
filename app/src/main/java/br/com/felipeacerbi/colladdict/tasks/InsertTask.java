package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;

import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertTask extends AsyncTask<Object, Void, Void> {

    private final boolean isModify;
    TaskManager tm;

    public InsertTask(TaskManager tm, boolean isModify) {

        this.tm = tm;
        this.isModify = isModify;

        tm.getApp().register(this);
    }

    @Override
    protected Void doInBackground(Object... objects) {

        CollectionsContract contract = new CollectionsContract(tm.getAppCompatActivity());
        Object insert = objects[0];

        if(insert instanceof CollectionStorage) {
            CollectionStorage storage = (CollectionStorage) insert;
            if (isModify) {
                contract.updateCollectionStorage(storage);
            } else {
                storage.setId(contract.insertCollectionStorage(storage));
            }
        } else if (insert instanceof CollectionItem) {
            CollectionItem item = (CollectionItem) insert;
            if(isModify) {
                contract.updateCollectionItem((CollectionItem) insert);
            } else {
                item.setId(contract.insertCollectionItem(item));
            }
        } else if(insert instanceof Category) {
            Category category = (Category) insert;
            if(isModify) {
                contract.updateCategory(category);
            } else {
                category.setId(contract.insertCategory(category));
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        tm.getApp().unregister(this);
    }
}
