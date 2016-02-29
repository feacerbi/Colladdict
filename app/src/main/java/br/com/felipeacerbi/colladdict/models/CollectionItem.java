package br.com.felipeacerbi.colladdict.models;

import java.io.Serializable;

/**
 * Created by felipe.acerbi on 28/09/2015.
 */
public class CollectionItem implements Serializable {

    private long id;
    private String title;
    private String description;
    private String photoPath;
    private int rarity;
    private int value;
    private long storageId;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public long getStorageId() {
        return storageId;
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

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
