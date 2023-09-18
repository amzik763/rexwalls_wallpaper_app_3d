package com.amzi.cnews3.model;

public class manager {

    String showads,available,link;
    int version;

    public manager(){

    }

    public manager(String showads, String available, int version, String link) {
        this.showads = showads;
        this.available = available;
        this.version = version;
        this.link = link;
    }

    public String getShowads() {
        return showads;
    }

    public void setShowads(String showads) {
        this.showads = showads;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
