package br.com.felipeacerbi.colladdict.models;

/**
 * Created by felipe.acerbi on 09/11/2015.
 */
public class Category implements ListItem {

    private long id;
    private String title;

    public Category() {
    }

    public Category(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
