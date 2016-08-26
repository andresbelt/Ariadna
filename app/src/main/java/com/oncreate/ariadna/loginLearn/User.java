package com.oncreate.ariadna.loginLearn;

public class User {
    private String alternateName;
    private String email;
    private boolean hasAvatar;
    private int id;
    private int level;
    private String name;
    private int xp;

    public String getAlternateName() {
        return this.alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public boolean hasAvatar() {
        return this.hasAvatar;
    }

    public void setAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
