package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.oncreate.ariadna.BuildConfig;
import com.oncreate.ariadna.Util.ConstantVariables;

public class PrefixedEditText extends AppCompatEditText {
    private String fitText;
    private float fitWidth;
    private boolean invalidateLayout;
    private boolean isLayedOut;
    private String postfix;
    private int postfixWidth;
    private String prefix;
    private TextPaint prefixPaint;
    private int prefixWidth;
    private float selectedWidth;
    private float textWidth;

    public PrefixedEditText(Context context) {
        super(context);
        this.prefixWidth = 0;
        this.postfixWidth = 0;
        this.fitWidth = 0.0f;
        this.textWidth = 0.0f;
        this.selectedWidth = 0.0f;
        this.invalidateLayout = true;
        this.isLayedOut = false;
    }

    public PrefixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.prefixWidth = 0;
        this.postfixWidth = 0;
        this.fitWidth = 0.0f;
        this.textWidth = 0.0f;
        this.selectedWidth = 0.0f;
        this.invalidateLayout = true;
        this.isLayedOut = false;
    }

    public PrefixedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.prefixWidth = 0;
        this.postfixWidth = 0;
        this.fitWidth = 0.0f;
        this.textWidth = 0.0f;
        this.selectedWidth = 0.0f;
        this.invalidateLayout = true;
        this.isLayedOut = false;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if (this.isLayedOut) {
            invalidateSelf();
        }
    }

    public String getPostfix() {
        return this.postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
        if (this.isLayedOut) {
            invalidateSelf();
        }
    }

    public String getFitText() {
        return this.fitText;
    }

    public void setFitText(String fitText) {
        this.fitText = fitText;
        setSingleLine();
        setFilters(new InputFilter[]{new LengthFilter(fitText.length())});
        if (this.isLayedOut) {
            invalidateSelf();
        }
    }

    private void invalidateSelf() {
        this.invalidateLayout = true;
        super.invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.invalidateLayout) {
            this.prefixWidth = (int) Math.ceil((double) getPaint().measureText(getMeasureString(this.prefix)));
            this.postfixWidth = (int) Math.ceil((double) getPaint().measureText(getMeasureString(this.postfix)));
            if (this.fitText == null) {
                getLayoutParams().width = -2;
            } else {
                this.fitWidth = getPaint().measureText(this.fitText);
                this.selectedWidth = Math.max(this.fitWidth, this.textWidth);
                getLayoutParams().width = (int) Math.ceil((double) ((((float) getTotalPaddingLeft()) + this.selectedWidth) + ((float) getTotalPaddingRight())));
            }
            this.invalidateLayout = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.isLayedOut = true;
    }

    public void requestLayout() {
        if (!this.isLayedOut || this.invalidateLayout) {
            super.requestLayout();
        }
    }

    private String getMeasureString(String string) {
        if (string == null) {
            string = ConstantVariables.VERSION_NAME;
        }
        if (this.fitText != null && this.fitText.length() == 1 && string.length() == 0) {
            return "|";
        }
        return string;
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textWidth = getPaint().measureText(text.toString());
        if (Math.max(this.textWidth, this.fitWidth) != this.selectedWidth) {
            this.invalidateLayout = true;
            requestLayout();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.prefixPaint == null) {
            this.prefixPaint = new TextPaint(getPaint());
            this.prefixPaint.setAlpha(120);
        }
        if (this.prefix != null) {
            canvas.drawText(this.prefix, (float) super.getCompoundPaddingLeft(), (float) getBaseline(), this.prefixPaint);
        }
        if (this.postfix != null) {
            canvas.drawText(this.postfix, (float) ((super.getMeasuredWidth() - super.getCompoundPaddingRight()) - this.postfixWidth), (float) getBaseline(), this.prefixPaint);
        }
    }

    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + this.prefixWidth;
    }

    public int getCompoundPaddingRight() {
        return super.getCompoundPaddingRight() + this.postfixWidth;
    }
}
