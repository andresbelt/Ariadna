package com.oncreate.ariadna.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oncreate.ariadna.Base.AppActivity;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.ModelsVO.Lesson;
import com.oncreate.ariadna.ModelsVO.LessonState;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collection;

public class LessonAdapter extends Adapter<LessonAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Lesson> items;
    private Listener listener;

    public interface Listener {
        void loadLesson(Lesson lesson, LessonState lessonState);
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView details;
        private ImageView icon;
        private Lesson lesson;
        private TextView name;
        private TextView number;
        private LessonState state;
        private ViewGroup status;

        /* renamed from: com.sololearn.app.adapters.LessonAdapter.ViewHolder.1 */
        class C04901 implements OnClickListener {
            final /* synthetic */ LessonAdapter val$this$0;

            C04901(LessonAdapter lessonAdapter) {
                this.val$this$0 = lessonAdapter;
            }

            public void onClick(View view) {
                if (LessonAdapter.this.listener != null) {
                    LessonAdapter.this.listener.loadLesson(ViewHolder.this.lesson, ViewHolder.this.state);
                }
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.lesson);
            this.name = (TextView) itemView.findViewById(R.id.lesson_name);
            this.number = (TextView) itemView.findViewById(R.id.lesson_number);
            this.details = (TextView) itemView.findViewById(R.id.lesson_details);
            this.status = (ViewGroup) itemView.findViewById(R.id.lesson_statusbar);
            this.icon = (ImageView) itemView.findViewById(R.id.lesson_icon);
            this.cardView.setOnClickListener(new C04901(LessonAdapter.this));
        }

        public void apply(Lesson lesson) {
            this.lesson = lesson;
            this.name.setText(lesson.getName());
            int lessonNumber = LessonAdapter.this.items.indexOf(lesson) + 1;
            this.number.setText(LessonAdapter.this.context.getString(R.string.lesson_number_format, new Object[]{Integer.valueOf(lessonNumber), Integer.valueOf(LessonAdapter.this.items.size())}));
            this.state = AriadnaApplication.getInstance().getProgressManager().getLessonState(lesson.getId());
            switch (this.state.getState()) {
//                case LessonCommentFragment.TYPE_TEXT_OR_VIDEO /*0*/:
//                    this.status.setBackgroundColor(0);
//                    this.details.setTextColor(-7829368);
//                    this.cardView.setCardBackgroundColor(ContextCompat.getColor(LessonAdapter.this.context, R.color.card_disabled_background));
//                    this.icon.setVisibility(0);
//                    this.icon.setImageResource(R.drawable.lesson_locked_icon);
//                    break;
                case AppActivity.OFFSET_TOOLBAR /*1*/:
                    this.status.setBackgroundResource(R.drawable.lesson_status_normal);
                    this.details.setTextColor(-1);
                    this.cardView.setCardBackgroundColor(ContextCompat.getColor(LessonAdapter.this.context, R.color.card_background));
                    this.icon.setVisibility(8);
                    break;
                case AppActivity.OFFSET_TABS /*2*/:
                    this.status.setBackgroundResource(R.drawable.lesson_status_active);
                    this.details.setTextColor(-1);
                    this.cardView.setCardBackgroundColor(ContextCompat.getColor(LessonAdapter.this.context, R.color.card_background));
                    this.icon.setVisibility(0);
                    this.icon.setImageResource(lesson.getType() == 0 ? R.drawable.lesson_checkpoint_icon : R.drawable.lesson_quiz_icon);
                    break;
            }
            if (lesson.getType() == 1 || lesson.getMode() == 1 || lesson.getMode() == 2) {
                this.details.setText(LessonAdapter.this.context.getResources().getQuantityString(R.plurals.lesson_text_details, lesson.getQuizzes().size(), new Object[]{Integer.valueOf(lesson.getQuizzes().size())}));
            } else {
               // this.details.setText(LessonAdapter.this.context.getString(R.string.lesson_video_details, new Object[]{DateTimeUtils.getDurationString(lesson.getVideoDuration())}));
            }
            this.cardView.setCardElevation((float) ((this.state.getState() * 4) + 2));
        }
    }

    public LessonAdapter(Context context, Collection<Lesson> lessons) {
        this.context = context;
        this.items = new ArrayList(lessons);
        setHasStableIds(true);
    }

    public void setItems(Collection<Lesson> lessons) {
        this.items = new ArrayList(lessons);
        notifyDataSetChanged();
    }

    public ArrayList<Lesson> getItems() {
        return this.items;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public long getItemId(int i) {
        return (long) ((Lesson) this.items.get(i)).getId();
    }

    public int getItemCount() {
        return this.items.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.view_lesson, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.apply((Lesson) this.items.get(i));
    }

    public int getItemViewType(int i) {
        return 0;
    }
}
