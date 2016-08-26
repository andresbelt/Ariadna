package com.oncreate.ariadna.ModelsVO;

public class LessonState {
    public static final int ACTIVE = 2;
    public static final int DISABLED = 0;
    public static final int NORMAL = 1;
    private int activeQuizId;
    private boolean isStarted;
    private int progressPercent;
    private int state;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getActiveQuizId() {
        return this.activeQuizId;
    }

    public void setActiveQuizId(int activeQuizId) {
        this.activeQuizId = activeQuizId;
    }

    public int getProgressPercent() {
        return this.progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
