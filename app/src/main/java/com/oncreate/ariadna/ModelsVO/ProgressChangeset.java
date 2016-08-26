package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;

public class ProgressChangeset {
    private ArrayList<LessonChangeset> lessonProgress;
    private ArrayList<Exchange> pointExchanges;
    private ArrayList<QuizChangeset> quizProgress;

    public ProgressChangeset() {
        this.lessonProgress = new ArrayList();
        this.quizProgress = new ArrayList();
        this.pointExchanges = new ArrayList();
    }

    public void addLesson(LessonProgress progress) {
        this.lessonProgress.add(new LessonChangeset(progress));
    }

    public void addQuiz(QuizProgress progress) {
        this.quizProgress.add(new QuizChangeset(progress));
    }

    public void addExchange(int quizId, int points, int action) {
        this.pointExchanges.add(new Exchange(quizId, points, action));
    }

    public void clear() {
        this.lessonProgress.clear();
        this.quizProgress.clear();
        this.pointExchanges.clear();
    }

    public void clearTop(int lessonCount, int quizCount, int exchangeCount) {
        while (lessonCount > 0 && this.lessonProgress.size() > 0) {
            this.lessonProgress.remove(0);
            lessonCount--;
        }
        while (quizCount > 0 && this.quizProgress.size() > 0) {
            this.quizProgress.remove(0);
            quizCount--;
        }
        while (exchangeCount > 0 && this.pointExchanges.size() > 0) {
            this.pointExchanges.remove(0);
            exchangeCount--;
        }
    }

    public boolean isEmpty() {
        return this.lessonProgress.size() == 0 && this.quizProgress.size() == 0 && this.pointExchanges.size() == 0;
    }
}
