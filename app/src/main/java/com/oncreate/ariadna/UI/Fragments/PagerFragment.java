package com.oncreate.ariadna.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PagerFragment extends AppFragment implements OnPageChangeListener {
    private static final String SELECTION_STORE_PREFIX = "PagerLastSelection_";
    private static final AtomicInteger sNextGeneratedId;
    protected Adapter adapter;
    private SparseArray<TextView> badges;
    private ArrayList<Page> fragmentMap;
    private SparseArray<Fragment> fragments;
    private boolean isEntryPoint;
    private boolean isMenuEnabled;
    private boolean isPagingEnabled;
    private boolean isResumed;
    private int menuId;
    private String selectionKey;
    private int viewId;

    protected ViewPager viewPager;

    public interface BadgeListener {
        void onBadgeChanged(BadgeProvider badgeProvider, int i);
    }

    public interface BadgeProvider {
        int getBadgeCount();

        void setBadgeListener(BadgeListener badgeListener);
    }

    private static class Page {
        private Bundle args;
        private Class fragmentType;
        private String name;
        private int nameResId;

        public Page(@StringRes int nameResId, Class fragmentType, Bundle args) {
            this.nameResId = nameResId;
            this.fragmentType = fragmentType;
            this.args = args;
        }

        public Page(String name, Class fragmentType, Bundle args) {
            this.name = name;
            this.fragmentType = fragmentType;
            this.args = args;
        }
    }


    class C11781 extends ViewPager {
        C11781(Context x0) {
            super(x0);
        }

        public void onRestoreInstanceState(Parcelable state) {
            if (PagerFragment.this.selectionKey != null) {
                state = null;
            }
            super.onRestoreInstanceState(state);
        }

        public boolean onTouchEvent(MotionEvent event) {
            return PagerFragment.this.isPagingEnabled && super.onTouchEvent(event);
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            return PagerFragment.this.isPagingEnabled && super.onInterceptTouchEvent(event);
        }
    }

    public class Adapter extends FragmentPagerAdapter implements BadgeListener {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public CharSequence getPageTitle(int position) {
            Page page = PagerFragment.this.fragmentMap.get(position);
            if (page.name != null) {
                return page.name;
            }
            return PagerFragment.this.getContext().getString(page.nameResId);
        }

        public Fragment getItem(int position) {
            Page page = (Page) PagerFragment.this.fragmentMap.get(position);
            try {
                Fragment fragment = (Fragment) page.fragmentType.newInstance();
                if (page.args != null) {
                    fragment.setArguments(page.args);
                }
                PagerFragment.this.fragments.put(position, fragment);
                if (position == PagerFragment.this.viewPager.getCurrentItem() && (fragment instanceof AppFragment) && PagerFragment.this.getApp().getActivity() != null) {
                    PagerFragment.this.getApp().getActivity().setViewPagerPage(position, PagerFragment.this.adapter.getPageTitle(position).toString(), ((AppFragment) fragment).getExternalName());
                }
                TextView badge = (TextView) PagerFragment.this.badges.get(position);
                if (badge != null) {
                    BadgeProvider badgeProvider = (BadgeProvider) fragment;
                    badgeProvider.setBadgeListener(this);
                    updateBadge(badge, badgeProvider.getBadgeCount());
                }
                PagerFragment.this.onFragmentCreated(fragment, position);
                return fragment;
            } catch (Exception e) {
                return null;
            }
        }

        public int getCount() {
            return PagerFragment.this.fragmentMap.size();
        }

        public View getTabView(int position) {
            View tab = null;
            int i = 0;
            if (BadgeProvider.class.isAssignableFrom(((Page) PagerFragment.this.fragmentMap.get(position)).fragmentType)) {
                tab = LayoutInflater.from(PagerFragment.this.getContext()).inflate(R.layout.tab_with_badge, null, false);
                TextView badge = (TextView) tab.findViewById(R.id.tab_badge);
                PagerFragment.this.badges.put(position, badge);
                Fragment fragment = (Fragment) PagerFragment.this.fragments.get(position);
                if (fragment instanceof BadgeProvider) {
                    i = ((BadgeProvider) fragment).getBadgeCount();
                }
                updateBadge(badge, i);
            }
            return tab;
        }

        public void onBadgeChanged(BadgeProvider provider, int count) {
            for (int i = 0; i < PagerFragment.this.fragments.size(); i++) {
                if (PagerFragment.this.fragments.valueAt(i) == provider) {
                    TextView badge = (TextView) PagerFragment.this.badges.get(PagerFragment.this.fragments.keyAt(i));
                    if (badge != null) {
                        updateBadge(badge, count);
                    }
                }
            }
        }

        private void updateBadge(TextView badge, int count) {
            badge.setVisibility(count > 0 ? 0 : 8);
            badge.setText(Integer.toString(count));
        }

        public int getTitleRes(int position) {
            return ((Page) PagerFragment.this.fragmentMap.get(position)).nameResId;
        }
    }

    public PagerFragment() {
        this.menuId = 0;
        this.fragmentMap = new ArrayList();
        this.fragments = new SparseArray();
        this.badges = new SparseArray();
        this.isPagingEnabled = true;
    }

    public void onResume() {
        super.onResume();
        this.isResumed = true;
    }

    public void onPause() {
        super.onPause();
        this.isResumed = false;
    }

    public boolean isMenuEnabled() {
        return this.isMenuEnabled;
    }

    public void setIsMenuEnabled(boolean isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public int getMenuId() {
        return this.menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public boolean isEntryPoint() {
        return this.isEntryPoint;
    }

    public void setIsEntryPoint(boolean isEntryPoint) {
        this.isEntryPoint = isEntryPoint;
    }

    public <T extends Fragment> void addFragment(String title, Class<T> fragmentClass, Bundle args) {
        this.fragmentMap.add(new Page(title, (Class) fragmentClass, args));
    }

    public <T extends Fragment> void addFragment(@StringRes int title, Class<T> fragmentClass, Bundle args) {
        this.fragmentMap.add(new Page(title, (Class) fragmentClass, args));
    }

    public <T extends Fragment> void addFragment(@StringRes int title, Class<T> fragmentClass) {
        addFragment(title, (Class) fragmentClass, null);
    }

    public void onCreate(Bundle savedInstanceState) {
        this.viewId = generateViewId();
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewPager = new C11781(getContext());
        this.viewPager.setId(this.viewId);
        this.viewPager.setOffscreenPageLimit(1);
        this.viewPager.addOnPageChangeListener(this);
        this.adapter = new Adapter(getChildFragmentManager());
        this.viewPager.setAdapter(this.adapter);
        restoreSelection();
        return this.viewPager;
    }

    public void onDestroyViewAfterAnimation() {
        super.onDestroyViewAfterAnimation();
        try {
            this.viewPager.removeOnPageChangeListener(this);
            this.viewPager.setAdapter(null);
        } catch (Exception e) {
        }
    }

    public boolean onBackPressed() {
        Fragment fragment = (Fragment) this.fragments.get(this.viewPager.getCurrentItem());
        if (fragment instanceof AppFragment) {
            return ((AppFragment) fragment).onBackPressed();
        }
        return super.onBackPressed();
    }

    public boolean interceptNavigation() {
        if (this.viewPager != null) {
            Fragment fragment = (Fragment) this.fragments.get(this.viewPager.getCurrentItem());
            if (fragment instanceof AppFragment) {
                return ((AppFragment) fragment).interceptNavigation();
            }
        }
        return super.interceptNavigation();
    }

    public void promptNavigate(NavigationPromptListener listener) {
        if (this.viewPager != null) {
            Fragment fragment = (Fragment) this.fragments.get(this.viewPager.getCurrentItem());
            if (fragment instanceof AppFragment) {
                ((AppFragment) fragment).promptNavigate(listener);
                return;
            }
        }
        super.promptNavigate(listener);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        if (this.isResumed) {
            storeSelection(position);
        }
        Fragment fragment = (Fragment) this.fragments.get(position);
        if (fragment instanceof AppFragment) {
            getApp().getActivity().setViewPagerPage(position, this.adapter.getPageTitle(position).toString(), ((AppFragment) fragment).getExternalName());
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void setSelectionStore(String storeKey) {
        this.selectionKey = storeKey;
    }

    protected int getDefaultPage() {
        return 0;
    }

    private void restoreSelection() {
        int selectedPage = getDefaultPage();
        if (this.selectionKey != null) {
            selectedPage = getApp().getStorage().getInt(SELECTION_STORE_PREFIX + this.selectionKey, selectedPage);
        }
        this.viewPager.setCurrentItem(selectedPage);
    }

    private void storeSelection(int position) {
        if (this.selectionKey != null) {
            getApp().getStorage().setInt(SELECTION_STORE_PREFIX + this.selectionKey, position);
        }
    }

    public static void storeSelection(String key, int position) {
        AriadnaApplication.getInstance().getStorage().setInt(SELECTION_STORE_PREFIX + key, position);
    }

    static {
        sNextGeneratedId = new AtomicInteger(2);
    }

    public static int generateViewId() {
        int result;
        int newValue;
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > ViewCompat.MEASURED_SIZE_MASK) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.viewPager != null) {
            Fragment fragment = (Fragment) this.fragments.get(this.viewPager.getCurrentItem());
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("fragment_menu_enabled", this.isMenuEnabled);
        outState.putInt("fragment_menu_id", this.menuId);
        outState.putBoolean("fragment_entry_point", this.isEntryPoint);
        super.onSaveInstanceState(outState);
        for (int i = 0; i < this.fragmentMap.size(); i++) {
            Page page = (Page) this.fragmentMap.get(i);
            outState.putInt("page" + i + "NameResId", page.nameResId);
            outState.putString("page" + i + "FragmentType", page.fragmentType.getName());
            outState.putBundle("page" + i + "Args", page.args);
        }
    }

    protected void restoreState(Bundle savedInstanceState) {
        super.restoreState(savedInstanceState);
        boolean changed = false;
        this.isMenuEnabled = savedInstanceState.getBoolean("fragment_menu_enabled", this.isMenuEnabled);
        this.menuId = savedInstanceState.getInt("fragment_menu_id", this.menuId);
        this.isEntryPoint = savedInstanceState.getBoolean("fragment_entry_point", this.isEntryPoint);
        int i = 0;
        while (true) {
            int nameRes = savedInstanceState.getInt("page" + i + "NameResId", -1);
            if (nameRes == -1) {
                break;
            }
            String typeName = savedInstanceState.getString("page" + i + "FragmentType");
            Bundle args = savedInstanceState.getBundle("page" + i + "Args");
            try {
                this.fragmentMap.add(new Page(nameRes, Class.forName(typeName), args));
                changed = true;
                i++;
            } catch (ClassNotFoundException e) {
            }
        }
        if (changed) {
            this.adapter.notifyDataSetChanged();
        }
    }

    protected int getHeaderOffset() {
        return getApp().getActivity().getToolbarOffset(2);
    }

    public void onOrientationChange(int orientation) {
        super.onOrientationChange(orientation);
        for (int i = 0; i < this.fragments.size(); i++) {
            Fragment fragment = (Fragment) this.fragments.valueAt(i);
            if (fragment instanceof AppFragment) {
                ((AppFragment) fragment).onOrientationChange(orientation);
            }
        }
    }

    public void setPage(int position) {
        getApp().getActivity().setTab(position);
    }

    protected void onFragmentCreated(Fragment fragment, int position) {
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void lockViewPager(boolean lock) {
        this.isPagingEnabled = !lock;
    }

    public void setPageOffset(float offset) {
        Fragment fragment = (Fragment) this.fragments.get(this.viewPager.getCurrentItem());
        if (fragment instanceof AppFragment) {
            ((AppFragment) fragment).setPageOffset(offset);
        }
    }
}
