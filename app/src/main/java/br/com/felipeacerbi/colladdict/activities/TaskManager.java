package br.com.felipeacerbi.colladdict.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;

import br.com.felipeacerbi.colladdict.app.CollectionsApplication;

/**
 * Created by felipe.acerbi on 17/02/2016.
 */
public interface TaskManager {

    AppCompatActivity getAppCompatActivity();

    CollectionsApplication getApp();

    ActionMode getActionMode();

    boolean isActionMode();

    ActionMode.Callback getActionModeCallback();
}
