package com.oncreate.ariadna;

import android.content.Context;
import android.os.Bundle;
import com.android.volley.Response.Listener;
import com.google.android.gms.common.ConnectionResult;
import com.oncreate.ariadna.Adapters.TimelineAdapter;
import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.Base.AppFragment;
import com.oncreate.ariadna.Base.AppFragment.Entry;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.LessonState;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.loginLearn.ParamMap;
import com.oncreate.ariadna.loginLearn.StringUtils;
import com.oncreate.ariadna.loginLearn.WebService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class LessonManager {
    public static final String ARG_LESSON_ID = "lessonId";
    public static final String ARG_QUIZ_ID = "quizId";
    public static final String ARG_QUIZ_INDEX = "quizIndex";
    public static final String ARG_SHORTCUT = "isShortcut";
    public static final String ARG_SHORTCUT_MODULE_ID = "shortcutModuleId";
    public static final String ARG_SHORTCUT_QUIZ_IDS = "shortcutQuizIds";
    public static final String ARG_SHORTCUT_QUIZ_RESULTS = "shortcutResults";
    public static final String ARG_SHOW_COMMENTS = "showComments";
    public static final String ENTRY_LESSON = "Lesson";
    public static final String ENTRY_LESSONS = "Lessons";
    public static final String ENTRY_QUIZ = "Quiz";
    private static Dictionary<String, Integer> commentCounts;
    private static final List<String> lessonBackEntries;
    private static Dictionary<String, Integer> questionCounts;
    private boolean isQuiz;
    private boolean isShortcut;
    private Lesson lesson;
    private Quiz quiz;
    private int quizIndex;
    private ShortcutManager shortcut;

    public interface GetQuestionCountListener {
        void onResponse(int i);
    }

//    /* renamed from: com.sololearn.app.LessonManager.1 */
//    class C11281 implements Listener<DiscussionPostResult> {
//        final /* synthetic */ GetQuestionCountListener val$listener;
//
//        C11281(GetQuestionCountListener getQuestionCountListener) {
//            this.val$listener = getQuestionCountListener;
//        }
//
//        public void onResponse(DiscussionPostResult response) {
//            if (response.isSuccessful()) {
//                int count = response.getCount();
//                LessonManager.questionCounts.put(LessonManager.this.lesson.getTags(), Integer.valueOf(count));
//                this.val$listener.onResponse(count);
//            }
//        }
//    }
//
//    /* renamed from: com.sololearn.app.LessonManager.2 */
//    class C11292 implements Listener<DiscussionPostResult> {
//        final /* synthetic */ GetQuestionCountListener val$listener;
//        final /* synthetic */ int val$quizId;
//        final /* synthetic */ int val$type;
//
//        C11292(int i, int i2, GetQuestionCountListener getQuestionCountListener) {
//            this.val$quizId = i;
//            this.val$type = i2;
//            this.val$listener = getQuestionCountListener;
//        }
//
//        public void onResponse(DiscussionPostResult response) {
//            if (response.isSuccessful()) {
//                int count = response.getCount();
//                LessonManager.commentCounts.put(this.val$quizId + "-" + this.val$type, Integer.valueOf(count));
//                this.val$listener.onResponse(count);
//            }
//        }
//    }

    static {
        lessonBackEntries = Collections.singletonList(ENTRY_LESSONS);
    }

    public static AppFragment getFragment(int lessonId) {
        return getFragment(lessonId, -1, false);
    }

    public static AppFragment getFragment(int lessonId, int quizId) {
        return getFragment(lessonId, quizId, false);
    }

    public static AppFragment getFragment(int lessonId, int quizId, boolean forceQuiz) {
        return getFragment(AriadnaApplication.getInstance().getCourseManager().getLessonById(lessonId), quizId, forceQuiz);
    }

    public static AppFragment getFragment(Lesson lesson) {
        return getFragment(lesson, -1, false);
    }

    public static AppFragment getFragment(Lesson lesson, int quizId) {
        return getFragment(lesson, quizId, false);
    }

    public static AppFragment getFragment(Lesson lesson, int quizId, boolean forceQuiz) {
        Bundle args = new Bundle();
        if (quizId <= 0) {
            LessonState state = AriadnaApplication.getInstance().getProgressManager().getLessonState(lesson.getId());
            if (state != null && state.isStarted()) {
                quizId = state.getActiveQuizId();
            }
            if (quizId <= 0) {
                quizId = lesson.getQuiz(0).getId();
            }
        }
        args.putInt(ARG_LESSON_ID, lesson.getId());
        args.putInt(ARG_QUIZ_ID, quizId);
        AppFragment fragment = null;
        if (lesson.getType() != 1 && !forceQuiz && lesson.getMode() != 0) {
            switch (lesson.getMode()) {
                case AppActivity.OFFSET_TOOLBAR /*1*/:
                    fragment = new TextFragment();
                    break;
                case AppActivity.OFFSET_TABS /*2*/:
                case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                    fragment = new LessonFragment();
                    break;
                case LoadingDialog.HORIZONTAL_INDETERMINATE /*3*/:
                    fragment = new VideoFragment();
                    break;
                default:
                    break;
            }
        }
        fragment = new QuizFragment();
        if (fragment != null) {
            fragment.setName(lesson.getName());
            fragment.setArguments(args);
        }
        return fragment;
    }

    public static AppFragment getNextFragment(LessonManager manager) {
        if (manager.lesson.getIsShortcut()) {
            QuizFragment nextFragment = new QuizFragment();
            LessonManager newManager = new LessonManager(manager);
            List<Quiz> quizzes = manager.lesson.getQuizzes();
            int quizIndex = quizzes.indexOf(manager.quiz) + 1;
            if (quizIndex >= quizzes.size()) {
                return null;
            }
            newManager.quiz = (Quiz) quizzes.get(quizIndex);
            newManager.quizIndex = newManager.lesson.getQuizzes().indexOf(newManager.quiz);
            nextFragment.setLessonManager(newManager);
            nextFragment.setArguments(createShortcutBundle(newManager));
            return nextFragment;
        }
        int index = manager.quizIndex + 1;
        return index < manager.lesson.getQuizzes().size() ? getFragment(manager.lesson, manager.lesson.getQuiz(index).getId()) : null;
    }

    private static Bundle createShortcutBundle(LessonManager manager) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_SHORTCUT, true);
        bundle.putInt(ARG_QUIZ_INDEX, manager.quizIndex);
        bundle.putInt(ARG_SHORTCUT_MODULE_ID, manager.shortcut.getModuleId());
        List<Quiz> quizzes = manager.lesson.getQuizzes();
        int[] quizIds = new int[quizzes.size()];
        for (int i = 0; i < quizzes.size(); i++) {
            quizIds[i] = ((Quiz) quizzes.get(i)).getId();
        }
        bundle.putIntArray(ARG_SHORTCUT_QUIZ_IDS, quizIds);
        bundle.putBooleanArray(ARG_SHORTCUT_QUIZ_RESULTS, manager.shortcut.getResults());
        return bundle;
    }

    public static AppFragment getShortcutFragment(int moduleId) {
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(new BundleBuilder().putBoolean(ARG_SHORTCUT, true).putInt(ARG_SHORTCUT_MODULE_ID, moduleId).toBundle());
        return fragment;
    }

    private LessonManager(LessonManager manager) {
        this.lesson = manager.lesson;
        this.quiz = manager.quiz;
        this.isQuiz = manager.isQuiz;
        this.isShortcut = manager.isShortcut;
        this.shortcut = manager.shortcut;
    }

    private LessonManager(Bundle args, boolean isQuiz) {
        this.isQuiz = isQuiz;
        this.isShortcut = args.getBoolean(ARG_SHORTCUT, false);
        if (this.isShortcut) {
            int shortcutModuleId = args.getInt(ARG_SHORTCUT_MODULE_ID);
            this.quizIndex = args.getInt(ARG_QUIZ_INDEX);
            this.lesson = generateShortcut(shortcutModuleId, args.getIntArray(ARG_SHORTCUT_QUIZ_IDS));
            this.quiz = this.lesson.getQuiz(this.quizIndex);
            this.shortcut = new ShortcutManager(shortcutModuleId, this.lesson.getQuizzes().size());
            boolean[] quizResults = args.getBooleanArray(ARG_SHORTCUT_QUIZ_RESULTS);
            if (quizResults != null) {
                for (int i = 0; i < quizResults.length; i++) {
                    this.shortcut.setResult(i, quizResults[i]);
                }
                return;
            }
            return;
        }
        int lessonId = args.getInt(ARG_LESSON_ID);
        int quizId = args.getInt(ARG_QUIZ_ID);
        this.lesson = AriadnaApplication.getInstance().getCourseManager().getLessonById(lessonId);
        this.quiz = this.lesson.getQuizById(quizId);
        if (this.quiz == null) {
            this.quiz = this.lesson.getQuiz(0);
        }
        this.quizIndex = this.lesson.getQuizzes().indexOf(this.quiz);
    }

    public static LessonManager forLesson(Bundle args) {
        return new LessonManager(args, false);
    }

    public static LessonManager forQuiz(Bundle args) {
        return new LessonManager(args, true);
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public int getLessonId() {
        return this.lesson.getId();
    }

    public int getQuizId() {
        return this.quiz.getId();
    }

    public int getQuizIndex() {
        return this.quizIndex;
    }

    public int getQuizCount() {
        return this.lesson.getQuizzes().size();
    }

    public boolean isShortcut() {
        return this.isShortcut;
    }

    public ShortcutManager getShortcut() {
        return this.shortcut;
    }

    public TimelineAdapter getHeaderAdapter(Context context) {
        return new TimelineAdapter(context, this.lesson, AriadnaApplication.getInstance().getProgressManager().getLessonProgress(this.lesson.getId()), this.quiz != null ? this.quiz.getId() : -1, this.isQuiz ? 2 : 1);
    }

    public Entry getEntry(AppFragment oldFragment, AppFragment newFragment) {
        Entry entry = null;
        String oldEntry = oldFragment.getEntryName();
        String newEntry = newFragment.getEntryName();
        if (!(oldEntry == null || newEntry == null || ((!oldEntry.equals(ENTRY_LESSON) && !oldEntry.equals(ENTRY_QUIZ)) || (!newEntry.equals(ENTRY_LESSON) && !newEntry.equals(ENTRY_QUIZ))))) {
            Bundle aArgs = oldFragment.getArguments();
            Bundle bArgs = newFragment.getArguments();
            if (!(oldEntry.equals(ENTRY_LESSON) && newEntry.equals(ENTRY_QUIZ) && aArgs.getInt(ARG_QUIZ_ID) == bArgs.getInt(ARG_QUIZ_ID))) {
                if (aArgs == null || bArgs == null || aArgs.getInt(ARG_QUIZ_ID, 0) <= bArgs.getInt(ARG_QUIZ_ID, 0)) {
                    entry = new Entry(lessonBackEntries, R.anim.enter_from_right, R.anim.exit_to_left);
                } else {
                    entry = new Entry(lessonBackEntries, R.anim.enter_from_left, R.anim.exit_to_right);
                }
                if (newEntry.equals(ENTRY_QUIZ) && this.lesson.getType() == 0) {
                    entry.injectFragment(getFragment(this.lesson, this.quiz.getId()));
                }
            }
        }
        return entry;
    }

    public Module getModule() {
        return AriadnaApplication.getInstance().getCourseManager().getModuleByLesson(this.lesson.getId());
    }

    private Lesson generateShortcut(int moduleId) {
        return generateShortcut(moduleId, null);
    }

    private Lesson generateShortcut(int moduleId, int[] quizIds) {
        int i = 0;
        Lesson shortcut = new Lesson();
        shortcut.setType(1);
        shortcut.setName(AriadnaApplication.getInstance().getString(R.string.quiz_shortcut_title));
        shortcut.setIsShortcut(true);
        ArrayList<Quiz> quizzes = new ArrayList();
        ProgressManager progressManager = AriadnaApplication.getInstance().getProgressManager();
        Iterator it = AriadnaApplication.getInstance().getCourseManager().getCourse().getModules().iterator();
        while (it.hasNext()) {
            Iterator it2;
            Module module = (Module) it.next();
            if (module.getId() == moduleId) {
                break;
            } else if (progressManager.getModuleState(module.getId()).getState() != 1) {
                it2 = module.getLessons().iterator();
                while (it2.hasNext()) {
                    Lesson lesson = (Lesson) it2.next();
                    if (lesson.getType() == 1 && progressManager.getLessonState(lesson.getId()).getState() != 1) {
                        quizzes.addAll(lesson.getQuizzes());
                    }
                }
            }
        }
        if (quizIds == null) {
            Collections.shuffle(quizzes);
            shortcut.setQuizzes(quizzes.subList(0, Math.min(quizzes.size(), 10)));
        } else {
            List<Quiz> restoredQuizzes = new ArrayList();
            int length = quizIds.length;
            while (i < length) {
                int id = quizIds[i];
                Iterator<Quiz> it2 = quizzes.iterator();
                while (it2.hasNext()) {
                    Quiz quiz = (Quiz) it2.next();
                    if (quiz.getId() == id) {
                        restoredQuizzes.add(quiz);
                        break;
                    }
                }
                i++;
            }
            shortcut.setQuizzes(restoredQuizzes);
        }
        return shortcut;
    }

    public void getQuestionCount(GetQuestionCountListener listener) {
        int count = 0;
        if (questionCounts == null) {
            questionCounts = new Hashtable();
        }
        if (!StringUtils.isNullOrWhitespace(this.lesson.getTags())) {
            Integer storedCount = (Integer) questionCounts.get(this.lesson.getTags());
            if (storedCount == null) {
             //   AriadnaApplication.getInstance().getWebService().request(DiscussionPostResult.class, WebService.DISCUSSION_GET_TAGGED_COUNT, ParamMap.create().add("tags", this.lesson.getTags()), new C11281(listener));
                return;
            }
            count = storedCount.intValue();
        }
        listener.onResponse(count);
    }

    public void getCommentCount(GetQuestionCountListener listener) {
        if (commentCounts == null) {
            commentCounts = new Hashtable();
        }
        int type = this.isQuiz ? 3 : 0;
        int quizId = this.quiz.getId();
        Integer storedCount = (Integer) commentCounts.get(quizId + "-" + type);
        if (storedCount == null) {
          //  AriadnaApplication.getInstance().getWebService().request(DiscussionPostResult.class, WebService.DISCUSSION_GET_LESSON_COMMENT_COUNT, ParamMap.create().add(ARG_QUIZ_ID, Integer.valueOf(quizId)).add("type", Integer.valueOf(type)), new C11292(quizId, type, listener));
        } else {
            listener.onResponse(storedCount.intValue());
        }
    }

    public static void invalidateCommentCount(int quizId, int type) {
        if (commentCounts != null) {
            commentCounts.remove(quizId + "-" + type);
        }
    }
}
