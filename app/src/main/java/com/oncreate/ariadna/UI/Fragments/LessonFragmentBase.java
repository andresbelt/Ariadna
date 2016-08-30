package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.oncreate.ariadna.Adapters.HeaderAdapter;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.LessonManager;

import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.views.ActionMenuItemBadgeView;
import com.oncreate.ariadna.loginLearn.StringUtils;


public abstract class LessonFragmentBase extends AppFragment implements OnClickListener {
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Button commentsButton;
  //  private LessonCommentFragment commentsFragment;
    private LessonManager lessonManager;

    class C05383 implements Runnable {
        C05383() {
        }

        public void run() {
            if (LessonFragmentBase.this.isAlive()) {
                LessonFragmentBase.this.showComments();
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.LessonFragmentBase.1 */
    class C11761 implements LessonManager.GetQuestionCountListener {
        final /* synthetic */ ActionMenuItemBadgeView val$badge;

        C11761(ActionMenuItemBadgeView actionMenuItemBadgeView) {
            this.val$badge = actionMenuItemBadgeView;
        }

        public void onResponse(int count) {
            if (LessonFragmentBase.this.isAlive()) {
                this.val$badge.setCount(count);
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.LessonFragmentBase.2 */
    class C11772 extends BottomSheetCallback {
        int pageTop;

        C11772() {
            this.pageTop = 0;
        }

        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            this.pageTop = LessonFragmentBase.this.getHeaderOffset();
            switch (newState) {
                case LoadingDialog.HORIZONTAL_INDETERMINATE /*3*/:
                    LessonFragmentBase.this.lockParentViewPager(true);
                    break;
                case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                    break;
                case ConnectionResult.INVALID_ACCOUNT /*5*/:
                    LessonFragmentBase.this.updateCommentsNumber();
                    break;
            }
            LessonFragmentBase.this.lockParentViewPager(false);
            Log.i("onSlide", "onStateChanged: " + newState);
        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            LessonFragmentBase.this.getApp().getActivity().setHeaderShift((float) Math.min(0, bottomSheet.getTop() - this.pageTop));
        }
    }


    class C11784 implements LessonManager.GetQuestionCountListener {
        C11784() {
        }

        public void onResponse(int count) {
            if (LessonFragmentBase.this.isAlive()) {
                LessonFragmentBase.this.commentsButton.setText(LessonFragmentBase.this.getResources().getQuantityString(R.plurals.quiz_comments_button_format, count, new Object[]{Integer.valueOf(count)}));
            }
        }
    }

    protected abstract int getCommentType();

    protected abstract View getShiftView();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.quiz, menu);
        MenuItem item = menu.findItem(R.id.action_discuss);
        ActionMenuItemBadgeView view = new ActionMenuItemBadgeView(getContext());
        view.initialize(item);
        item.setActionView(view);
        view.setOnClickListener(this);
        updateDiscussBadge(view);
    }

    private void updateDiscussBadge(ActionMenuItemBadgeView badge) {
//        if (getApp().isDiscussionEnabled()) {
//            getLessonManager().getQuestionCount(new C11761(badge));
//        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discuss :
                navigate(DiscussionFragment.createBackstackAwareWithQuery(getLessonManager().getLesson().getTags()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getName() {
        return getLessonManager().getLesson().getName();
    }

    public LessonManager getLessonManager() {
        if (this.lessonManager == null) {
            if (LessonManager.ENTRY_LESSON.equals(getEntryName())) {
                this.lessonManager = LessonManager.forLesson(getArguments());
            } else {
                this.lessonManager = LessonManager.forQuiz(getArguments());
            }
        }
        return this.lessonManager;
    }

    public void setLessonManager(LessonManager lessonManager) {
        this.lessonManager = lessonManager;
    }

    public String getEntryName() {
        return LessonManager.ENTRY_LESSON;
    }

    public Entry getEntry(Fragment currentFragment) {
        if (currentFragment instanceof AppFragment) {
            Entry entry = getLessonManager().getEntry((AppFragment) currentFragment, this);
            if (entry != null) {
                return entry;
            }
        }
        return super.getEntry(currentFragment);
    }

    public HeaderAdapter getHeaderAdapter() {
        return getLessonManager().getHeaderAdapter(getContext());
    }

    protected int getHeaderOffset() {
        if (getParentFragment() instanceof LessonFragment) {
            return ((LessonFragment) getParentFragment()).getHeaderOffset();
        }
        return getApp().getActivity().getToolbarOffset(16);
    }

    protected void setHeaderShift(int shift) {
        ((MarginLayoutParams) getShiftView().getLayoutParams()).topMargin = shift;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.quiz_comments));
        this.bottomSheetBehavior.setHideable(true);
        this.bottomSheetBehavior.setPeekHeight(0);
        this.bottomSheetBehavior.setBottomSheetCallback(new C11772());
        this.commentsButton = (Button) view.findViewById(R.id.quiz_comments_button);
        this.commentsButton.setOnClickListener(this);
        updateCommentsNumber();
        if (getArguments().getBoolean(LessonManager.ARG_SHOW_COMMENTS, false)) {
            view.postDelayed(new C05383(), 100);
        }
    }

    private void updateCommentsNumber() {
        getLessonManager().getCommentCount(new C11784());
    }

    private void lockParentViewPager(boolean lock) {
        if (getParentFragment() instanceof PagerFragment) {
            ((PagerFragment) getParentFragment()).lockViewPager(lock);
        }
    }

    private void showComments() {
        this.bottomSheetBehavior.setPeekHeight(getActivity().getWindow().getDecorView().getHeight() / 2);
        this.bottomSheetBehavior.setState(4);
        if (this.commentsFragment == null) {
        //    this.commentsFragment = LessonCommentFragment.forQuiz(getLessonManager().getQuizId(), getCommentType());
          //  getChildFragmentManager().beginTransaction().add((int) R.id.quiz_comments, this.commentsFragment).commit();
        }
    }

    public boolean onBackPressed() {
        if (this.commentsFragment != null && this.bottomSheetBehavior.getState() == 3 && this.commentsFragment.onBackPressed()) {
            return true;
        }
        if (this.commentsFragment == null || (this.bottomSheetBehavior.getState() != 4 && this.bottomSheetBehavior.getState() != 3)) {
            return super.onBackPressed();
        }
        this.bottomSheetBehavior.setState(5);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_comments_button /*2131755475*/:
                showComments();
            case R.id.action_discuss /*2131755732*/:
                String tags = getLessonManager().getLesson().getTags();
                if (StringUtils.isNullOrWhitespace(tags)) {
                    tags = getApp().getCourseManager().getCourse().getTags();
                }
                navigate(DiscussionFragment.createBackstackAwareWithQuery(tags));
            default:
        }
    }

    public boolean maximizeComments() {
        if (this.bottomSheetBehavior.getState() == 3) {
            return false;
        }
        this.bottomSheetBehavior.setState(3);
        return true;
    }

    public boolean interceptNavigation() {
        if (this.commentsFragment != null) {
            getChildFragmentManager().beginTransaction().remove(this.commentsFragment).commitAllowingStateLoss();
            this.commentsFragment = null;
        }
        return super.interceptNavigation();
    }
}
