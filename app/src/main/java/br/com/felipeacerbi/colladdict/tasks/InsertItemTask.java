package br.com.felipeacerbi.colladdict.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.dbs.CollectionsContract;
import br.com.felipeacerbi.colladdict.models.CollectionItem;

/**
 * Created by felipe.acerbi on 30/10/2015.
 */
public class InsertItemTask extends AsyncTask<CollectionItem, Void, Void> {

    private final boolean isModify;
    AppCompatActivity aca;

    public InsertItemTask(AppCompatActivity aca, boolean isModify) {

        this.aca = aca;
        this.isModify = isModify;

        ((CollectionsApplication) aca.getApplication()).register(this);
    }

    @Override
    protected Void doInBackground(CollectionItem... items) {

        CollectionsContract contract = new CollectionsContract(aca);

        if(isModify) {
            contract.updateCollectionItem(items[0]);
        } else {
            contract.insertCollectionItem(items[0]);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((CollectionsApplication) aca.getApplication()).unregister(this);
    }
}
