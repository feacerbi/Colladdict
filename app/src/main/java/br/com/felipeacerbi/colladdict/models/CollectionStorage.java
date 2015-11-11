package br.com.felipeacerbi.colladdict.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe.acerbi on 29/09/2015.
 */
public class CollectionStorage implements Serializable {

    private long id;
    private String title;
    private String description;
    private String photoPath;
    private List<CollectionItem> collectionItems;
    private Category category;

    public CollectionStorage() {
        collectionItems = new ArrayList<>();
    }

    public void addItem(CollectionItem item) {
        collectionItems.add(item);
    }

    public void removeItem(int position) {
        collectionItems.remove(position);
    }

    public void removeItem(CollectionItem item) {
        collectionItems.remove(item);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<CollectionItem> getCollectionItems() {
        return collectionItems;
    }

    public Category getCategory() {
        return category;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCollectionItems(List<CollectionItem> collectionItems) {
        this.collectionItems = collectionItems;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
