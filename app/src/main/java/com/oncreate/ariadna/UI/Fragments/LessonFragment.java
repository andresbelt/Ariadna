package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.oncreate.ariadna.Adapters.HeaderAdapter;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.LessonManager;
import com.oncreate.ariadna.R;


public class LessonFragment extends PagerFragment {
    private LessonManager lessonManager;

    private LessonManager getLessonManager() {
        if (this.lessonManager == null) {
            this.lessonManager = LessonManager.forLesson(getArguments());
        }
        return this.lessonManager;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
           // addFragment((int) R.string.page_title_lesson_text, TextFragment.class, getArguments());
           // addFragment((int) R.string.page_title_lesson_video, VideoFragment.class, getArguments());
        }
        setHasOptionsMenu(true);
    }

    public HeaderAdapter getHeaderAdapter() {
        return getLessonManager().getHeaderAdapter(getContext());
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

    protected int getHeaderOffset() {
        return getApp().getActivity().getToolbarOffset(18);
    }

    protected void setHeaderShift(int shift) {
    }
}
