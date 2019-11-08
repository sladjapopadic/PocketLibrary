package com.example.pocketlibrary.data;

import java.util.ArrayList;

public class BookInfo {
    private String title;
    private ArrayList<String> authors = new ArrayList<>();
    private int pageCount;
    private ImageLinks imageLinks;
    private String language;


    public String getTitle() {
        return title;
    }


    public ArrayList<String> getAuthors() {
        return authors;
    }

    public int getPageCount() {
        return pageCount;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public String getLanguage() {
        return language;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
