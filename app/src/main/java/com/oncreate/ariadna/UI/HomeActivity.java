package com.oncreate.ariadna.UI;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Request.Services;
import com.oncreate.ariadna.UI.Fragments.ModulesFragment;
import com.oncreate.ariadna.UI.Fragments.PagerFragment;
import com.oncreate.ariadna.UserManager;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue requestQueue;
    JsonObjectRequest jsArrayRequest;
    private Button btn_ing;
    private EditText email;
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

    class C11261 implements DrawerListener {
        C11261() {
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
        drawerLayout.setDrawerListener(new C11261());
//        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        if (Build.VERSION.SDK_INT >= OFFSET_LIST) {
//            switchToChangeTransition(this.tabLayout.getLayoutTransition());
//            switchToChangeTransition(this.headerList.getLayoutTransition());
//            switchToChangeTransition(this.headerExtra.getLayoutTransition());
//        }
    //    this.appListeners = new AppListeners();
   //     getApp().getCourseManager().addOnUpdateListener(this.appListeners);
       // getApp().getUserManager().addListener(this.appListeners);
     //   getApp().getAchievementManager().setContainer((ViewGroup) findViewById(C0471R.id.achievement_container));
        promptLocationPermissions();
     //   calculateHeaderDimensions();

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
//            this.menuName.setText(user.getName());
//            this.menuLogin.setVisibility(8);
//            this.menuAvatar.setImageResource(C0471R.drawable.no_avatar);
//            getApp().getImageManager().getAvatarFromCacheAndUpdate(user.getId(), new C11288());
//            this.menuUser.setVisibility(0);
            Log.d("e","logueado");
            return;
        }
        Log.d("e","nologueado");

//        this.menuLogin.setVisibility(0);
//        this.menuUser.setVisibility(8);
    }

    @Override
    protected void initializeApp() {
        navigateHomeOnStart();
    }



    protected void navigateHomeOnStart() {

        boolean forceSplash = true;
        boolean i = true;

        if (!(forceSplash || getApp().getUserManager().isAuthenticated())) {
            boolean i2;
            forceSplash = false;
            if (!getApp().isStartupLoginEnabled() || getApp().getSettings().isLoginSkipPreferred()) {
                i2 = false;
            } else {
                i2 = true;
            }
            forceSplash = i2;
            if (getApp().isStartupLoginEnabled() || !getApp().getSettings().forceSplashLogin()) {
                i = false;
            }
            forceSplash = i;
        }
        if (forceSplash) {

            navigate(new LoginFragment());
            getApp().getSettings().setForceSplashLogin(false);

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
            toggleTabBar(fragment instanceof PagerFragment);
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
     //   getApp().hideSoftKeyboard();
    }

    protected boolean canHandleIntentAction(String action) {
        return Arrays.asList(acceptableIntentActions).contains(action);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }



    private void toggleTabBar(boolean enabled) {
//        if (this.tabLayout != null) {
//            LayoutParams lp = this.tabLayout.getLayoutParams();
//            lp.height = enabled ? -2 : 0;
//            this.tabLayout.setLayoutParams(lp);
//        }
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

    public void openNavigationMenu() {
        this.drawerLayout.openDrawer(this.navigationView);
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
        //homeFragment.setName(getApp().getCourseManager().getCourse().getName());
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

    public void setViewPager(ViewPager viewPager) {
//        tabLayout.setupWithViewPager(null);
//        if (viewPager != null && this.tabLayout.getTabCount() > 0 && viewPager.getAdapter() != null && this.tabLayout.getSelectedTabPosition() >= viewPager.getAdapter().getCount()) {
//            Tab firstTab = this.tabLayout.getTabAt(0);
//            if (firstTab != null) {
//                firstTab.select();
//            }
//        }
//        this.tabLayout.setupWithViewPager(viewPager);
//        if (viewPager != null && (viewPager.getAdapter() instanceof Adapter)) {
//            Adapter adapter = (Adapter) viewPager.getAdapter();
//            int tabCount = this.tabLayout.getTabCount();
//            for (int i = 0; i < tabCount; i++) {
//                View customTab = adapter.getTabView(i);
//                Tab tab = this.tabLayout.getTabAt(i);
//                if (!(customTab == null || tab == null)) {
//                    tab.setCustomView(customTab);
//                    customTab.setSelected(tab.isSelected());
//                }
//            }
//        }
    }




    @Override
    public void setTab(int i) {

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_ingre:

                if (!email.getText().toString().isEmpty()) {


                    Services.loginPost(this, email.getText().toString());
//                    JSONObject js = new JSONObject();
//                    try {
//                        js.put("email", email.getText());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    jsArrayRequest = new JsonObjectRequest(Request.Method.POST, ConstantVariables.URL_BASE + "/login", js, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            Log.d(ConstantVariables.TAG, "Respuesta en JSON: " + response);
//                            Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
//
//                            startActivity(intent);
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.d(ConstantVariables.TAG, "Error Respuesta en JSON: " + error.getMessage());
//
//                        }
//
//
//                    }) {
//
//
//                        @Override
//                        protected VolleyError parseNetworkError(VolleyError volleyError) {
//                            if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//                                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//                                volleyError = error;
//                            }
//
//                            return volleyError;
//                        }
//
//
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            HashMap<String, String> headers = new HashMap<String, String>();
//                            headers.put("Authorization", "Bearer " + "000");
//
//                            return headers;
//                        }
//
//                    };
//
//
//                    requestQueue.add(jsArrayRequest);
                }


                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
