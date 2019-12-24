package com.wy521angel.dragtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DragListenerGridView extends ViewGroup {

    private static final int COLUMNS = 2;
    private static final int ROWS = 3;

    ViewConfiguration viewConfiguration;
    OnDragListener dragListener = new ViewDragListener();
    View draggedView;
    List<View> orderedChildren = new ArrayList<>();

    public DragListenerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewConfiguration = ViewConfiguration.get(context);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            orderedChildren.add(child);
            child.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    draggedView = v;
                    //此处第三个参数将View自己传入，后面使用
                    v.startDrag(null, new DragShadowBuilder(v), v, 0);
                    return false;
                }
            });
            child.setOnDragListener(dragListener);

        }
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return super.onDragEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = specWidth / COLUMNS;
        int childHeight = specHeight / ROWS;

        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(specWidth, specHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int childLeft;
        int childTop;
        int childWidth = getWidth() / COLUMNS;
        int childHeight = getHeight() / ROWS;
        //都摆在同一个位置（0，0）然后做偏移铺开，
        // 此处为了动画的排序简单使用，正式开放不可以这样写，如果再增加动画可能会有问题
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            childLeft = i % 2 * childWidth;
            childTop = i / 2 * childHeight;
            child.layout(0, 0, childWidth, childHeight);
            child.setTranslationX(childLeft);
            child.setTranslationY(childTop);
        }

    }

    private class ViewDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //开始拖拽，并不是只有拖拽的View会收到，而是每一个View都会收到
                    if (event.getLocalState() == v) {
                        //如果拖拽起来的View是自己本身，则将原位置的View隐藏（手中拖拽的View会自动成半透明效果）
                        v.setVisibility(INVISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //手指拖拽起View的移动过程中，触摸到了某个View的区域内
                    if (event.getLocalState() != v) {
                        //当前拖拽起View的手指移动到了别的View区域内，排序
                        sort(v);
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getLocalState() == v)
                        v.setVisibility(VISIBLE);
                    break;
            }
            return true;
        }
    }

    private void sort(View targetView) {
        int draggedIndex = -1;
        int targetIndex = -1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = orderedChildren.get(i);
            if (targetView == child) {
                targetIndex = i;
            } else if (draggedView == child) {
                draggedIndex = i;
            }
        }
        if (targetIndex < draggedIndex) {
            orderedChildren.remove(draggedIndex);
            orderedChildren.add(targetIndex, draggedView);
        } else if (targetIndex > draggedIndex) {
            orderedChildren.remove(draggedIndex);
            orderedChildren.add(targetIndex, draggedView);
        }
        int childLeft;
        int childTop;
        int childWidth = getWidth() / COLUMNS;
        int childHeight = getHeight() / ROWS;
        for (int index = 0; index < getChildCount(); index++) {
            View child = orderedChildren.get(index);
            childLeft = index % 2 * childWidth;
            childTop = index / 2 * childHeight;
            child.animate()
                    .translationX(childLeft)
                    .translationY(childTop)
                    .setDuration(150);
        }
    }
}
