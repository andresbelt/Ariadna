package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oncreate.ariadna.Adapters.LessonAdapter;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.LessonManager;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.LessonState;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ProgressManager;
import com.oncreate.ariadna.R;


import java.util.List;

public class LessonsFragment extends AppFragment implements LessonAdapter.Listener {
    private LessonAdapter adapter;
    private boolean isForwardNavigation;
    GridLayoutManager layoutManager;
    private ProgressManager.Listener progressListener;

    /* renamed from: com.sololearn.app.fragments.LessonsFragment.1 */
    class C11791 implements ProgressManager.Listener {
        C11791() {
        }

        public void onModuleChange(int moduleId) {
        }

        public void onLessonChange(int lessonId) {
        }

        public void onGlobalChange() {
            if (LessonsFragment.this.adapter != null) {
                LessonsFragment.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.LessonsFragment.2 */
    class C11802 implements MessageDialog.Listener {
        C11802() {
        }

        public void onResult(int result) {
            if (result == -1) {
                LessonsFragment.this.navigate(LoginFragment.createBackStackAware());
            }
        }
    }

    public LessonsFragment() {
        this.isForwardNavigation = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Module module = AriadnaApplication.getInstance().getCourseManager().getModuleById(getArguments().getInt("moduleId"));
        setName(module.getName());
        this.adapter = new LessonAdapter(getContext(), module.getLessons());
        this.adapter.setListener(this);
        this.progressListener = new C11791();
        getApp().getProgressManager().addListener(this.progressListener);
    }

    public static LessonsFragment forModule(int moduleId) {
        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        LessonsFragment fragment = new LessonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lessons, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.lesson_items_per_row));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(this.layoutManager);
        recyclerView.setAdapter(this.adapter);
        if (!this.isForwardNavigation) {
            recyclerView.setLayoutAnimation(null);
        }
        this.isForwardNavigation = false;
        scrollToCurrentLesson();
        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        getApp().getProgressManager().removeListener(this.progressListener);
    }

    public void loadLesson(Lesson lesson, LessonState state) {
        if (state.getState() == 0) {
            MessageDialog.create(getContext(), (int) R.string.lesson_locked_title, (int) R.string.lesson_locked_text, (int) R.string.action_ok).show(getChildFragmentManager());
        } else if (getApp().getUserManager().isAuthenticated() || (lesson.getType() != 1 && getApp().getCourseManager().getCourse().getModule(0).getLessons().contains(lesson))) {
            navigate(LessonManager.getFragment(lesson));
        } else {
           // MessageDialog.create(getContext(), (int) R.string.lesson_login_required_title, (int) R.string.lesson_login_required_text, (int) R.string.action_login, (int) R.string.action_cancel, new C11802()).show(getChildFragmentManager());
        }
    }

    public String getEntryName() {
        return LessonManager.ENTRY_LESSONS;
    }

    private void scrollToCurrentLesson() {
        ProgressManager progressManager = getApp().getProgressManager();
        List<Lesson> items = this.adapter.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (progressManager.getLessonState(((Lesson) items.get(i)).getId()).getState() == 2) {
                this.layoutManager.scrollToPosition(i);
                return;
            }
        }
    }

    public void onOrientationChange(int orientation) {
        super.onOrientationChange(orientation);
        if (this.layoutManager != null) {
            this.layoutManager.setSpanCount(getResources().getInteger(R.integer.lesson_items_per_row));
        }
    }
}
