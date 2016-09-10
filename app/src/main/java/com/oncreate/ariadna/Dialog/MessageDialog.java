package com.oncreate.ariadna.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.oncreate.ariadna.R;

public class MessageDialog extends AppDialog {
    public static final int CANCEL = -3;
    public static final int NO = -2;
    public static final int YES = -1;
    private android.support.v7.app.AlertDialog.Builder dialogBuilder;
    private Listener listener;


    class C05101 implements Runnable {
        C05101() {
        }

        public void run() {
            MessageDialog.this.listener.onResult(MessageDialog.CANCEL);
        }
    }

    public static class Builder {
        private AlertDialog.Builder builder;
        private Boolean cancelable;
        private OnClickListener clickListener;
        private Listener listener;
        private Fragment resultFragment;
        private int resultId;

        class C05121 implements OnClickListener {

            class C05111 implements Runnable {
                final /* synthetic */ int val$which;

                C05111(int i) {
                    this.val$which = i;
                }

                public void run() {
                    if (Builder.this.listener != null) {
                        Builder.this.listener.onResult(this.val$which);
                    }
                    if (Builder.this.resultFragment != null) {
                        Builder.this.resultFragment.onActivityResult(Builder.this.resultId, this.val$which, null);
                    }
                }
            }

            C05121() {
            }

            public void onClick(DialogInterface dialog, int which) {
                new Handler().post(new C05111(which));
            }
        }

        public Builder(Context context) {
            this.listener = null;
            this.resultFragment = null;
            this.resultId = 0;
            this.builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppDialogTheme);
            this.clickListener = new C05121();
        }

        public Builder setTitle(@StringRes int id) {
            this.builder.setTitle(id);
            return this;
        }

        public Builder setTitle(CharSequence text) {
            this.builder.setTitle(text);
            return this;
        }

        public Builder setMessage(@StringRes int id) {
            this.builder.setMessage(id);
            this.builder.setMessage(id);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.builder.setMessage(message);
            return this;
        }

        public Builder setPositiveButton(@StringRes int id) {
            this.builder.setPositiveButton(id, this.clickListener);
            return this;
        }

        public Builder setPositiveButton(CharSequence text) {
            this.builder.setPositiveButton(text, this.clickListener);
            return this;
        }

        public Builder setNegativeButton(@StringRes int id) {
            this.builder.setNegativeButton(id, this.clickListener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text) {
            this.builder.setNegativeButton(text, this.clickListener);
            return this;
        }

        public Builder setListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder forResult(Fragment fragment, int requestId) {
            this.resultFragment = fragment;
            this.resultId = requestId;
            return this;
        }

        public MessageDialog create() {
            MessageDialog dialog = new MessageDialog();
            dialog.dialogBuilder = this.builder;
            dialog.listener = this.listener;
            if (this.cancelable != null) {
                dialog.setCancelable(this.cancelable.booleanValue());
            }
            return dialog;
        }

        public android.support.v7.app.AlertDialog.Builder getBuilder() {
            return this.builder;
        }

        public void show(FragmentManager fragmentManager) {
            create().show(fragmentManager);
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = Boolean.valueOf(cancelable);
            return this;
        }
    }

    public interface Listener {
        void onResult(int i);
    }

    protected android.support.v7.app.AlertDialog.Builder getDialogBuilder() {
        return this.dialogBuilder;
    }

    public Dialog onCreateAppDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = getDialogBuilder();
        if (builder != null) {
            return builder.create();
        }
        dismissAllowingStateLoss();
        return super.onCreateAppDialog(savedInstanceState);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.listener != null) {
            new Handler().post(new C05101());
        }
    }

    public static Builder build(Context context) {
        return new Builder(context);
    }

    public static MessageDialog create(Context context, CharSequence message, CharSequence buttonText) {
        return create(context, null, message, buttonText, null, null);
    }

    public static MessageDialog create(Context context, CharSequence message, CharSequence buttonText, Listener listener) {
        return create(context, null, message, buttonText, null, listener);
    }

    public static MessageDialog create(Context context, CharSequence message, CharSequence yesText, CharSequence noText, Listener listener) {
        return create(context, null, message, yesText, noText, listener);
    }

    public static MessageDialog create(Context context, CharSequence title, CharSequence message, CharSequence yesText, CharSequence noText, Listener listener) {
        Builder builder = new Builder(context);
        builder.setMessage(message).setPositiveButton(yesText).setListener(listener);
        if (title != null) {
            builder.setTitle(title);
        }
        if (noText != null) {
            builder.setNegativeButton(noText);
        }
        return builder.create();
    }

    public static MessageDialog create(Context context, @StringRes int message, @StringRes int buttonText) {
        return create(context, (int) YES, message, buttonText, YES, null);
    }

    public static MessageDialog create(Context context, @StringRes int message, @StringRes int buttonText, Listener listener) {
        return create(context, (int) YES, message, buttonText, YES, listener);
    }

    public static MessageDialog create(Context context, @StringRes int title, @StringRes int message, @StringRes int buttonText) {
        return create(context, title, message, buttonText, (int) YES, null);
    }

    public static MessageDialog create(Context context, @StringRes int message, @StringRes int yesText, @StringRes int noText, Listener listener) {
        return create(context, (int) YES, message, yesText, noText, listener);
    }

    public static MessageDialog create(Context context, @StringRes int title, @StringRes int message, @StringRes int yesText, @StringRes int noText, Listener listener) {
        Builder builder = new Builder(context);
        builder.setMessage(message).setPositiveButton(yesText).setListener(listener);
        if (title != YES) {
            builder.setTitle(title);
        }
        if (noText != YES) {
            builder.setNegativeButton(noText);
        }
        return builder.create();
    }

    public static MessageDialog showNoConnectionDialog(Context context, FragmentManager fragmentManager) {
        MessageDialog dialog = create(context, (int) R.string.no_internet_connection_title, (int) R.string.no_internet_connection_message, (int) R.string.action_ok, (int) YES, null);
        dialog.show(fragmentManager, null);
        return dialog;
    }

    public static MessageDialog showUnknownErrorDialog(Context context, FragmentManager fragmentManager) {
        MessageDialog dialog = create(context, (int) R.string.unknown_error_title, (int) R.string.unknown_error_message, (int) R.string.action_ok, (int) YES, null);
        dialog.show(fragmentManager, null);
        return dialog;
    }
}
