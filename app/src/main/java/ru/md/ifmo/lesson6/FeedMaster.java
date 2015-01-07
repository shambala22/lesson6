package ru.md.ifmo.lesson6;


public class FeedMaster {
    int id = 0;
    String title = null;
    String link = null;

    public FeedMaster(int id, String title, String link) {
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String number) {
        this.title = number;
    }


    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}