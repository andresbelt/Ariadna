package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuiz extends LoadingQuizView {
    protected int correctAnswerCount;
    protected AbsListView listView;
    protected List<Answer> shuffledAnswers;

    public MultipleChoiceQuiz(Context context) {
        super(context);
    }

    protected final View onCreateQuiz(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        int i;
        View rootView = inflater.inflate(getLayout(), parent, false);
        this.shuffledAnswers = getShuffledAnswers();
        this.correctAnswerCount = 0;
        ArrayList<String> answers = new ArrayList();
        for (Answer answer : this.shuffledAnswers) {
            answers.add(answer.getText());
            if (answer.isCorrect()) {
                this.correctAnswerCount++;
            }
        }
        this.listView = (AbsListView) rootView.findViewById(R.id.list_view);
        this.listView.setAdapter(createAdapter(quiz, answers));
        AbsListView absListView = this.listView;
        if (this.correctAnswerCount > 1) {
            i = 2;
        } else {
            i = 1;
        }
        absListView.setChoiceMode(i);
        onPostCreateQuiz(rootView);
        return rootView;
    }

    protected void onPostCreateQuiz(View view) {

    }

    @LayoutRes
    protected int getLayout() {
        return R.layout.quiz_multiple_choice;
    }

    @LayoutRes
    protected int getItemLayout() {
        return this.correctAnswerCount > 1 ? R.layout.quiz_multiple_choice_item : R.layout.quiz_single_choice_item;
    }

    protected ListAdapter createAdapter(Quiz quiz, List<String> answers) {
        return new ArrayAdapter(getContext(), getItemLayout(), answers);
    }

    protected SparseBooleanArray getCheckedItemPositions() {
        return this.listView.getCheckedItemPositions();
    }

    public void check() {
        SparseBooleanArray checked = getCheckedItemPositions();
        boolean isCorrect = true;
        int checkedCount = 0;
        if (checked != null) {
            for (int i = 0; i < checked.size(); i++) {
                if (checked.valueAt(i)) {
                    checkedCount++;
                    if (!(this.shuffledAnswers.get(checked.keyAt(i))).isCorrect()) {
                        isCorrect = false;
                        break;
                    }
                }
            }
            if (isCorrect && this.correctAnswerCount == checkedCount) {
                isCorrect = true;
            } else {
                isCorrect = false;
            }
        } else {
            isCorrect = this.correctAnswerCount == 0;
        }
        setResult(isCorrect);
    }

    public void unlock() {
        for (int i = 0; i < this.shuffledAnswers.size(); i++) {
            this.listView.setItemChecked(i, ((Answer) this.shuffledAnswers.get(i)).isCorrect());
        }
        check();
    }

    protected String getTip() {
        String quizTip = this.quiz.getTip();
        if (quizTip != null) {
            return quizTip;
        }
        if (this.correctAnswerCount > 1) {
            return getResources().getString(R.string.quiz_multiple_choice_tip);
        }
        return null;
    }
}
