package br.com.felipeacerbi.colladdict.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.ListItem;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class DialogListAdapter implements ListAdapter {

    private final Context context;
    private List<ListItem> items;
    private SparseBooleanArray selectedList;

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItem listItem = (ListItem) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.list_item_dialog_view, parent, false);
        TextView titleField = (TextView) convertView.findViewById(R.id.category_name);

        if(isSelected(position)) {
            titleField.setTextColor(context.getResources().getColor(android.R.color.white));
            titleField.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            titleField.setTextColor(context.getResources().getColor(android.R.color.black));
            titleField.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                selectItem(position);
            }
        });

        titleField.setText(listItem.getTitle());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public DialogListAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;

        selectedList = new SparseBooleanArray();
    }

    public void selectItem(int position) {
        selectedList.put(position, true);
    }

    public boolean isSelected(int position) {
        return selectedList.get(position);
    }

    public void clearSelected() {
        selectedList.clear();
    }

}
