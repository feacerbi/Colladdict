package br.com.felipeacerbi.colladdict.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import br.com.felipeacerbi.colladdict.Constants;
import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.adapters.CollectionStoragesAdapter;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.LoadTask;
import br.com.felipeacerbi.colladdict.tasks.RemoveTask;
import br.com.felipeacerbi.colladdict.ui.TransitionsListener;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionStorageFragment extends Fragment implements ActionMode.Callback, TaskManager {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private CollectionStoragesAdapter collectionStoragesAdapter;
    private TextView emptyText;
    private FloatingActionButton fab;
    private MenuItem layoutMenuItem;
    private boolean isActionMode;
    private ActionMode actionMode;
    private List<CollectionStorage> deleteList;
    private LayoutManagerType currentLayoutManagerType;
    private boolean remove = false;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGERED_LAYOUT_MANAGER
    }

    public static CollectionStorageFragment newInstance() {
        CollectionStorageFragment fragment = new CollectionStorageFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CollectionStorageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActionMode = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setEnterTransition(new Fade());

        View collectionsList = inflater.inflate(R.layout.fragment_collections, container, false);

        recyclerView = (RecyclerView) collectionsList.findViewById(R.id.all_collections);
        emptyText = (TextView) collectionsList.findViewById(R.id.empty_text);
        fab = (FloatingActionButton) collectionsList.findViewById(R.id.fab);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewCollectionActivity.class);
                getActivity().startActivityForResult(intent, Constants.REQUEST_NEW_COLLECTION_STORAGE);
            }
        });

        currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        setRecyclerViewLayoutManager(currentLayoutManagerType);

        return collectionsList;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadTask(this, recyclerView, emptyText, Constants.LOAD_COLLECTION_STORAGES, fab).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.show();
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                if(layoutMenuItem != null) layoutMenuItem.setIcon(R.drawable.ic_view_stream_white_24dp);
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                if(layoutMenuItem != null) layoutMenuItem.setIcon(R.drawable.ic_view_module_white_24dp);
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
     public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.collections_context_menu, menu);

        collectionStoragesAdapter = (CollectionStoragesAdapter) recyclerView.getAdapter();
        actionMode = mode;

        mode.setTitle(String.valueOf(collectionStoragesAdapter.getSelectedItemsCount()));
        isActionMode = true;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Deprecated method for newer versions.
//       context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorAccentDark, null));
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorAccentDark));
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_collection:
                deleteList = collectionStoragesAdapter.getSelectedItems();
                collectionStoragesAdapter.notifyItemsRemoved();
                remove = true;
                Snackbar.make(getView().findViewById(R.id.coordinator), "Collections removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                collectionStoragesAdapter.notifyItemsInserted(deleteList);
                                remove = false;
                            }
                        }).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(remove) {
                            new RemoveTask(CollectionStorageFragment.this).execute(deleteList);
                        }
                        super.onDismissed(snackbar, event);
                    }
                }).show();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Deprecated method for newer versions.
//        context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        collectionStoragesAdapter.deselectAll();
        isActionMode = false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collections, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_change_layout:
                layoutMenuItem = item;
                currentLayoutManagerType = (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) ?
                        LayoutManagerType.GRID_LAYOUT_MANAGER : LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                setRecyclerViewLayoutManager(currentLayoutManagerType);
                return true;
            case R.id.action_settings:
                return true;
        }

        return true;
    }

    @Override
    public ActionMode.Callback getActionModeCallback() {
        return this;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public CollectionsApplication getApp() {
        return (CollectionsApplication) getAppCompatActivity().getApplication();
    }

    @Override
    public ActionMode getActionMode() {
        return actionMode;
    }

    @Override
    public boolean isActionMode() {
        return isActionMode;
    }

    public CollectionStoragesAdapter getCollectionStoragesAdapter() {
        return (CollectionStoragesAdapter) recyclerView.getAdapter();
    }
}