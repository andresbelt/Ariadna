package com.oncreate.ariadna.ModelsVO;

import java.util.List;

public class Lesson {
    public static final int MODE_NONE = 0;
    public static final int MODE_TEXT = 1;
    public static final int MODE_TEXT_WITH_VIDEO = 2;
    public static final int MODE_VIDEO = 3;
    public static final int MODE_VIDEO_WITH_TEXT = 4;
    public static final int TYPE_CHECKPOINT = 0;
    public static final int TYPE_QUIZ = 1;
    private int id;
    private boolean isRestricted;
    private boolean isShortcut;
    private int mode;
    private String name;
    private List<Quiz> quizzes;
    private String tags;
    private int type;
    private String videoId;

    public Lesson() {
        this.isShortcut = false;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String value) {
        this.videoId = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int value) {
        this.type = value;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int value) {
        this.mode = value;
    }

    public boolean getIsRestricted() {
        return this.isRestricted;
    }

    public void setIsRestricted(boolean value) {
        this.isRestricted = value;
    }

    public boolean getIsShortcut() {
        return this.isShortcut;
    }

    public void setIsShortcut(boolean value) {
        this.isShortcut = value;
    }

    public List<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public Quiz getQuiz(int index) {
        if (index >= this.quizzes.size()) {
            return null;
        }
        return (Quiz) this.quizzes.get(index);
    }

    public Quiz getQuizById(int id) {
        for (Quiz quiz : this.quizzes) {
            if (quiz.getId() == id) {
                return quiz;
            }
        }
        return null;
    }

    public int getVideoDuration() {
        int duration = TYPE_CHECKPOINT;
        for (Quiz quiz : this.quizzes) {
            duration = (int) (((float) duration) + (quiz.getVideoEnd() - quiz.getVideoStart()));
        }
        return duration;
    }

    public String getTags() {
        return this.tags;
    }
}
