package com.fr.design.remote;

public class RemoteMember {

    public static final RemoteMember DEFAULT_MEMBER = new RemoteMember("查询中...");


    private String username;
    private String realName;
    private String userId;

    private boolean selected;

    public RemoteMember() {

    }

    public RemoteMember(String username) {
        this.username = username;
    }

    public RemoteMember username(String username) {
        this.username = username;
        return this;
    }

    public RemoteMember realName(String realName) {
        this.realName = realName;
        return this;
    }

    public RemoteMember userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
