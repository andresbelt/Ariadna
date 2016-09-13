package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Util.ConstantVariables;

public class MultipleTypeInQuiz extends PlaceholderQuizBase {
    private boolean isUnlocking;
    private SparseArray<TypeInQuiz> typeIns;

    /* renamed from: com.sololearn.app.views.quizzes.MultipleTypeInQuiz.2 */
    class C06712 implements Runnable {
        final /* synthetic */ int val$index;

        /* renamed from: com.sololearn.app.views.quizzes.MultipleTypeInQuiz.2.1 */
        class C06701 implements Runnable {
            C06701() {
            }

            public void run() {
                MultipleTypeInQuiz.this.unlockItem(C06712.this.val$index + 1);
            }
        }

        C06712(int i) {
            this.val$index = i;
        }

        public void run() {
            MultipleTypeInQuiz.this.postDelayed(new C06701(), 300);
        }
    }

    /* renamed from: com.sololearn.app.views.quizzes.MultipleTypeInQuiz.1 */
    class C12891 implements TypeInQuiz.OnFilledListener {
        C12891() {
        }

        public void onFilled() {
            if (!MultipleTypeInQuiz.this.isUnlocking) {
                int i = 0;
                while (i < MultipleTypeInQuiz.this.typeIns.size()) {
                    TypeInQuiz typeInQuiz = (TypeInQuiz) MultipleTypeInQuiz.this.typeIns.get(i);
                    if (typeInQuiz.isFilled()) {
                        i++;
                    } else {
                        typeInQuiz.requestInput();
                        return;
                    }
                }
                MultipleTypeInQuiz.this.releaseInput();
            }
        }
    }

    public MultipleTypeInQuiz(Context context) {
        super(context);
    }

    protected void onPreCreateQuiz() {
        super.onPreCreateQuiz();
        this.typeIns = new SparseArray();
    }

    protected View getPlaceholder(ViewGroup parent, int index) {
        Answer answer = (Answer) this.quiz.getAnswers().get(index);
        TypeInQuiz typeIn = new TypeInQuiz(getContext());
        typeIn.setInputListener(this.inputListener);
        typeIn.setAllowEmptyAnswer(allowEmptyAnswer());
        typeIn.setQuiz(this.quiz);
        typeIn.setAnswer(answer);
        typeIn.setOnFilledListener(new C12891());
        typeIn.setTextSize(this.textSize);
        LayoutParams lp = new LayoutParams(-2, -2);
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.quiz_multiple_typein_padding_top);
        int paddingBottom = getResources().getDimensionPixelSize(R.dimen.quiz_multiple_typein_padding_bottom);
        lp.leftMargin = -typeIn.getInnerPaddingLeft();
        lp.rightMargin = -typeIn.getInnerPaddingRight();
        lp.topMargin = -paddingTop;
        lp.bottomMargin = -paddingBottom;
        typeIn.setInnerPadding(typeIn.getInnerPaddingLeft(), paddingTop, typeIn.getInnerPaddingRight(), paddingBottom);
        typeIn.setLayoutParams(lp);
        this.typeIns.put(index, typeIn);
        typeIn.setEditTextId(index + 1);
        return typeIn;
    }

    protected String getReplacement(int index) {
        Answer answer = (Answer) this.quiz.getAnswers().get(index);
        String replacement = ConstantVariables.VERSION_NAME;
        if (answer.getProperties().containsKey(TypeInQuiz.PREFIX_KEY)) {
            replacement = replacement + ((String) answer.getProperties().get(TypeInQuiz.PREFIX_KEY));
        }
        replacement = replacement + answer.getText();
        if (answer.getProperties().containsKey(TypeInQuiz.POSTFIX_KEY)) {
            return replacement + ((String) answer.getProperties().get(TypeInQuiz.POSTFIX_KEY));
        }
        return replacement;
    }

    public void check() {
        if (!this.isUnlocking && canCheck()) {
            int i = 0;
            while (i < this.typeIns.size()) {
                TypeInQuiz typeIn = (TypeInQuiz) this.typeIns.valueAt(i);
                if (typeIn.canCheck()) {
                    i++;
                } else {
                    typeIn.requestInput();
                    return;
                }
            }
            boolean isCorrect = true;
            for (i = 0; i < this.typeIns.size(); i++) {
                if (!((TypeInQuiz) this.typeIns.valueAt(i)).checkImmediate()) {
                    isCorrect = false;
                }
            }
            setResult(isCorrect);
        }
    }

    public int getHintMode() {
        return 11;
    }

    public void hint() {
        boolean isFilled = true;
        for (int i = 0; i < this.typeIns.size(); i++) {
            if (!((TypeInQuiz) this.typeIns.valueAt(i)).hintImmediate()) {
                isFilled = false;
            }
        }
        if (isFilled) {
            setResult(true);
        }
    }

    public void unlock() {
        this.isUnlocking = true;
        unlockItem(0);
    }

    private void unlockItem(int index) {
        if (!ViewCompat.isAttachedToWindow(this)) {
            this.isUnlocking = false;
        } else if (index == this.typeIns.size()) {
            this.isUnlocking = false;
            setResult(true);
        } else {
            ((TypeInQuiz) this.typeIns.valueAt(index)).unlock(new C06712(index));
        }
    }

    protected void onDisableInput() {
        super.onDisableInput();
        for (int i = 0; i < this.typeIns.size(); i++) {
            ((TypeInQuiz) this.typeIns.valueAt(i)).onDisableInput();
        }
    }

    protected void onEnableInput() {
        super.onEnableInput();
        for (int i = 0; i < this.typeIns.size(); i++) {
            ((TypeInQuiz) this.typeIns.valueAt(i)).onEnableInput();
        }
    }

    public int getTimeLimit() {
        int limit = super.getTimeLimit();
        for (int i = 0; i < this.typeIns.size(); i++) {
            limit = (int) (((TypeInQuiz) this.typeIns.valueAt(i)).getTimeMargin() + ((float) limit));
        }
        return limit;
    }
}
