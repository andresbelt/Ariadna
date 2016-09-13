package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.Dialog.LoadingView;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.Request.MixedLayoutBuilder;
import com.oncreate.ariadna.Request.SpanStyle;
import com.oncreate.ariadna.Util.ConstantVariables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PlaceholderQuizBase extends LoadingQuizView {
    private static final String FORMATTER_REGEX = "\\[!([a-zA-Z0-9]+)!\\](.*(?!\\[!([a-zA-Z0-9]+)!\\]))";
    private static final String PLACEHOLDER_REGEX = "\\{(\\d+)\\}";
    private ViewGroup container;
    private ViewGroup currentLayout;
    private Pattern formatterPattern;
    private LoadingView loadingView;
    private Pattern placeholderPattern;
    private String question;
    protected float textSize;

    private static class HighlightTask extends AsyncTask<Void, Void, Void> {
        private ArrayList<Block> blocks;
        ArrayList<SpanStyle> decorations;
        private MixedLayoutBuilder layoutBuilder;

        private static class Block {
            String content;
            String format;

            public Block(String format, String content) {
                this.format = format;
                this.content = content;
            }
        }

        public HighlightTask(MixedLayoutBuilder layoutBuilder) {
            this.blocks = new ArrayList();
            this.layoutBuilder = layoutBuilder;
        }

        public void addBlock(String format, String content) {
            this.blocks.add(new Block(format, content));
        }

        protected Void doInBackground(Void... params) {
//            SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter();
//            int index = 0;
//            this.decorations = new ArrayList();
//            Iterator it = this.blocks.iterator();
//            while (it.hasNext()) {
//                Block block = (Block) it.next();
//                List<SpanStyle> styles = syntaxHighlighter.parse(block.format, block.content);
//                if (index > 0) {
//                    for (SpanStyle style : styles) {
//                        style.shift(index);
//                    }
//                }
//                this.decorations.addAll(styles);
//                index += block.content.length();
//            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.layoutBuilder.setDecorations(this.decorations);
        }
    }

    protected abstract View getPlaceholder(ViewGroup viewGroup, int i);

    protected abstract String getReplacement(int i);

    public PlaceholderQuizBase(Context context) {
        super(context);
        this.currentLayout = null;
        this.question = null;
        this.textSize = 0.0f;
        this.textSize = getResources().getDimension(R.dimen.quiz_placeholder_text_size);
        this.formatterPattern = Pattern.compile(FORMATTER_REGEX, 32);
        this.placeholderPattern = Pattern.compile(PLACEHOLDER_REGEX, 32);
    }

    public LoadingView getLoadingView() {
        return this.loadingView;
    }

    protected void onShowContent() {
        super.onShowContent();
        if (this.currentLayout != null) {
            this.currentLayout.setVisibility(0);
        }
    }

    protected void onHideContent() {
        super.onHideContent();
        if (this.currentLayout != null) {
            this.currentLayout.setVisibility(4);
        }
    }

    protected final View onCreateQuiz(LayoutInflater inflater, ViewGroup parent, Quiz quiz) {
        this.question = quiz.getQuestion();
        onPreCreateQuiz();
        Matcher matcher = this.formatterPattern.matcher(this.question);
        if (matcher.find()) {
            this.question = this.question.substring(0, matcher.start()).trim();
        }
        View view = inflater.inflate(R.layout.quiz_placeholder, parent, false);
        this.loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        this.container = (ViewGroup) view.findViewById(R.id.quiz_placeholder_container);
        parse();
        return view;
    }

    protected void onPreCreateQuiz() {
    }

    protected boolean centerInLayout() {
        return false;
    }

    protected boolean canCheck() {
        return this.loadingView.getMode() == 0;
    }

    public String getQuestion() {
        return this.question;
    }

    protected void parse() {
        if (this.currentLayout != null) {
            this.container.removeView(this.currentLayout);
            this.currentLayout = null;
        }
        MixedLayoutBuilder layoutBuilder = new MixedLayoutBuilder(getContext());
        layoutBuilder.setTextSize(this.textSize);
        layoutBuilder.setEnableLineLayoutTransitions(true);
        layoutBuilder.prepareDecorations();
        Matcher matcher = this.formatterPattern.matcher(this.quiz.getQuestion());
        HighlightTask highlightTask = new HighlightTask(layoutBuilder);
        while (matcher.find()) {
            highlightTask.addBlock(matcher.group(1), parseGroup(layoutBuilder, matcher.group(2).replaceAll("\\r", ConstantVariables.VERSION_NAME)));
        }
        this.currentLayout = layoutBuilder.getLayout();
        this.container.addView(this.currentLayout);
        highlightTask.execute(new Void[0]);
    }

    private String parseGroup(MixedLayoutBuilder layoutBuilder, String content) {
        layoutBuilder.startBlock();
        Matcher matcher = this.placeholderPattern.matcher(content);
        int index = 0;
        StringBuilder sb = new StringBuilder(content.length());
        while (matcher.find()) {
            if (index < matcher.start()) {
                String text = content.substring(index, matcher.start());
                layoutBuilder.insertText(text);
                sb.append(text);
            }
            int answerIndex = Integer.parseInt(matcher.group(1));
            String replacement = getReplacement(answerIndex);
            layoutBuilder.insertView(getPlaceholder(layoutBuilder.prepareViewParent(), answerIndex), replacement.length());
            sb.append(replacement);
            index = matcher.end();
        }
        if (index < content.length()) {
            String text = content.substring(index);
            layoutBuilder.insertText(text);
            sb.append(text);
        }
        return sb.toString();
    }
}
