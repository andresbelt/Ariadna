package com.oncreate.ariadna.UI;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListAdapter;

import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.Fragments.LoginFragment;
import com.oncreate.ariadna.UI.Fragments.ModulesFragment;
import com.oncreate.ariadna.UI.Fragments.PagerFragment;
import com.oncreate.ariadna.UserManager;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private static final String[] acceptableIntentActions;
    private int previousOrientation;
    boolean isDrawerEnabled;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    public HomeActivity() {
        this.previousOrientation = -1;
        this.isDrawerEnabled = true;
    }

    static {
        acceptableIntentActions = new String[]{"LoadContest", "LoadLessonDiscussion", "LoadLessonComments", "LoadDiscussion", "LoadProfile", "CheckAchievements"};
    }


    public void ErrorLogout() {

        UserManager userManager = AriadnaApplication.getInstance().getUserManager();
        if (userManager.isAuthenticated()) {
            HomeActivity.this.toggleNavigationMenu(true);
            userManager.logout();
            HomeActivity.this.navigateHome();
        }
    }


    class Drawer implements DrawerListener {
        Drawer() {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (HomeActivity.this.isDrawerEnabled) {
                HomeActivity.this.drawerToggle.onDrawerSlide(drawerView, slideOffset);
            }
        }

        public void onDrawerOpened(View drawerView) {
            if (HomeActivity.this.isDrawerEnabled) {
                HomeActivity.this.drawerToggle.onDrawerOpened(drawerView);
            }
        }

        public void onDrawerClosed(View drawerView) {
            if (HomeActivity.this.isDrawerEnabled) {
                HomeActivity.this.drawerToggle.onDrawerClosed(drawerView);
            }
        }

        public void onDrawerStateChanged(int newState) {
            if (HomeActivity.this.isDrawerEnabled) {
                HomeActivity.this.drawerToggle.onDrawerStateChanged(newState);
            }
        }
    }


    class DarwerListener implements ValueAnimator.AnimatorUpdateListener {
        DarwerListener() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            HomeActivity.this.drawerToggle.onDrawerSlide(HomeActivity.this.drawerLayout, ((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Si el app tiene cache
        //getApp().initializeFromCache();
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_principal);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbare);
        this.navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        this.navigationView.setNavigationItemSelectedListener(this);
        initializeMenu();
         //  this.headerList = (ViewGroup) findViewById(R.id.header_list);
         //   this.headerListView = (HorizontalListView) findViewById(R.id.header_list_view);
         //  this.headerExtra = (ViewGroup) findViewById(R.id.header_extra);
//        if (!getApp().isPlaygroundEnabled()) {
//            this.navigationView.getMenu().findItem(R.id.navigation_action_playground).setVisible(false);
//        }
//        if (!getApp().isDiscussionEnabled()) {
//            this.navigationView.getMenu().findItem(R.id.navigation_action_discussion).setVisible(false);
//        }
      setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.acc_open_drawer, R.string.acc_close_drawer);
        drawerLayout.setDrawerListener(new Drawer());
        promptLocationPermissions();

    }



    private void initializeMenu() {
//        View header = this.navigationView.inflateHeaderView(R.layout.view_navigation_header);
//        this.menuAvatar = (ImageView) header.findViewById(R.id.menu_avatar);
//        this.menuUser = (ViewGroup) header.findViewById(R.id.menu_user);
//        this.menuName = (TextView) header.findViewById(R.id.menu_name);
//        this.menuEmail = (TextView) header.findViewById(R.id.menu_email);
//        this.menuLogin = (Button) header.findViewById(R.id.menu_login);
//        this.menuLogout = (Button) header.findViewById(R.id.menu_logout);
//        this.menuUser.setOnClickListener(new C04685());
//        this.menuLogout.setOnClickListener(new C04696());
//        this.menuLogin.setOnClickListener(new C04707());
        handleUserChange();
    }

    private void handleUserChange() {
        UserManager user = AriadnaApplication.getInstance().getUserManager();
        if (user.isAuthenticated()) {
//            this.menuEmail.setText(user.getEmail());
//            this.menuName.setText(user.getTitulo());
//            this.menuLogin.setVisibility(8);
//            this.menuAvatar.setImageResource(C0471R.drawable.no_avatar);
//            getApp().getImageManager().getAvatarFromCacheAndUpdate(user.getId(), new C11288());
//            this.menuUser.setVisibility(0);
            Log.d("e","logueado");
            return;
        }
        Log.d("e", "nologueado");

//        this.menuLogin.setVisibility(0);
//        this.menuUser.setVisibility(8);
    }

    @Override
    protected void initializeApp() {
        navigateHomeOnStart();
    }



    protected void navigateHomeOnStart() {

        boolean forceSplash = true;

        if (getApp().getUserManager().isAuthenticated()) {
            forceSplash = false;
        }

        if (forceSplash) {
            navigate(new LoginFragment());

        } else {
            super.navigateHomeOnStart();
        }



    }

    protected void resetHome() {

        toggleNavigationMenu(true);
    }


    @Override
    protected void syncFragmentState(Fragment fragment) {
        int i = 0;
        if (fragment == null) {
            fragment = this.fragmentManager.findFragmentById(R.id.container);
        }
        ListAdapter headerAdapter = null;
        if (fragment != null) {
            if (fragment instanceof AppFragment) {
                AppFragment appFragment = (AppFragment) fragment;
                toggleNavigationMenu(appFragment.isMenuEnabled(), appFragment.isMenuBlocked());
                toggleToolbar(appFragment.isToolbarEnabled());
                syncNavbarState(appFragment);
                headerAdapter = appFragment.getHeaderAdapter();
               // ViewGroup.LayoutParams extraLP = this.headerExtra.getLayoutParams();
//                if (appFragment.inflateHeaderExtras(getLayoutInflater(), this.headerExtra)) {
//                    extraLP.height = -2;
//                } else {
//                    extraLP.height = 0;
//                }
//                this.headerExtra.setLayoutParams(extraLP);
                getSupportActionBar().setTitle(appFragment.getName());
//                if (!(appFragment instanceof PagerFragment)) {
//                   // sendAnalyticsHit(appFragment.getExternalName());
//                }
//                if (appFragment.getMenuId() == R.id.navigation_action_home) {
//                   // getApp().getExperience().navigatedHome();
//                }
                if (Build.VERSION.SDK_INT >= 21) {
                    float headerElevation = appFragment.getHeaderElevation();
                    if (headerElevation < 0.0f) {
                        headerElevation = getResources().getDimension(R.dimen.header_elevation);
                    }
                    this.toolbar.setElevation(headerElevation);
                }
            } else {
                toggleNavigationMenu(false);
                toggleToolbar(true);
               //this.headerExtra.removeAllViews();
            }
        }
      //  HorizontalListView horizontalListView = this.headerListView;
//        if (headerAdapter == null) {
//            i = 8;
//        }
//        horizontalListView.setVisibility(i);
//        this.headerListView.setAdapter(headerAdapter);
//        if (headerAdapter != null) {
//            this.headerListView.setSelection(headerAdapter.getSelectedPosition());
//        }
        getApp().hideSoftKeyboard();
    }

    protected boolean canHandleIntentAction(String action) {
        return Arrays.asList(acceptableIntentActions).contains(action);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }


    private void toggleToolbar(boolean enabled) {
        if (enabled) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }


    private void toggleNavigationMenu(boolean enable) {
        toggleNavigationMenu(enable, false);
    }

    private void toggleNavigationMenu(boolean enable, boolean block) {
        float f = 0.0f;
        if (this.drawerLayout != null) {
            boolean wasEnabled = this.isDrawerEnabled;
            if (enable) {
                this.drawerLayout.closeDrawer(this.navigationView);
            }
            if (block) {
                this.drawerLayout.setDrawerLockMode(1);
            } else if (this.drawerLayout.getDrawerLockMode(this.navigationView) == 1) {
                this.drawerLayout.setDrawerLockMode(0);
            }
            if (wasEnabled != enable) {
                float f2;
                this.isDrawerEnabled = enable;
                float[] fArr = new float[2];
                if (enable) {
                    f2 = 1.0f;
                } else {
                    f2 = 0.0f;
                }
                fArr[0] = f2;
                if (!enable) {
                    f = 1.0f;
                }
                fArr[1] = f;
                ValueAnimator anim = ValueAnimator.ofFloat(fArr);
                anim.addUpdateListener(new DarwerListener());
                anim.setInterpolator(new DecelerateInterpolator());
                anim.setDuration(300);
                anim.start();
            }
        }
    }

    private void syncNavbarState(AppFragment appFragment) {
        int menuId = appFragment.getMenuId();
        if (menuId != 0) {
            this.navigationView.getMenu().findItem(menuId).setChecked(true);
            return;
        }
        Menu menu = this.navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
    }


    @Override
    protected AppFragment createHomeFragment() {
        AppFragment homeFragment;
        if (getApp().isPlayEnabled()) {
            PagerFragment pagerFragment = new PagerFragment();
            pagerFragment.setSelectionStore("Home");
            pagerFragment.setIsMenuEnabled(true);
            pagerFragment.setMenuId(R.id.navigation_action_home);
            pagerFragment.setIsEntryPoint(true);
            pagerFragment.addFragment(R.string.page_title_home_learn, ModulesFragment.class);
        //    pagerFragment.addFragment(R.string.page_title_home_play, LoginFragment.class);
            homeFragment = pagerFragment;
        } else {
            homeFragment = new ModulesFragment();
        }
        //homeFragment.setTitulo(getApp().getCourseManager().getCourse().getTitulo());
        clearFragmentManager();
        return homeFragment;
    }


    private void clearFragmentManager() {
        List<Fragment> fragments = this.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    int index = fragments.indexOf(fragment);
                    if (index >= 0) {
                        fragments.set(index, null);
                        Log.i("BackStack", "Removed Initial Fragment: " + fragment.getClass().toString());
                    } else {
                        Log.i("BackStack", "Skipped Initial remove Fragment: " + fragment.getClass().toString());
                    }
                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        getApp().uninitialize();
        //   getApp().getCourseManager().removeOnUpdateListener(this.appListeners);
        //    getApp().getUserManager().removeListener(this.appListeners);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.isDrawerEnabled && this.drawerLayout.getDrawerLockMode(this.navigationView) == 0 && this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean handleBackPress() {
        if (!this.drawerLayout.isDrawerOpen(this.navigationView)) {
            return false;
        }
        this.drawerLayout.closeDrawer(this.navigationView);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_action_home /*2131755721*/:
                navigateHome();
                return true;
            case R.id.navigation_action_glossary /*2131755726*/:
                //   fragment = new GlossaryFragment();
                break;
            case R.id.navigation_action_settings /*2131755729*/:
                // fragment = new SettingsFragment();
                break;
            default:
                return false;
        }
        navigate(fragment);
        return true;
    }
}
