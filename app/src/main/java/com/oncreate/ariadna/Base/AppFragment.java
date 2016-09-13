package com.oncreate.ariadna.Base;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.oncreate.ariadna.Adapters.HeaderAdapter;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Util.ConstantVariables;
import com.oncreate.ariadna.loginLearn.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azulandres92 on 8/13/16.
 */
public abstract class AppFragment extends Fragment {

    private boolean isAlive;
    private boolean cancelDelayedDestroy;
    private String name;
    private boolean isVisibleChildFragment;
    private boolean isChildFragmentInAnimation;
    private int forcefeedAnimation;
    private boolean isStringName;
    public static final Entry DEFAULT_ENTRY;
    private Bundle savedState;
    private boolean isInitialNavigation;

    public void invalidate() {
    }

    static {
        DEFAULT_ENTRY = new Entry();
    }

    class C05241 implements Runnable {
        final /* synthetic */ View val$view;

        C05241(View view) {
            this.val$view = view;
        }

        public void run() {
            if (AppFragment.this.isChildFragmentInAnimation) {
                AppFragment.this.isChildFragmentInAnimation = false;
                this.val$view.clearAnimation();
            }
            AppFragment.this.onDestroyViewAfterAnimation();
        }
    }


    public AppFragment() {
        this.name = ConstantVariables.VERSION_NAME;
        this.isAlive = false;
        this.isInitialNavigation = true;
    }


    protected AriadnaApplication getApp() {
        return AriadnaApplication.getInstance();
    }


    public boolean isInitialNavigation() {
        return this.isInitialNavigation;
    }


    public void onDestroyView() {
        this.cancelDelayedDestroy = false;
        super.onDestroyView();
        this.isInitialNavigation = false;
        View view = getView();
        if (view != null) {
            view.postDelayed(new C05241(view), 300);
        } else {
            onDestroyViewAfterAnimation();
        }
        this.isAlive = false;
//        if (this.showcase != null) {
//            this.showcase.cancel();
//        }
    }


    public boolean isMenuEnabled() {
        return false;
    }

    public interface NavigationPromptListener {
        void onAction(boolean z);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedState = savedInstanceState;
        this.isAlive = true;
    }

    public boolean inflateHeaderExtras(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        return false;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void promptNavigate(NavigationPromptListener listener) {
        if (listener != null) {
            listener.onAction(true);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleChildFragment = isVisibleToUser;
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (this.forcefeedAnimation != 0) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), this.forcefeedAnimation);
            this.forcefeedAnimation = 0;
            return animation;
        } else if (enter || !this.isVisibleChildFragment) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        } else {
            Animation doNothingAnim = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            doNothingAnim.setDuration(300);
            this.isChildFragmentInAnimation = true;
            Log.i("FRAGMENT", "DUMMY ANIM: " + getClass().getSimpleName());
            return doNothingAnim;
        }
    }


    public boolean onBackPressed() {
//        if (this.showcase == null || !this.showcase.isOpen()) {
//            return false;
//        }
//        this.showcase.close();
        return false;
    }

    protected void getShowcase() {
//        if (this.showcase == null) {
//            this.showcase = new Showcase(getActivity(), getApp().getStorage());
//        }
//        return this.showcase;
    }

    protected void navigateHome() {
        navigateHome(-1);
    }


    protected void navigateHome(int position) {
        if (!isToolbarEnabled()) {
            TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16843499});
            int actionBarSize = (int) styledAttributes.getDimension(0, 0.0f);
            styledAttributes.recycle();
            setHeaderShift(-actionBarSize);
        }
        ((AppActivity) getActivity()).navigateHome(position);
    }

    @IdRes
    public int getMenuId() {
        return 0;
    }


    public String getName() {
        return this.name;
    }

    public HeaderAdapter getHeaderAdapter() {
        return null;
    }

    public float getHeaderElevation() {
        return -1.0f;
    }

    public boolean isToolbarEnabled() {
        return true;
    }


    public boolean isMenuBlocked() {
        return false;
    }


    protected void onDestroyViewAfterAnimation() {
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.isAlive = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHeaderShift(getHeaderOffset());
        this.isAlive = true;
        this.cancelDelayedDestroy = true;
        if (getResources().getConfiguration().orientation == 2) {
            onOrientationChange(2);
        }


    }

    protected int getHeaderOffset() {
        return getApp().getActivity().getToolbarOffset(0);
    }

    protected void setHeaderShift(int shift) {
        View view = getView();
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) layoutParams).topMargin = shift;
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).topMargin = shift;
            }
        }
    }

    public void enableHeaderShift(boolean enable) {
        setHeaderShift(enable ? getHeaderOffset() : 0);
    }


    protected void restoreState(Bundle savedInstanceState) {
        String savedName = savedInstanceState.getString("app_fragment_name");
        if (savedName != null) {
            setName(savedName);
        }
        this.savedState = null;
    }

    public void setName(String name) {
        this.name = name;
        this.isStringName = true;
    }

    protected void setName(@StringRes int id) {
        this.name = getString(id);
        this.isStringName = false;
    }

    public boolean interceptNavigation() {
        return true;
    }

    public Entry getEntry(Fragment currentFragment) {
        return DEFAULT_ENTRY;
    }

    public boolean isEntryPoint() {
        return false;
    }

    public void forcefeedTransitionsOnce(int forcefeedAnimation) {
        this.forcefeedAnimation = forcefeedAnimation;
    }

    protected boolean navigateBack() {
        return ((AppActivity) getActivity()).navigateBack();
    }

    protected void navigateBack(String entry) {
        ((AppActivity) getActivity()).navigateBack(entry);
    }

    public void onOrientationChange(int orientation) {
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.isStringName) {
            outState.putString("app_fragment_name", this.name);
        }
        if (this.savedState != null) {
            outState.putAll(this.savedState);
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    public String getExternalName() {
        return StringUtils.insertWhitespace(getClass().getSimpleName().replace("Fragment", "Page"));
    }

    public String getEntryName() {
        return null;
    }

    public static class Entry {
        private List<String> acceptableBackEntries;
        @AnimRes
        private int backwardInAnimation;
        @AnimRes
        private int backwardOutAnimation;
        @AnimRes
        private int forcefeedInAnimation;
        @AnimRes
        private int forcefeedOutAnimation;
        @AnimRes
        private int forwardInAnimation;
        @AnimRes
        private int forwardOutAnimation;
        private List<String> inacceptableBackEntries;
        private List<Fragment> injectedFragments;

        public Entry() {
            this.forwardInAnimation = R.anim.enter_from_bottom;
            this.forwardOutAnimation = R.anim.exit_to_top;
            this.backwardInAnimation = R.anim.enter_from_top;
            this.backwardOutAnimation = R.anim.exit_to_bottom;
        }

        public Entry(List<String> acceptableBackEntries, @AnimRes int forwardInAnimation, @AnimRes int forwardOutAnimation, @AnimRes int backwardInAnimation, @AnimRes int backwardOutAnimation) {
            this.acceptableBackEntries = acceptableBackEntries;
            this.forwardInAnimation = forwardInAnimation;
            this.forwardOutAnimation = forwardOutAnimation;
            this.backwardInAnimation = backwardInAnimation;
            this.backwardOutAnimation = backwardOutAnimation;
        }

        public Entry(List<String> acceptableBackEntries) {
            this();
            this.acceptableBackEntries = acceptableBackEntries;
        }

        public Entry(List<String> acceptableBackEntries, List<String> inacceptableBackEntries) {
            this(acceptableBackEntries);
            this.inacceptableBackEntries = inacceptableBackEntries;
        }

        public Entry(List<String> acceptableBackEntries, @AnimRes int inAnimation, @AnimRes int outAnimation) {
            this(acceptableBackEntries);
            this.forcefeedInAnimation = inAnimation;
            this.forcefeedOutAnimation = outAnimation;
        }

        public int getForwardInAnimation() {
            return this.forwardInAnimation;
        }

        public int getBackwardInAnimation() {
            return this.backwardInAnimation;
        }

        public int getForwardOutAnimation() {
            return this.forwardOutAnimation;
        }

        public int getBackwardOutAnimation() {
            return this.backwardOutAnimation;
        }

        public boolean altersBackStack() {
            return (this.acceptableBackEntries == null && this.inacceptableBackEntries == null) ? false : true;
        }

        public List<String> getAcceptableBackEntries() {
            return this.acceptableBackEntries;
        }

        public List<String> getInacceptableBackEntries() {
            return this.inacceptableBackEntries;
        }

        public boolean injectsFragments() {
            return this.injectedFragments != null;
        }

        public List<Fragment> getInjectedFragments() {
            return this.injectedFragments;
        }


        //metodo para agregar fragments a la cola de fragments

        public void injectFragment(AppFragment fragment) {
            if (this.injectedFragments == null) {
                this.injectedFragments = new ArrayList();
            }
            this.injectedFragments.add(fragment);
        }

        public boolean forcefeedTransitions() {
            return (this.forcefeedInAnimation == 0 && this.forcefeedOutAnimation == 0) ? false : true;
        }

        @AnimRes
        public int getForcefeedInAnimation() {
            return this.forcefeedInAnimation;
        }

        @AnimRes
        public int getForcefeedOutAnimation() {
            return this.forcefeedOutAnimation;
        }
    }

    protected void navigate(Fragment fragment) {
        ((AppActivity) getActivity()).navigate(fragment);
    }

    public void navigate() {
        AriadnaApplication.getInstance().getActivity().navigate(this);
    }


    public void setPageOffset(float offset) {
//        if (this.fab != null) {
//            this.fab.setTranslationY(offset);
//        }
    }


}
