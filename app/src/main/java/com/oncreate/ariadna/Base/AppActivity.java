package com.oncreate.ariadna.Base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.oncreate.ariadna.Dialog.AppDialog;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.UI.Fragments.PagerFragment;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.HomeActivity;
import com.oncreate.ariadna.UserManager;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by azulandres92 on 8/13/16.
 */
public abstract class AppActivity extends AppCompatActivity {
    protected static final int FRAGMENT_CONTAINER = R.id.container;
    public static final int OFFSET_TOOLBAR = 1;
    public static final int OFFSET_TABS = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 311;
    private Stack<WeakReference<Fragment>> backStackFragments;
    private int backStackItemsRemoved;
    protected FragmentManager fragmentManager;
    private AppFragment homeFragment;
    private int inputResizeRequests;
    private boolean invalidatePreviousFragment;
    private boolean isActive;
    private boolean isClearingBackStack;
    private boolean isHomeFragmentCreated;
    private boolean isIntentQueued;
    private Queue<AppDialog> dialogQueue;
    private boolean isLandscapeLocked;
    protected boolean isPortraitOnly;
    private boolean isStartQueued;
 private SparseArray<PermissionRequestCallback> permissionRequestCallbacks;
    private List<Fragment> removedFragments;


    public interface PermissionRequestCallback {
        void onResponse(boolean z, boolean z2);
    }


    static int access$006(AppActivity activity) {
        int i = activity.backStackItemsRemoved - 1;
        activity.backStackItemsRemoved = i;

        return i;
    }

    protected boolean handleBackPress() {
        return false;
    }


    class C11182 implements AppFragment.NavigationPromptListener {
        C11182() {
        }

        public void onAction(boolean allowNavigation) {
            if (allowNavigation) {
                AppActivity.this.popFragment();
                AppActivity.super.onBackPressed();
            }
        }
    }

    public void onBackPressed() {
        if (!handleBackPress()) {
            Fragment fragment = this.fragmentManager.findFragmentById(FRAGMENT_CONTAINER);
            if (fragment instanceof AppFragment) {
                AppFragment appFragment = (AppFragment) fragment;
                if (!appFragment.onBackPressed()) {
                    if (appFragment.interceptNavigation()) {
                        appFragment.promptNavigate(new C11182());
                        return;
                    }
                }
                return;
            }
            popFragment();
            if (this.backStackFragments.size() == 0) {
                super.onBackPressed();
            }
            super.onBackPressed();
        }
    }


    class BackStack implements FragmentManager.OnBackStackChangedListener {
        BackStack() {
        }

        public void onBackStackChanged() {
            Log.i("BackStack", "Changed " + AppActivity.this.backStackItemsRemoved + " isClearingBackStack: " + AppActivity.this.isClearingBackStack);
            if (!AppActivity.this.isClearingBackStack) {
                if (!AppActivity.this.removedFragments.isEmpty()) {
                    List<Fragment> fragments = AppActivity.this.fragmentManager.getFragments();
                    for (Fragment fragment : AppActivity.this.removedFragments) {
                        int index = fragments.indexOf(fragment);
                        if (index >= 0) {
                            fragments.set(index, null);
                            Log.i("BackStack", "Removed Fragment: " + fragment.getClass().toString());
                        } else {
                            Log.i("BackStack", "Skipped remove Fragment: " + fragment.getClass().toString());
                        }
                    }
                    AppActivity.this.removedFragments.clear();
                }
                AppActivity.this.setHeaderShift(0.0f);
                AppActivity.this.syncFragmentState(null);
            } else if (AppActivity.access$006(AppActivity.this) == 0) {
                AppActivity.this.isClearingBackStack = false;
            }
        }
    }

    public void setHeaderShift(float shift) {
    }

    public AppActivity() {
        isHomeFragmentCreated = false;
        isClearingBackStack = false;
     backStackItemsRemoved = 0;
      dialogQueue = new ArrayDeque(OFFSET_TABS);
      permissionRequestCallbacks = new SparseArray();
    }

    protected AriadnaApplication getApp() {
        return AriadnaApplication.getInstance();
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setViewPagerPage(int position, String name, String externalName) {
       // sendAnalyticsHit(externalName);
    }

    public void promptLocationPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && getApp().getSettings().isLocationEnabled() && checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            String[] strArr = new String[OFFSET_TABS];
            strArr[0] = "android.permission.ACCESS_FINE_LOCATION";
            strArr[OFFSET_TOOLBAR] = "android.permission.ACCESS_COARSE_LOCATION";
            requestPermissions(strArr, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    class ChangeFragmente implements AppFragment.NavigationPromptListener {
        final Fragment toFragment;

        ChangeFragmente(Fragment fragment) {
            this.toFragment = fragment;
        }

        public void onAction(boolean allowNavigation) {
            if (!allowNavigation) {
                return;
            }
            if (this.toFragment == null) {
                AppActivity.this.navigateHome(-1, true);
            } else {
                AppActivity.this.navigate(this.toFragment, true);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        getApp().setActivity(this);
        super.onCreate(savedInstanceState);
        if (!getResources().getBoolean(R.bool.landscape_mode)) {
            setRequestedOrientation(OFFSET_TOOLBAR);
            this.isPortraitOnly = true;
        }
        this.backStackFragments = new Stack();
        this.removedFragments = new ArrayList();
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentManager.addOnBackStackChangedListener(new BackStack());
        this.isIntentQueued = true;
    }

    protected void onStart() {
        super.onStart();
        this.isStartQueued = true;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.isIntentQueued = true;
    }

    protected void resetHome() {
    }

    protected abstract void initializeApp();

    protected abstract void syncFragmentState(Fragment fragment);

    protected abstract boolean canHandleIntentAction(String str);

    protected abstract AppFragment createHomeFragment();


    protected void navigateHomeOnStart() {
        navigateHome();
    }

    public void navigate(Fragment fragment) {
        navigate(fragment, false);
    }

    public void navigateHome() {
        navigateHome(-1, false);
    }

    public void navigateHome(int position) {
        navigateHome(position, false);
    }

    private void navigateHome(int position, boolean skipNavigationPrompt) {
        if (position != -1) {
            PagerFragment.storeSelection("Home", position);
        }
        if (this.isHomeFragmentCreated) {
            if (this.fragmentManager.getBackStackEntryCount() > 0) {
                Fragment current = this.fragmentManager.findFragmentById(FRAGMENT_CONTAINER);
                if (current instanceof AppFragment) {
                    AppFragment appFragment = (AppFragment) current;
                    if (skipNavigationPrompt || !promptNavigation(appFragment, null)) {
                        appFragment.forcefeedTransitionsOnce(R.anim.exit_to_top);
                    } else {
                        return;
                    }
                }
                this.homeFragment.forcefeedTransitionsOnce(R.anim.enter_from_bottom);
            }
            if (!clearBackStack(true)) {
                resetHome();
            }
        } else {
            this.homeFragment = createHomeFragment();
            navigate(this.homeFragment);
            this.isHomeFragmentCreated = true;
         //   getApp().getExperience().showLaunchPopup();
        }
        if (this.isIntentQueued) {
            this.isIntentQueued = false;
         //   handleIntentAction();
        }
    }

    private void navigate(Fragment fragment, boolean skipNavigationPrompt) {
        boolean addToBackStack = true;
        AppFragment.Entry entry = AppFragment.DEFAULT_ENTRY;
        String entryName = null;
        if (fragment instanceof AppFragment) {
            Fragment currentFragment = this.fragmentManager.findFragmentById(FRAGMENT_CONTAINER);
            AppFragment appFragment = (AppFragment) fragment;
            if (skipNavigationPrompt || !(currentFragment instanceof AppFragment) || !promptNavigation((AppFragment) currentFragment, fragment)) {
                entry = appFragment.getEntry(currentFragment);
                entryName = appFragment.getEntryName();
                if (appFragment.isEntryPoint()) {
                    addToBackStack = false;
                }
                if (entry.forcefeedTransitions()) {
                    appFragment.forcefeedTransitionsOnce(entry.getForcefeedInAnimation());
                    if (currentFragment instanceof AppFragment) {
                        ((AppFragment) currentFragment).forcefeedTransitionsOnce(entry.getForcefeedOutAnimation());
                    }
                }
                if (appFragment.isMenuEnabled()) {
                    clearBackStack(false);
                } else if (entry.altersBackStack()) {
                    if (entry.getAcceptableBackEntries() != null) {
                        clearBackStackUntil(entry.getAcceptableBackEntries(), false);
                    }
                    if (entry.getInacceptableBackEntries() != null) {
                        clearBackStackUntil(entry.getInacceptableBackEntries(), true);
                    }
                }
                if (entry.injectsFragments()) {
                    injectFragments(entry.getInjectedFragments());
                }
            } else {
                return;
            }
        }
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.setCustomAnimations(entry.getForwardInAnimation(), entry.getForwardOutAnimation(), entry.getBackwardInAnimation(), entry.getBackwardOutAnimation());
        if (addToBackStack) {
            transaction.addToBackStack(entryName);
            this.backStackFragments.push(new WeakReference(fragment));
        } else {
            syncFragmentState(fragment);
        }
        transaction.replace(FRAGMENT_CONTAINER, fragment);
        transaction.commitAllowingStateLoss();
    }

    public boolean navigateBack() {
        if (this.fragmentManager.getBackStackEntryCount() == 0) {
            return false;
        }
        popFragment();
        this.fragmentManager.popBackStackImmediate();
        return true;
    }

    public void enqueueDialog(AppDialog appDialog) {
        this.dialogQueue.add(appDialog);
    }

    public void navigateBack(String entry) {
        clearBackStackUntil(Collections.singletonList(entry), false);
    }

    private void injectFragments(List<Fragment> injectedFragments) {
        for (Fragment fragment : injectedFragments) {
            String entryName = null;
            if (fragment instanceof AppFragment) {
                entryName = ((AppFragment) fragment).getEntryName();
            }
            FragmentTransaction transaction = this.fragmentManager.beginTransaction();
            transaction.setCustomAnimations(0, 0, AppFragment.DEFAULT_ENTRY.getBackwardInAnimation(), AppFragment.DEFAULT_ENTRY.getBackwardOutAnimation());
            transaction.addToBackStack(entryName);
            transaction.replace(FRAGMENT_CONTAINER, fragment);
            this.backStackFragments.push(new WeakReference(fragment));
            transaction.commitAllowingStateLoss();
        }
    }


    private void clearBackStackUntil(List<String> names, boolean invert) {
        this.isClearingBackStack = true;
        for (int backStackCount = this.fragmentManager.getBackStackEntryCount(); backStackCount != 0; backStackCount--) {
            int i;
            String stackName = this.fragmentManager.getBackStackEntryAt(backStackCount - 1).getName();
            if (stackName == null || !names.contains(stackName)) {
                i = 0;
            } else {
                i = OFFSET_TOOLBAR;
            }
            if (i != 0) {
                break;
            }
            popFragment();
            this.fragmentManager.popBackStack();
        }
        this.isClearingBackStack = false;
    }

    protected void beforePromptNavigation(AppFragment fromFragment) {
    }

    protected void onResume() {
        super.onResume();

        if (this.isStartQueued) {
            this.isStartQueued = false;
            if (!getApp().isInitialized()) {
                initializeApp();

            } else if (this.fragmentManager.findFragmentById(FRAGMENT_CONTAINER) == null) {

                navigateHomeOnStart();

            } else {
                syncFragmentState(null);
                if (this.isIntentQueued) {
                    this.isIntentQueued = false;
                   // handleIntentAction();
                }
            }
            getApp().initializeInternet();
        }

        this.isActive = true;
      //  AppGcmListenerService.clearById(this, 0);
        while (!this.dialogQueue.isEmpty()) {
            ((AppDialog) this.dialogQueue.poll()).show(getSupportFragmentManager());
        }
    }


    private boolean promptNavigation(AppFragment fromFragment, Fragment toFragment) {
        if (!fromFragment.interceptNavigation()) {
            return false;
        }
        beforePromptNavigation(fromFragment);
        fromFragment.promptNavigate(new ChangeFragmente(toFragment));
        return true;
    }



    private boolean clearBackStack(boolean allowSync) {
        boolean backStackChanged = false;
        int backStackCount = this.fragmentManager.getBackStackEntryCount();
        this.backStackItemsRemoved = backStackCount;
        boolean z = !allowSync && backStackCount > 0;
        this.isClearingBackStack = z;
        while (backStackCount != 0) {
            popFragment();
            this.fragmentManager.popBackStack();
            backStackChanged = true;
            backStackCount--;
        }
        return backStackChanged;
    }

    private void popFragment() {
        if (!this.backStackFragments.empty()) {
            Fragment fragment = (Fragment) ((WeakReference) this.backStackFragments.pop()).get();
            if (fragment != null) {
                this.removedFragments.add(fragment);
                Log.i("BackStack", " Scheduled remove: " + fragment.getClass().toString());
            }
            if (this.invalidatePreviousFragment && !this.backStackFragments.isEmpty()) {
                Fragment next = (Fragment) ((WeakReference) this.backStackFragments.peek()).get();
                if (next instanceof AppFragment) {
                    ((AppFragment) next).invalidate();
                    this.invalidatePreviousFragment = false;
                }
            }
        }
    }

    public int getToolbarOffset(int flags) {
        return 0;
    }


    protected void onPause() {
        super.onPause();
        this.isActive = false;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("back_stack_fragment_count", this.backStackFragments.size());
        this.isActive = false;
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int backstackCount = savedInstanceState.getInt("back_stack_fragment_count", 0);
        for (int i = 0; i < backstackCount; i += OFFSET_TOOLBAR) {
            this.backStackFragments.push(new WeakReference(null));
        }
    }


}
