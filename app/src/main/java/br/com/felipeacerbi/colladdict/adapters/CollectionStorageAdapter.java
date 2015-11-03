package br.com.felipeacerbi.colladdict.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.CollectionItemsActivity;
import br.com.felipeacerbi.colladdict.activities.Collections;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionStorageAdapter extends RecyclerView.Adapter<CollectionStorageAdapter.ViewHolder>  implements ActionMode.Callback {

    private final AppCompatActivity context;
    private List<CollectionStorage> storages;
    private boolean isActionMode = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final TextView descField;
        private final ImageView photoField;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.collection_title);
            descField = (TextView) itemView.findViewById(R.id.collection_description);
            photoField = (ImageView) itemView.findViewById(R.id.collection_photo);

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

    public CollectionStorageAdapter(AppCompatActivity context, List<CollectionStorage> storages) {
        this.context = context;
        this.storages = storages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_storage_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CollectionStorage storage = storages.get(position);

        holder.getPhotoField().setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.getTitleField().setText(storage.getTitle());
        holder.getDescField().setText(storage.getDescription());

        if(storage.getPhotoPath() != null) {
            if(storage.getPhotoPath().equals("3")) {
                holder.getPhotoField().setImageResource(R.drawable.shells);
            } else if(storage.getPhotoPath().equals("4")) {
                holder.getPhotoField().setImageResource(R.drawable.cds);
            } else {
//            holder.getPhotoField().setImageURI(Uri.parse(storage.getPhotoPath()));
                holder.getPhotoField().setImageResource(R.drawable.absolut_vodka_bottles);
            }
        } else {
            holder.getPhotoField().setImageResource(R.drawable.beer_bottle_caps_collection);
//            holder.getPhotoField().setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.sym_def_app_icon), 300, 400, true));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActionMode) {
                    v.setSelected(!v.isSelected());
                }
                Pair photoPair = Pair.create(holder.getPhotoField(), "photo");
                Intent intent = new Intent(context, CollectionItemsActivity.class);
                intent.putExtra("storage", storage);
                context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(context, photoPair).toBundle());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startSupportActionMode(CollectionStorageAdapter.this);
                v.setSelected(true);
                return true;
            }
        });;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.collections_context_menu, menu);
        mode.setTitle("Select Collections | 1");
        isActionMode = true;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        mode.setTitle("Select Collections | 2");
        switch (item.getItemId()) {
            case R.id.action_remove_collection:
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        isActionMode = false;
    }

    public List<CollectionStorage> getStorages() {
        return storages;
    }

    @Override
    public int getItemCount() {
        return storages.size();
    }

    public int getItemsSelectedCount() {
        int count = 0;

        for(CollectionStorage storage : storages) {

        }
        return count;
    }
}
