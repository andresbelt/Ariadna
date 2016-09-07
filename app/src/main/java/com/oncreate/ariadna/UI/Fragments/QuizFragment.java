package com.oncreate.ariadna.UI.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oncreate.ariadna.Adapters.HeaderAdapter;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Dialog.MessageDialog;
import com.oncreate.ariadna.LessonManager;
import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Course;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ProgressManager;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.HomeActivity;
import com.oncreate.ariadna.UI.LoginFragment;
import com.oncreate.ariadna.UI.Quiz.QuizSelector;
import com.oncreate.ariadna.UI.Quiz.QuizView;
import com.oncreate.ariadna.UserManager;



import java.util.Date;
import java.util.List;

public class QuizFragment extends LessonFragmentBase implements QuizView.Listener {
    private Button checkButton;
    private Button commentsButton;
    private List<Answer> currentShuffledAnswers;
    private long endTime;
    private Handler handler;
    private View hintButton;
    private boolean isChecked;
    private boolean isCorrect;
    private boolean isStarted;
    private ProgressManager progressManager;
    private QuizSelector quizSelector;
    private String quizTimeFormat;
    private TextView resultAttempts;
    private Button resultButton;
    private ViewGroup resultContainer;
    private ImageView resultIcon;
    private TextView resultText;
    private ViewGroup resultView;
    private long startTime;
    private View unlockButton;
    private UserManager userManager;

    /* renamed from: com.sololearn.app.fragments.QuizFragment.5 */
    class C05475 implements Runnable {
        C05475() {
        }

        public void run() {
            QuizFragment.this.quizSelector.hint();
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.6 */
    class C05486 implements Runnable {
        C05486() {
        }

        public void run() {
            QuizFragment.this.quizSelector.unlock();
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.9 */
    class C05499 implements OnTouchListener {
        private final float maxClickDrag;
        private float resultDragMaxY;
        private float resultDragMinY;
        private float resultDragOriginalY;
        private float resultDragY;
        private boolean resultIsClick;

        C05499() {
            this.maxClickDrag = QuizFragment.this.getResources().getDimension(R.dimen.max_click_drag);
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case 0/*0*/:
                    this.resultDragOriginalY = event.getRawY();
                    this.resultDragY = QuizFragment.this.resultView.getY() - this.resultDragOriginalY;
                    this.resultDragMinY = QuizFragment.this.resultContainer.getY();
                    this.resultDragMaxY = (this.resultDragMinY + ((float) QuizFragment.this.resultContainer.getHeight())) - ((float) QuizFragment.this.resultView.getHeight());
                    this.resultIsClick = true;
                    break;
                case HomeActivity.OFFSET_TOOLBAR /*1*/:
                    if (this.resultIsClick) {
                        QuizFragment.this.resultView.performClick();
                        break;
                    }
                    break;
                case HomeActivity.OFFSET_TABS /*2*/:
                    float rawY = event.getRawY();
                    QuizFragment.this.resultView.setY(Math.min(this.resultDragMaxY, Math.max(this.resultDragMinY, this.resultDragY + rawY)));
                    if (Math.abs(this.resultDragOriginalY - rawY) > this.maxClickDrag) {
                        this.resultIsClick = false;
                        break;
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.1 */
    class C11931 implements QuizView.InputListener {
        C11931() {
        }

        public void onRequestInput(View view) {
            QuizFragment.this.getApp().showSoftKeyboard(view);
        }

        public void onReleaseInput() {
            QuizFragment.this.getApp().hideSoftKeyboard();
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.2 */
    class C11942 implements MessageDialog.Listener {
        C11942() {
        }

        public void onResult(int result) {
            QuizFragment.this.navigateBack();
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.3 */
    class C11953 implements MessageDialog.Listener {
        C11953() {
        }

        public void onResult(int result) {
            QuizFragment.this.navigateBack();
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.4 */
    class C11964 implements MessageDialog.Listener {
        C11964() {
        }

        public void onResult(int result) {
            if (result == -1) {
                QuizFragment.this.navigateBack();
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.7 */
    class C11977 implements MessageDialog.Listener {
        C11977() {
        }

        public void onResult(int result) {
            if (result == -1) {
                QuizFragment.this.navigate(LoginFragment.createBackStackAware());
            }
        }
    }

    /* renamed from: com.sololearn.app.fragments.QuizFragment.8 */
    class C11988 implements MessageDialog.Listener {
        final /* synthetic */ Runnable val$onAccept;
        final /* synthetic */ int val$requiredXp;
        final /* synthetic */ int val$type;

        C11988(int i, int i2, Runnable runnable) {
            this.val$requiredXp = i;
            this.val$type = i2;
            this.val$onAccept = runnable;
        }

        public void onResult(int result) {
            if (result == -1 && QuizFragment.this.canExchange(this.val$requiredXp)) {
                QuizFragment.this.progressManager.addExchange(QuizFragment.this.getLessonManager().getQuizId(), this.val$requiredXp, this.val$type);
                this.val$onAccept.run();
            }
        }
    }

    public QuizFragment() {
        this.isStarted = false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLessonManager().isShortcut()) {
            setHasOptionsMenu(false);
        }
        this.userManager = getApp().getUserManager();
        this.progressManager = getApp().getProgressManager();
        this.handler = new Handler();
        this.quizTimeFormat = getString(R.string.quiz_time_format);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        this.quizSelector = (QuizSelector) rootView.findViewById(R.id.quiz_selector);
        this.checkButton = (Button) rootView.findViewById(R.id.quiz_check_button);
        this.resultContainer = (ViewGroup) rootView.findViewById(R.id.quiz_result);
        this.resultView = (ViewGroup) rootView.findViewById(R.id.quiz_result_popup);
        this.resultIcon = (ImageView) rootView.findViewById(R.id.quiz_result_icon);
        this.resultText = (TextView) rootView.findViewById(R.id.quiz_result_text);
        this.resultAttempts = (TextView) rootView.findViewById(R.id.quiz_result_attempts);
        this.resultButton = (Button) rootView.findViewById(R.id.quiz_result_button);
        this.resultButton.setOnClickListener(this);
        this.checkButton.setOnClickListener(this);
        this.resultView.setOnClickListener(this);
        this.commentsButton = (Button) rootView.findViewById(R.id.quiz_comments_button);
        this.quizSelector.setListener(this);
        this.quizSelector.setInputListener(new C11931());
        this.quizSelector.setQuiz(getLessonManager().getQuiz(), this.currentShuffledAnswers);
        this.currentShuffledAnswers = this.quizSelector.getShuffledAnswers();
        setupResultDrag();
        this.commentsButton.setVisibility(8);
        if (this.isChecked) {
            setResult(this.isCorrect, false);
            this.quizSelector.setInputDisabled(true);
        } else {
            startQuiz();
        }
//        LessonManager lm = getLessonManager();
//        if (!lm.isShortcut() && getApp().getProgressManager().getQuizAttempt(lm.getLessonId(), lm.getQuizId()) > 2) {
//            Showcase showcase = getShowcase();
//            if (!showcase.isDismissed("quiz_unlock")) {
//                showcase.startSequence(ShowcaseSequence.create("quiz_unlock").addItem(ShowcaseItem.forView((int) R.id.quiz_unlock_button).withTitle((int) R.string.quiz_unlock_showcase_title).withMessage((int) R.string.quiz_unlock_showcase_message)));
//            } else if (!(this.quizSelector.getHintMode() == 10 || showcase.isDismissed("quiz_hint"))) {
//                showcase.startSequence(ShowcaseSequence.create("quiz_hint").addItem(ShowcaseItem.forView((int) R.id.quiz_hint_button).withTitle((int) R.string.quiz_hint_showcase_title).withMessage((int) R.string.quiz_hint_showcase_message)));
//            }
//        }
        return rootView;
    }

    public void onResume() {
        super.onResume();
       // getApp().getSoundService().requestSound(1, 2);
    }

    public void onPause() {
        super.onPause();
       // getApp().getSoundService().releaseSound(1, 2);
    }

    public void onDestroyView() {
        stopQuiz();
        super.onDestroyView();
    }

    public boolean inflateHeaderExtras(LayoutInflater inflater, ViewGroup viewGroup) {
        int i = 8;
        super.inflateHeaderExtras(inflater, viewGroup);
        viewGroup.setBackgroundColor(-7617718);
        View quizInfo = inflater.inflate(R.layout.quiz_details, viewGroup);
        TextView quizNumberText = (TextView) quizInfo.findViewById(R.id.quiz_info_number);
        this.unlockButton = quizInfo.findViewById(R.id.quiz_unlock_button);
        this.hintButton = quizInfo.findViewById(R.id.quiz_hint_button);
        View shareButton = quizInfo.findViewById(R.id.quiz_share_button);
        this.unlockButton.setOnClickListener(this);
        this.hintButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        quizNumberText.setText(getString(R.string.quiz_number_format, Integer.valueOf(getLessonManager().getQuizIndex() + 1), Integer.valueOf(getLessonManager().getQuizCount())));
        if (getLessonManager().isShortcut()) {
            this.hintButton.setVisibility(8);
            this.unlockButton.setVisibility(8);
        } else {
            View view = this.hintButton;
            if (this.quizSelector.getHintMode() != 10) {
                i = 0;
            }
            view.setVisibility(i);
        }
        return true;
    }

    public HeaderAdapter getHeaderAdapter() {
        return getLessonManager().getHeaderAdapter(getContext());
    }

    public String getEntryName() {
        return LessonManager.ENTRY_QUIZ;
    }

    public void onResult(int result) {
        boolean z;
        this.isChecked = true;
        if (result == 1) {
            z = true;
        } else {
            z = false;
        }
        this.isCorrect = z;
        stopQuiz();
      //  getApp().getSoundService().play(result == 1 ? 1 : 2);
        LessonManager lessonManager = getLessonManager();
        if (lessonManager.isShortcut()) {
//            ShortcutManager shortcutManager = lessonManager.getShortcut();
//            shortcutManager.setResult(lessonManager.getQuizIndex(), this.isCorrect);
//            if (shortcutManager.getAttempts() == 0) {
//                MessageDialog.build(getContext()).setTitle((int) R.string.quiz_shortcut_failed_title).setMessage((int) R.string.quiz_shortcut_failed_text).setPositiveButton((int) R.string.action_ok).setListener(new C11942()).show(getChildFragmentManager());
//            } else if (shortcutManager.isCompleted()) {
//                MessageDialog.build(getContext()).setTitle((int) R.string.quiz_shortcut_completed_title).setMessage((int) R.string.quiz_shortcut_completed_text).setPositiveButton((int) R.string.action_continue).setListener(new C11953()).show(getChildFragmentManager());
//                int completionPercent = (int) ((((float) shortcutManager.getCorrectCount()) * 100.0f) / ((float) shortcutManager.getCount()));
//                float score = 5.0f;
//                if (completionPercent <= 85) {
//                    score = 3.0f;
//                } else if (completionPercent <= 95) {
//                    score = 4.0f;
//                }
//                this.progressManager.addShortcut(shortcutManager.getModuleId(), score);

        } else {
            getApp().getProgressManager().addResult(getLessonManager().getLessonId(), getLessonManager().getQuizId(), result == 1, (int) ((new Date().getTime() - this.startTime) / 1000));
        }
        if (this.unlockButton != null) {
            this.unlockButton.setClickable(false);
        }
        if (this.hintButton != null) {
            this.hintButton.setClickable(false);
        }
        setResult(this.isCorrect, true);
    }

    private void startQuiz() {
        if (!this.isStarted) {
            this.startTime = System.currentTimeMillis();
            this.isStarted = true;
        }
    }

    private void stopQuiz() {
        if (this.isStarted) {
            this.endTime = System.currentTimeMillis();
            this.isStarted = false;
        }
    }

    private void primaryAction() {
        if (!this.isChecked) {
            this.quizSelector.check();
        } else if (this.isCorrect || getLessonManager().isShortcut()) {
            navigateNext();
        } else {
            navigate(LessonManager.getFragment(getLessonManager().getLesson(), getLessonManager().getQuizId(), true));
        }
    }

    private void setResult(boolean isCorrect, boolean animate) {
        Lesson lesson = getLessonManager().getLesson();
        Button button = this.resultButton;
        int i = (isCorrect || lesson.getType() == 1) ? 8 : 0;
        button.setVisibility(i);
        this.resultIcon.setImageResource(isCorrect ? R.drawable.quiz_correct_icon : R.drawable.quiz_wrong_icon);
        this.resultText.setText(isCorrect ? R.string.quiz_correct_text : R.string.quiz_wrong_text);
        this.resultText.setTextColor(ContextCompat.getColor(getContext(), isCorrect ? R.color.correct_text : R.color.wrong_text));
        if (!lesson.getIsShortcut() || isCorrect) {
            this.resultAttempts.setVisibility(8);
        } else {
            this.resultAttempts.setText(getResources().getQuantityString(R.plurals.quiz_shortcut_attempts_left, getLessonManager().getShortcut().getAttempts(), new Object[]{Integer.valueOf(getLessonManager().getShortcut().getAttempts())}));
            this.resultAttempts.setVisibility(0);
        }
        if (!lesson.getIsShortcut()) {
            this.commentsButton.setVisibility(0);
        }
        this.resultContainer.setVisibility(0);
        if (animate) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
            this.resultView.setAnimation(anim);
            this.commentsButton.setAnimation(anim);
        }
        Button button2 = this.checkButton;
        i = (isCorrect || lesson.getIsShortcut()) ? R.string.action_continue : R.string.action_retry;
        button2.setText(i);
    }

    public boolean onBackPressed() {
        if (!getLessonManager().isShortcut()) {
            return super.onBackPressed();
        }
        MessageDialog.create(getContext(), (int) R.string.quiz_shortcut_leave_title, (int) R.string.quiz_shortcut_leave_text, (int) R.string.challenge_leave_dialog_button_text, (int) R.string.action_cancel, new C11964()).show(getChildFragmentManager());
        return true;
    }

    private void shareQuiz() {
        //ShareDialog.share(this.quizSelector.renderToBitmap());
    }

    private void hint() {
      // getShowcase().dismiss("quiz_hint");
        promptExchange(1, new C05475());
    }

    private void unlock() {
        //getShowcase().dismiss("quiz_unlock");
        promptExchange(2, new C05486());
    }

    private void promptExchange(int type, Runnable onAccept) {
        if (!this.quizSelector.isInputDisabled()) {
            if (this.userManager.isAuthenticated()) {
                Module module = getLessonManager().getModule();
                int requiredXp = type == 1 ? module.getHintPrice() : module.getSkipPrice();
                if (canExchange(requiredXp)) {
                    MessageDialog.build(getContext()).setTitle(type == 1 ? R.string.quiz_hint_prompt_title : R.string.quiz_unlock_prompt_title).setMessage(getString(type == 1 ? R.string.quiz_hint_prompt_text : R.string.quiz_unlock_prompt_text, Integer.valueOf(requiredXp), Integer.valueOf(this.progressManager.getXp()))).setPositiveButton((int) R.string.action_ok).setNegativeButton((int) R.string.action_cancel).setListener(new C11988(requiredXp, type, onAccept)).show(getChildFragmentManager());
                    return;
                }
                return;
            }
            MessageDialog.create(getContext(), (int) R.string.quiz_login_hint_title, (int) R.string.quiz_login_hint_text, (int) R.string.action_login, (int) R.string.action_cancel, new C11977()).show(getChildFragmentManager());
        }
    }

    private boolean canExchange(int requiredXp) {
        if (this.progressManager.canExchange(requiredXp)) {
            return true;
        }
        MessageDialog.create(getContext(), (int) R.string.quiz_hint_no_xp_title, (int) R.string.quiz_hint_no_xp_text, (int) R.string.action_ok).show(getChildFragmentManager());
        return false;
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.quiz_check_button /*2131755476*/:
                break;
            case R.id.quiz_result_popup /*2131755478*/:
                if (!this.isCorrect) {
                    return;
                }
                break;
            case R.id.quiz_result_button /*2131755482*/:
                navigateBack();
                return;
            case R.id.quiz_hint_button /*2131755572*/:
                hint();
                return;
            case R.id.quiz_unlock_button /*2131755573*/:
                unlock();
                return;
            case R.id.quiz_share_button /*2131755574*/:
                shareQuiz();
                return;
            default:
                return;
        }
        primaryAction();
    }

    private void setupResultDrag() {
        this.resultView.setOnTouchListener(new C05499());
    }

    protected int getHeaderOffset() {
        return getApp().getActivity().getToolbarOffset(16) + getResources().getDimensionPixelSize(R.dimen.quiz_details_height);
    }

    private void navigateNext() {
        LessonManager lessonManager = getLessonManager();
        AppFragment nextFragment = LessonManager.getNextFragment(lessonManager);
        if (nextFragment != null) {
            navigate(nextFragment);
        } else if (lessonManager.isShortcut()) {
            navigateHome();
        } else {
            Lesson lesson = lessonManager.getLesson();
            Module module = lessonManager.getModule();
            if (module.getLesson(module.getLessonCount() - 1) == lesson) {
                Course course = getApp().getCourseManager().getCourse();
                if (course.getModule(course.getModuleCount() - 1) == module) {
                    //navigate(new CertificateFragment());
                    return;
                }
                navigateHome();
                if (getApp().isPlayEnabled()) {
                    this.handler.postDelayed(new Runnable() {
                        public void run() {
                          //  QuizFragment.this.getApp().getExperience().showPlayPopup();
                        }
                    }, 1500);
                    return;
                }
                return;
            }
            navigateBack(LessonManager.ENTRY_LESSONS);
        }
    }

    protected View getShiftView() {
        return this.quizSelector;
    }

    protected int getCommentType() {
        return 3;
    }
}
