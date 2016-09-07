package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;

import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.Dialog.LoadingDialog;
import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.HomeActivity;
import com.oncreate.ariadna.UI.views.PlaceholderTextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlaceholderQuiz extends PlaceholderQuizBase {
    private DragManager dragManager;
    private View fixedContent;
    protected ArrayList<Item> items;
    protected FrameLayout overlay;
    protected SparseArray<Placeholder> placeholders;
    protected ViewGroup sourceLayout;

    /* renamed from: com.sololearn.app.views.quizzes.PlaceholderQuiz.1 */
    class C06721 implements Runnable {
        final /* synthetic */ int val$index;

        C06721(int i) {
            this.val$index = i;
        }

        public void run() {
            PlaceholderQuiz.this.unlockPreStep(this.val$index + 1);
        }
    }

    /* renamed from: com.sololearn.app.views.quizzes.PlaceholderQuiz.2 */
    class C06732 implements Runnable {
        final /* synthetic */ int val$index;

        C06732(int i) {
            this.val$index = i;
        }

        public void run() {
            PlaceholderQuiz.this.unlockStep(this.val$index + 1);
        }
    }

    protected class DragManager implements OnTouchListener {
        private boolean clickPending;
        private View dragShadow;
        private Rect fromRect;
        protected Item item;
        private int minDragMove;
        private Rect overlayRect;
        protected Placeholder placeholder;
        private SparseArray<Rect> placeholderLocations;
        protected Placeholder selectedPlaceholder;
        private float touchX;
        private float touchY;

        /* renamed from: com.sololearn.app.views.quizzes.PlaceholderQuiz.DragManager.1 */
        class C06741 implements Runnable {
            final /* synthetic */ Runnable val$endAction;
            final /* synthetic */ View val$shadow;
            final /* synthetic */ Item val$toItem;

            C06741(View view, Item item, Runnable runnable) {
                this.val$shadow = view;
                this.val$toItem = item;
                this.val$endAction = runnable;
            }

            public void run() {
                PlaceholderQuiz.this.overlay.removeView(this.val$shadow);
                this.val$toItem.setEnabled(true);
                if (this.val$endAction != null) {
                    this.val$endAction.run();
                }
            }
        }

        /* renamed from: com.sololearn.app.views.quizzes.PlaceholderQuiz.DragManager.2 */
        class C06752 implements Runnable {
            final /* synthetic */ Runnable val$endAction;
            final /* synthetic */ Item val$moveItem;
            final /* synthetic */ View val$shadow;
            final /* synthetic */ Placeholder val$toPlaceholder;

            C06752(Placeholder placeholder, Item item, View view, Runnable runnable) {
                this.val$toPlaceholder = placeholder;
                this.val$moveItem = item;
                this.val$shadow = view;
                this.val$endAction = runnable;
            }

            public void run() {
                this.val$toPlaceholder.setItem(this.val$moveItem);
                this.val$toPlaceholder.setCandidate(null);
                PlaceholderQuiz.this.overlay.removeView(this.val$shadow);
                if (this.val$endAction != null) {
                    this.val$endAction.run();
                }
            }
        }

        protected DragManager() {
            this.item = null;
            this.placeholder = null;
            this.selectedPlaceholder = null;
            this.placeholderLocations = new SparseArray();
            this.touchX = 0.0f;
            this.touchY = 0.0f;
            this.clickPending = false;
        }

        protected void onClick(View view) {
            if (this.placeholder == null) {
                for (int i = 0; i < PlaceholderQuiz.this.placeholders.size(); i++) {
                    Placeholder placeholder = (Placeholder) PlaceholderQuiz.this.placeholders.valueAt(i);
                    if (placeholder.isEmpty()) {
                        moveToPlaceholder(this.dragShadow, this.item, placeholder, null);
                        return;
                    }
                }
            } else {
                this.placeholder.setCandidate(null);
            }
            moveToOrigin(this.dragShadow, this.item, null);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PlaceholderQuiz.this.isInputDisabled()) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            switch (motionEvent.getActionMasked()) {
                case 0/*0*/:
                    this.minDragMove = PlaceholderQuiz.this.getResources().getDimensionPixelSize(R.dimen.min_drag_move);
                    this.touchX = rawX;
                    this.touchY = rawY;
                    return prepareDrag(view);
                case HomeActivity.OFFSET_TOOLBAR /*1*/:
                case LoadingDialog.HORIZONTAL_INDETERMINATE /*3*/:
                    if (this.clickPending) {
                        onClick(view);
                        if (this.selectedPlaceholder != null) {
                            this.selectedPlaceholder.setCandidate(null);
                        }
                    } else if (this.selectedPlaceholder != null) {
                        moveToPlaceholder(this.dragShadow, this.item, this.selectedPlaceholder, null);
                    } else {
                        moveToOrigin(this.dragShadow, this.item, null);
                    }
                    this.selectedPlaceholder = null;
                    this.placeholder = null;
                    this.item = null;
                    return true;
                case HomeActivity.OFFSET_TABS /*2*/:
                    if (this.clickPending && Math.abs(this.touchX - rawX) > ((float) this.minDragMove) && Math.abs(this.touchY - rawX) > ((float) this.minDragMove)) {
                        this.clickPending = false;
                    }
                    this.dragShadow.setTranslationX((((float) this.fromRect.left) + rawX) - this.touchX);
                    this.dragShadow.setTranslationY((((float) this.fromRect.top) + rawY) - this.touchY);
                    Placeholder newPlaceholder = findClosestPlaceholder(rawX, rawY);
                    if (newPlaceholder != this.selectedPlaceholder) {
                        if (this.selectedPlaceholder != null) {
                            this.selectedPlaceholder.setCandidate(null);
                        }
                        if (newPlaceholder != null) {
                            newPlaceholder.setCandidate(this.item);
                        }
                        this.selectedPlaceholder = newPlaceholder;
                    }
                    return true;
                default:
                    return true;
            }
        }

        private boolean prepareDrag(View view) {
            Object tag = view.getTag();
            boolean fromPlaceholder = false;
            if (tag instanceof Item) {
                this.item = (Item) tag;
            } else if (tag instanceof Placeholder) {
                this.placeholder = (Placeholder) tag;
                this.item = this.placeholder.getItem();
                if (this.item != null) {
                    this.placeholder.setItem(null);
                    fromPlaceholder = true;
                }
            }
            if (this.item == null || (!this.item.isEnabled() && !fromPlaceholder)) {
                return false;
            }
            updateOverlayRect();
            this.fromRect = getViewRect(view, this.overlayRect);
            this.dragShadow = createShadow(this.item, this.fromRect);
            this.item.setEnabled(false);
            this.clickPending = true;
            updatePlaceholderPositions();
            ViewParent parent = PlaceholderQuiz.this.getParent();
            if (parent == null) {
                return true;
            }
            parent.requestDisallowInterceptTouchEvent(true);
            return true;
        }

        private View createShadow(Item item, Rect rect) {
            View shadow = PlaceholderQuiz.this.createItem(PlaceholderQuiz.this.overlay, item.getContent()).getView();
            shadow.setTranslationX((float) rect.left);
            shadow.setTranslationY((float) rect.top);
            PlaceholderQuiz.this.overlay.addView(shadow);
            return shadow;
        }

        private void moveToOrigin(View shadow, Item toItem, Runnable endAction) {
            Rect toRect = getViewRect(toItem.getView(), this.overlayRect);
            ViewCompat.animate(shadow).translationX((float) toRect.left).translationY((float) toRect.top).setDuration(300).withEndAction(new C06741(shadow, toItem, endAction));
        }

        private void moveToPlaceholder(View shadow, Item moveItem, Placeholder toPlaceholder, Runnable endAction) {
            Rect toRect = getViewRect(toPlaceholder.getView(), this.overlayRect);
            clearPlaceholder(toPlaceholder, null);
            toPlaceholder.setCandidate(moveItem);
            ViewCompat.animate(shadow).translationX((float) toRect.left).translationY((float) toRect.top).setDuration(300).withEndAction(new C06752(toPlaceholder, moveItem, shadow, endAction));
        }

        public void moveToPlaceholder(Item moveItem, Placeholder toPlaceholder, Runnable endAction) {
            updateOverlayRect();
            moveToPlaceholder(createShadow(moveItem, getViewRect(moveItem.getView(), this.overlayRect)), moveItem, toPlaceholder, endAction);
        }

        public void clearPlaceholder(Placeholder clearPlaceholder, Runnable endAction) {
            updateOverlayRect();
            Item currentItem = clearPlaceholder.getItem();
            if (currentItem != null) {
                View shadow = createShadow(currentItem, getViewRect(clearPlaceholder.getView(), this.overlayRect));
                clearPlaceholder.setItem(null);
                if (this.placeholder != null) {
                    moveToPlaceholder(shadow, currentItem, this.placeholder, endAction);
                } else {
                    moveToOrigin(shadow, currentItem, endAction);
                }
            } else if (endAction != null) {
                endAction.run();
            }
        }

        private void updateOverlayRect() {
            this.overlayRect = getViewRect(PlaceholderQuiz.this.overlay, null);
        }

        private void updatePlaceholderPositions() {
            for (int i = 0; i < PlaceholderQuiz.this.placeholders.size(); i++) {
                int index = PlaceholderQuiz.this.placeholders.keyAt(i);
                Placeholder placeholder = (Placeholder) PlaceholderQuiz.this.placeholders.get(index);
                Rect rect = (Rect) this.placeholderLocations.get(index);
                if (rect == null) {
                    rect = new Rect();
                    this.placeholderLocations.put(index, rect);
                }
                getViewRect(placeholder.getView(), rect, null);
            }
        }

        protected Rect getViewRect(View view, Rect relative) {
            Rect rect = new Rect();
            getViewRect(view, rect, relative);
            return rect;
        }

        protected void getViewRect(View view, Rect rect, Rect relative) {
            view.getGlobalVisibleRect(rect);
            if (relative != null) {
                rect.offset(-relative.left, -relative.top);
            }
        }

        private Placeholder findClosestPlaceholder(float dragX, float dragY) {
            float minDistance = -1.0f;
            int closest = -1;
            for (int i = 0; i < this.placeholderLocations.size(); i++) {
                Rect r = (Rect) this.placeholderLocations.valueAt(i);
                float dx = dragX - r.exactCenterX();
                float dy = dragY - r.exactCenterY();
                float distance = (dx * dx) + (dy * dy);
                if (distance < minDistance || minDistance == -1.0f) {
                    minDistance = distance;
                    closest = i;
                }
            }
            if (closest == -1 || Math.sqrt((double) minDistance) >= 200.0d) {
                return null;
            }
            return (Placeholder) PlaceholderQuiz.this.placeholders.valueAt(closest);
        }
    }

    protected static class Item {
        private String content;
        protected boolean isEnabled;
        private View view;

        public Item(Context context, ViewGroup parent, float textSize, String content) {
            this.isEnabled = true;
            this.content = content;
            this.view = createView(context, parent, textSize, content);
            this.view.setTag(this);
        }

        protected View createView(Context context, ViewGroup parent, float textSize, String content) {
            TextView textView = new TextView(context);
            int p = context.getResources().getDimensionPixelSize(R.dimen.quiz_placeholder_item_padding);
            textView.setPadding(p, p, p, p);
            textView.setTextSize(0, textSize);
            textView.setTextColor(ContextCompat.getColor(context, R.color.app_accent_color_700));
            textView.setText(content);
            return textView;
        }

        public final View getView() {
            return this.view;
        }

        public final String getContent() {
            return this.content;
        }

        public void setEnabled(boolean enabled) {
            this.isEnabled = enabled;
            ViewCompat.animate(this.view).alpha(enabled ? DefaultRetryPolicy.DEFAULT_BACKOFF_MULT : 0.5f);
        }

        public boolean isEnabled() {
            return this.isEnabled;
        }
    }

    protected static class Placeholder {
        protected int activeColor;
        protected Item candidate;
        protected int color;
        protected Item item;
        private String placeholder;
        protected View view;

        public Placeholder(Context context, ViewGroup parent, float textSize, String placeholder) {
            this.item = null;
            this.candidate = null;
            this.placeholder = placeholder;
            this.color = ContextCompat.getColor(context, R.color.app_accent_color_700);
            this.activeColor = ContextCompat.getColor(context, R.color.app_primary_color_700);
            this.view = createView(context, parent, textSize, placeholder);
            this.view.setTag(this);
        }

        protected View createView(Context context, ViewGroup parent, float textSize, String placeholder) {
            PlaceholderTextView textView = new PlaceholderTextView(context);
            textView.setTextSize(0, textSize);
            textView.setPlaceholder(placeholder);
            textView.setTextColor(this.color);
            return textView;
        }

        public final View getView() {
            return this.view;
        }

        public final String getPlaceholder() {
            return this.placeholder;
        }

        protected void setContent(String content) {
            ((TextView) this.view).setText(content);
        }

        public final Item getItem() {
            return this.item;
        }

        public final void setItem(Item item) {
            if (this.item != null) {
                this.item.setEnabled(true);
            }
            this.item = item;
            if (item != null) {
                item.setEnabled(false);
                setContent(item.getContent());
                return;
            }
            setContent(null);
        }

        public void setCandidate(Item item) {
            this.candidate = item;
            PlaceholderTextView textView = (PlaceholderTextView) this.view;
            textView.setTextColor(item != null ? this.activeColor : this.color);
            String content = item != null ? item.getContent() : this.item != null ? this.item.getContent() : this.placeholder;
            int toWidth = textView.getWidth(content);
            if (toWidth != 0) {
                textView.setPlaceholder(null);
                this.view.getLayoutParams().width = toWidth;
                this.view.requestLayout();
            }
        }

        public boolean isEmpty() {
            return this.item == null && this.candidate == null;
        }
    }

    public PlaceholderQuiz(Context context) {
        super(context);
        this.overlay = new FrameLayout(getContext());
        this.dragManager = createDragManager();
    }

    protected void onPreCreateQuiz() {
        super.onPreCreateQuiz();
        this.placeholders = new SparseArray();
        this.items = new ArrayList();
    }

    protected View onCreateFixedContent(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        int i;
        this.fixedContent = inflater.inflate(R.layout.quiz_placeholder_source, parent, false);
        this.sourceLayout = (ViewGroup) this.fixedContent.findViewById(R.id.quiz_placeholder_source_layout);
        for (Answer answer : getShuffledAnswers()) {
            Item i2 = createItem(this.sourceLayout, answer.getText());
            View item = i2.getView();
            item.setOnTouchListener(this.dragManager);
            this.sourceLayout.addView(item);
            this.items.add(i2);
        }
        View view = this.fixedContent;
        if (isContentHidden()) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        return this.fixedContent;
    }

    public void check() {
        if (canCheck()) {
            boolean isCorrect = true;
            for (int i = 0; i < this.placeholders.size(); i++) {
                Placeholder placeholder = (Placeholder) this.placeholders.valueAt(i);
                Item item = placeholder.getItem();
                if (item == null || !placeholder.getPlaceholder().equals(item.getContent())) {
                    isCorrect = false;
                    break;
                }
            }
            setResult(isCorrect);
        }
    }

    public void unlock() {
        unlockPreStep(0);
    }

    private void unlockPreStep(int index) {
        if (index == this.placeholders.size()) {
            unlockStep(0);
            return;
        }
        Placeholder placeholder = (Placeholder) this.placeholders.valueAt(index);
        Item item = placeholder.getItem();
        if (item == null || placeholder.getPlaceholder().equals(item.getContent())) {
            unlockPreStep(index + 1);
        } else {
            this.dragManager.clearPlaceholder(placeholder, new C06721(index));
        }
    }

    private void unlockStep(int index) {
        if (index == this.placeholders.size()) {
            setResult(true);
            return;
        }
        Placeholder placeholder = (Placeholder) this.placeholders.valueAt(index);
        Item item = placeholder.getItem();
        if (item == null) {
            Iterator it = this.items.iterator();
            while (it.hasNext()) {
                Item i = (Item) it.next();
                if (i.getContent().equals(placeholder.getPlaceholder())) {
                    item = i;
                    break;
                }
            }
            this.dragManager.moveToPlaceholder(item, placeholder, new C06732(index));
            return;
        }
        unlockStep(index + 1);
    }

    protected View onCreateOverlay(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        return this.overlay;
    }

    protected void onShowContent() {
        super.onShowContent();
        if (this.fixedContent != null) {
            this.fixedContent.setVisibility(0);
        }
    }

    protected void onHideContent() {
        super.onHideContent();
        if (this.fixedContent != null) {
            this.fixedContent.setVisibility(4);
        }
    }

    protected final View getPlaceholder(ViewGroup parent, int index) {
        Placeholder placeholder = (Placeholder) this.placeholders.get(index);
        if (placeholder == null) {
            placeholder = createPlaceholder(parent, getReplacement(index));
            placeholder.getView().setOnTouchListener(this.dragManager);
            this.placeholders.put(index, placeholder);
        }
        return placeholder.getView();
    }

    protected String getReplacement(int index) {
        List<Answer> answers = this.quiz.getAnswers();
        int ci = -1;
        for (int i = 0; i < answers.size(); i++) {
            if (((Answer) answers.get(i)).isCorrect()) {
                ci++;
            }
            if (ci == index) {
                return ((Answer) answers.get(i)).getText();
            }
        }
        return BuildConfig.VERSION_NAME;
    }

    protected Placeholder createPlaceholder(ViewGroup parent, String placeholder) {
        return new Placeholder(getContext(), parent, this.textSize, placeholder);
    }

    protected Item createItem(ViewGroup parent, String content) {
        return new Item(getContext(), parent, this.textSize, content);
    }

    protected DragManager createDragManager() {
        return new DragManager();
    }
}
