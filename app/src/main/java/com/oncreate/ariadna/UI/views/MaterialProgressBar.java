package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.oncreate.ariadna.R;


public class MaterialProgressBar extends ImageView {
    private static final int DEFAULT_CIRCLE_DIAMETER = 56;
    private static final int STROKE_WIDTH_LARGE = 5;
    private int mArrowHeight;
    private int mArrowWidth;
    private int[] mColors;
    private int mInnerRadius;
    private AnimationListener mListener;
    private int mMax;
    private int mProgress;
    private MaterialProgressDrawable mProgressDrawable;
    private int mProgressStokeWidth;
    private boolean mShowArrow;

    public MaterialProgressBar(Context context) {
        super(context);
        this.mColors = new int[]{ViewCompat.MEASURED_STATE_MASK};
        init(context, null, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mColors = new int[]{ViewCompat.MEASURED_STATE_MASK};
        init(context, attrs, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mColors = new int[]{ViewCompat.MEASURED_STATE_MASK};
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        float density = getContext().getResources().getDisplayMetrics().density;
        this.mInnerRadius = -1;
        this.mProgressStokeWidth = (int) (5.0f * density);
        this.mArrowWidth = -1;
        this.mArrowHeight = -1;
        this.mShowArrow = false;
        this.mProgress = 0;
        this.mMax = 100;
        this.mColors = new int[]{context.getResources().getColor(R.color.app_primary_color), context.getResources().getColor(R.color.app_primary_color_700)};
        this.mProgressDrawable = new MaterialProgressDrawable(getContext(), this);
        super.setImageDrawable(this.mProgressDrawable);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float density = getContext().getResources().getDisplayMetrics().density;
        int mDiameter = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if (mDiameter <= 0) {
            mDiameter = ((int) density) * DEFAULT_CIRCLE_DIAMETER;
        }
        this.mProgressDrawable.setColorSchemeColors(this.mColors);
        this.mProgressDrawable.setSizeParameters((double) mDiameter, (double) mDiameter, this.mInnerRadius <= 0 ? (double) ((mDiameter - (this.mProgressStokeWidth * 2)) / 2) : (double) this.mInnerRadius, (double) this.mProgressStokeWidth, this.mArrowWidth < 0 ? (float) (this.mProgressStokeWidth * 4) : (float) this.mArrowWidth, this.mArrowHeight < 0 ? (float) (this.mProgressStokeWidth * 2) : (float) this.mArrowHeight);
        if (isShowArrow()) {
            this.mProgressDrawable.setArrowScale(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            this.mProgressDrawable.showArrow(true);
        }
        super.setImageDrawable(null);
        super.setImageDrawable(this.mProgressDrawable);
        this.mProgressDrawable.setAlpha(MotionEventCompat.ACTION_MASK);
        this.mProgressDrawable.start();
    }

    public final void setImageResource(int resId) {
    }

    public boolean isShowArrow() {
        return this.mShowArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.mShowArrow = showArrow;
    }

    public final void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }

    public final void setImageDrawable(Drawable drawable) {
    }

    public void setAnimationListener(AnimationListener listener) {
        this.mListener = listener;
    }

    public void onAnimationStart() {
        super.onAnimationStart();
        if (this.mListener != null) {
            this.mListener.onAnimationStart(getAnimation());
        }
    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.mListener != null) {
            this.mListener.onAnimationEnd(getAnimation());
        }
    }

    public void setColorSchemeResources(int... colorResIds) {
        Resources res = getResources();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = res.getColor(colorResIds[i]);
        }
        setColorSchemeColors(colorRes);
    }

    public void setColorSchemeColors(int... colors) {
        this.mColors = colors;
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setColorSchemeColors(colors);
        }
    }

    public void setBackgroundColor(@ColorRes int colorRes) {
        if (getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) getBackground()).getPaint().setColor(getResources().getColor(colorRes));
        }
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int progress) {
        if (getMax() > 0) {
            this.mProgress = progress;
        }
    }

    public int getVisibility() {
        return super.getVisibility();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mProgressDrawable != null) {
            boolean z;
            if (visibility != 0) {
                this.mProgressDrawable.stop();
            }
            MaterialProgressDrawable materialProgressDrawable = this.mProgressDrawable;
            if (visibility == 0) {
                z = true;
            } else {
                z = false;
            }
            materialProgressDrawable.setVisible(z, false);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mProgressDrawable != null) {
            boolean z;
            this.mProgressDrawable.stop();
            MaterialProgressDrawable materialProgressDrawable = this.mProgressDrawable;
            if (getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            materialProgressDrawable.setVisible(z, false);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.stop();
            this.mProgressDrawable.setVisible(false, false);
        }
    }
}
