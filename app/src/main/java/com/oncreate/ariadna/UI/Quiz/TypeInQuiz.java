package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.views.PrefixedEditText;


import java.util.Map;

public class TypeInQuiz extends QuizView implements TextWatcher {
    public static final String POSTFIX_KEY = "postfix";
    public static final String PREFIX_KEY = "prefix";
    private Answer answer;
    private PrefixedEditText editText;
    private boolean isUnlocking;
    private OnFilledListener onFilledListener;
    private int preLength;

    /* renamed from: com.sololearn.app.views.quizzes.TypeInQuiz.1 */
    class C06801 implements Runnable {
        final /* synthetic */ Runnable val$callback;

        C06801(Runnable runnable) {
            this.val$callback = runnable;
        }

        public void run() {
            TypeInQuiz.this.unlock(this.val$callback);
        }
    }

    public interface OnFilledListener {
        void onFilled();
    }

    public TypeInQuiz(Context context) {
        super(context);
    }

    protected View onCreateQuiz(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        View view = inflater.inflate(R.layout.quiz_typein, parent, false);
        this.editText = (PrefixedEditText) view.findViewById(R.id.edit_text);
        this.answer = quiz.getAnswer();
        bindAnswerToView(this.editText, this.answer);
        this.editText.setImeOptions(268435456);
        this.editText.addTextChangedListener(this);
        return view;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
        bindAnswerToView(this.editText, answer);
    }

    public void setTextSize(float textSize) {
        this.editText.setTextSize(0, textSize);
    }

    public void setInnerPadding(int left, int top, int right, int bottom) {
        this.editText.setPadding(left, top, right, bottom);
    }

    public int getInnerPaddingLeft() {
        return this.editText.getPaddingLeft();
    }

    public int getInnerPaddingRight() {
        return this.editText.getPaddingRight();
    }

    public int getInnerPaddingTop() {
        return this.editText.getPaddingTop();
    }

    public int getInnerPaddingBottom() {
        return this.editText.getPaddingBottom();
    }

    public static void bindAnswerToView(PrefixedEditText view, Answer answer) {
        Map<String, String> properties = answer.getProperties();
        if (properties.containsKey(PREFIX_KEY)) {
            view.setPrefix((String) properties.get(PREFIX_KEY));
        }
        if (properties.containsKey(POSTFIX_KEY)) {
            view.setPostfix((String) properties.get(POSTFIX_KEY));
        }
        view.setFitText(answer.getText());
    }

    public void check() {
        if (canCheck()) {
            setResult(checkImmediate());
        } else {
            requestInput(this.editText);
        }
    }

    public boolean checkImmediate() {
        if (!ViewCompat.isAttachedToWindow(this)) {
            return false;
        }
        String input = this.editText.getText().toString();
        String answerText = this.answer.getText();
        if (canCheck()) {
            int index = getCorrectCount(input, answerText);
            SpannableString span = new SpannableString(input);
            if (index > 0) {
                span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.green_text)), 0, index, 17);
            }
            if (index < input.length()) {
                span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.wrong_text)), index, input.length(), 17);
            }
            this.editText.setText(span);
            this.editText.setSelection(this.editText.length());
            if (index == answerText.length()) {
                return true;
            }
            return false;
        }
        requestInput(this.editText);
        return false;
    }

    private int getCorrectCount(String input, String answerText) {
        answerText = answerText.toLowerCase();
        int index = 0;
        while (index < answerText.length() && index < input.length() && Character.toLowerCase(input.charAt(index)) == answerText.charAt(index)) {
            index++;
        }
        return index;
    }

    public boolean isFilled() {
        int answerLength = this.answer.getText().length();
        return this.editText.length() == answerLength || this.preLength == answerLength;
    }

    public boolean canCheck() {
        return allowEmptyAnswer() || isFilled();
    }

    public void setOnFilledListener(OnFilledListener onFilledListener) {
        this.onFilledListener = onFilledListener;
    }

    public int getHintMode() {
        return 11;
    }

    public void hint() {
        hintImmediate();
    }

    public boolean hintImmediate() {
        String answerText = this.answer.getText();
        int correctCount = getCorrectCount(this.editText.getText().toString(), answerText);
        if (correctCount < answerText.length()) {
            correctCount++;
            this.editText.setText(answerText.substring(0, correctCount));
            this.editText.setSelection(this.editText.length());
        }
        if (correctCount != answerText.length()) {
            return false;
        }
        check();
        return true;
    }

    public void unlock() {
        this.isUnlocking = true;
        unlock(null);
    }

    public void unlock(Runnable callback) {
        if (hintImmediate()) {
            this.isUnlocking = false;
            if (callback != null) {
                callback.run();
                return;
            }
            return;
        }
        postDelayed(new C06801(callback), 300);
    }

    public void requestInput() {
        requestInput(this.editText);
    }

    protected void onEnableInput() {
        super.onEnableInput();
        this.editText.setFocusable(true);
    }

    protected void onDisableInput() {
        super.onDisableInput();
        this.editText.setFocusable(false);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.preLength = (s.length() - count) + after;
        if (this.preLength != this.answer.getText().length()) {
            return;
        }
        if (this.onFilledListener == null || this.isUnlocking) {
            releaseInput();
        } else {
            this.onFilledListener.onFilled();
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable editable) {
    }

    public float getTimeMargin() {
        float timeMargin = 0.0f;
        for (char c : this.answer.getText().toCharArray()) {
            timeMargin = (float) ((Character.isLetterOrDigit(c) ? 0.5d : 1.0d) + ((double) timeMargin));
        }
        return timeMargin;
    }

    public int getTimeLimit() {
        return (int) (((float) super.getTimeLimit()) + getTimeMargin());
    }

    public void setEditTextId(int id) {
        this.editText.setId(id);
    }
}
