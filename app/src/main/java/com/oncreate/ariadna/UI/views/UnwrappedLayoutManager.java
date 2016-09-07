package com.oncreate.ariadna.UI.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import java.lang.reflect.Field;

public class UnwrappedLayoutManager extends LinearLayoutManager {
    private static final int CHILD_HEIGHT = 1;
    private static final int CHILD_WIDTH = 0;
    private static final int DEFAULT_CHILD_SIZE = 100;
    private static boolean canMakeInsetsDirty;
    private static Field insetsDirtyField;
    boolean allowMeasure;
    int cachedHeight;
    int cachedWidth;
    private final int[] childDimensions;
    int measure;
    private final Rect tmpRect;

    public UnwrappedLayoutManager(Context context) {
        super(context);
        this.childDimensions = new int[2];
        this.tmpRect = new Rect();
        this.measure = CHILD_WIDTH;
        this.allowMeasure = true;
        this.cachedWidth = CHILD_WIDTH;
        this.cachedHeight = CHILD_WIDTH;
    }

    public UnwrappedLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.childDimensions = new int[2];
        this.tmpRect = new Rect();
        this.measure = CHILD_WIDTH;
        this.allowMeasure = true;
        this.cachedWidth = CHILD_WIDTH;
        this.cachedHeight = CHILD_WIDTH;
    }

    public UnwrappedLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.childDimensions = new int[2];
        this.tmpRect = new Rect();
        this.measure = CHILD_WIDTH;
        this.allowMeasure = true;
        this.cachedWidth = CHILD_WIDTH;
        this.cachedHeight = CHILD_WIDTH;
    }

    public boolean canScrollVertically() {
        return false;
    }

    public boolean canScrollHorizontally() {
        return false;
    }

    static {
        canMakeInsetsDirty = true;
        insetsDirtyField = null;
    }

    public static int makeUnspecifiedSpec() {
        return MeasureSpec.makeMeasureSpec(CHILD_WIDTH, CHILD_WIDTH);
    }

    public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
        if (state.isPreLayout()) {
            this.allowMeasure = true;
        }
        StringBuilder append = new StringBuilder().append("onMeasure: ");
        int i = this.measure;
        this.measure = i + CHILD_HEIGHT;
        Log.i("ULM", append.append(i).append(" IPL: ").append(state.isPreLayout()).append(" C: ").append(state.getItemCount()).append(" DSC: ").append(state.didStructureChange()).toString());
        if (this.allowMeasure) {
            int widthMode = MeasureSpec.getMode(widthSpec);
            int heightMode = MeasureSpec.getMode(heightSpec);
            int widthSize = MeasureSpec.getSize(widthSpec);
            int heightSize = MeasureSpec.getSize(heightSpec);
            boolean hasWidthSize = widthMode != 0;
            boolean hasHeightSize = heightMode != 0;
            boolean exactWidth = widthMode == 1073741824;
            boolean exactHeight = heightMode == 1073741824;
            int unspecified = makeUnspecifiedSpec();
            if (exactWidth && exactHeight) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
                return;
            }
            boolean vertical = getOrientation() == CHILD_HEIGHT;
            initChildDimensions(widthSize, heightSize, vertical);
            int width = CHILD_WIDTH;
            int height = CHILD_WIDTH;
            recycler.clear();
            int stateItemCount = state.getItemCount();
            int adapterItemCount = getItemCount();
            for (int i2 = CHILD_WIDTH; i2 < adapterItemCount; i2 += CHILD_HEIGHT) {
                if (!vertical) {
                    if (i2 < stateItemCount) {
                        measureChild(recycler, i2, unspecified, heightSize, this.childDimensions);
                    }
                    width += this.childDimensions[CHILD_WIDTH];
                    if (i2 == 0) {
                        height = this.childDimensions[CHILD_HEIGHT];
                    }
                    if (hasWidthSize && width >= widthSize) {
                        break;
                    }
                }
                if (i2 < stateItemCount) {
                    measureChild(recycler, i2, widthSize, unspecified, this.childDimensions);
                }
                height += this.childDimensions[CHILD_HEIGHT];
                if (i2 == 0) {
                    width = this.childDimensions[CHILD_WIDTH];
                }
                if (hasHeightSize && height >= heightSize) {
                    break;
                }
            }
            if (exactWidth) {
                width = widthSize;
            } else {
                width += getPaddingLeft() + getPaddingRight();
                if (hasWidthSize) {
                    width = Math.min(width, widthSize);
                }
            }
            if (exactHeight) {
                height = heightSize;
            } else {
                height += getPaddingTop() + getPaddingBottom();
                if (hasHeightSize) {
                    height = Math.min(height, heightSize);
                }
            }
            setMeasuredDimension(width, height);
            this.cachedWidth = width;
            this.cachedHeight = height;
            if (!state.isPreLayout()) {
                this.allowMeasure = false;
                return;
            }
            return;
        }
        setMeasuredDimension(this.cachedWidth, this.cachedHeight);
    }

    private void initChildDimensions(int width, int height, boolean vertical) {
        if (this.childDimensions[CHILD_WIDTH] != 0 || this.childDimensions[CHILD_HEIGHT] != 0) {
            return;
        }
        if (vertical) {
            this.childDimensions[CHILD_WIDTH] = width;
            this.childDimensions[CHILD_HEIGHT] = DEFAULT_CHILD_SIZE;
            return;
        }
        this.childDimensions[CHILD_WIDTH] = DEFAULT_CHILD_SIZE;
        this.childDimensions[CHILD_HEIGHT] = height;
    }

    public void setOrientation(int orientation) {
        if (!(this.childDimensions == null || getOrientation() == orientation)) {
            this.childDimensions[CHILD_WIDTH] = CHILD_WIDTH;
            this.childDimensions[CHILD_HEIGHT] = CHILD_WIDTH;
        }
        super.setOrientation(orientation);
    }

    private void measureChild(Recycler recycler, int position, int widthSize, int heightSize, int[] dimensions) {
        try {
            View child = recycler.getViewForPosition(position);
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            int hPadding = getPaddingLeft() + getPaddingRight();
            int vPadding = getPaddingTop() + getPaddingBottom();
            int hMargin = p.leftMargin + p.rightMargin;
            int vMargin = p.topMargin + p.bottomMargin;
            makeInsetsDirty(p);
            calculateItemDecorationsForChild(child, this.tmpRect);
            child.measure(LayoutManager.getChildMeasureSpec(widthSize, (hPadding + hMargin) + (getRightDecorationWidth(child) + getLeftDecorationWidth(child)), p.width, getOrientation() == 0), LayoutManager.getChildMeasureSpec(heightSize, (vPadding + vMargin) + (getTopDecorationHeight(child) + getBottomDecorationHeight(child)), p.height, getOrientation() == CHILD_HEIGHT));
            dimensions[CHILD_WIDTH] = (getDecoratedMeasuredWidth(child) + p.leftMargin) + p.rightMargin;
            dimensions[CHILD_HEIGHT] = (getDecoratedMeasuredHeight(child) + p.bottomMargin) + p.topMargin;
            makeInsetsDirty(p);
            recycler.recycleView(child);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    private static void makeInsetsDirty(LayoutParams p) {
        if (canMakeInsetsDirty) {
            try {
                if (insetsDirtyField == null) {
                    insetsDirtyField = LayoutParams.class.getDeclaredField("mInsetsDirty");
                    insetsDirtyField.setAccessible(true);
                }
                insetsDirtyField.set(p, Boolean.valueOf(true));
            } catch (NoSuchFieldException e) {
                canMakeInsetsDirty = false;
            } catch (IllegalAccessException e2) {
                canMakeInsetsDirty = false;
            }
        }
    }
}
