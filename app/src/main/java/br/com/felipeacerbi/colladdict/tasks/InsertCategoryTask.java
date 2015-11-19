package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertCategoryTask extends AsyncTask<Category, Void, Void> {

    AppCompatActivity aca;

    public InsertCategoryTask(AppCompatActivity aca) {

        this.aca = aca;

        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected Void doInBackground(Category... categories) {

        CollectionsContract contract = new CollectionsContract(aca);
        categories[0].setId(contract.insertCategory(categories[0]));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((CollectionsApplication) aca.getApplication()).unregister(this);
    }
}
