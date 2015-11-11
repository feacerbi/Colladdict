package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class RemoveCategoryTask extends AsyncTask<List<Category>, Void, Void> {

    Collections col;

    public RemoveCategoryTask(Collections col) {

        this.col = col;

        col.getApp().register(this);
    }

    @Override
    protected Void doInBackground(List<Category>... categories) {

        CollectionsContract contract = new CollectionsContract(col);

        for(Category category : categories[0]) {
            contract.deleteCategory(category.getId());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        col.getApp().unregister(this);
    }
}
