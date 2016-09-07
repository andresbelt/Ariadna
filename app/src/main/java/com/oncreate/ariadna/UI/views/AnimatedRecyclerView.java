package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.GridLayoutAnimationController.AnimationParameters;
import android.view.animation.LayoutAnimationController;

public class AnimatedRecyclerView extends RecyclerView {
    public AnimatedRecyclerView(Context context) {
        super(context);
    }

    public AnimatedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count) {
        if (getAdapter() != null && (getLayoutManager() instanceof GridLayoutManager)) {
            AnimationParameters animationParams = (AnimationParameters) params.layoutAnimationParameters;
            if (animationParams == null) {
                animationParams = new AnimationParameters();
                params.layoutAnimationParameters = animationParams;
            }
            int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            animationParams.count = count;
            animationParams.index = index;
            animationParams.columnsCount = columns;
            animationParams.rowsCount = count / columns;
            int invertedIndex = (count - 1) - index;
            animationParams.column = (columns - 1) - (invertedIndex % columns);
            animationParams.row = (animationParams.rowsCount - 1) - (invertedIndex / columns);
        } else if (getAdapter() == null || !(getLayoutManager() instanceof LinearLayoutManager)) {
            super.attachLayoutAnimationParameters(child, params, index, count);
        } else {
            LayoutAnimationController.AnimationParameters animationParams2 = params.layoutAnimationParameters;
            if (animationParams2 == null) {
                animationParams2 = new LayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animationParams2;
            }
            animationParams2.count = count;
            animationParams2.index = index;
        }
    }
}
