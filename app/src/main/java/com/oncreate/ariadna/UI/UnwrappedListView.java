package com.oncreate.ariadna.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class UnwrappedListView extends ListView {
    public UnwrappedListView(Context context) {
        super(context);
    }

    public UnwrappedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnwrappedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, LinearLayoutManager.INVALID_OFFSET));
        getLayoutParams().height = getMeasuredHeight();
    }
}
