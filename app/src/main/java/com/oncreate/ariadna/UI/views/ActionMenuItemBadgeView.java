package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.oncreate.ariadna.R;

public class ActionMenuItemBadgeView extends AppCompatTextView implements OnLongClickListener {
    private static final int MAX_ICON_SIZE = 32;
    private TextView badgeView;
    private int count;
    private Drawable mIcon;
    private MenuItem mItemData;
    private int mMaxIconSize;
    private int mMinWidth;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;
    private int wrapContent;

    public ActionMenuItemBadgeView(Context context) {
        this(context, null);
    }

    public ActionMenuItemBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMenuItemBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.count = 0;
        float density = context.getResources().getDisplayMetrics().density;
        this.mMaxIconSize = (int) ((32.0f * density) + 0.5f);
        setOnLongClickListener(this);
        this.mSavedPaddingLeft = -1;
        this.badgeView = (TextView) LayoutInflater.from(context).inflate(R.layout.view_toolbar_badge, null, false);
        this.wrapContent = MeasureSpec.makeMeasureSpec(0, 0);
        this.mMinWidth = (int) ((48.0f * density) + 0.5f);
        if (VERSION.SDK_INT >= 21) {
            setBackgroundResource(R.drawable.action_view_background);
        }
    }

    public void setPadding(int l, int t, int r, int b) {
        this.mSavedPaddingLeft = l;
        super.setPadding(l, t, r, b);
    }

    public void initialize(MenuItem itemData) {
        this.mItemData = itemData;
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitle());
        setId(itemData.getItemId());
        setVisibility(itemData.isVisible() ? 0 : 8);
        setEnabled(itemData.isEnabled());
    }

    private void updateTextButtonVisibility() {
        boolean visible;
        boolean i = true;
        if (TextUtils.isEmpty(this.mTitle)) {
            visible = false;
        } else {
            visible = true;
        }
        if (this.mIcon != null) {
            i = false;
        }
        setText(visible & i ? this.mTitle : null);
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (icon != null) {
            float scale;
            int width = icon.getIntrinsicWidth();
            int height = icon.getIntrinsicHeight();
            if (width > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) width);
                width = this.mMaxIconSize;
                height = (int) (((float) height) * scale);
            }
            if (height > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) height);
                height = this.mMaxIconSize;
                width = (int) (((float) width) * scale);
            }
            icon.setBounds(0, 0, width, height);
        }
        setCompoundDrawables(icon, null, null, null);
        updateTextButtonVisibility();
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(getText());
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        setContentDescription(this.mTitle);
        updateTextButtonVisibility();
    }

    public boolean onLongClick(View v) {
        if (hasText()) {
            return false;
        }
        int[] screenPos = new int[2];
        Rect displayFrame = new Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);
        Context context = getContext();
        int width = getWidth();
        int height = getHeight();
        int midy = screenPos[1] + (height / 2);
        int referenceX = screenPos[0] + (width / 2);
        if (ViewCompat.getLayoutDirection(v) == 0) {
            referenceX = context.getResources().getDisplayMetrics().widthPixels - referenceX;
        }
        Toast cheatSheet = Toast.makeText(context, this.mItemData.getTitle(), 0);
        if (midy < displayFrame.height()) {
            cheatSheet.setGravity(8388661, referenceX, (screenPos[1] + height) - displayFrame.top);
        } else {
            cheatSheet.setGravity(81, 0, height);
        }
        cheatSheet.show();
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean textVisible = hasText();
        if (textVisible && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int oldMeasuredWidth = getMeasuredWidth();
        int targetWidth = widthMode == LinearLayoutManager.INVALID_OFFSET ? Math.min(widthSize, this.mMinWidth) : this.mMinWidth;
        if (widthMode != 1073741824 && this.mMinWidth > 0 && oldMeasuredWidth < targetWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(targetWidth, 1073741824), heightMeasureSpec);
        }
        if (!textVisible && this.mIcon != null) {
            super.setPadding((getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    }

    private void prepareView() {
        this.badgeView.setText(Integer.toString(this.count));
        this.badgeView.measure(this.wrapContent, this.wrapContent);
        this.badgeView.layout(0, 0, this.badgeView.getMeasuredWidth(), this.badgeView.getMeasuredHeight());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.count > 0) {
            canvas.save();
            canvas.translate((((float) canvas.getWidth()) * 0.9f) - ((float) this.badgeView.getMeasuredWidth()), ((float) canvas.getHeight()) * 0.1f);
            this.badgeView.draw(canvas);
            canvas.restore();
        }
    }

    public void setCount(int count) {
        this.count = count;
        if (count > 0) {
            prepareView();
        }
        invalidate();
    }
}
