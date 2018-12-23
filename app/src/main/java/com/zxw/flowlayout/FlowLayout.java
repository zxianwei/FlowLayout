package com.zxw.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * FlowLayout.class
 *
 * @author zxw
 * @date 2018/12/20
 */
public class FlowLayout extends ViewGroup {
    /**
     * 用来保存每行view的列表
     */
    private List<List<View>> mViewLineList = new ArrayList<>();
    /**
     * 保存每行view的行高
     */
    private List<Integer> mLineHeightList = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 需要一些marglin属性需要重写
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewLineList.clear();
        mLineHeightList.clear();

        //获取父容器为FlowLayout设置的测量模式和大小
        int iWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int iHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int iWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int iHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        int iCurLineW = 0;
        int iCurLineH = 0;
        //保存view
        if (iWidthMode == MeasureSpec.EXACTLY && iHeightMode == MeasureSpec.EXACTLY) {
            measureWidth = iWidthSize;
            measureHeight = iHeightSize;
        } else {
            int childCount = getChildCount();
            int childWidth = 0;
            int childHeight = 0;
            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                if (iCurLineW + childWidth > iWidthSize) {
                    //记录当前行的信息

                    //1. 记录当前行的最大宽度，高度累计添加
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;
                    //把view保存到集合中
                    mViewLineList.add(viewList);
                    //把高保存到集合中
                    mLineHeightList.add(iCurLineH);

                    //2.记录新的一行的信息

                    //重写赋值新一行的宽高
                    iCurLineH = childHeight;
                    iCurLineW = childWidth;

                    //把新的view添加到集合中
                    viewList = new ArrayList<View>();
                    viewList.add(childView);

                } else {
                    //记录某行内的信息
                    //1.宽度叠加，高度进行比较
                    iCurLineW += childWidth;
                    iCurLineH = Math.max(iCurLineH, childHeight);
                    viewList.add(childView);

                }

                /*****3、如果正好是最后一行需要换行**********/
                if (i == childCount - 1) {
                    //1、记录当前行的最大宽度，高度累加
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;

                    //2、将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    mViewLineList.add(viewList);
                    mLineHeightList.add(iCurLineH);

                }
            }
        }

        //最终目的
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top, right, bottom;
        int curTop = 0;
        int curLeft = 0;
        int lineCount = mViewLineList.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> viewList = mViewLineList.get(i);
            int lineViewSize = viewList.size();
            for (int j = 0; j < lineViewSize; j++) {
                View childView = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                curLeft += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            curLeft = 0;
            curTop += mLineHeightList.get(i);
        }

    }
}
