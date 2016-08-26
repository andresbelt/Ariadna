package com.oncreate.ariadna;


import com.oncreate.ariadna.ModelsVO.Achievement;

import java.util.List;

public class PushResult extends ExperienceResult {
    private List<Achievement> achievements;
    private int[] exchanges;
    private int[] lessons;
    private int[] quizzes;

    public int[] getLessons() {
        return this.lessons;
    }

    public int[] getQuizzes() {
        return this.quizzes;
    }

    public int[] getExchanges() {
        return this.exchanges;
    }

    public List<Achievement> getAchievements() {
        return this.achievements;
    }
}
