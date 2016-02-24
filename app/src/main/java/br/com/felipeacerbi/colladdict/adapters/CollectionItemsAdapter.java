package br.com.felipeacerbi.colladdict.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.CollectionItemsActivity;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.activities.NewCollectionActivity;
import br.com.felipeacerbi.colladdict.activities.NewItemActivity;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionItemsAdapter extends RecyclerView.Adapter<CollectionItemsAdapter.ViewHolder> {

    private final TaskManager context;
    private final AppCompatActivity activity;
    private final SparseBooleanArray selectedItems;
    private List<CollectionItem> items;
    private CollectionStorage storage;
    public static final int LIST_ICON_SIZE = 40;
    private SparseBooleanArray oldSelectedPositions;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final ImageView photoField;
        private final TextView descField;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.item_title);
            descField = (TextView) itemView.findViewById(R.id.item_description);
            photoField = (ImageView) itemView.findViewById(R.id.item_image);

        }

        public TextView getTitleField() {
            return titleField;
        }

        public TextView getDescField() {
            return descField;
        }

        public ImageView getPhotoField() {
            return photoField;
        }
    }

    public CollectionItemsAdapter(TaskManager context, List<CollectionItem> items, CollectionStorage storage) {
        this.context = context;
        this.items = items;
        this.storage = storage;
        this.activity = context.getAppCompatActivity();
        selectedItems = new SparseBooleanArray();
        oldSelectedPositions = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CollectionItem item = items.get(position);

        holder.getTitleField().setText(item.getTitle());
        holder.getDescField().setText(item.getDescription());

        if(selectedItems.get(position, false)) {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.fadeImage));
        } else {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
        }

        if(item.getPhotoPath() != null) {
            Picasso.with(context.getAppCompatActivity())
                    .load(new File(item.getPhotoPath()))
                    .resize(LIST_ICON_SIZE, LIST_ICON_SIZE)
                    .centerCrop()
                    .error(R.drawable.beer_bottle_caps_collection)
                    .into(holder.getPhotoField());
        } else {
            Picasso.with(context.getAppCompatActivity())
                    .load(R.drawable.beer_bottle_caps_collection)
                    .resize(LIST_ICON_SIZE, LIST_ICON_SIZE)
                    .centerCrop()
                    .error(R.drawable.beer_bottle_caps_collection)
                    .into(holder.getPhotoField());
        }

        holder.photoField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getPhotoPath() != null)
                    fullImage(item.getPhotoPath());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.isActionMode()) {
                    select(position);
                } else {
                    Intent intent = new Intent(activity, NewItemActivity.class);
                    intent.putExtra("collection_item", item);
                    intent.putExtra("collection_storage", storage);
                    activity.startActivityForResult(intent, Collections.REQUEST_MODIFY_COLLECTION_ITEM);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.startSupportActionMode(context.getActionModeCallback());
                select(position);
                return true;
            }
        });
    }

    public List<CollectionItem> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void select(int position) {
        if(selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);

        int selectedCount = getSelectedItemsCount();
        context.getActionMode().setTitle(String.valueOf(selectedCount));
    }

    public void deselectAll() {
        oldSelectedPositions = selectedItems.clone();
        selectedItems.clear();
        notifyItemsChanged();
    }

    public int getSelectedItemsCount(){ return selectedItems.size(); }

    public List<CollectionItem> getSelectedItems() {
        List<CollectionItem> selectedObjects = new ArrayList<>(getSelectedItemsCount());
        for (int i = 0; i < getSelectedItemsCount(); i++) {
            selectedObjects.add(items.get(selectedItems.keyAt(i)));
        }
        return selectedObjects;
    }

    public void fullImage(String path) {
        Dialog mSplashDialog = new Dialog(activity);
        mSplashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSplashDialog.setContentView(R.layout.image_fullscreen);
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSplashDialog.setCancelable(true);
        Picasso.with(activity)
                .load(new File(path))
                .fit()
                .error(R.drawable.shells)
                .into(((ImageView) mSplashDialog.findViewById(R.id.imageview_fullscreen)));
        mSplashDialog.show();
    }

    public void notifyItemsRemoved() {
        getItems().removeAll(getSelectedItems());
        for(int i = 0; i < getSelectedItemsCount(); i++) {
            int position = selectedItems.keyAt(i);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void notifyItemsInserted(List<CollectionItem> collectionItems) {
        for(int i = 0; i < oldSelectedPositions.size(); i++) {
            int position = oldSelectedPositions.keyAt(i);
            getItems().add(position, collectionItems.get(i));
            notifyItemInserted(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void notifyItemsChanged() {
        for(int i = 0; i < getSelectedItemsCount(); i++) {
            int position = selectedItems.keyAt(i);
            notifyItemChanged(position);
        }
    }
}
