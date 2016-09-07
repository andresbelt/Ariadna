package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;

public class PlaceholderTextView extends AppCompatTextView {
    private int dh;
    private int ds;
    private int dw;
    private String placeholder;
    private Paint underlinePaint;

    public PlaceholderTextView(Context context) {
        super(context);
        this.dw = 0;
        this.dh = 0;
        this.ds = 2;
        setSingleLine();
        this.underlinePaint = new Paint();
        this.underlinePaint.setStrokeWidth((float) ((this.ds * 2) + 1));
        this.underlinePaint.setColor(getPaint().getColor());
    }

    public void setTextColor(int color) {
        super.setTextColor(color);
        this.underlinePaint.setColor(color);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.placeholder != null) {
            setMinimumWidth((int) Math.ceil((double) getPaint().measureText(this.placeholder)));
        } else {
            setMinimumWidth(0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.dw = w - (this.ds * 2);
        this.dh = h - (this.ds * 2);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int start = (int) (((float) this.dh) * 0.8f);
        canvas.drawLines(new float[]{(float) this.ds, (float) start, (float) this.ds, (float) this.dh, (float) this.ds, (float) this.dh, (float) this.dw, (float) this.dh, (float) this.dw, (float) this.dh, (float) this.dw, (float) start}, this.underlinePaint);
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        invalidate();
    }

    public int getWidth(String content) {
        return (getTotalPaddingLeft() + ((int) Math.ceil((double) getPaint().measureText(content)))) + getTotalPaddingRight();
    }
}
