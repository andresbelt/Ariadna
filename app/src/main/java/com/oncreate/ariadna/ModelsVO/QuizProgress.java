package com.oncreate.ariadna.ModelsVO;

public class QuizProgress {
    private int attempt;
    private boolean isCompleted;
    private int quizId;
    private float score;
    private int time;

    public int getQuizId() {
        return this.quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getAttempt() {
        return this.attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
