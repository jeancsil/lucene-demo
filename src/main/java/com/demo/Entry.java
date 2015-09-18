package com.demo;

public class Entry {
    private String title;
    private String text;

    public Entry() {
        this.title = "null-object-title";
        this.text = "null-object-text";
    }

    public Entry(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
