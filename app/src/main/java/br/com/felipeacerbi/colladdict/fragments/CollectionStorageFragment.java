package br.com.felipeacerbi.colladdict.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.CollectionItemsActivity;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.adapters.CollectionStorageAdapter;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionStorageFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CollectionStorageAdapter collectionStorageAdapter;

    private List<CollectionStorage> storages;
    private TextView emptyText;
    private FloatingActionButton fab;
    private MenuItem layoutMenuItem;

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
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storages = new ArrayList<>();

        createDemoStorage();
        createDemo2Storage();
        createDemo4Storage();
        createDemo3Storage();
        createDemo2Storage();
        createDemoStorage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View collectionsList = inflater.inflate(R.layout.fragment_collection_storage, container, false);

        recyclerView = (RecyclerView) collectionsList.findViewById(R.id.all_collections);
        emptyText = (TextView) collectionsList.findViewById(R.id.empty_text);
        fab = (FloatingActionButton) collectionsList.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            Intent intent = new Intent(getActivity(), NewCollectionActivity.class);
                getActivity().startActivity(intent);

            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(currentLayoutManagerType);

        collectionStorageAdapter = new CollectionStorageAdapter(getActivity(), storages);
        recyclerView.setAdapter(collectionStorageAdapter);

        if(collectionStorageAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }

        return collectionsList;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDemoStorage() {
        CollectionStorage storage = new CollectionStorage();
        storage.setTitle("Vodkas");
        storage.setDescription("Collection description.");
        storage.setPhotoPath("/sdcard/Download/vodkas.jpg");
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        createDemoItem(storage);
        storages.add(storage);
    }
    private void createDemo2Storage() {
        CollectionStorage storage = new CollectionStorage();
        storage.setTitle("Bottle Caps");
        storage.setDescription("Collection description.");
        storages.add(storage);
    }
    private void createDemo3Storage() {
        CollectionStorage storage = new CollectionStorage();
        storage.setTitle("Shells");
        storage.setDescription("Collection description.");
        storage.setPhotoPath("3");
        storages.add(storage);
    }
    private void createDemo4Storage() {
        CollectionStorage storage = new CollectionStorage();
        storage.setTitle("CD's");
        storage.setDescription("Collection description.");
        storage.setPhotoPath("4");
        storages.add(storage);
    }

    private void createDemoItem(CollectionStorage storage) {
        CollectionItem item = new CollectionItem();
        item.setTitle("Absolut");
        item.setDescription("Sweden vodka.");
        storage.addItem(item);
    }
}
