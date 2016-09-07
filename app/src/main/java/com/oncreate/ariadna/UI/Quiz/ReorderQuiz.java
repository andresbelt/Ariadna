package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oncreate.ariadna.ModelsVO.Answer;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.views.UnwrappedLayoutManager;

import java.util.Collections;
import java.util.List;

public class ReorderQuiz extends QuizView {
    private Adapter adapter;

    /* renamed from: com.sololearn.app.views.quizzes.ReorderQuiz.1 */
    class C06761 implements Runnable {
        C06761() {
        }

        public void run() {
            ReorderQuiz.this.setResult(true);
        }
    }

    protected static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Context context;
        private ItemTouchHelper itemTouchHelper;
        private List<Answer> items;

        class C06771 implements OnTouchListener {
            final /* synthetic */ ViewHolder val$holder;

            C06771(ViewHolder viewHolder) {
                this.val$holder = viewHolder;
            }

            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == 0) {
                    Adapter.this.itemTouchHelper.startDrag(this.val$holder);
                }
                return false;
            }
        }

        public Adapter(Context context, List<Answer> answers) {
            this.context = context;
            this.items = answers;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.quiz_reorder_item, parent, false));
            holder.itemView.setOnTouchListener(new C06771(holder));
            return holder;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind((Answer) this.items.get(position));
        }

        public int getItemCount() {
            return this.items.size();
        }

        public void onItemMove(int fromPosition, int toPosition) {
            int i;
            if (fromPosition < toPosition) {
                for (i = fromPosition; i < toPosition; i++) {
                    Collections.swap(this.items, i, i + 1);
                }
            } else {
                for (i = fromPosition; i > toPosition; i--) {
                    Collections.swap(this.items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
            this.itemTouchHelper = itemTouchHelper;
        }

        public Answer getItem(int index) {
            return (Answer) this.items.get(index);
        }

        public List<Answer> getItems() {
            return this.items;
        }
    }

    private static class ItemTouchHelperCallback extends Callback {
        private final Adapter mAdapter;

        public ItemTouchHelperCallback(Adapter adapter) {
            this.mAdapter = adapter;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return Callback.makeMovementFlags(3, 0);
        }

        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            this.mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).setSelected(actionState != 0);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(Answer item) {
            this.textView.setText(item.getText());
        }

        public void setSelected(boolean isSelected) {
            this.itemView.setSelected(isSelected);
        }
    }

    public ReorderQuiz(Context context) {
        super(context);
    }

    protected View onCreateQuiz(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        View view = inflater.inflate(R.layout.quiz_list, parent, false);
        this.adapter = new Adapter(getContext(), getShuffledAnswers());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(this.adapter));
        this.adapter.setItemTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new UnwrappedLayoutManager(getContext()));
        recyclerView.setAdapter(this.adapter);
        return view;
    }

    public void check() {
        boolean isCorrect = true;
        List<Answer> answers = this.quiz.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i) != this.adapter.getItem(i)) {
                isCorrect = false;
                break;
            }
        }
        setResult(isCorrect);
    }

    public void unlock() {
        List<Answer> items = this.adapter.getItems();
        List<Answer> answers = this.quiz.getAnswers();
        boolean isChanged = false;
        for (int i = 0; i < answers.size(); i++) {
            int currentIndex = items.indexOf((Answer) answers.get(i));
            if (currentIndex != i) {
                this.adapter.onItemMove(currentIndex, i);
                isChanged = true;
            }
        }
        if (isChanged) {
            postDelayed(new C06761(), 400);
        } else {
            setResult(true);
        }
    }
}
