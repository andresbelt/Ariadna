package com.oncreate.ariadna;

import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.oncreate.ariadna.ModelsVO.Achievement;
import com.oncreate.ariadna.UI.CircleImageView;
import com.oncreate.ariadna.Util.StorageService;

import java.util.ArrayList;
import java.util.Set;

public class AchievementManager {
    private static final String IGNORED_ACHIEVEMENT_PREFIX = "ignore_achievement_";
    private TextView achievementDesc;
    private CircleImageView achievementIcon;
    private View achievementRoot;
    private TextView achievementTitle;
    private ViewGroup container;
    private Achievement currentAchievement;
    private Animation hideAnimation;
    private ImageManager imageManager;
    private boolean isRunning;
    private ArrayList<Achievement> queue;
    private Animation showAnimation;

    private StorageService storageService;

    /* renamed from: com.sololearn.app.AchievementManager.1 */
    class C04591 implements OnClickListener {
        C04591() {
        }

        public void onClick(View v) {
            if (AchievementManager.this.currentAchievement != null) {
             //   AchievementsFragment.handlePopup(AchievementManager.this.currentAchievement.getId());
            }
        }
    }

    /* renamed from: com.sololearn.app.AchievementManager.2 */
    class C04612 implements AnimationListener {

        /* renamed from: com.sololearn.app.AchievementManager.2.1 */
        class C04601 implements Runnable {
            C04601() {
            }

            public void run() {
                AchievementManager.this.showQueuedAchievement();
            }
        }

        C04612() {
        }

        public void onAnimationStart(Animation animation) {
            if (animation == AchievementManager.this.showAnimation) {
                AchievementManager.this.container.setVisibility(0);
               // AchievementManager.this.soundService.play(4);
            }
        }

        public void onAnimationEnd(Animation animation) {
            if (animation == AchievementManager.this.showAnimation) {
                AchievementManager.this.achievementRoot.startAnimation(AchievementManager.this.hideAnimation);
                return;
            }
            AchievementManager.this.currentAchievement = null;
            AchievementManager.this.container.setVisibility(8);
            AchievementManager.this.container.postDelayed(new C04601(), 2000);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /* renamed from: com.sololearn.app.AchievementManager.3 */
    class C11113 implements ImageManager.Listener {
        final /* synthetic */ Achievement val$achievement;

        C11113(Achievement achievement) {
            this.val$achievement = achievement;
        }

        public void onResult(Bitmap result) {
            if (result != null) {
                AchievementManager.this.currentAchievement = this.val$achievement;
                AchievementManager.this.queue.remove(0);
                AchievementManager.this.achievementIcon.setImageBitmap(result);
                AchievementManager.this.achievementIcon.setFillColor(Color.parseColor(this.val$achievement.getColor()));
                AchievementManager.this.achievementTitle.setText(this.val$achievement.getTitle());
                AchievementManager.this.achievementDesc.setText(this.val$achievement.getDescription());
                AchievementManager.this.achievementRoot.startAnimation(AchievementManager.this.showAnimation);
                return;
            }
            AchievementManager.this.stopQueue();
        }
    }

    public AchievementManager(ImageManager imageManager, StorageService storageService) {
        this.queue = new ArrayList();
        this.isRunning = false;
        this.imageManager = imageManager;
        this.storageService = storageService;
      //  this.soundService = soundService;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
        container.setVisibility(8);
    }

    public void enqueueAchievement(Achievement achievement) {
        if (this.storageService.getBoolean(IGNORED_ACHIEVEMENT_PREFIX + achievement.getId(), false)) {
            this.storageService.getPreferences().edit().remove(IGNORED_ACHIEVEMENT_PREFIX + achievement.getId()).apply();
            return;
        }
        this.queue.add(achievement);
        startQueue();
    }

    private void startQueue() {
        if (!this.isRunning && this.container != null) {
            this.isRunning = true;
           // this.soundService.requestSound(4);
//            this.achievementRoot = LayoutInflater.from(this.container.getContext()).inflate(R.layout.popup_achievement, this.container, true);
//            this.achievementRoot.setVisibility(8);
//            this.achievementIcon = (CircleImageView) this.achievementRoot.findViewById(R.id.popup_achievements_image);
//            this.achievementTitle = (TextView) this.achievementRoot.findViewById(R.id.popup_achievements_title);
//            this.achievementDesc = (TextView) this.achievementRoot.findViewById(R.id.popup_achievements_desc);
//            this.achievementRoot.findViewById(R.id.popup_achievement).setOnClickListener(new C04591());
//            this.showAnimation = AnimationUtils.loadAnimation(this.container.getContext(), R.anim.show_achievements);
//            this.hideAnimation = AnimationUtils.loadAnimation(this.container.getContext(), R.anim.hide_achievements);
            AnimationListener animationListener = new C04612();
            this.showAnimation.setAnimationListener(animationListener);
            this.hideAnimation.setAnimationListener(animationListener);
            this.hideAnimation.setStartOffset(2000);
            showQueuedAchievement();
        }
    }

    private void stopQueue() {
        if (this.isRunning) {
            this.isRunning = false;
          //  this.soundService.releaseSound(4);
            this.container.removeAllViews();
            this.achievementRoot = null;
            this.achievementIcon = null;
            this.achievementTitle = null;
            this.achievementDesc = null;
            this.showAnimation = null;
            this.hideAnimation = null;
        }
    }

    private void showQueuedAchievement() {
        if (this.queue.size() == 0) {
            stopQueue();
            return;
        }
        Achievement achievement = (Achievement) this.queue.get(0);
        String iconName = achievement.getId() + ".png";
        this.imageManager.getImage("achievements/" + iconName, "/uploads/achievements/" + iconName, new C11113(achievement));
    }

    public void ignoreAchievement(int achievementId) {
        this.storageService.setBoolean(IGNORED_ACHIEVEMENT_PREFIX + achievementId, true);
    }

    public void resetIgnored() {
        Set<String> keys = this.storageService.getPreferences().getAll().keySet();
        Editor editor = this.storageService.getPreferences().edit();
        for (String key : keys) {
            if (key.startsWith(IGNORED_ACHIEVEMENT_PREFIX)) {
                editor.remove(key);
            }
        }
        editor.apply();
    }
}
