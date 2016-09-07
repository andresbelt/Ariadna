package com.oncreate.ariadna.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.oncreate.ariadna.R;

public class CheckableImageView extends ImageView implements Checkable {
    private static final int[] CheckedStateSet;
    private Drawable foregroundDrawable;
    private boolean isChecked;

    static {
        CheckedStateSet = new int[]{16842912};
    }

    public CheckableImageView(Context context) {
        super(context);
        this.isChecked = false;
        init();
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isChecked = false;
        init();
    }

    private void init() {
        this.foregroundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.checkable_image_foreground, getContext().getTheme());
        if (this.foregroundDrawable != null) {
            this.foregroundDrawable.setCallback(this);
        }
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean b) {
        if (b != this.isChecked) {
            this.isChecked = b;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!this.isChecked);
    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.foregroundDrawable.setState(getDrawableState());
        invalidate();
    }

    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        this.foregroundDrawable.setBounds(pL, pT, width - pL, height - pT);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.foregroundDrawable.draw(canvas);
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.foregroundDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.foregroundDrawable.jumpToCurrentState();
    }

    @TargetApi(21)
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.foregroundDrawable != null) {
            this.foregroundDrawable.setHotspot(x, y);
        }
    }
}
