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

    @Override
    public String toString() {
        return getTitle();
    }
}
