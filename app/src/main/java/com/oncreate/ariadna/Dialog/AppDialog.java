package com.oncreate.ariadna.Dialog;

import android.app.Dialog;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.android.volley.DefaultRetryPolicy;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.R;


public abstract class AppDialog extends AppCompatDialogFragment {
    private static long lastOpenedMillis;
    private boolean isDismissQueued;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(0, R.style.AppDialogTheme);
    }

    public Dialog onCreateAppDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    protected boolean fitHorizontal() {
        return true;
    }

    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = onCreateAppDialog(savedInstanceState);
        if (dialog != null && VERSION.SDK_INT >= 14) {
            dialog.getWindow().setDimAmount(0.2f);
        }
        return dialog;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (fitHorizontal() && dialog != null) {
            LayoutParams lp = new LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            float dialogMaxWidth = getResources().getDimension(R.dimen.dialog_max_width) * (DefaultRetryPolicy.DEFAULT_BACKOFF_MULT - lp.horizontalMargin);
            if (((float) getResources().getDisplayMetrics().widthPixels) > dialogMaxWidth) {
                lp.width = (int) dialogMaxWidth;
            } else {
                lp.width = -1;
            }
            window.setAttributes(lp);
        }
    }

    public void dismiss() {
        if (getApp().getActivity().isActive()) {
            super.dismiss();
        } else {
            this.isDismissQueued = true;
        }
    }

    public void onResume() {
        super.onResume();
        if (this.isDismissQueued) {
            this.isDismissQueued = false;
            dismiss();
        }
    }

    protected AriadnaApplication getApp() {
        return AriadnaApplication.getInstance();
    }

    protected void navigate(Fragment fragment) {
        getApp().getActivity().navigate(fragment);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    public void show(FragmentManager manager, String tag) {
        if (getApp().getActivity().isActive()) {
            super.show(manager, tag);
            lastOpenedMillis = System.currentTimeMillis();
            return;
        }
        getApp().getActivity().enqueueDialog(this);
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(view, 1);
        }
    }

    public boolean hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        if (getDialog().getCurrentFocus() == null) {
            return false;
        }
        inputMethodManager.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public static long getLastOpenedMillis() {
        return lastOpenedMillis;
    }
}
