package com.oncreate.ariadna.ModelsVO;

import java.util.Date;

public class PostBase implements IUserItem {
    private Date date;
    private boolean hasAvatar;
    private int id;
    private boolean inEditMode;
    private String message;
    private int parentId;
    private int userId;
    private String userName;
    private int vote;
    private int votes;

    public int getId() {
        return this.id;
    }

    public int getParentId() {
        return this.parentId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.userName;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean hasAvatar() {
        return this.hasAvatar;
    }

    public Date getDate() {
        return this.date;
    }

    public int getVotes() {
        return this.votes;
    }

    public int getVote() {
        return this.vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
    }

    public boolean isInEditMode() {
        return this.inEditMode;
    }
}
