package com.example.booklist.data;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;

    public Book(String title, int coverResourceID) {
        this.setTitle(title);
        this.setCoverResourceID(coverResourceID);
    }

    private int coverResourceID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResourceID() {
        return coverResourceID;
    }

    public void setCoverResourceID(int coverResourceID) {
        this.coverResourceID = coverResourceID;
    }
}
