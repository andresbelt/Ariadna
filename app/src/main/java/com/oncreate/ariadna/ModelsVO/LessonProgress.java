package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;
import java.util.List;

public class LessonProgress {
    private int activeQuizId;
    private int attempt;
    private float bestScore;
    private boolean isCompleted;
    private boolean isStarted;
    private int lessonId;
    private List<QuizProgress> quizzes;
    private float score;

    public LessonProgress() {
        setQuizzes(new ArrayList());
    }

    public int getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getBestScore() {
        return this.bestScore;
    }

    public void setBestScore(float bestScore) {
        this.bestScore = bestScore;
    }

    public Boolean getIsStarted() {
        return Boolean.valueOf(this.isStarted);
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted = isStarted.booleanValue();
    }

    public int getAttempt() {
        return this.attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public Boolean getIsCompleted() {
        return Boolean.valueOf(this.isCompleted);
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted.booleanValue();
    }

    public int getActiveQuizId() {
        return this.activeQuizId;
    }

    public void setActiveQuizId(int activeQuizId) {
        this.activeQuizId = activeQuizId;
    }

    public List<QuizProgress> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(List<QuizProgress> quizzes) {
        this.quizzes = quizzes;
    }
}
