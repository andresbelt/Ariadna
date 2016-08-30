package com.oncreate.ariadna.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.LessonManager;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.LessonProgress;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.ModelsVO.QuizProgress;
import com.oncreate.ariadna.R;

import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends HeaderAdapter {
    public static final int LESSON = 1;
    public static final int QUIZ = 2;
    private Context context;
    private List<TimelineItem> items;

    private static class TimelineItem {
        private boolean isEnabled;
        private boolean isSelected;
        private int lessonId;
        private int mode;
        private int quizId;

        public TimelineItem(int lessonId, int quizId, int mode, boolean isEnabled, boolean isSelected) {
            this.lessonId = lessonId;
            this.quizId = quizId;
            this.mode = mode;
            this.isEnabled = isEnabled;
            this.isSelected = isSelected;
        }
    }

    public class ViewHolder {
        private TimelineItem item;
        private ImageView timelineIcon;
        private View view;


        class C05001 implements OnClickListener {
            final /* synthetic */ TimelineAdapter val$this$0;

            C05001(TimelineAdapter timelineAdapter) {
                this.val$this$0 = timelineAdapter;
            }

            public void onClick(View view) {
                ((AppActivity) TimelineAdapter.this.context).navigate(LessonManager.getFragment(ViewHolder.this.item.lessonId, ViewHolder.this.item.quizId, ViewHolder.this.item.mode == TimelineAdapter.QUIZ));
            }
        }

        public ViewHolder(View itemView) {
            this.view = itemView;
            this.timelineIcon = (ImageView) itemView.findViewById(R.id.timeline_item);
            this.view.setOnClickListener(new C05001(TimelineAdapter.this));
        }

        public void bind(TimelineItem item) {
            this.item = item;
            this.view.setActivated(item.isSelected);
            this.view.setEnabled(item.isEnabled);
            this.timelineIcon.setImageResource(item.mode == TimelineAdapter.LESSON ? R.drawable.timeline_video : R.drawable.quiz_icon);
        }
    }

    public TimelineAdapter(Context context, Lesson lesson, LessonProgress lessonProgress, int currentQuizId, int mode) {
        this.context = context;
        boolean includeLesson = lesson.getType() == 0;
        int lessonId = lesson.getId();
        List<Quiz> quizzes = lesson.getQuizzes();
        this.items = new ArrayList(includeLesson ? quizzes.size() * QUIZ : quizzes.size());
        boolean isShortcut = lesson.getIsShortcut();
        boolean isCompleted = lessonProgress != null && lessonProgress.getIsCompleted().booleanValue();
        boolean prevCompleted = true;
        for (Quiz quiz : quizzes) {
            boolean isSelected;
            List list;
            boolean z;
            int quizId = quiz.getId();
            boolean isCurrent = quizId == currentQuizId;
            boolean isUnlocked = !isShortcut && (isCompleted || isCurrent || prevCompleted);
            prevCompleted = false;
            if (!(isShortcut || isCompleted || lessonProgress == null)) {
                for (QuizProgress qp : lessonProgress.getQuizzes()) {
                    if (qp.getQuizId() == quizId) {
                        prevCompleted = qp.isCompleted();
                        isUnlocked = isUnlocked || prevCompleted;
                    }
                }
            }
            if (includeLesson) {
                isSelected = mode == LESSON && isCurrent;
                list = this.items;
                z = isUnlocked && !isSelected;
                list.add(new TimelineItem(lessonId, quizId, LESSON, z, isSelected));
            }
            isSelected = mode == QUIZ && isCurrent;
            list = this.items;
            if (!isUnlocked || isSelected) {
                z = false;
            } else {
                z = true;
            }
            list.add(new TimelineItem(lessonId, quizId, QUIZ, z, isSelected));
        }
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int i) {
        return this.items.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.view_timeline_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bind((TimelineItem) this.items.get(i));
        return view;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < this.items.size(); i += LESSON) {
            if (((TimelineItem) this.items.get(i)).isSelected) {
                return i;
            }
        }
        return 0;
    }
}
