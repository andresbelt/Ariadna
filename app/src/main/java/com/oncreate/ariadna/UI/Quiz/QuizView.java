package com.oncreate.ariadna.UI.Quiz;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class QuizView extends FrameLayout {
    public static final int CORRECT = 1;
    public static final int HINT_INTERNAL = 11;
    public static final int HINT_NONE = 10;
    public static final int HINT_POPUP = 12;
    public static final int TIMEOUT = -1;
    private static final int TIME_LIMIT = 30;
    public static final int WRONG = 0;
    private boolean allowEmptyAnswer;
    protected InputListener inputListener;
    private boolean isDisabled;
    protected Listener listener;
    protected Quiz quiz;
    private List<Answer> shuffledAnswers;

    public interface InputListener {
        void onReleaseInput();

        void onRequestInput(View view);
    }

    public interface Listener {
        void onResult(int i);
    }

    public abstract void check();

    public QuizView(Context context) {
        super(context);
        this.allowEmptyAnswer = false;
    }

    public QuizView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.allowEmptyAnswer = false;
    }

    public QuizView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.allowEmptyAnswer = false;
    }

    @TargetApi(21)
    public QuizView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.allowEmptyAnswer = false;
    }

    public List<Answer> getShuffledAnswers() {
        if (this.shuffledAnswers == null) {
            this.shuffledAnswers = new ArrayList(this.quiz.getAnswers());
            Collections.shuffle(this.shuffledAnswers);
        }
        return this.shuffledAnswers;
    }

    protected View onCreateQuiz(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        return null;
    }

    protected View onCreateFixedContent(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        return null;
    }

    protected View onCreateOverlay(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        return null;
    }

    protected boolean centerInLayout() {
        return true;
    }

    public int getHintMode() {
        return HINT_NONE;
    }

    public void hint() {
    }

    public void unlock() {
    }

    protected String getQuestion() {
        return this.quiz.getQuestion();
    }

    protected String getTip() {
        return this.quiz.getTip();
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        setQuiz(quiz, null);
    }

    protected void setQuiz(Quiz quiz, List<Answer> shuffledAnswers) {
        this.quiz = quiz;
        this.shuffledAnswers = shuffledAnswers;
        removeAllViews();
        if (quiz != null) {
            View newQuiz = onCreateQuiz(LayoutInflater.from(getContext()), this, quiz);
            if (newQuiz != null) {
                addView(newQuiz);
            }
            onEnableInput();
        }
    }

    public Listener getListener() {
        return this.listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInputListener(InputListener listener) {
        this.inputListener = listener;
    }

    protected void setResult(boolean isCorrect) {
        onDisableInput();
        if (this.listener != null) {
            this.listener.onResult(isCorrect ? CORRECT : 0);
        }
    }

    public boolean allowEmptyAnswer() {
        return this.allowEmptyAnswer;
    }

    public void setAllowEmptyAnswer(boolean allowEmptyAnswer) {
        this.allowEmptyAnswer = allowEmptyAnswer;
    }

    protected void requestInput(View view) {
        if (this.inputListener != null) {
            this.inputListener.onRequestInput(view);
        }
    }

    protected void releaseInput() {
        if (this.inputListener != null) {
            this.inputListener.onReleaseInput();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isDisabled || super.onInterceptTouchEvent(ev);
    }

    protected void onDisableInput() {
        this.isDisabled = true;
        releaseInput();
    }

    protected void onEnableInput() {
        this.isDisabled = false;
    }

    public boolean isInputDisabled() {
        return this.isDisabled;
    }

    public void setInputDisabled(boolean disabled) {
        if (disabled) {
            onDisableInput();
        } else {
            onEnableInput();
        }
    }

    public int getTimeLimit() {
        return (int) (30.0f + (Math.max(0.0f, ((float) this.quiz.getQuestion().length()) - 50.0f) / 15.0f));
    }
}
