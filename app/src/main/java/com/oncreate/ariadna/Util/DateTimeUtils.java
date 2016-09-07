package com.oncreate.ariadna.Util;

import android.content.Context;
import android.content.res.Resources;

import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.R;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    private static final int SECONDS_IN_DAY = 86400;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;

    public static String getDurationString(int totalSeconds) {
        Context context = AriadnaApplication.getInstance();
        int hours = (totalSeconds % SECONDS_IN_DAY) / SECONDS_IN_HOUR;
        int minutes = (totalSeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        int seconds = totalSeconds % SECONDS_IN_MINUTE;
        if (totalSeconds / SECONDS_IN_HOUR == 0) {
            return String.format(context.getString(R.string.duration_format), new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format(context.getString(R.string.duration_format_hour), new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    public static String getDateString(Date date) {
        int differenceSeconds = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
        Resources resources = AriadnaApplication.getInstance().getResources();
        if (differenceSeconds < SECONDS_IN_DAY) {
            return getRelativeDateString(differenceSeconds);
        }
        Calendar.getInstance().setTime(date);
        return String.format(resources.getString(R.string.date_format_day), new Object[]{Integer.valueOf(Calendar.getInstance().get(2) + 1), Integer.valueOf(Calendar.getInstance().get(5)), Integer.valueOf(Calendar.getInstance().get(1))});
    }

    public static String getRelativeDateString(Date date, boolean ignoreNegatives) {
        int differenceSeconds = (int) ((System.currentTimeMillis() - date.getTime()) / 1000);
        if (ignoreNegatives && differenceSeconds < 0) {
            differenceSeconds = -differenceSeconds;
        }
        return getRelativeDateString(differenceSeconds);
    }

    private static String getRelativeDateString(int differenceSeconds) {
        Resources resources = AriadnaApplication.getInstance().getResources();
        if (differenceSeconds < SECONDS_IN_MINUTE) {
            return resources.getString(R.string.date_format_now);
        }
        if (differenceSeconds < SECONDS_IN_HOUR) {
            return resources.getQuantityString(R.plurals.date_format_relative_minutes, differenceSeconds / SECONDS_IN_MINUTE, new Object[]{Integer.valueOf(differenceSeconds / SECONDS_IN_MINUTE)});
        } else if (differenceSeconds < SECONDS_IN_DAY) {
            return resources.getQuantityString(R.plurals.date_format_relative_hours, differenceSeconds / SECONDS_IN_HOUR, new Object[]{Integer.valueOf(differenceSeconds / SECONDS_IN_HOUR)});
        } else {
            return resources.getQuantityString(R.plurals.date_format_relative_days, differenceSeconds / SECONDS_IN_DAY, new Object[]{Integer.valueOf(differenceSeconds / SECONDS_IN_DAY)});
        }
    }
}
