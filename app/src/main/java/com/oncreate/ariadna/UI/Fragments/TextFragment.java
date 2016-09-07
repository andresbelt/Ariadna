package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oncreate.ariadna.LessonManager;
import com.oncreate.ariadna.R;


public class TextFragment extends LessonFragmentBase {
    private View continueButton;
   // private TextParser parser;
    private View scrollView;

    /* renamed from: com.sololearn.app.fragments.TextFragment.1 */
    class C05641 implements Runnable {
        final /* synthetic */ View val$rootView;
        final /* synthetic */ View val$runCode;

        C05641(View view, View view2) {
            this.val$runCode = view;
            this.val$rootView = view2;
        }

        public void run() {
            int[] location = new int[2];
            this.val$runCode.getLocationInWindow(location);
            if (TextFragment.this.isVisible() && ((float) location[1]) < ((float) (this.val$rootView.getHeight() * 4)) / 5.0f) {
               // Showcase showcase = TextFragment.this.getShowcase();
//                if (!showcase.isDismissed("try_it_yourself")) {
//                    showcase.startSequence(ShowcaseSequence.create("try_it_yourself").addItem(ShowcaseItem.forView(this.val$runCode).withTitle((int) R.string.lesson_run_code_showcase_title).withMessage((int) R.string.lesson_run_code_showcase_message)));
//                }
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text, container, false);
        ViewGroup textContainer = (ViewGroup) rootView.findViewById(R.id.text_container);
        this.scrollView = rootView.findViewById(R.id.scroll_view);
        this.continueButton = rootView.findViewById(R.id.btn_text_continue);
        this.continueButton.setOnClickListener(this);
       // this.parser = new TextParser(getContext());
       // this.parser.parse(getLessonManager().getQuiz().getTextContent());
       // textContainer.addView(this.parser.getLayout());
//        View runCode = textContainer.findViewById(R.id.run_code);
//        if (runCode != null && runCode.getVisibility() == 0) {
//            runCode.post(new C05641(runCode, rootView));
//        } else if (getApp().getCourseManager().getCourse().getModule(0).getLessons().indexOf(getLessonManager().getLesson()) >= 2) {
////            Showcase showcase = getShowcase();
////            if (!showcase.isDismissed("lesson_discussion")) {
////                showcase.startSequence(ShowcaseSequence.create("lesson_discussion").addItem(ShowcaseItem.forView((int) R.id.action_discuss).withTitle((int) R.string.lesson_discuss_showcase_title).withMessage((int) R.string.lesson_discuss_showcase_message)));
////            }
//        }
        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
//        if (this.parser != null) {
//            this.parser.release();
//        }
        this.continueButton.setOnClickListener(null);
        this.continueButton = null;
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_text_continue /*2131755512*/:
                navigate(LessonManager.getFragment(getLessonManager().getLesson(), getLessonManager().getQuiz().getId(), true));
            default:
        }
    }

    protected View getShiftView() {
        return this.scrollView;
    }

    protected int getCommentType() {
        return 1;
    }
}
