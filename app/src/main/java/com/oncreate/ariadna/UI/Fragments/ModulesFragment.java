package com.oncreate.ariadna.UI.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.oncreate.ariadna.Adapters.ModuleAdapter;
import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.CourseManager;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ModelsVO.ModuleState;
import com.oncreate.ariadna.ProgressManager;
import com.oncreate.ariadna.R;

import java.util.List;

public class ModulesFragment extends AppFragment implements ModuleAdapter.Listener {

    private ModuleAdapter adapter;
    private GridLayoutManager layoutManager;
    private ModuleDecoration moduleDecoration;
    private ProgressManager.Listener progressListener;
    private RecyclerView recyclerView;

    /* renamed from: com.sololearn.app.fragments.ModulesFragment.4 */
    class C05414 implements Runnable {
        C05414() {
        }

        public void run() {
            if (ModulesFragment.this.recyclerView != null) {
                ModulesFragment.this.recyclerView.setAdapter(ModulesFragment.this.adapter);
                ModulesFragment.this.recyclerView.invalidateItemDecorations();
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.ModulesFragment.1 */
    class C11841 extends SpanSizeLookup {
        C11841() {
        }

        public int getSpanSize(int position) {
            switch (ModulesFragment.this.adapter.getItemType(position)) {
                case AppActivity.OFFSET_TOOLBAR /*1*/:
                    return 1;
                default:
                    return 2;
            }
        }
    }


    /* renamed from: com.sololearn.app.fragments.ModulesFragment.2 */
    class C11852 implements ProgressManager.Listener {
        C11852() {
        }

        public void onModuleChange(int moduleId) {
        }

        public void onLessonChange(int lessonId) {
        }

        public void onGlobalChange() {
            if (ModulesFragment.this.adapter != null) {
                ModulesFragment.this.adapter.setItems(ModulesFragment.this.getApp().getCourseManager().getCourse().getModules());
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.ModulesFragment.3 */
//    class C11863 implements MessageDialog.Listener {
//        C11863() {
//        }
//
//        public void onResult(int result) {
//            if (result == -1) {
//                ModulesFragment.this.navigate(LoginFragment.createBackStackAware());
//            }
//        }
//    }

    private class ModuleDecoration extends ItemDecoration {
        private ModuleDecoration() {
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int itemWidth = ((parent.getWidth() - parent.getPaddingLeft()) - parent.getPaddingRight()) / 2;
            switch (ModulesFragment.this.adapter.getItemType(parent.getChildAdapterPosition(view))) {

                case AppActivity.OFFSET_TABS /*2*/:
                    outRect.left = itemWidth ;
                    outRect.right = itemWidth;
                case ConnectionResult.RESOLUTION_REQUIRED /*6*/:
                    outRect.left = itemWidth / 2;
                    outRect.right = itemWidth / 2;
                case 3/*3*/:
                    outRect.left = itemWidth;
                case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                    outRect.right = itemWidth;
                default:
            }
        }
    }

    public boolean isMenuEnabled() {
        return true;
    }

    public boolean isEntryPoint() {
        return true;
    }

    public int getMenuId() {
        return R.id.navigation_action_home;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CourseManager course = getApp().getCourseManager();
         this.adapter = new ModuleAdapter(getContext(), course.getCourse().getId(), course.getCourse().getModules());
        this.adapter.setListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.adapter = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_modules, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.layoutManager = new GridLayoutManager(getContext(), 2);
        this.layoutManager.setSpanSizeLookup(new C11841());
        this.moduleDecoration = new ModuleDecoration();
      //  this.recyclerView.addItemDecoration(this.moduleDecoration);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.progressListener = new C11852();
        getApp().getProgressManager().addListener(this.progressListener);
        scrollToCurrentModule();
        return rootView;
    }

    public void onDestroyView() {
        super.onDestroyView();
        getApp().getProgressManager().removeListener(this.progressListener);
        this.progressListener = null;
    }

    public void onDestroyViewAfterAnimation() {
        super.onDestroyViewAfterAnimation();
        if (this.recyclerView != null) {
            this.recyclerView.setAdapter(null);
            this.recyclerView.removeItemDecoration(this.moduleDecoration);
            this.moduleDecoration = null;
            this.recyclerView = null;
        }
        if (this.layoutManager != null) {
            this.layoutManager.setSpanSizeLookup(null);
        }
    }

    public void loadModule(Module module, ModuleState state) {
        if (state.getState() == 0) {
         //   MessageDialog.create(getContext(), (int) R.string.module_locked_title, (int) R.string.module_locked_text, (int) R.string.action_ok).show(getChildFragmentManager());
        } else {
            navigate(LessonsFragment.forModule(module.getId()));
        }
    }

    public void loadShortcut(Module module) {
        if (!getApp().getUserManager().isAuthenticated()) {
            List<Module> modules = getApp().getCourseManager().getCourse().getModules();
            int firstShortcutModuleId = -1;
            for (int i = 1; i < modules.size(); i++) {
                if ((modules.get(i)).getAllowShortcut()) {
                    firstShortcutModuleId = ( modules.get(i)).getId();
                    break;
                }
            }
            if (module.getId() != firstShortcutModuleId) {
              //  MessageDialog.create(getContext(), (int) C0471R.string.lesson_login_required_title, (int) R.string.lesson_login_required_text, (int) C0471R.string.action_login, (int) C0471R.string.action_cancel, new C11863()).show(getChildFragmentManager());
                return;
            }
        }
      //  navigate(LessonManager.getShortcutFragment(module.getId()));
    }

    public void onOrientationChange(int orientation) {
        super.onOrientationChange(orientation);
        if (this.recyclerView != null) {
            this.recyclerView.postDelayed(new C05414(), 100);
        }
    }

    private void scrollToCurrentModule() {
        ProgressManager progressManager = getApp().getProgressManager();
        List<Object> items = this.adapter.getItems();
        int i = 0;
        while (i < items.size()) {
            if ((items.get(i) instanceof Module) && progressManager.getModuleState(((Module) items.get(i)).getId()).getState() == 2) {
                this.layoutManager.scrollToPosition(Math.min(0, i - 3));
                return;
            }
            i++;
        }
    }
}
