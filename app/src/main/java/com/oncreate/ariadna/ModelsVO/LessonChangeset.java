package com.oncreate.ariadna.ModelsVO;

import java.util.Date;

public class LessonChangeset {
    private int activeQuizId;
    private int attempt;
    private boolean isStarted;
    private int lessonId;
    private Date progressDate;
    private float score;

    public LessonChangeset(int lessonId, float score, int attempt, boolean isStarted, int activeQuizId) {
        this.lessonId = lessonId;
        this.score = score;
        this.attempt = attempt;
        this.isStarted = isStarted;
        this.activeQuizId = activeQuizId;
        this.progressDate = new Date();
    }

    public LessonChangeset(LessonProgress lessonProgress) {
        this.lessonId = lessonProgress.getLessonId();
        this.score = lessonProgress.getScore();
        this.attempt = lessonProgress.getAttempt();
        this.isStarted = lessonProgress.getIsStarted().booleanValue();
        this.activeQuizId = lessonProgress.getActiveQuizId();
        this.progressDate = new Date();
    }
}
