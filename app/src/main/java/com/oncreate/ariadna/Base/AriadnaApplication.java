package com.oncreate.ariadna.Base;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.oncreate.ariadna.AchievementManager;
import com.oncreate.ariadna.CourseManager;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.ImageManager;
import com.oncreate.ariadna.ModelsVO.Course;
import com.oncreate.ariadna.ProgressManager;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.SettingsManager;
import com.oncreate.ariadna.UI.Fragments.ModulesFragment;
import com.oncreate.ariadna.UserManager;
import com.oncreate.ariadna.Util.StorageService;
import com.oncreate.ariadna.loginLearn.WebService;

/**
 * Created by azulandres92 on 8/12/16.
 */

public class AriadnaApplication extends Application {

    private static AriadnaApplication app;
    private AppActivity activity;
    private boolean isInitialized;
    private boolean isPlayEnabled;
    public StorageService storage;
    private UserManager userManager;
    public SettingsManager settingsManager;
    private CourseManager courseManager;
    private boolean isProgressInitialized;
    private ProgressManager progressManager;
    private ImageManager imageManager;
    private AchievementManager achievementManager;
    private WebService webService;

    public static AriadnaApplication getInstance() {
        return app;
    }


    public interface InitializationListener {
        void onError();

        void onSuccess();

    }


    class inicializationCurso implements CourseManager.Listener {
        final /* synthetic */ ModulesFragment.InitializationListener val$listener;

        inicializationCurso(ModulesFragment.InitializationListener initializationListener) {
            this.val$listener = initializationListener;
        }

        public void onResult(Course course) {
            if (course != null) {
                AriadnaApplication.this.isInitialized = true;
                AriadnaApplication.this.initializeComponents();
                this.val$listener.onSuccess();
                return;
            }
            this.val$listener.onError();
        }

    }


    class MensajeInternet implements MessageDialog.Listener {

        public void onResult(int result) {

            if (result == MessageDialog.YES) {

                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Resources r = getResources();
        isPlayEnabled = r.getBoolean(R.bool.is_play_enabled);
        storage = new StorageService(this);
        achievementManager = new AchievementManager(getImageManager(), this.storage);
        settingsManager = new SettingsManager(this, this.storage, isPlayEnabled);
        webService = new WebService(this, this.storage, this.settingsManager);
        courseManager = new CourseManager(this.storage, webService);
        progressManager = new ProgressManager(webService, this.storage, this.courseManager, this.userManager, achievementManager);

        userManager = new UserManager(webService, storage);

        if (settingsManager.shouldResetCourse()) {
            courseManager.reset();
        }
        //  progressManager.setProgressListener(new C11092());

    }


    public ImageManager getImageManager() {
        if (this.imageManager == null) {
            this.imageManager = new ImageManager(this, this.storage);
        }
        return this.imageManager;
    }

    public ProgressManager getProgressManager() {
        if (!this.isProgressInitialized) {
            this.progressManager.initializeFromCache();
            this.isProgressInitialized = true;
        }
        return this.progressManager;
    }


    public CourseManager getCourseManager() {
        return this.courseManager;
    }

    public boolean isPlayEnabled() {
        return this.isPlayEnabled;
    }


    public boolean isInitialized() {
        return this.isInitialized;
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(view, 1);
        }
    }


    public void initializeComponents() {
        getProgressManager();
        if (this.userManager.isAuthenticated()) {
            this.progressManager.sync();
        }
    }


    public void iniciarCurso(ModulesFragment.InitializationListener listener) {

        this.courseManager.initialize(new inicializationCurso(listener));

    }


    public void initializeInternet() {
        if (!webService.isNetworkAvailable())
            MessageDialog.create(getActivity(), (int) R.string.no_internet_connection_title, (int) R.string.no_internet_connection_message, (int) R.string.action_ok, (int) MessageDialog.YES, new MensajeInternet()).show(getActivity().getSupportFragmentManager());

    }


    public void initializeInternet(InitializationListener listener) {
        if (webService.isNetworkAvailable())
            listener.onSuccess();
        else
            listener.onError();
    }


    public StorageService getStorage() {
        return this.storage;
    }

    public boolean hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (this.activity.getCurrentFocus() == null) {
            return false;
        }
        inputMethodManager.hideSoftInputFromWindow(this.activity.getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    public SettingsManager getSettings() {
        return this.settingsManager;
    }

    public AppActivity getActivity() {
        return this.activity;
    }

    public void setActivity(AppActivity activity) {
        this.activity = activity;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public void uninitialize() {
        this.isInitialized = false;
    }

}
