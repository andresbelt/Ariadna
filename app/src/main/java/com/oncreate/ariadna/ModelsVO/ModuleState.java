package com.oncreate.ariadna.ModelsVO;

public class ModuleState {
    public static final int ACTIVE = 2;
    public static final int DISABLED = 0;
    public static final int NORMAL = 1;
    private int completedItems;
    private int completedLessons;
    private int state;
    private int totalItems;
    private int totalLessons;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCompletedLessons() {
        return this.completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public int getCompletedItems() {
        return this.completedItems;
    }

    public void setCompletedItems(int completedItems) {
        this.completedItems = completedItems;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalLessons() {
        return this.totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }
}
