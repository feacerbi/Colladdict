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

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.Constants;
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
    private SparseBooleanArray oldSelectedPositions;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final ImageView photoField;
        private final TextView descField;
        private final ImageView valueIcon;
        private final ImageView rarityIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.item_title);
            descField = (TextView) itemView.findViewById(R.id.item_description);
            photoField = (ImageView) itemView.findViewById(R.id.item_image);
            valueIcon = (ImageView) itemView.findViewById(R.id.item_value);
            rarityIcon = (ImageView) itemView.findViewById(R.id.item_rarity);

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

        public ImageView getRarityIcon() {
            return rarityIcon;
        }

        public ImageView getValueIcon() {
            return valueIcon;
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
            Glide.with(context.getAppCompatActivity())
                    .load(item.getPhotoPath())
                    .centerCrop()
                    .error(R.drawable.beer_bottle_caps_collection)
                    .into(holder.getPhotoField());
        } else {
            Glide.with(context.getAppCompatActivity())
                    .load(R.drawable.beer_bottle_caps_collection)
                    .centerCrop()
                    .error(R.drawable.beer_bottle_caps_collection)
                    .into(holder.getPhotoField());
        }

        Glide.with(context.getAppCompatActivity())
                .load(getRarityIcon(item.getRarity()))
                .into(holder.getRarityIcon());

        Glide.with(context.getAppCompatActivity())
                .load(getValueIcon(item.getValue()))
                .into(holder.getValueIcon());

        holder.getPhotoField().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getPhotoPath() != null)
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
                    intent.putExtra("position", position);
                    activity.startActivityForResult(intent, Constants.REQUEST_MODIFY_COLLECTION_ITEM);
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
        notifyItemsChanged();
        selectedItems.clear();
    }

    public int getSelectedItemsCount(){ return selectedItems.size(); }

    public List<CollectionItem> getSelectedItems() {
        List<CollectionItem> selectedObjects = new ArrayList<>(getSelectedItemsCount());
        for (int i = 0; i < getSelectedItemsCount(); i++) {
            selectedObjects.add(items.get(selectedItems.keyAt(i)));
        }
        return selectedObjects;
    }

    public void notifyItemsRemoved() {
        for(int i = 0; i < getSelectedItemsCount(); i++) {
            int position = selectedItems.keyAt(i);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
        getItems().removeAll(getSelectedItems());
    }

    public void notifyItemsInserted(List<CollectionItem> collectionItems) {
        for(int i = 0; i < oldSelectedPositions.size(); i++) {
            int position = oldSelectedPositions.keyAt(i);
            getItems().add(position, collectionItems.get(i));
            notifyItemInserted(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void notifyNewItemInserted(CollectionItem collectionItem) {
        getItems().add(collectionItem);
        int position = getItemCount();
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void notifyItemsChanged() {
        for(int i = 0; i < getSelectedItemsCount(); i++) {
            int position = selectedItems.keyAt(i);
            notifyItemChanged(position);
        }
    }

    public int getRarityIcon(int rarity) {
        int icon = -1;
        switch(rarity) {
            case Constants.RARITY_VERY_COMMON:
                icon = R.drawable.ic_whatshot_black_24dp;
                break;
            case Constants.RARITY_COMMON:
                icon = R.drawable.ic_whatshot_black_24dp;
                break;
            case Constants.RARITY_RARE:
                icon = R.drawable.ic_whatshot_black_24dp;
                break;
            case Constants.RARITY_VERY_RARE:
                icon = R.drawable.ic_whatshot_black_24dp;
                break;
            case Constants.RARITY_UNIQUE:
                icon = R.drawable.ic_whatshot_black_24dp;
                break;
            default:
                icon = R.drawable.ic_whatshot_black_24dp;
        }

        return icon;
    }

    public int getValueIcon(int value) {
        int icon = -1;
        switch(value) {
            case Constants.VALUE_CHEAP:
                icon = R.drawable.ic_monetization_on_black_24dp;
                break;
            case Constants.VALUE_NORMAL:
                icon = R.drawable.ic_monetization_on_black_24dp;
                break;
            case Constants.VALUE_EXPENSIVE:
                icon = R.drawable.ic_monetization_on_black_24dp;
                break;
            case Constants.VALUE_VERY_EXPENSIVE:
                icon = R.drawable.ic_monetization_on_black_24dp;
                break;
            case Constants.VALUE_UNAFFORDABLE:
                icon = R.drawable.ic_monetization_on_black_24dp;
                break;
            default:
                icon = R.drawable.ic_monetization_on_black_24dp;
        }

        return icon;
    }

    public void fullImage(String path) {
        Dialog mSplashDialog = new Dialog(activity);
        mSplashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSplashDialog.setContentView(R.layout.image_fullscreen);
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSplashDialog.setCancelable(true);
        Glide.with(activity)
                .load(path)
                .fitCenter()
                .error(R.drawable.shells)
                .into(((ImageView) mSplashDialog.findViewById(R.id.imageview_fullscreen)));
        mSplashDialog.show();
    }
}
