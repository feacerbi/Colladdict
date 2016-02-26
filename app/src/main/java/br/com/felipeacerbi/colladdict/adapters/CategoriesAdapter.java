package br.com.felipeacerbi.colladdict.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.activities.TaskManager;
import br.com.felipeacerbi.colladdict.models.Category;
import br.com.felipeacerbi.colladdict.models.CollectionItem;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;
import br.com.felipeacerbi.colladdict.tasks.InsertTask;
import br.com.felipeacerbi.colladdict.tasks.RemoveTask;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final TaskManager context;
    private List<Category> categories;
    private SparseBooleanArray selectedItems;
    public static final int LIST_ICON_SIZE = 40;
    private List<Category> deleteList;
    private boolean remove;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleField;
        private final ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            titleField = (TextView) itemView.findViewById(R.id.category_title);
            deleteButton = (ImageView) itemView.findViewById(R.id.remove_category);

        }

        public TextView getTitleField() {
            return titleField;
        }

        public ImageView getDeleteButton() {
            return deleteButton;
        }
    }

    public CategoriesAdapter(TaskManager context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dialog_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Category category = categories.get(position);

        holder.getTitleField().setText(category.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(context.getAppCompatActivity());
                View inputView = layoutInflater.inflate(R.layout.input_dialog_view, null);
                AppCompatActivity activity = context.getAppCompatActivity();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                        .setView(inputView);
                alertDialog.create();

                final EditText categoryField = (EditText) inputView.findViewById(R.id.new_category);
                categoryField.setHint(activity.getResources().getString(R.string.new_category_hint));
                categoryField.setText(category.getTitle());
                categoryField.requestFocus();
                alertDialog.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newCategoryName = categoryField.getText().toString();
                        if (!newCategoryName.isEmpty()) {
                            category.setTitle(newCategoryName);
                            new InsertTask(context, true).execute(category);
                            notifyItemChanged(position);
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Cancel Action
                            }
                        })
                        .setTitle("Modify Category")
                        .show();
            }
        });

        if(context.isActionMode()) {
            holder.getDeleteButton().setVisibility(View.VISIBLE);
            holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(position);
                    deleteList = getSelectedItems();
                    getCategories().remove(category);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    remove = true;
                    Snackbar.make(context.getAppCompatActivity().findViewById(R.id.coordinator), category.getTitle() + " category removed", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    add(category, position);
                                    remove = false;
                                }
                            }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (remove) {
                                new RemoveTask(context).execute(deleteList);
                            }
                            deselectAll();
                            super.onDismissed(snackbar, event);
                        }
                    }).show();
                }
            });
        } else {
            holder.getDeleteButton().setOnLongClickListener(null);
            holder.getDeleteButton().setVisibility(View.GONE);
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.getAppCompatActivity().startSupportActionMode(context.getActionModeCallback());
                notifyItemRangeChanged(0, getItemCount());
                return true;
            }
        });
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void add(Category category, int position) {
        getCategories().add(position, category);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return getCategories().size();
    }

    public void select(int position) {
        if(selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
    }

    public void deselectAll() {
        notifyItemRangeChanged(0, getItemCount());
        selectedItems.clear();
    }

    public List<Category> getSelectedItems() {
        List<Category> selectedObjects = new ArrayList<>(1);
        selectedObjects.add(getCategories().get(selectedItems.keyAt(0)));
        return selectedObjects;
    }
}
