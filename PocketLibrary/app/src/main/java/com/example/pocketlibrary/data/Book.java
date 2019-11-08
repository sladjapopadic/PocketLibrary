package com.example.pocketlibrary.data;

public class Book {

    private String id;
    private BookInfo volumeInfo;

    public Book() {

    }

    public Book(String id, BookInfo volumeInfo) {

        this.id = id;
        this.volumeInfo = volumeInfo;
    }

    public String getId() {
        return id;
    }

    public BookInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setId(String id) {
        this.id = id;
    }
}
