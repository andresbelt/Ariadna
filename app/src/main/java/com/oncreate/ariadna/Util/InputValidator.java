package com.oncreate.ariadna.Util;

import android.content.Context;

import com.oncreate.ariadna.R;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
    private Context context;
    private boolean hideErrorMessages;

    public InputValidator(Context context) {
        this.context = context;
    }

    public String validateName(String name, boolean required) {
        if (name.trim().length() == 0) {
            if (required) {
                return this.hideErrorMessages ? " " : this.context.getString(R.string.error_name_required);
            } else {
                return null;
            }
        } else if (name.trim().length() < 3) {
            return this.hideErrorMessages ? " " : this.context.getString(R.string.error_name_invalid);
        } else {
            return null;
        }
    }

    public String validateEmail(String email, boolean required) {
        if (email.trim().length() == 0) {
            if (required) {
                return this.hideErrorMessages ? " " : this.context.getString(R.string.error_email_required);
            } else {
                return null;
            }
        } else if (Pattern.compile(EMAIL_PATTERN).matcher(email).find()) {
            return null;
        } else {
            return this.hideErrorMessages ? " " : this.context.getString(R.string.error_email_invalid);
        }
    }


    public void setHideErrorMessages(boolean hide) {
        this.hideErrorMessages = hide;
    }
}
