package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;

public class Module {
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_LEFT = 2;
    public static final int ALIGNMENT_NONE = 0;
    public static final int ALIGNMENT_RIGHT = 3;
    private int alignment;
    private boolean allowShortcut;
    private int groupId;
    private int hintPrice;
    private int id;
    private ArrayList<Lesson> lessons;
    private String name;
    private int rewardXp;
    private int skipPrice;

    public Module() {
        this.alignment = ALIGNMENT_NONE;
        this.lessons = new ArrayList();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int value) {
        this.groupId = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getHintPrice() {
        return this.hintPrice;
    }

    public void setHintPrice(int value) {
        this.hintPrice = value;
    }

    public int getSkipPrice() {
        return this.skipPrice;
    }

    public void setSkipPrice(int value) {
        this.skipPrice = value;
    }

    public int getRewardXp() {
        return this.rewardXp;
    }

    public void setRewardXp(int value) {
        this.rewardXp = value;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setAlignment(int value) {
        this.alignment = value;
    }

    public ArrayList<Lesson> getLessons() {
        return this.lessons;
    }

    public void setLessons(ArrayList<Lesson> value) {
        this.lessons = value;
    }

    public boolean getAllowShortcut() {
        return this.allowShortcut;
    }

    public void setAllowShortcut(boolean value) {
        this.allowShortcut = value;
    }

    public boolean isAllowShortcut() {
        return this.allowShortcut;
    }

    public Lesson getLesson(int index) {
        return (Lesson) this.lessons.get(index);
    }

    public int getLessonCount() {
        return this.lessons.size();
    }
}
