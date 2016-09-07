package com.oncreate.ariadna.UI;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.Checkable;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.common.ConnectionResult;
import com.oncreate.ariadna.Dialog.LoadingDialog;
import com.oncreate.ariadna.R;

public class StrikeTextView extends AppCompatTextView implements Checkable {
    private RectF drawRect;
    private RectF drawRectClipped;
    private boolean isChecked;
    private int measuredWidth;
    private OnCheckedChangeListener onCheckedChangeListener;
    private float percent;
    private Bitmap strikeBitmap;
    private float strikeHeight;
    private float strikePercentScale;
    private float strikePercentShift;
    private Rect strikeRect;
    private Rect strikeRectClipped;


    class C06581 implements AnimatorUpdateListener {
        final /* synthetic */ ValueAnimator val$animator;

        C06581(ValueAnimator valueAnimator) {
            this.val$animator = valueAnimator;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            StrikeTextView.this.percent = ((Float) this.val$animator.getAnimatedValue()).floatValue();
            StrikeTextView.this.invalidate();
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(StrikeTextView strikeTextView, boolean z);
    }

    public StrikeTextView(Context context) {
        this(context, null);
    }

    public StrikeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrikeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.strikeRect = new Rect();
        this.drawRect = new RectF();
        this.strikeRectClipped = new Rect();
        this.drawRectClipped = new RectF();
        this.percent = 0.0f;
        this.isChecked = false;
        this.strikeBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.strike);
        this.strikeRect.right = this.strikeBitmap.getWidth();
        this.strikeRect.bottom = this.strikeBitmap.getHeight();
        this.strikeHeight = context.getResources().getDimension(R.dimen.quiz_strike_line_height);
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (this.strikeBitmap != null) {
            this.drawRect.top = Math.max(0.0f, (((float) measuredHeight) - this.strikeHeight) / 2.0f);
            this.drawRect.bottom = Math.min((float) measuredHeight, this.strikeHeight + this.drawRect.top);
            float drawWidth = (this.drawRect.height() * ((float) this.strikeRect.width())) / ((float) this.strikeRect.height());
            this.drawRect.left = Math.max(0.0f, (((float) this.measuredWidth) - drawWidth) / 2.0f);
            this.drawRect.right = Math.min((float) this.measuredWidth, this.drawRect.left + drawWidth);
            this.strikePercentScale = ((float) this.measuredWidth) / this.drawRect.width();
            this.strikeRectClipped.left = this.strikeRect.left;
            this.strikeRectClipped.top = this.strikeRect.top;
            this.strikeRectClipped.bottom = this.strikeRect.bottom;
            this.drawRectClipped.left = this.drawRect.left;
            this.drawRectClipped.top = this.drawRect.top;
            this.drawRectClipped.bottom = this.drawRect.bottom;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float drawPercent = this.percent;
        if (this.strikeBitmap != null && drawPercent > 0.0f) {
            this.strikeRectClipped.right = (int) (((float) this.strikeRect.left) + (((float) this.strikeRect.width()) * drawPercent));
            this.drawRectClipped.right = this.drawRect.left + (this.drawRect.width() * drawPercent);
            canvas.drawBitmap(this.strikeBitmap, this.strikeRectClipped, this.drawRectClipped, getPaint());
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z = false;
        switch (event.getAction()) {
            case 0:
                float touchPercent = event.getX() / ((float) this.measuredWidth);
                if (this.isChecked && ((double) touchPercent) < 0.55d) {
                    return false;
                }
                if (this.isChecked || ((double) touchPercent) <= 0.45d) {
                    this.percent = touchPercent;
                    invalidate();
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                }
                return false;

            case HomeActivity.OFFSET_TOOLBAR /*1*/:
            case LoadingDialog.HORIZONTAL_INDETERMINATE /*3*/:
            case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
            case ConnectionResult.NETWORK_ERROR /*7*/:
            case ConnectionResult.DEVELOPER_ERROR /*10*/:
                if ((!this.isChecked && ((double) this.percent) > 0.4d) || (this.isChecked && ((double) this.percent) >= 0.6d)) {
                    z = true;
                }
                setCheckedInternal(z);
                animateTo(this.isChecked ? DefaultRetryPolicy.DEFAULT_BACKOFF_MULT : 0.0f);
                break;
            case HomeActivity.OFFSET_TABS /*2*/:
                this.percent = event.getX() / ((float) this.measuredWidth);
                if (this.percent < 0.0f) {
                    this.percent = 0.0f;
                }
                if (this.percent > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                    this.percent = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
                }
                invalidate();
                break;
        }
        return true;
    }

    private void animateTo(float toPercent) {
        float scale = Math.abs(this.percent - toPercent);
        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(new float[]{this.percent, toPercent});
        animator.setDuration((long) ((int) (700.0f * scale)));
        animator.addUpdateListener(new C06581(animator));
        animator.start();
    }

    private void setCheckedInternal(boolean isChecked) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked;
            if (this.onCheckedChangeListener != null) {
                this.onCheckedChangeListener.onCheckedChanged(this, isChecked);
            }
        }
    }

    public void setChecked(boolean isChecked) {
        setCheckedInternal(isChecked);
        animateTo(isChecked ? DefaultRetryPolicy.DEFAULT_BACKOFF_MULT : 0.0f);
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void toggle() {
        setChecked(!this.isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}
