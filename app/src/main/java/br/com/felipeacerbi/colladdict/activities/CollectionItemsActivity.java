package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.adapters.CollectionAdapter;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 01/10/2015.
 */
public class CollectionItemsActivity extends AppCompatActivity {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private CollectionStorage storage;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private LinearLayoutManager layoutManager;
    private Bundle savedInstanceState;
    private CollectionAdapter collectionAdapter;
    private View view;
    private CollapsingToolbarLayout coverPhoto;
    private FloatingActionButton floatButton;
    private TextView collectionTitle;
    private TextView collectionDesc;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private LayoutManagerType currentLayoutManagerType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        this.savedInstanceState = savedInstanceState;

        Intent intent = getIntent();
        if(intent != null) {
            storage = (CollectionStorage) intent.getExtras().get("storage");
        } else {
            storage = new CollectionStorage();
        }

        setToolbar();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getWindow().setEnterTransition(new Fade());
        coverPhoto = (CollapsingToolbarLayout) findViewById(R.id.collection_photo);
        coverPhoto.setTransitionName("photo");
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collectionTitle = (TextView) findViewById(R.id.collection_title);
        collectionDesc = (TextView) findViewById(R.id.collection_description);

        collectionTitle.setText(storage.getTitle());
        collectionDesc.setText(storage.getDescription());

        if(storage.getPhotoPath() != null) {
            if(storage.getPhotoPath().equals("3")) {
                coverPhoto.setBackgroundResource(R.drawable.shells);
            } else if(storage.getPhotoPath().equals("4")) {
                coverPhoto.setBackgroundResource(R.drawable.cds);
            } else {
//            holder.getPhotoField().setImageURI(Uri.parse(storage.getPhotoPath()));
                coverPhoto.setBackgroundResource(R.drawable.absolut_vodka_bottles);
            }
        } else {
            coverPhoto.setBackgroundResource(R.drawable.beer_bottle_caps_collection);
//            holder.getPhotoField().setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.sym_def_app_icon), 300, 400, true));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        view = getLayoutInflater().inflate(R.layout.activity_collection_items, (ViewGroup) findViewById(R.id.list_items));

        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.collection_list);
        emptyText = (TextView) view.findViewById(R.id.empty_text);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(currentLayoutManagerType);

        collectionAdapter = new CollectionAdapter(this, storage.getCollectionItems());
        recyclerView.setAdapter(collectionAdapter);

        if(collectionAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //TODO Save instance state.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_change_layout:
                currentLayoutManagerType = (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) ?
                        LayoutManagerType.GRID_LAYOUT_MANAGER : LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                setRecyclerViewLayoutManager(currentLayoutManagerType);
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
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
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
