package com.oncreate.ariadna;

import android.content.res.AssetManager;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Base.ServicesPrincipal;
import com.oncreate.ariadna.ModelsVO.Course;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.Util.StorageService;
import com.oncreate.ariadna.loginLearn.ParamMap;
import com.oncreate.ariadna.loginLearn.WebService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CourseManager {
    private static final String FILE_NAME = "course.json";
    private Course course;
    private SparseArray<Integer> lessonModules;
    private SparseArray<Lesson> lessons;
    private SparseArray<Integer> moduleIndexes;
    private SparseArray<Module> modules;
    private StorageService storage;
    private ArrayList<Listener> updateListeners;
    private WebService  webService;

    public interface Listener {
        void onResult(Course course);
    }


    class C12951 implements Response.Listener<GetCourseResult> {
        final Listener vallistener;

        C12951(Listener listener) {
            this.vallistener = listener;
        }

        public void onResponse(GetCourseResult response) {
            if (response.isUpdated()) {
                Gson gson = new Gson();
                Course LGNRegion = gson.fromJson(leer("ariadnarespuestas.txt"), Course.class);
                CourseManager.this.course = LGNRegion;
                CourseManager.this.initSparse();
              CourseManager.this.storage.writeText(CourseManager.FILE_NAME,   CourseManager.this.webService.getGson().toJson(CourseManager.this.course));
                CourseManager.this.raiseOnUpdateListeners();
            }
            if (this.vallistener != null) {
                this.vallistener.onResult(CourseManager.this.course);
            }
        }
    }

    public CourseManager(StorageService storage, WebService webService) {
        this.updateListeners = new ArrayList();
        this.storage = storage;
        this.webService = webService;

    }

    public String leer( String archivo )

    {
        String text = "";
        AssetManager assetManager = AriadnaApplication.getInstance().getAssets();
        InputStream inputStream = null;
        try {

            inputStream = assetManager.open( archivo );
            text = btoString( inputStream );

        } catch (IOException ex) {
            //com.org.cruzverde.util.MyTextView.setText( ex.getMessage() );
        }
        finally{
            if( inputStream != null )
            {
                try{
                    inputStream.close();
                }
                catch( IOException ex )
                {
                    Log.d("","");
                    //com.org.cruzverde.util.MyTextView.setText( ex.getMessage() );
                }
            }
        }
        return text;
    }


    /** Convierte bytes en texto
     * @param inputStream de tipo InputStream
     * */
    public String btoString( InputStream inputStream ) throws IOException
    {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len=0;
        while ( (len=inputStream.read(bytes))>0 )
        {
            b.write(bytes,0,len);
        }
        return new String( b.toByteArray(),"UTF8");
    }


    public Course getCourse() {
        return this.course;
    }

    public Module getModuleById(int id) {
        if (this.modules != null) {
            return (Module) this.modules.get(id);
        }
        return null;
    }

    public Module getModuleByLesson(int lessonId) {
        if (this.lessonModules != null) {
            return getModuleById(((Integer) this.lessonModules.get(lessonId)).intValue());
        }
        return null;
    }

    public Lesson getLessonById(int id) {
        if (this.lessons != null) {
            return (Lesson) this.lessons.get(id);
        }
        return null;
    }

    public int getCourseId() {
        return this.course.getId();
    }

    public Lesson findLessonByQuizId(int quizId) {
        Iterator it = this.course.getModules().iterator();
        while (it.hasNext()) {
            Iterator it2 = ((Module) it.next()).getLessons().iterator();
            while (it2.hasNext()) {
                Lesson l = (Lesson) it2.next();
                for (Quiz q : l.getQuizzes()) {
                    if (q.getId() == quizId) {
                        return l;
                    }
                }
            }
        }
        return null;
    }

    public int getModuleIndex(int id) {
        return ((Integer) this.moduleIndexes.get(id)).intValue();
    }

    public boolean initializeFromCache() {
        try {
            String cachedCourse = this.storage.readText(FILE_NAME);
            if (cachedCourse != null) {
                this.course = (Course) this.webService.getGson().fromJson(cachedCourse, Course.class);
                initSparse();
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    public boolean initialize(Listener listener) {
        return initialize(listener, false);
    }

    public boolean initialize(Listener listener, boolean forceUpdate) {
        boolean isImmediate = false;
        if (!forceUpdate && (this.course != null || initializeFromCache())) {
            listener.onResult(this.course);
            listener = null;
            isImmediate = true;
        }
        update(listener);
        return isImmediate;
    }

    public void addOnUpdateListener(Listener listener) {
        this.updateListeners.add(listener);
    }

    public void removeOnUpdateListener(Listener listener) {
        this.updateListeners.remove(listener);
    }

    private void raiseOnUpdateListeners() {
        Iterator it = this.updateListeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onResult(this.course);
        }
    }

    public void update(Listener listener) {
        int version;
        WebService webService = this.webService;
        Class cls = GetCourseResult.class;
        String str = ServicesPrincipal.GET_COURSE;
        ParamMap create = ParamMap.create();
        String str2 = "version";
        if (this.course != null) {
            version = this.course.getVersion();
        } else {
            version = 0;
        }
        webService.request(cls, str, create.add(str2, Integer.valueOf(version)), new C12951(listener));
       // ServicesPrincipal.getInstance(AriadnaApplication.getInstance()).request(cls, str, create.add(str2, Integer.valueOf(version)), new C12951(listener));
    }

    private void initSparse() {
        this.modules = new SparseArray();
        this.lessons = new SparseArray();
        this.moduleIndexes = new SparseArray();
        this.lessonModules = new SparseArray();
        int moduleIndex = 0;
        Iterator it = this.course.getModules().iterator();
        while (it.hasNext()) {
            Module module = (Module) it.next();
            this.modules.put(module.getId(), module);
            int moduleIndex2 = moduleIndex + 1;
            this.moduleIndexes.put(module.getId(), Integer.valueOf(moduleIndex));
            Iterator it2 = module.getLessons().iterator();
            while (it2.hasNext()) {
                Lesson lesson = (Lesson) it2.next();
                this.lessons.put(lesson.getId(), lesson);
                this.lessonModules.put(lesson.getId(), Integer.valueOf(module.getId()));
            }
            moduleIndex = moduleIndex2;
        }
    }

    public void reset() {
        this.course = null;
        this.storage.deleteFile(FILE_NAME);
    }
}
