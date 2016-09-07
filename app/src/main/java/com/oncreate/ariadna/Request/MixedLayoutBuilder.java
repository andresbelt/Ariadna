package com.oncreate.ariadna.Request;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MixedLayoutBuilder {
    private Context context;
    private StringBuilder currentString;
    private DecoratorTask decoratorTask;
    private boolean enableLineLayoutTransitions;
    private LinearLayout lineLayout;
    private LinearLayout root;
    private int spanStart;
    private float textSize;

    private static class DecoratorTask extends AsyncTask<Void, Void, Void> {
        private List<SpanStyle> decorations;
        private List<Item> items;

        private static class Item {
            private int end;
            private int start;
            private SpannableStringBuilder stringBuilder;
            private CharSequence text;
            private TextView textView;

            public Item(TextView textView, int start) {
                this.stringBuilder = null;
                this.textView = textView;
                this.start = start;
                this.text = textView.getText();
                this.end = this.text.length() + start;
            }
        }

        private DecoratorTask() {
            this.items = new ArrayList();
        }

        public void addTextView(TextView textview, int index) {
            this.items.add(new Item(textview, index));
        }

        protected Void doInBackground(Void[] params) {
            for (Item item : this.items) {
                for (SpanStyle style : this.decorations) {
                    if (style.getStart() >= item.start && style.getEnd() <= item.end) {
                        if (item.stringBuilder == null) {
                            item.stringBuilder = new SpannableStringBuilder(item.text);
                        }
                        item.stringBuilder.setSpan(new ForegroundColorSpan(style.getColor()), style.getStart() - item.start, style.getEnd() - item.start, 18);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Item item : this.items) {
                if (item.stringBuilder != null) {
                    item.textView.setText(item.stringBuilder);
                }
            }
        }
    }

    public void setEnableLineLayoutTransitions(boolean enableLineLayoutTransitions) {
        this.enableLineLayoutTransitions = enableLineLayoutTransitions;
    }

    public MixedLayoutBuilder(Context context) {
        this.spanStart = 0;
        this.enableLineLayoutTransitions = false;
        this.context = context;
        this.root = new LinearLayout(context);
        this.root.setOrientation(1);
        this.root.setClipChildren(false);
        this.currentString = new StringBuilder();
    }

    public void insertText(String text) {
        int firstLineIndex = -1;
        boolean shouldExitLayout = false;
        if (this.lineLayout != null) {
            firstLineIndex = text.indexOf("\n");
            if (firstLineIndex != -1) {
                text = text.substring(0, firstLineIndex) + text.substring(firstLineIndex + 1);
                shouldExitLayout = true;
            }
        }
        int endBeforeInsert = this.currentString.length();
        this.currentString.append(text);
        if (shouldExitLayout) {
            flushTo(endBeforeInsert + firstLineIndex);
            this.spanStart++;
            this.lineLayout = null;
        }
    }

    public ViewGroup prepareViewParent() {
        if (this.lineLayout == null) {
            int lastLineIndex = this.currentString.lastIndexOf("\n");
            if (lastLineIndex != -1) {
                flushTo(lastLineIndex);
                int firstLineIndex = this.currentString.indexOf("\n");
                if (firstLineIndex != -1 && this.currentString.substring(0, firstLineIndex).trim().length() == 0) {
                    this.currentString.delete(0, firstLineIndex + 1);
                    this.spanStart += firstLineIndex + 1;
                }
            }
            this.lineLayout = new LinearLayout(this.context);
            this.lineLayout.setClipChildren(false);
            this.lineLayout.setOrientation(0);
            if (VERSION.SDK_INT >= 16 && this.enableLineLayoutTransitions) {
                LayoutTransition transition = new LayoutTransition();
                transition.setDuration(200);
                transition.disableTransitionType(2);
                transition.disableTransitionType(0);
                transition.disableTransitionType(3);
                transition.disableTransitionType(1);
                transition.enableTransitionType(4);
                this.lineLayout.setLayoutTransition(transition);
            }
            this.root.addView(this.lineLayout);
        }
        return this.lineLayout;
    }

    public void insertView(View view, int shift) {
        prepareViewParent();
        flush();
        this.lineLayout.addView(view);
        this.spanStart += shift;
    }

    public LinearLayout getLayout() {
        flush();
        return this.root;
    }

    private void flush() {
        if (this.currentString.length() > 0) {
            TextView textView = new TextView(this.context);
            textView.setTextSize(0, this.textSize);
            textView.setText(this.currentString.toString());
            if (this.lineLayout != null) {
                this.lineLayout.addView(textView);
            } else {
                this.root.addView(textView);
            }
            if (this.decoratorTask != null) {
                this.decoratorTask.addTextView(textView, this.spanStart);
            }
            this.spanStart += this.currentString.length();
            this.currentString = new StringBuilder();
        }
    }

    private void flushTo(int index) {
        String pending = this.currentString.substring(index);
        this.currentString.delete(index, this.currentString.length());
        flush();
        this.currentString.append(pending);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void startBlock() {
        flush();
        this.lineLayout = null;
    }

    public void prepareDecorations() {
        this.decoratorTask = new DecoratorTask();
    }

    public void setDecorations(List<SpanStyle> decorations) {
        this.decoratorTask.decorations = decorations;
        this.decoratorTask.execute(new Void[0]);
    }
}
