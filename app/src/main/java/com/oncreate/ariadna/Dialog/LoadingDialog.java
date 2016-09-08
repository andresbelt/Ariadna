package com.oncreate.ariadna.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oncreate.ariadna.R;

public class LoadingDialog extends AppDialog {
    public static final int CIRCULAR = 1;
    public static final int HORIZONTAL = 2;
    public static final int HORIZONTAL_INDETERMINATE = 3;
    private View circularProgressBar;
    private ProgressBar horizontalProgressBar;
    private int max;
    private TextView messageText;
    private int mode;
    private int progress;

    public LoadingDialog() {
        this.circularProgressBar = null;
        this.horizontalProgressBar = null;
        this.mode = CIRCULAR;
        this.max = 0;
        this.progress = 0;
        setCancelable(false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(CIRCULAR, R.style.AppDialogTheme);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_loading, container, false);
        this.messageText = (TextView) rootView.findViewById(R.id.loading_view_message);
        this.circularProgressBar = rootView.findViewById(R.id.loading_view_progressbar);
        this.horizontalProgressBar = (ProgressBar) rootView.findViewById(R.id.loading_view_horizontal_progressbar);
        if (this.mode != CIRCULAR) {
            updateMode();
        }
        return rootView;
    }

    public void setMessage(CharSequence message) {
        this.messageText.setText(message);
    }

    public void setMessage(@StringRes int id) {
        this.messageText.setText(id);
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (this.circularProgressBar != null) {
            updateMode();
        }
    }

    public void setMax(int max) {
        this.max = max;
        updateProgress();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        updateProgress();
    }

    private void updateMode() {
        int i = 8;
        boolean z = false;
        if (this.circularProgressBar != null) {
            this.circularProgressBar.setVisibility(this.mode == CIRCULAR ? 0 : 8);
            ProgressBar progressBar = this.horizontalProgressBar;
            if (this.mode != CIRCULAR) {
                i = 0;
            }
            progressBar.setVisibility(i);
            progressBar = this.horizontalProgressBar;
            if (this.mode == HORIZONTAL_INDETERMINATE) {
                z = true;
            }
            progressBar.setIndeterminate(z);
            updateProgress();
        }
    }

    private void updateProgress() {
        if (this.horizontalProgressBar != null && this.mode == HORIZONTAL) {
            this.horizontalProgressBar.setMax(this.max);
            this.horizontalProgressBar.setProgress(this.progress);
        }
    }

    protected boolean fitHorizontal() {
        return false;
    }
}
