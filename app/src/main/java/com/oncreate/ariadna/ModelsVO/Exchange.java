package com.oncreate.ariadna.ModelsVO;

import java.util.Date;

public class Exchange {
    private int action;
    private Date exchangeDate;
    private int points;
    private int quizId;

    public Exchange(int quizId, int points, int action) {
        this.quizId = quizId;
        this.points = points;
        this.action = action;
        this.exchangeDate = new Date();
    }
}
