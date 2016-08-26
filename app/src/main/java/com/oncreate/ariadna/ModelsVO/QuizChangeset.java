package com.oncreate.ariadna.ModelsVO;

import java.util.Date;

public class QuizChangeset {
    private int attempt;
    private Date progressDate;
    private int quizId;
    private float score;
    private int time;

    public QuizChangeset(int quizId, float score, int attempt, int time) {
        this.quizId = quizId;
        this.score = score;
        this.attempt = attempt;
        this.time = time;
        this.progressDate = new Date();
    }

    public QuizChangeset(QuizProgress quizProgress) {
        this.quizId = quizProgress.getQuizId();
        this.score = quizProgress.getScore();
        this.attempt = quizProgress.getAttempt();
        this.time = quizProgress.getTime();
        this.progressDate = new Date();
    }
}
