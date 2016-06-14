package com.fr.design.style.background;

/**
 * Created by richie on 16/5/18.
 */
public class BackgroundUIWrapper {

    public static BackgroundUIWrapper create() {
        return new BackgroundUIWrapper();
    }

    private int index = -1;
    private String title;
    private Class<? extends BackgroundDetailPane> clazz;

    private BackgroundUIWrapper() {

    }

    public Class<? extends BackgroundDetailPane> getType() {
        return clazz;
    }

    public BackgroundUIWrapper setType(Class<? extends BackgroundDetailPane> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BackgroundUIWrapper setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getIndex() {
        return index;
    }


    public BackgroundUIWrapper setIndex(int index) {
        if (this.index == -1) {
            this.index = index;
        }
        return this;
    }
}
