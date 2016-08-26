package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;
import java.util.List;

public class LessonComment extends PostBase {
    public boolean forceDown;
    public List<LessonComment> loadedReplies;
    private Loader loader;
    private int quizId;
    private int replies;
    private boolean replyMode;
    private int type;



    public static class Loader {
        private boolean hasReachedEnd;
        private boolean isLoading;
        private LessonComment lessonComment;

        private Loader(LessonComment lessonComment) {
            this.lessonComment = lessonComment;
        }

        public boolean isLoading() {
            return this.isLoading;
        }

        public void setLoading(boolean loading) {
            this.isLoading = loading;
        }

        public boolean hasReachedEnd() {
            return this.hasReachedEnd;
        }

        public void setReachedEnd(boolean hasReachedEnd) {
            this.hasReachedEnd = hasReachedEnd;
        }

        public LessonComment getComment() {
            return this.lessonComment;
        }
    }

    public void setReplyMode(boolean replyMode) {
        this.replyMode = replyMode;
    }

    public boolean inReplyMode() {
        return this.replyMode;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getQuizId() {
        return this.quizId;
    }

    public int getType() {
        return this.type;
    }

    public int getReplies() {
        return this.replies;
    }

    public List<LessonComment> getLoadedReplies() {
        return this.loadedReplies;
    }

    public LessonComment() {
        this.loadedReplies = new ArrayList();
    }

    public boolean isForceDown() {
        return this.forceDown;
    }

    public void setForceDown(boolean forceDown) {
        this.forceDown = forceDown;
    }

    public Loader getLoader() {
        if (this.loader == null) {
            this.loader = new Loader();
        }
        return this.loader;
    }

    public int getReplyLoadIndex() {
        int index = 0;
        for (LessonComment comment : this.loadedReplies) {
            if (!comment.isForceDown()) {
                index++;
            }
        }
        return index;
    }
}
