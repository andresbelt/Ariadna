package com.oncreate.ariadna.UI.Quiz;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.oncreate.ariadna.Dialog.LoadingDialog;
import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.HomeActivity;

import java.util.List;

public class QuizSelector extends QuizView {
    private ViewGroup quizContainer;
    private ViewGroup quizFixedContent;
    private View quizOverlay;
    private TextView quizQuestion;
    private ViewGroup quizScrollContent;
    private TextView quizTip;
    private QuizView quizView;
    private ViewGroup rootView;

    public QuizSelector(Context context) {
        super(context);
        this.quizOverlay = null;
        initializeView(context);
    }

    public QuizSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.quizOverlay = null;
        initializeView(context);
    }

    public QuizSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.quizOverlay = null;
        initializeView(context);
    }

    @TargetApi(21)
    public QuizSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.quizOverlay = null;
        initializeView(context);
    }

    private void initializeView(Context context) {
        this.rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.quiz, this, true);
        this.quizQuestion = (TextView) this.rootView.findViewById(R.id.quiz_question);
        this.quizTip = (TextView) this.rootView.findViewById(R.id.quiz_tip);
        this.quizScrollContent = (ViewGroup) this.rootView.findViewById(R.id.quiz_scroll_content);
        this.quizContainer = (ViewGroup) this.rootView.findViewById(R.id.quiz_container);
        this.quizFixedContent = (ViewGroup) this.rootView.findViewById(R.id.quiz_fixed_content);
    }

    public void check() {
        this.quizView.check();
    }

    public void unlock() {
        this.quizView.unlock();
    }

    public void hint() {
        this.quizView.hint();
    }

    public int getHintMode() {
        return this.quizView.getHintMode();
    }

    public void setQuiz(Quiz quiz, List<Answer> shuffledAnswers) {
        if (this.quizView != null) {
            this.quizView.setListener(null);
            this.quizView.setInputListener(null);
            this.quizContainer.removeView(this.quizView);
        }
        this.quiz = quiz;
        switch (quiz.getType()) {
            case HomeActivity.OFFSET_TOOLBAR /*1*/:
                this.quizView = new MultipleChoiceQuiz(getContext());
                break;
            case HomeActivity.OFFSET_TABS /*2*/:
                this.quizView = new TypeInQuiz(getContext());
                break;
            case LoadingDialog.HORIZONTAL_INDETERMINATE /*3*/:
                this.quizView = new MultipleTypeInQuiz(getContext());
                break;
            case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
              //  this.quizView = new StrikeOutQuiz(getContext());
                break;
            case ConnectionResult.INVALID_ACCOUNT /*5*/:
                this.quizView = new ImageChoiceQuiz(getContext());
                break;
            case ConnectionResult.RESOLUTION_REQUIRED /*6*/:
                this.quizView = new PlaceholderQuiz(getContext());
                break;
            case ConnectionResult.NETWORK_ERROR /*7*/:
                this.quizView = new ImagePlaceholderQuiz(getContext());
                break;
            case ConnectionResult.INTERNAL_ERROR /*8*/:
                this.quizView = new ReorderQuiz(getContext());
                break;
        }
        if (this.quizView != null) {
            this.quizView.setInputListener(this.inputListener);
            this.quizView.setAllowEmptyAnswer(allowEmptyAnswer());
            this.quizView.setQuiz(quiz, shuffledAnswers);
            this.quizView.setListener(this.listener);
            this.quizQuestion.setText(this.quizView.getQuestion());
            String tip = this.quizView.getTip();
            if (tip != null) {
                this.quizTip.setText(tip);
                this.quizTip.setVisibility(0);
            } else {
                this.quizTip.setVisibility(8);
            }
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            if (this.quizView.centerInLayout()) {
                layoutParams.gravity = 17;
                layoutParams.height = -2;
            }
            this.quizView.setLayoutParams(layoutParams);
            this.quizContainer.addView(this.quizView);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            this.quizFixedContent.removeAllViews();
            View fixedContent = this.quizView.onCreateFixedContent(inflater, this.quizFixedContent, quiz);
            if (fixedContent != null) {
                this.quizFixedContent.addView(fixedContent);
            }
            if (this.quizOverlay != null) {
                this.rootView.removeView(this.quizOverlay);
            }
            this.quizOverlay = this.quizView.onCreateOverlay(inflater, this.rootView, quiz);
            if (this.quizOverlay != null) {
                this.rootView.addView(this.quizOverlay);
            }
        }
    }

    public Bitmap renderToBitmap() {
        int width = this.quizScrollContent.getWidth();
        int height = this.quizScrollContent.getHeight();
        int fixedHeight = this.quizFixedContent.getHeight();
        Bitmap b = Bitmap.createBitmap(width, height + fixedHeight, Config.RGB_565);
        Canvas c = new Canvas(b);
        c.drawColor(ContextCompat.getColor(getContext(), R.color.app_background_color));
        this.quizScrollContent.draw(c);
        if (fixedHeight > 0) {
            c.translate(0.0f, (float) height);
            this.quizFixedContent.draw(c);
        }
        return b;
    }

    public boolean isInputDisabled() {
        return this.quizView.isInputDisabled();
    }

    public void setInputDisabled(boolean disabled) {
        super.setInputDisabled(disabled);
        this.quizView.setInputDisabled(disabled);
    }

    public int getTimeLimit() {
        return this.quizView.getTimeLimit();
    }

    public List<Answer> getShuffledAnswers() {
        return this.quizView.getShuffledAnswers();
    }
}
