package com.fr.design.remote;

public class RemoteMember {

    public static final RemoteMember DEFAULT_MEMBER = new RemoteMember("查询中...");


    private String name;

    private boolean selected;

    public RemoteMember(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
