package ru.md.ifmo.lesson6;


public class ItemMaster {
    int id = 0;
    String title = null;
    String description = null;
    String link = null;
    String pubDate = null;

    public ItemMaster() {

    }

    public ItemMaster(int id, String title, String description, String link, String pubDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String number) {
        this.title = number;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getPubDate() {
        return this.pubDate;
    }

}
