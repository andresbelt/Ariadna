package com.oncreate.ariadna.ModelsVO;

import java.util.List;

public class Quiz {
    public static final int DRAG_N_DROP = 6;
    public static final int IMAGE_CHOICE = 5;
    public static final int IMAGE_DRAG_N_DROP = 7;
    public static final int IMAGE_REORDER = 9;
    public static final int MULTIPLE_CHOICE = 1;
    public static final int MULTIPLE_TYPE_IN = 3;
    public static final int REORDER = 8;
    public static final int STRIKE_OUT = 4;
    public static final int TYPE_IN = 2;
    private List<Answer> answers;
    private String hint;
    private int id;
    private String linkedVideoId;
    private String question;
    private String tip;
    private int type;
    private float videoEnd;
    private float videoStart;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public float getVideoStart() {
        return this.videoStart;
    }

    public void setVideoStart(float videoStart) {
        this.videoStart = videoStart;
    }

    public float getVideoEnd() {
        return this.videoEnd;
    }

    public void setVideoEnd(float videoEnd) {
        this.videoEnd = videoEnd;
    }

    public String getLinkedVideoId() {
        return this.linkedVideoId;
    }

    public void setLinkedVideoId(String linkedVideoId) {
        this.linkedVideoId = linkedVideoId;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getAnswer() {
        return getAnswer(0);
    }

    public Answer getAnswer(int index) {
        return (Answer) getAnswers().get(index);
    }
}
