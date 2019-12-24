package com.wy521angel.dragtest;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.customview.widget.ViewDragHelper;

public class VDHLayoutForTextView extends LinearLayout {

    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private View mEdgeForbiddenView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    public VDHLayoutForTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public VDHLayoutForTextView(Context context) {
        this(context, null);
    }

    public VDHLayoutForTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView || child == mEdgeForbiddenView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (child == mEdgeForbiddenView) {
                    final int topBound = getPaddingTop();
                    final int bottomBound = getHeight() - mDragView.getHeight() - topBound;
                    final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                    return newTop;
                }
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mEdgeForbiddenView) {
                    final int leftBound = getPaddingLeft();
                    final int rightBound = getWidth() - mDragView.getWidth() - leftBound;
                    final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                    return newLeft;
                }
                return left;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x,
                            mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            //在左边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                //setEdgeTrackingEnabled设置的边界滑动时触发
                //通过captureChildView对其进行捕获，该方法可以绕过tryCaptureView
                mViewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }


        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mEdgeForbiddenView = getChildAt(1);
        mAutoBackView = getChildAt(2);
        mEdgeTrackerView = getChildAt(3);
    }
}
