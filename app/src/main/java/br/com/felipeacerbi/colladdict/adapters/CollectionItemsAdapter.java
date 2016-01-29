package br.com.felipeacerbi.colladdict.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionItemsAdapter extends RecyclerView.Adapter<CollectionItemsAdapter.ViewHolder> {

    private final Context context;
    private final SparseBooleanArray selectedItems;
    private List<CollectionItem> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final ImageView photoField;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.item_title);
            photoField = (ImageView) itemView.findViewById(R.id.item_image);

        }

        public TextView getTitleField() {
            return titleField;
        }

        public ImageView getPhotoField() {
            return photoField;
        }
    }

    public CollectionItemsAdapter(Context context, List<CollectionItem> items) {
        this.context = context;
        this.items = items;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollectionItem item = items.get(position);

        holder.getPhotoField().setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.getTitleField().setText(item.getTitle());

        if(selectedItems.get(position, false)) {
            holder.getPhotoField().setColorFilter(R.color.fadeImage);
        } else {
            holder.getPhotoField().setColorFilter(null);
        }

        // TODO Get Image from real source.
        if(item.getPhotoPath() != null) {
            if(item.getPhotoPath().equals("3")) {
                holder.getPhotoField().setImageResource(R.drawable.shells);
            } else if(item.getPhotoPath().equals("4")) {
                holder.getPhotoField().setImageResource(R.drawable.cds);
            } else {
//            holder.getPhotoField().setImageURI(Uri.parse(storage.getPhotoPath()));
                holder.getPhotoField().setImageResource(R.drawable.absolut_vodka_bottles);
            }
        } else {
            holder.getPhotoField().setImageResource(R.drawable.beer_bottle_caps_collection);
//            holder.getPhotoField().setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.sym_def_app_icon), 30, 30, true));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
//        fragment.getActionMode().setTitle(String.valueOf(selectedCount)); TODO Create Action Mode for Collection Items
    }

    public void deselectAll() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount(){ return selectedItems.size(); }

    public List<CollectionStorage> getSelectedItems() {
        List<CollectionStorage> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(items.get(selectedItems.keyAt(i)));
        }
        return items;
    }
}