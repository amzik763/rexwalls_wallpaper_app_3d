package com.amzi.cnews3.model;

public class model_categories {


    String name;
    String displaylink;
    public model_categories(){

    }

    public model_categories(String name, String displaylink) {
        this.name = name;
        this.displaylink = displaylink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplaylink() {
        return displaylink;
    }

    public void setDisplaylink(String displaylink) {
        this.displaylink = displaylink;
    }
}
