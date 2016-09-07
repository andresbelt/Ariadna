package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;

import com.oncreate.ariadna.Dialog.LoadingView;
import com.oncreate.ariadna.R;

public abstract class LoadingQuizView extends QuizView {
    private boolean blockLoading;
    private int loadingCount;
    private LoadingView loadingView;

    /* renamed from: com.sololearn.app.views.quizzes.LoadingQuizView.1 */
    class C06691 implements Runnable {
        C06691() {
        }

        public void run() {
            LoadingQuizView.this.blockLoading = false;
            LoadingQuizView.this.onRetry();
        }
    }

    public LoadingQuizView(Context context) {
        super(context);
        this.loadingView = null;
        this.loadingCount = 0;
        this.blockLoading = false;
    }

    private boolean initializeLoading() {
        if (this.loadingView == null) {
            this.loadingView = getLoadingView();
            if (this.loadingView != null) {
                this.loadingView.setOnRetryListener(new C06691());
                this.loadingView.setErrorRes(R.string.internet_connection_failed);
            }
        }
        return this.loadingView != null;
    }

    protected LoadingView getLoadingView() {
        return null;
    }

    protected final void incrementLoading() {
        if (!this.blockLoading) {
            this.loadingCount++;
            if (initializeLoading() && this.loadingCount == 1) {
                onHideContent();
                this.loadingView.setMode(1);
            }
        }
    }

    protected final void decrementLoading() {
        if (!this.blockLoading) {
            this.loadingCount--;
            if (initializeLoading() && this.loadingCount == 0) {
                onShowContent();
                this.loadingView.setMode(0);
            }
        }
    }

    protected final void setError() {
        this.blockLoading = true;
        this.loadingCount = 0;
        if (initializeLoading()) {
            onHideContent();
            this.loadingView.setMode(2);
        }
    }

    protected void onRetry() {
    }

    protected void onHideContent() {
    }

    protected void onShowContent() {
    }

    protected final boolean isContentHidden() {
        return (this.loadingView == null || this.loadingView.getMode() == 0) ? false : true;
    }
}
