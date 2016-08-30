package com.oncreate.ariadna;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oncreate.ariadna.Base.ServicesPrincipal;
import com.oncreate.ariadna.ModelsVO.Achievement;
import com.oncreate.ariadna.ModelsVO.Course;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.LessonProgress;
import com.oncreate.ariadna.ModelsVO.LessonState;
import com.oncreate.ariadna.ModelsVO.Level;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ModelsVO.ModuleState;
import com.oncreate.ariadna.ModelsVO.Progress;
import com.oncreate.ariadna.ModelsVO.ProgressChangeset;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.ModelsVO.QuizProgress;
import com.oncreate.ariadna.Request.Services;
import com.oncreate.ariadna.Util.StorageService;
import com.oncreate.ariadna.loginLearn.AppFieldNamingPolicy;
import com.oncreate.ariadna.loginLearn.UtcDateTypeAdapter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProgressManager {
    public static final int EXCHANGE_HINT = 1;
    public static final int EXCHANGE_UNLOCK = 2;
    private static final String FILE_NAME_CHANGESET = "changeset.json";
    private static final String FILE_NAME_CONTEST_UPDATES = "contest_updates.json";
    private static final String FILE_NAME_PROGRESS = "progress.json";
    private static final String STORAGE_KEY_COMPLETED_LESSONS = "totalCompletedLessons";
    private AchievementManager achievementManager;
    private ProgressChangeset changeset;
    private SparseArray<Long> contestUpdates;
    private CourseManager courseManager;
    private Gson gson;
    private boolean isPushing;
    private boolean isSyncQueued;
    private SparseArray<LessonState> lessonStates;
    private ArrayList<Listener> listeners;
    private SparseArray<ModuleState> moduleStates;
    private int percent;
    private Progress progress;
    private ProgressListener progressListener;
    private StorageService storage;
    private int totalCompletedItems;
    private int totalCompletedLessons;
    private int totalItems;
    private UserManager userManager;


    class C06835 extends AsyncTask<Void, Void, Void> {
        private int currentLevel;
        private int currentXp;
        final /* synthetic */ int val$moduleId;
        final /* synthetic */ float val$score;

        C06835(int i, float f) {
            this.val$moduleId = i;
            this.val$score = f;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.currentLevel = ProgressManager.this.progress.getLevel();
            this.currentXp = ProgressManager.this.progress.getXp();
        }

        protected Void doInBackground(Void... voids) {
            ProgressManager.this.addShortcutBlocking(this.val$moduleId, this.val$score);
            ProgressManager.this.rebuildStates();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            ProgressManager.this.notifyGlobalChange();
            if (this.currentLevel != ProgressManager.this.progress.getLevel()) {
                ProgressManager.this.notifyLevelUp(this.currentLevel, ProgressManager.this.progress.getLevel(), this.currentXp, ProgressManager.this.progress.getXp());
            }
        }
    }

    /* renamed from: com.sololearn.core.ProgressManager.7 */
    class C06847 extends AsyncTask<Void, Void, Void> {
        C06847() {
        }

        protected Void doInBackground(Void... params) {
            ProgressManager.this.rebuildStates();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressManager.this.notifyGlobalChange();
        }
    }

    class C06859 extends AsyncTask<Boolean, Void, Void> {
        C06859() {
        }

        protected Void doInBackground(Boolean... params) {
            if (params[0].booleanValue() && ProgressManager.this.progress != null) {
                ProgressManager.this.storage.writeText(ProgressManager.FILE_NAME_PROGRESS, ProgressManager.this.gson.toJson(ProgressManager.this.progress));
            }
            if (params[ProgressManager.EXCHANGE_HINT].booleanValue() && ProgressManager.this.changeset != null) {
                ProgressManager.this.storage.writeText(ProgressManager.FILE_NAME_CHANGESET, ProgressManager.this.gson.toJson(ProgressManager.this.changeset));
            }
            if (params[ProgressManager.EXCHANGE_UNLOCK].booleanValue() && ProgressManager.this.contestUpdates != null) {
                ProgressManager.this.storage.writeText(ProgressManager.FILE_NAME_CONTEST_UPDATES, ProgressManager.this.gson.toJson(ProgressManager.this.contestUpdates));
            }
            return null;
        }
    }

    public interface Listener {
        void onGlobalChange();

        void onLessonChange(int i);

        void onModuleChange(int i);
    }

    public interface ProgressListener {
        void onLevelUp(int i, int i2, int i3, int i4);

        void onModuleCompleted(Module module);

        void onPushCompleted(PushResult pushResult);

        void onSignificantProgress();
    }

    /* renamed from: com.sololearn.core.ProgressManager.1 */
    class C12971 extends TypeToken<SparseArray<Long>> {
        C12971() {
        }
    }

    class C12982 extends TypeToken<SparseArray<Level>> {
        C12982() {
        }
    }


    class C12993 extends TypeToken<SparseArray<LessonProgress>> {
        C12993() {
        }
    }


    class C13004 implements CourseManager.Listener {
        C13004() {
        }

        public void onResult(Course course) {
            ProgressManager.this.rebuildStatesAsync();
        }
    }

    /* renamed from: com.sololearn.core.ProgressManager.6 */
    class C13016 implements com.android.volley.Response.Listener<ProgressResult> {
        C13016() {
        }

        public void onResponse(ProgressResult response) {
            if (response.isSuccessful()) {
                SparseArray<Level> levels = new SparseArray(response.getLevels().size() + ProgressManager.EXCHANGE_HINT);
                levels.put(0, new Level(response.getLevel(),response.getXp()));
                Iterator it = response.getLevels().iterator();
                while (it.hasNext()) {
                    Level level = (Level) it.next();
                    levels.put(level.getNumber(), level);
                }
                ProgressManager.this.progress.setLevels(levels);
                SparseArray<LessonProgress> localProgress = new SparseArray(response.getProgress().size());
                it = response.getProgress().iterator();
                while (it.hasNext()) {
                    LessonProgress lp = (LessonProgress) it.next();
                    localProgress.put(lp.getLessonId(), lp);
                }
                ProgressManager.this.progress.setLocalProgress(localProgress);
                ProgressManager.this.progress.setLevel(response.getLevel());
                ProgressManager.this.progress.setXp(response.getXp());
                ProgressManager.this.progress.setPoints(response.getPoints());
                ProgressManager.this.rebuildStatesAsync();
                ProgressManager.this.cache(true, false, false);
            }
        }
    }


    class C13028 extends TypeToken<SparseArray<Long>> {
        C13028() {
        }
    }

    public ProgressManager(StorageService storage, CourseManager courseManager, UserManager userManager, AchievementManager achievementManager) {
        this.moduleStates = new SparseArray();
        this.lessonStates = new SparseArray();
        this.listeners = new ArrayList();

        this.storage = storage;
        this.courseManager = courseManager;
        this.userManager = userManager;
        this.achievementManager = achievementManager;
      //  this.gson = new GsonBuilder().setFieldNamingStrategy(new AppFieldNamingPolicy()).registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).registerTypeAdapter(new C12993().getType(), new SparseArrayTypeAdapter(LessonProgress.class, webService.getGson())).registerTypeAdapter(new C12982().getType(), new SparseArrayTypeAdapter(Level.class, Services.getGson())).registerTypeAdapter(new C12971().getType(), new SparseArrayTypeAdapter(Long.class, webService.getGson())).create();
        this.totalCompletedLessons = storage.getInt(STORAGE_KEY_COMPLETED_LESSONS, 0);
    }

    public void initializeFromCache() {
        restoreProgress();
        restoreChangeset();
        restoreContestUpdates();
        this.courseManager.addOnUpdateListener(new C13004());
    }

    public int getPercent() {
        if (this.lessonStates.size() == 0) {
            rebuildStates();
        }
        return this.percent;
    }

    public int getXp() {
        return this.progress.getXp();
    }

    public int getLevel() {
        return this.progress.getLevel();
    }

    public int getPoints() {
        return this.progress.getPoints();
    }

    public ModuleState getModuleState(int moduleId) {
        if (this.moduleStates.get(moduleId) == null) {
            rebuildStates();
        }
        return (ModuleState) this.moduleStates.get(moduleId);
    }

    public LessonState getLessonState(int lessonId) {
        if (this.lessonStates.get(lessonId) == null) {
            rebuildStates();
        }
        return (LessonState) this.lessonStates.get(lessonId);
    }

    public void addResult(int lessonId, int quizId, boolean correct, int time) {
        LessonProgress lp = (LessonProgress) this.progress.getLocalProgress().get(lessonId);
        QuizProgress qp = null;
        int pointChange = 0;
        if (lp == null) {
            lp = new LessonProgress();
            lp.setLessonId(lessonId);
            lp.setBestScore(-1.0f);
            this.progress.getLocalProgress().put(lessonId, lp);
        }
        if (!lp.getIsStarted().booleanValue()) {
            lp.setIsStarted(Boolean.valueOf(true));
            lp.setAttempt(lp.getAttempt() + EXCHANGE_HINT);
            lp.setScore(0.0f);
            lp.getQuizzes().clear();
        }
        for (QuizProgress quiz : lp.getQuizzes()) {
            if (quiz.getQuizId() == quizId) {
                qp = quiz;
                break;
            }
        }
        if (qp == null) {
            qp = new QuizProgress();
            qp.setQuizId(quizId);
            lp.getQuizzes().add(qp);
        }
        boolean lessonCompleted = false;
        qp.setTime(time);
        qp.setAttempt(qp.getAttempt() + EXCHANGE_HINT);
        if (correct) {
            qp.setCompleted(true);
            int quizIndex = 0;
            List<Quiz> quizzes = this.courseManager.getLessonById(lessonId).getQuizzes();
            for (int i = 0; i < quizzes.size(); i += EXCHANGE_HINT) {
                if (((Quiz) quizzes.get(i)).getId() == quizId) {
                    quizIndex = i + EXCHANGE_HINT;
                    break;
                }
            }
            qp.setScore((5.0f / ((float) quizzes.size())) / ((float) ((qp.getAttempt() * EXCHANGE_UNLOCK) - 1)));
            lessonCompleted = quizIndex == quizzes.size();
            if (quizIndex >= quizzes.size()) {
                quizIndex = 0;
            }
            lp.setActiveQuizId(((Quiz) quizzes.get(quizIndex)).getId());
        } else {
            lp.setActiveQuizId(quizId);
        }
        if (lessonCompleted) {
            float lpScore = 0.0f;
            for (QuizProgress quiz2 : lp.getQuizzes()) {
                lpScore += quiz2.getScore();
            }
            float bestScore = Math.max(lp.getBestScore(), 0.0f);
            if (lpScore > bestScore) {
                lp.setBestScore(lpScore);
                pointChange = (int) (lpScore - bestScore);
            }
            lp.setScore((float) Math.max(Math.min(Math.round(lpScore), 6 - lp.getAttempt()), EXCHANGE_HINT));
            lp.setIsStarted(Boolean.valueOf(false));
            lp.setIsCompleted(Boolean.valueOf(true));
            syncLessonState(lp);
        } else {
            syncQuizState(lp);
        }
        this.changeset.addLesson(lp);
        this.changeset.addQuiz(qp);
        this.progress.addPoints(pointChange);
        int currentLevel = this.progress.getLevel();
        int currentXp = this.progress.getXp();
        boolean levelUp = this.progress.addXp(pointChange);
        cache(true, true, false);
        pushChanges();
        if (levelUp) {
            notifyLevelUp(currentLevel, this.progress.getLevel(), currentXp, this.progress.getXp());
        }
        if (lessonCompleted && !levelUp) {
            incrementCompletedLessons();
        }
    }

    private void addShortcutBlocking(int moduleId, float score) {
        float addScore = 0.0f;
        SparseArray<LessonProgress> localProgress = this.progress.getLocalProgress();
        Iterator it = this.courseManager.getCourse().getModules().iterator();
        while (it.hasNext()) {
            Module module = (Module) it.next();
            if (module.getId() == moduleId) {
                break;
            } else if (getModuleState(module.getId()).getState() != EXCHANGE_HINT) {
                Iterator it2 = module.getLessons().iterator();
                while (it2.hasNext()) {
                    Lesson lesson = (Lesson) it2.next();
                    LessonProgress lessonProgress = (LessonProgress) localProgress.get(lesson.getId());
                    if (lessonProgress == null) {
                        lessonProgress = new LessonProgress();
                        lessonProgress.setAttempt(EXCHANGE_HINT);
                        lessonProgress.setLessonId(lesson.getId());
                        lessonProgress.setScore(score);
                        localProgress.put(lesson.getId(), lessonProgress);
                        addScore += score;
                    } else if (lessonProgress.getScore() < score) {
                        addScore += score - Math.max(0.0f, lessonProgress.getScore());
                        lessonProgress.setScore(score);
                    }
                    lessonProgress.setIsCompleted(Boolean.valueOf(true));
                    lessonProgress.setIsStarted(Boolean.valueOf(false));
                    lessonProgress.setActiveQuizId(lesson.getQuiz(0).getId());
                    this.changeset.addLesson(lessonProgress);
                    List<QuizProgress> quizProgresses = new ArrayList();
                    float quizScore = score / ((float) lesson.getQuizzes().size());
                    for (Quiz quiz : lesson.getQuizzes()) {
                        QuizProgress quizProgress = new QuizProgress();
                        quizProgress.setQuizId(quiz.getId());
                        quizProgress.setAttempt(EXCHANGE_HINT);
                        quizProgress.setTime(311);
                        quizProgress.setScore(quizScore);
                        quizProgress.setCompleted(true);
                        quizProgresses.add(quizProgress);
                        this.changeset.addQuiz(quizProgress);
                    }
                    lessonProgress.setQuizzes(quizProgresses);
                }
            }
        }
        this.progress.addXp((int) addScore);
        this.progress.addPoints((int) addScore);
        cache(true, true, false);
        pushChanges();
    }

    public void addShortcut(int moduleId, float score) {
        new C06835(moduleId, score).execute(new Void[0]);
    }

    private void syncQuizState(LessonProgress lp) {
        LessonState state = (LessonState) this.lessonStates.get(lp.getLessonId());
        if (state != null) {
            state.setIsStarted(lp.getIsStarted().booleanValue());
            state.setActiveQuizId(lp.getActiveQuizId());
        }
    }

    private void syncLessonState(LessonProgress lp) {
        LessonState state = (LessonState) this.lessonStates.get(lp.getLessonId());
        if (state != null) {
            if (lp.getIsCompleted().booleanValue()) {
                state.setState(EXCHANGE_HINT);
                Module module = this.courseManager.getModuleByLesson(lp.getLessonId());
                Lesson lesson = this.courseManager.getLessonById(lp.getLessonId());
                ModuleState moduleState = getModuleState(module.getId());
                List<Lesson> lessons = module.getLessons();
                int nextLessonIndex = lessons.indexOf(lesson) + EXCHANGE_HINT;
                Lesson nextLesson = null;
                if (nextLessonIndex < lessons.size()) {
                    nextLesson = (Lesson) lessons.get(nextLessonIndex);
                } else {
                    moduleState.setState(EXCHANGE_HINT);
                    List<Module> modules = this.courseManager.getCourse().getModules();
                    int nextModuleIndex = this.courseManager.getModuleIndex(module.getId()) + EXCHANGE_HINT;
                    if (nextModuleIndex < modules.size()) {
                        Module nextModule = (Module) modules.get(nextModuleIndex);
                        nextLesson = nextModule.getLesson(0);
                        ModuleState nextModuleState = (ModuleState) this.moduleStates.get(nextModule.getId());
                        if (nextModuleState.getState() == 0) {
                            nextModuleState.setState(EXCHANGE_UNLOCK);
                            if (this.progressListener != null) {
                                this.progressListener.onModuleCompleted(module);
                            }
                        }
                    }
                }
                if (nextLesson != null) {
                    LessonState nextLessonState = getLessonState(nextLesson.getId());
                    if (nextLessonState.getState() == 0) {
                        nextLessonState.setState(EXCHANGE_UNLOCK);
                    }
                }
                int prevCompletedItems = moduleState.getCompletedItems();
                int completedLessons = 0;
                int completedItems = 0;
                for (Lesson l : lessons) {
                    LessonState ls = getLessonState(l.getId());
                    if (ls != null && ls.getState() == EXCHANGE_HINT) {
                        completedItems += EXCHANGE_HINT;
                        if (l.getType() == 0) {
                            completedLessons += EXCHANGE_HINT;
                        }
                    }
                }
                moduleState.setCompletedItems(completedItems);
                moduleState.setCompletedLessons(completedLessons);
                this.totalCompletedItems += completedItems - prevCompletedItems;
                calculatePercent();
            } else {
                state.setState(EXCHANGE_UNLOCK);
            }
            state.setIsStarted(lp.getIsStarted().booleanValue());
            state.setActiveQuizId(lp.getActiveQuizId());
        }
    }

    public boolean canExchange(int xp) {
        return this.progress.getXp() >= xp;
    }

    public void addExchange(int quizId, int xp, int type) {
        this.changeset.addExchange(quizId, xp, type);
        this.progress.addPoints(-xp);
        this.progress.addXp(-xp);
        cache(true, false, false);
        pushChanges();
    }

    public void sync() {
        pushChanges();
        if (this.isPushing) {
            this.isSyncQueued = true;
        } else {

            //servicios
          //  this.webService.request(ProgressResult.class, WebService.GET_PROGRESS, null, new C13016());
        }
    }

    public void checkAchievements() {
        if (!this.isPushing && this.userManager.isAuthenticated()) {
            pushChanges(true);
        }
    }

    private void rebuildStates() {
        this.moduleStates.clear();
        this.lessonStates.clear();
        List<Module> modules = this.courseManager.getCourse().getModules();
        boolean prevModuleCompleted = true;
        this.totalItems = 0;
        this.totalCompletedItems = 0;
        for (Module module : modules) {
            List<Lesson> lessons = module.getLessons();
            ModuleState state = new ModuleState();
            int lessonCount = 0;
            int lessonPassed = 0;
            int totalPassed = 0;
            boolean prevLessonCompleted = prevModuleCompleted;
            for (Lesson lesson : lessons) {
                boolean isLesson = lesson.getType() == 0;
                LessonState lessonState = new LessonState();
                LessonProgress lp = (LessonProgress) this.progress.getLocalProgress().get(lesson.getId());
                if (isLesson) {
                    lessonCount += EXCHANGE_HINT;
                }
                if (lp != null) {
                    if (lp.getIsCompleted().booleanValue()) {
                        lessonState.setState(EXCHANGE_HINT);
                        totalPassed += EXCHANGE_HINT;
                        if (isLesson) {
                            lessonPassed += EXCHANGE_HINT;
                        }
                    } else {
                        lessonState.setState(EXCHANGE_UNLOCK);
                    }
                    lessonState.setIsStarted(lp.getIsStarted().booleanValue());
                    lessonState.setActiveQuizId(lp.getActiveQuizId());
                } else if (prevLessonCompleted) {
                    lessonState.setState(EXCHANGE_UNLOCK);
                }
                this.lessonStates.put(lesson.getId(), lessonState);
                prevLessonCompleted = lessonState.getState() == EXCHANGE_HINT;
            }
            state.setCompletedLessons(lessonPassed);
            state.setTotalLessons(lessonCount);
            state.setCompletedItems(totalPassed);
            state.setTotalItems(lessons.size());
            if (state.getCompletedItems() == state.getTotalItems()) {
                state.setState(EXCHANGE_HINT);
            } else if (prevModuleCompleted || state.getCompletedItems() > 0) {
                state.setState(EXCHANGE_UNLOCK);
            }
            this.moduleStates.put(module.getId(), state);
            prevModuleCompleted = state.getState() == EXCHANGE_HINT;
            this.totalItems += state.getTotalItems();
            this.totalCompletedItems += state.getCompletedItems();
        }
        calculatePercent();
    }

    private void calculatePercent() {
        this.percent = (int) ((100.0d * ((double) this.totalCompletedItems)) / ((double) this.totalItems));
    }

    private void rebuildStatesAsync() {
        new C06847().execute(new Void[0]);
    }

    public SparseArray<Level> getLevels() {
        return this.progress.getLevels();
    }

    @Deprecated
    public Level getLevel(float xp) {
        return this.progress.getLevelForXp((int) xp);
    }

    public Level getLevelForXp(int xp) {
        return this.progress.getLevelForXp(xp);
    }

    public Level getLevel(int level) {
        return (Level) getLevels().get(level);
    }

    public int[] getLevelArray() {
        SparseArray<Level> levelSparseArray = this.progress.getLevels();
        int[] levels = new int[levelSparseArray.size()];
        for (int i = 0; i < levelSparseArray.size(); i += EXCHANGE_HINT) {
            levels[i] = ((Level) levelSparseArray.valueAt(i)).getMaxXp();
        }
        return levels;
    }

    public void reset() {
        if (this.progress != null) {
            this.progress.reset();
        }
        this.changeset = new ProgressChangeset();
        this.moduleStates = new SparseArray();
        this.lessonStates = new SparseArray();
        this.contestUpdates = new SparseArray();
        this.storage.deleteFile(FILE_NAME_PROGRESS);
        this.storage.deleteFile(FILE_NAME_CHANGESET);
        this.storage.deleteFile(FILE_NAME_CONTEST_UPDATES);
        cache(true, false, false);
        notifyGlobalChange();
    }

    private void notifyGlobalChange() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onGlobalChange();
        }
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    private void incrementCompletedLessons() {
        this.totalCompletedLessons += EXCHANGE_HINT;
        this.storage.setInt(STORAGE_KEY_COMPLETED_LESSONS, this.totalCompletedLessons);
        if (this.totalCompletedLessons % 6 == 0 && this.progressListener != null) {
            this.progressListener.onSignificantProgress();
        }
    }

    private boolean restoreProgress() {
        try {
            String cachedProgress = this.storage.readText(FILE_NAME_PROGRESS);
            if (cachedProgress != null) {
                this.progress = (Progress) this.gson.fromJson(cachedProgress, Progress.class);
                return true;
            }
        } catch (Exception e) {
        }
        this.progress = new Progress();
        return false;
    }

    private boolean restoreChangeset() {
        try {
            String cachedChangeset = this.storage.readText(FILE_NAME_CHANGESET);
            if (cachedChangeset != null) {
                this.changeset = (ProgressChangeset) this.gson.fromJson(cachedChangeset, ProgressChangeset.class);
                return true;
            }
        } catch (Exception e) {
        }
        this.changeset = new ProgressChangeset();
        return false;
    }

    private boolean restoreContestUpdates() {

        try {
            String cachedContestUpdates = this.storage.readText(FILE_NAME_CONTEST_UPDATES);
            if (cachedContestUpdates != null) {
                this.contestUpdates = (SparseArray) this.gson.fromJson(cachedContestUpdates, new C13028().getType());
                return true;
            }
            this.contestUpdates = new SparseArray();
            return false;
        } catch (Exception e) {
        }

        return false;
    }

    private void cache(boolean cacheProgress, boolean cacheChangeset, boolean cacheContestUpdates) {
        new C06859().execute(new Boolean[]{Boolean.valueOf(cacheProgress), Boolean.valueOf(cacheChangeset), Boolean.valueOf(cacheContestUpdates)});
    }

    private void handleExperienceResult(ExperienceResult result) {
        if (result.isSuccessful() && result.getPoints() >= EXCHANGE_HINT && result.getLevel() >= EXCHANGE_HINT && result.getXp() >= EXCHANGE_HINT) {
            int levelBefore = this.progress.getLevel();
            int xpBefore = this.progress.getXp();
            this.progress.setXp(result.getXp());
            this.progress.setLevel(result.getLevel());
            if (levelBefore < result.getLevel()) {
                notifyLevelUp(levelBefore, this.progress.getLevel(), xpBefore, this.progress.getXp());
            }
        }
    }

    public boolean hasChanges() {
        return (this.changeset == null || this.changeset.isEmpty()) ? false : true;
    }

    public void pushChanges() {
        pushChanges(false);
    }

    private void pushChanges(boolean pushEmpty) {
        if (!this.isPushing) {
            if ((this.changeset != null && !this.changeset.isEmpty()) || pushEmpty) {
                this.isPushing = true;
                Log.i("PROGRESS", "Started push");
//                this.webService.request(PushResult.class, WebService.PUSH_PROGRESS, this.changeset, new com.android.volley.Response.Listener<PushResult>() {
//                    public void onResponse(PushResult response) {
//                        Log.i("PROGRESS", "Push successful: " + response.isSuccessful());
//                        if (response.isSuccessful()) {
//                            ProgressManager.this.changeset.clearTop(response.getLessons().length, response.getQuizzes().length, response.getExchanges().length);
//                            ProgressManager.this.handleExperienceResult(response);
//                            if (response.getAchievements() != null) {
//                                for (Achievement achievement : response.getAchievements()) {
//                                    ProgressManager.this.achievementManager.enqueueAchievement(achievement);
//                                }
//                            }
//                            ProgressManager.this.cache(false, true, false);
//                        }
//                        ProgressManager.this.isPushing = false;
//                        if (ProgressManager.this.isSyncQueued) {
//                            ProgressManager.this.isSyncQueued = false;
//                            ProgressManager.this.sync();
//                        }
//                        if (response.isSuccessful()) {
//                            ProgressManager.this.pushChanges(false);
//                        }
//                        if (ProgressManager.this.progressListener != null) {
//                            ProgressManager.this.progressListener.onPushCompleted(response);
//                        }
//                    }
//                });
            }
        }
    }

    public long getContestUpdate(int id) {
        Long update = (Long) this.contestUpdates.get(id);
        return update != null ? update.longValue() : 0;
    }

    public void setContestUpdate(int id, long time) {
        this.contestUpdates.put(id, Long.valueOf(time));
        cache(false, false, true);
    }

    public boolean hasContestUpdateData() {
        return this.contestUpdates.size() > 0;
    }



    public LessonProgress getLessonProgress(int id) {
        return (LessonProgress) this.progress.getLocalProgress().get(id);
    }

    public int getQuizAttempt(int lessonId, int quizId) {
        LessonProgress lp = (LessonProgress) this.progress.getLocalProgress().get(lessonId);
        if (lp != null) {
            for (QuizProgress qp : lp.getQuizzes()) {
                if (qp.getQuizId() == quizId) {
                    return qp.getAttempt() + EXCHANGE_HINT;
                }
            }
        }
        return EXCHANGE_HINT;
    }

    private void notifyLevelUp(int fromLevel, int toLevel, int fromXp, int toXp) {
        if (this.progressListener != null) {
            this.progressListener.onLevelUp(fromLevel, toLevel, fromXp, toXp);
        }
    }
}
