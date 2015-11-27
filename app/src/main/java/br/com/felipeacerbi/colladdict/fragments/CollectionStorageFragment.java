package br.com.felipeacerbi.colladdict.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.adapters.CollectionStorageAdapter;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertStorageTask;
import br.com.felipeacerbi.colladdict.tasks.LoadStoragesTask;
import br.com.felipeacerbi.colladdict.tasks.RemoveStorageTask;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionStorageFragment extends Fragment implements ActionMode.Callback {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private CollectionStorageAdapter collectionStorageAdapter;
    private TextView emptyText;
    private FloatingActionButton fab;
    private MenuItem layoutMenuItem;
    private boolean isActionMode;
    private ActionMode actionMode;
    private List<CollectionStorage> deleteList;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private LayoutManagerType currentLayoutManagerType;

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
    public void onResume() {
        super.onResume();
        reload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View collectionsList = inflater.inflate(R.layout.fragment_collection_storage, container, false);

        recyclerView = (RecyclerView) collectionsList.findViewById(R.id.all_collections);
        emptyText = (TextView) collectionsList.findViewById(R.id.empty_text);
        fab = (FloatingActionButton) collectionsList.findViewById(R.id.fab);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewCollectionActivity.class);
                getActivity().startActivityForResult(intent, Collections.REQUEST_NEW_COLLECTION_STORAGE);
            }
        });

        layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        setRecyclerViewLayoutManager(currentLayoutManagerType);

        reload();

        return collectionsList;
    }

    public void reload() {
        new LoadStoragesTask(this, recyclerView, emptyText).execute();
    }

    public void reloadAndScroll() {
        new LoadStoragesTask(this, recyclerView, emptyText).execute();

        collectionStorageAdapter = (CollectionStorageAdapter) recyclerView.getAdapter();
        // TODO Fix scroll to position.
        int scrollPosition = 0;
        scrollPosition = (collectionStorageAdapter.getItemCount() % 2 == 0) ?
                collectionStorageAdapter.getItemCount() / 2 :
                (collectionStorageAdapter.getItemCount() / 2) + 1;
        recyclerView.scrollToPosition(scrollPosition);
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

        collectionStorageAdapter = (CollectionStorageAdapter) recyclerView.getAdapter();
        actionMode = mode;

        mode.setTitle(String.valueOf(collectionStorageAdapter.getSelectedItemsCount()));
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
                deleteList = collectionStorageAdapter.getSelectedItems();
                new RemoveStorageTask((Collections) getActivity()).execute(deleteList);
                new LoadStoragesTask(this, recyclerView, emptyText).execute();
                Snackbar.make(getView().findViewById(R.id.coordinator), "Collections removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (CollectionStorage storage : deleteList) {
                                    new InsertStorageTask((Collections) getActivity(), false).execute(storage);
                                }
                                reloadAndScroll();
                            }
                        }).show();
                reloadAndScroll();
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
        collectionStorageAdapter.deselectAll();
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

    public ActionMode getActionMode() {
        return actionMode;
    }

    public boolean isActionMode() {
        return isActionMode;
    }

    private void createDemoStorage() {
        CollectionStorage storage = new CollectionStorage();
        storage.setTitle("Vodkas");
        storage.setDescription("Collection description.");
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        collectionStorageAdapter.getStorages().add(storage);

        CollectionStorage storage2 = new CollectionStorage();
        storage2.setTitle("Bottle Caps");
        storage2.setDescription("Collection description.");
        collectionStorageAdapter.getStorages().add(storage2);

        CollectionStorage storage3 = new CollectionStorage();
        storage3.setTitle("Shells");
        storage3.setDescription("Collection description.");
        storage3.setPhotoPath("3");
        collectionStorageAdapter.getStorages().add(storage3);

        CollectionStorage storage4 = new CollectionStorage();
        storage4.setTitle("CD's");
        storage4.setDescription("Collection description.");
        storage4.setPhotoPath("4");
        collectionStorageAdapter.getStorages().add(storage4);
    }

    private void createDemoItem(CollectionStorage storage) {
        CollectionItem item = new CollectionItem();
        item.setTitle("Absolut");
        item.setDescription("Sweden vodka.");
        storage.addItem(item);
    }
}