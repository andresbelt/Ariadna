package com.oncreate.ariadna.Dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.R;

public class LoadingView extends LinearLayout {
    public static final int ERROR = 2;
    public static final int LOADING = 1;
    public static final int NONE = 0;
    private Button button;
    private boolean darkModeEnabled;
    private int errorStringId;
    private int loadingStringId;
    private int mode;
    private Runnable onRetryListener;
    private View progressBar;
    private TextView textView;

    /* renamed from: com.sololearn.app.views.LoadingView.1 */
    class C06541 implements OnClickListener {
        C06541() {
        }

        public void onClick(View view) {
            if (LoadingView.this.onRetryListener != null) {
                LoadingView.this.onRetryListener.run();
            }
        }
    }

    public LoadingView(Context context) {
        super(context);
        this.mode = 0;
        this.errorStringId = -1;
        this.loadingStringId = -1;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mode = 0;
        this.errorStringId = -1;
        this.loadingStringId = -1;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mode = 0;
        this.errorStringId = -1;
        this.loadingStringId = -1;
        init();
    }

    @TargetApi(21)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mode = 0;
        this.errorStringId = -1;
        this.loadingStringId = -1;
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.view_loading, this);
        this.progressBar = rootView.findViewById(R.id.loading_view_progressbar);
        this.textView = (TextView) rootView.findViewById(R.id.loading_view_message);
        this.button = (Button) rootView.findViewById(R.id.loading_view_action);
        this.button.setOnClickListener(new C06541());
        setMode(0);
    }

    public void setOnRetryListener(Runnable listener) {
        this.onRetryListener = listener;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case 0 /*0*/:
                setVisibility(8);
            case LOADING /*1*/:
                if (this.loadingStringId != -1) {
                    this.textView.setText(this.loadingStringId);
                    this.textView.setVisibility(0);
                } else {
                    this.textView.setText(BuildConfig.VERSION_NAME);
                    this.textView.setVisibility(8);
                }
                this.progressBar.setVisibility(0);
                this.button.setVisibility(8);
                setVisibility(0);
            case ERROR /*2*/:
                if (this.errorStringId != -1) {
                    this.textView.setText(this.errorStringId);
                    this.textView.setVisibility(0);
                } else {
                    this.textView.setText(BuildConfig.VERSION_NAME);
                    this.textView.setVisibility(8);
                }
                this.progressBar.setVisibility(8);
                this.button.setVisibility(0);
                setVisibility(0);
            default:
        }
    }

    public void setLoadingRes(@StringRes int id) {
        this.loadingStringId = id;
        if (this.mode == LOADING && this.loadingStringId != -1) {
            this.textView.setText(this.loadingStringId);
        }
    }

    public void setErrorRes(@StringRes int id) {
        this.errorStringId = id;
        if (this.mode == ERROR && this.errorStringId != -1) {
            this.textView.setText(this.errorStringId);
        }
    }

    public void setButtonRes(@StringRes int id) {
        if (id != -1) {
            this.button.setText(id);
        }
    }

    public void setDarkModeEnabled(boolean darkModeEnabled) {
        this.textView.setTextColor(ContextCompat.getColor(getContext(), darkModeEnabled ? R.color.white_overlay_5 : R.color.black_87));
        this.button.setAlpha(darkModeEnabled ? 0.5f : DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
