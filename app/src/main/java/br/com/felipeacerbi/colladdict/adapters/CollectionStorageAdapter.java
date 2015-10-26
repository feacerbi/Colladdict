package br.com.felipeacerbi.colladdict.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public class CollectionStorageAdapter extends RecyclerView.Adapter<CollectionStorageAdapter.ViewHolder> {

    private final Context context;
    private List<CollectionStorage> storages;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final TextView descField;
        private final ImageView photoField;
        private final LinearLayout scrim;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.collection_title);
            descField = (TextView) itemView.findViewById(R.id.collection_description);
            photoField = (ImageView) itemView.findViewById(R.id.collection_photo);
            scrim = (LinearLayout) itemView.findViewById(R.id.scrim);

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

        public LinearLayout getScrim() {
            return scrim;
        }
    }

    public CollectionStorageAdapter(Context context, List<CollectionStorage> storages) {
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
        holder.getTitleField().setText(storages.get(position).getTitle());
        holder.getDescField().setText(storages.get(position).getDescription());

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
                Activity exit = (Activity) context;
                Pair photo = Pair.create(holder.getPhotoField(), "photo");
//                Pair title = Pair.create(holder.getTitleField(), "title");
//                Pair desc = Pair.create(holder.getDescField(), "desc");
                Pair scrim = Pair.create(holder.getScrim(), "scrim");
                Intent intent = new Intent(context, CollectionItemsActivity.class);
                intent.putExtra("storage", storage);
                context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(exit, photo, scrim).toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return storages.size();
    }
}
