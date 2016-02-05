package br.com.felipeacerbi.colladdict.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.CollectionItemsActivity;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.fragments.CollectionStorageFragment;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionStoragesAdapter extends RecyclerView.Adapter<CollectionStoragesAdapter.ViewHolder> {

    private AppCompatActivity context;
    private CollectionStorageFragment fragment;
    private List<CollectionStorage> storages;
    private SparseBooleanArray selectedItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final ImageView photoField;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.collection_title);
            photoField = (ImageView) itemView.findViewById(R.id.collection_photo);
        }

        public TextView getTitleField() {
            return titleField;
        }

        public ImageView getPhotoField() {
            return photoField;
        }
    }

    public CollectionStoragesAdapter(CollectionStorageFragment fragment, List<CollectionStorage> storages) {
        this.fragment = fragment;
        this.storages = storages;
        selectedItems = new SparseBooleanArray();
        context = (AppCompatActivity) fragment.getActivity();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_storage_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CollectionStorage storage = storages.get(position);

        holder.getPhotoField().setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.getTitleField().setText(storage.getTitle());

        if(selectedItems.get(position, false)) {
            holder.getPhotoField().setColorFilter(R.color.fadeImage);
        } else {
            holder.getPhotoField().setColorFilter(null);
        }

        if(storage.getPhotoPath() != null) {
//            Bitmap bmp = BitmapFactory.decodeFile(storage.getPhotoPath());
//            holder.photoField.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300, 400, true));
            Picasso.with(context)
                    .load(new File(storage.getPhotoPath()))
                    .resize(300, 400)
                    .centerCrop()
                    .error(android.R.drawable.btn_default)
                    .into(holder.photoField);
        } else {
//            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shells);
//            holder.getPhotoField().setImageBitmap(Bitmap.createScaledBitmap(bmp, 300, 400, true));
            Picasso.with(context)
                    .load(R.drawable.shells)
                    .resize(300, 400)
                    .centerCrop()
                    .error(android.R.drawable.btn_default)
                    .into(holder.photoField);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.isActionMode()) {
                    select(position);
                } else {
                    Pair photoPair = Pair.create(holder.getPhotoField(), "photo");
                    Intent intent = new Intent(context, CollectionItemsActivity.class);
                    intent.putExtra("collection_storage", storage);
                    context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(context, photoPair).toBundle());
                    //context.startActivity(intent);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startSupportActionMode(fragment);
                select(position);
                return true;
            }
        });
    }

    public List<CollectionStorage> getStorages() {
        return storages;
    }

    @Override
    public int getItemCount() {
        return storages.size();
    }


    public void select(int position) {
        if(selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);

        int selectedCount = getSelectedItemsCount();
        fragment.getActionMode().setTitle(String.valueOf(selectedCount));
    }

    public void deselectAll() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount(){ return selectedItems.size(); }

    public List<CollectionStorage> getSelectedItems() {
        List<CollectionStorage> selectedObjects = new ArrayList<>(getSelectedItemsCount());
        for (int i = 0; i < getSelectedItemsCount(); i++) {
            selectedObjects.add(storages.get(selectedItems.keyAt(i)));
        }
        return selectedObjects;
    }
}
