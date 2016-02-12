package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.adapters.CollectionItemsAdapter;
import br.com.felipeacerbi.colladdict.adapters.CollectionStoragesAdapter;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertItemTask;
import br.com.felipeacerbi.colladdict.tasks.InsertStorageTask;
import br.com.felipeacerbi.colladdict.tasks.LoadItemsTask;
import br.com.felipeacerbi.colladdict.tasks.LoadStoragesTask;
import br.com.felipeacerbi.colladdict.tasks.RemoveItemsTask;
import br.com.felipeacerbi.colladdict.tasks.RemoveStoragesTask;

/**
 * Created by felipe.acerbi on 01/10/2015.
 */
public class CollectionItemsActivity extends AppCompatActivity implements ActionMode.Callback {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private CollectionStorage storage;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private LinearLayoutManager layoutManager;
    private Bundle savedInstanceState;
    private CollectionItemsAdapter collectionItemsAdapter;
    private View view;
    private ImageView coverPhoto;
    private FloatingActionButton floatButton;
    private TextView collectionTitle;
    private TextView collectionDesc;
    private LinearLayout scrim;
    private CollapsingToolbarLayout collapToolbar;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LayoutManagerType currentLayoutManagerType;
    private ActionMode actionMode;
    private boolean isActionMode;
    private List<CollectionItem> deleteList;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        //this.savedInstanceState = savedInstanceState;

        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null) {
            storage = (CollectionStorage) intent.getExtras().get("collection_storage");
        } else {
            storage = new CollectionStorage();
        }

        setToolbar();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        getWindow().setEnterTransition(new Fade());

        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        coverPhoto = (ImageView) findViewById(R.id.collection_photo);
        collectionTitle = (TextView) findViewById(R.id.collection_title);
        collectionDesc = (TextView) findViewById(R.id.collection_description);
        collapToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.all_items);
        emptyText = (TextView) findViewById(R.id.empty_text);

        coverPhoto.setTransitionName("photo");

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectionItemsActivity.this, NewItemActivity.class);
                intent.putExtra("collection_storage", storage);
                startActivityForResult(intent, Collections.REQUEST_NEW_COLLECTION_ITEM);
            }
        });

        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

//        if (savedInstanceState != null) {
//            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
//                    .getSerializable(KEY_LAYOUT_MANAGER);
//        }
        setRecyclerViewLayoutManager(currentLayoutManagerType);

        CollectionItemsAdapter adapter = new CollectionItemsAdapter(this, null, storage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();
    }

    public void reload() {
        new LoadItemsTask(this, recyclerView, emptyText, storage).execute();
    }

    public void reloadAndScroll() {
        new LoadItemsTask(this, recyclerView, emptyText, storage).execute();

        collectionItemsAdapter = (CollectionItemsAdapter) recyclerView.getAdapter();
        // TODO Fix scroll to position.
        int scrollPosition = 0;
        scrollPosition = (collectionItemsAdapter.getItemCount());
        recyclerView.scrollToPosition(scrollPosition);
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(storage.getTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (storage.getPhotoPath() != null) {
            Picasso.with(this)
                    .load(new File(storage.getPhotoPath()))
                    .fit()
                    .error(android.R.drawable.btn_default)
                    .into(coverPhoto);
        } else {
            Picasso.with(this)
                    .load("default")
                    .fit()
                    .error(android.R.drawable.btn_default)
                    .into(coverPhoto);
        }
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Collections.REQUEST_MODIFY_COLLECTION_STORAGE) {
                storage = (CollectionStorage) data.getExtras().getSerializable("collection_storage");
                setToolbar();
                Snackbar.make(
                        findViewById(R.id.coordinator),
                        storage.getTitle() + " collection modified",
                        Snackbar.LENGTH_SHORT).show();
            } else if (requestCode == Collections.REQUEST_NEW_COLLECTION_ITEM) {
                final CollectionItem item = (CollectionItem) data.getExtras().getSerializable("collection_item");
                reloadAndScroll();
                Snackbar.make(
                        findViewById(R.id.coordinator),
                        item.getTitle() + " item created",
                        Snackbar.LENGTH_SHORT).show();
            } else if (requestCode == Collections.REQUEST_MODIFY_COLLECTION_ITEM) {
                final CollectionItem item = (CollectionItem) data.getExtras().getSerializable("collection_item");
                reloadAndScroll();
                Snackbar.make(
                        findViewById(R.id.coordinator),
                        item.getTitle() + " item modified",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        toolbar.setVisibility(View.GONE);
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.collection_items_context_menu, menu);

        collectionItemsAdapter = (CollectionItemsAdapter) recyclerView.getAdapter();
        actionMode = mode;

        mode.setTitle(String.valueOf(collectionItemsAdapter.getSelectedItemsCount()));
        isActionMode = true;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Deprecated method for newer versions.
//        context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark, null));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_remove_collection_item:
                deleteList = collectionItemsAdapter.getSelectedItems();
                new RemoveItemsTask(this).execute(deleteList);
                reloadAndScroll();
                Snackbar.make(findViewById(R.id.coordinator), "Items removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (CollectionItem item : deleteList) {
                                    new InsertItemTask(CollectionItemsActivity.this, false).execute(item);
                                }
                                reloadAndScroll();
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
        toolbar.setVisibility(View.VISIBLE);
        collectionItemsAdapter.deselectAll();
        isActionMode = false;
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public boolean isActionMode() {
        return isActionMode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(this, NewCollectionActivity.class);
                intent.putExtra("collection_storage", storage);
                startActivityForResult(intent, Collections.REQUEST_MODIFY_COLLECTION_STORAGE);
                break;
            case R.id.action_change_layout:
                currentLayoutManagerType = (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) ?
                        LayoutManagerType.GRID_LAYOUT_MANAGER : LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                setRecyclerViewLayoutManager(currentLayoutManagerType);
                break;
            case R.id.action_settings:
                break;
        }

        return true;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(this, SPAN_COUNT);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(this);
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(this);
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
