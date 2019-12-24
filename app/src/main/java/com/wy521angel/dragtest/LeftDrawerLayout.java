package com.wy521angel.dragtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.customview.widget.ViewDragHelper;

public class LeftDrawerLayout extends ViewGroup {

    private static final int MIN_DRAWER_MARGIN = 64; // dp

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;

    private View mLeftMenuView;
    private View mContentView;

    private ViewDragHelper mHelper;

    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScrren;


    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setup drawer's minMargin
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //捕获该view
                return child == mLeftMenuView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(-child.getWidth(), Math.min(left, 0));
                //始终都是取left的值，初始值为-child.getWidth()，当向右拖动的时候left值增大，当left大于0的时候取0
                return newLeft;
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mHelper.captureChildView(mLeftMenuView, pointerId);
            }

            //手指释放的时候回调，计算当前显示的百分比，以及加速度来决定是否显示drawer
            //这里注意一点xvel的值只有大于我们设置的minVelocity才会出现大于0，如果小于我们设置的值则一直是0。
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                final int childWidth = releasedChild.getWidth();
                //0~1f
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
                //由于offset 取值为0~1，所以settleCapturedViewAt初始值为 -childWidth，滑动小于0.5取值也为-childWidth，
                //大于0.5取值为0
                invalidate();
            }

            //整个pos变化的过程中，计算offset保存，这里可以使用接口将offset回调出去，方便做动画。
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = changedView.getWidth();
                float offset = (float)(childWidth + left) / childWidth;
                mLeftMenuOnScrren = offset;
                //offset can callback here
                changedView.setVisibility(offset == 0 ? INVISIBLE : VISIBLE);
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                //始终取值为child.getWidth()
                return mLeftMenuView == child ? child.getWidth() : 0;
            }
        });
        //设置edge_left track
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置minVelocity
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
        View leftMenuView = getChildAt(1);
        MarginLayoutParams layoutParams = (MarginLayoutParams) leftMenuView.getLayoutParams();
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);

        View contentView = getChildAt(0);
        layoutParams = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - layoutParams.leftMargin - layoutParams.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - layoutParams.topMargin - layoutParams.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;
    }

    //onLayout将menu设置到屏幕的左侧以至于不可见
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScrren);
        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
                lp.topMargin + menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 0.f;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
    }

    public void openDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 1.0f;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}


















