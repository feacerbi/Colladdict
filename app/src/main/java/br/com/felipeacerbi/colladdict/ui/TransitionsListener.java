package br.com.felipeacerbi.colladdict.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.widget.TextView;

import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.LoadTask;

/**
 * Created by felipe.acerbi on 25/02/2016.
 */
public class TransitionsListener implements Transition.TransitionListener {

    private final TaskManager tm;
    private final RecyclerView recyclerView;
    private final TextView emptyText;
    private final int type;
    private final Object object;

    public TransitionsListener(TaskManager tm, RecyclerView recyclerView, TextView emptyText, int type, Object object) {
        this.tm = tm;
        this.recyclerView = recyclerView;
        this.emptyText = emptyText;
        this.type = type;
        this.object = object;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        new LoadTask(tm, recyclerView, emptyText, type, object).execute();
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}
