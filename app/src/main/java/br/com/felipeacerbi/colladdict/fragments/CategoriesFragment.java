package br.com.felipeacerbi.colladdict.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.adapters.CategoriesAdapter;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.tasks.InsertTask;
import br.com.felipeacerbi.colladdict.tasks.LoadTask;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CategoriesFragment extends Fragment implements ActionMode.Callback, TaskManager {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView emptyText;
    private FloatingActionButton fab;
    private MenuItem layoutMenuItem;
    private boolean isActionMode;
    private ActionMode actionMode;
    private List<Category> deleteList;
    private LayoutManagerType currentLayoutManagerType;
    private CategoriesAdapter categoriesAdapter;
    private boolean remove;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGERED_LAYOUT_MANAGER
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setHasOptionsMenu(true);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View categoriesList = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = (RecyclerView) categoriesList.findViewById(R.id.all_categories);
        emptyText = (TextView) categoriesList.findViewById(R.id.empty_text);
        fab = (FloatingActionButton) categoriesList.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View inputView = layoutInflater.inflate(R.layout.input_dialog_view, null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                        .setView(inputView);
                alertDialog.create();

                final EditText newCategoryField = (EditText) inputView.findViewById(R.id.new_category);
                newCategoryField.setHint(getResources().getString(R.string.new_category_hint));
                newCategoryField.requestFocus();

                alertDialog.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newCategoryName = newCategoryField.getText().toString();
                        if (!newCategoryName.isEmpty()) {
                            Category category = new Category(newCategoryName);
                            new InsertTask(CategoriesFragment.this, false).execute(category);
                            categoriesAdapter = (CategoriesAdapter) recyclerView.getAdapter();
                            categoriesAdapter.add(category, categoriesAdapter.getItemCount());
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Cancel Action
                            }
                        })
                        .setTitle("Add Category")
                        .show();
            }
        });

        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }

        setRecyclerViewLayoutManager(currentLayoutManagerType);

        return categoriesList;
    }

    @Override
    public void onStart() {
        super.onStart();
        reload();
    }

    public void reload() {
        new LoadTask(this, recyclerView, emptyText, Collections.LOAD_CATEGORIES, null).execute();
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
                if(layoutMenuItem != null) layoutMenuItem.setIcon(R.drawable.ic_view_stream_white_24dp);
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity());
                if(layoutMenuItem != null) layoutMenuItem.setIcon(R.drawable.ic_view_module_white_24dp);
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                if(layoutMenuItem != null) layoutMenuItem.setIcon(R.drawable.ic_view_module_white_24dp);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
     public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.categories_context_menu, menu);

        categoriesAdapter = (CategoriesAdapter) recyclerView.getAdapter();
        fab.hide();
        actionMode = mode;

        mode.setTitle(getActivity().getResources().getString(R.string.action_remove_category));
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
            default:
                mode.finish();
                break;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Deprecated method for newer versions.
//        context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        categoriesAdapter.deselectAll();
        fab.show();
        isActionMode = false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collections_linear, menu);
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
}