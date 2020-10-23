package com.bhex.lib.uikit.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/11
 * Time: 10:51
 */
public class GridLayoutItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDriver ;

    // 网上绝大部分用的系统的一个属性 叫做 android.R.attrs.listDriver ，这个也可以，需要在清单文件中配置
    public GridLayoutItemDecoration(Context context, int drawableRescourseId){
        // 解析获取 Drawable
        mDriver = ContextCompat.getDrawable(context , drawableRescourseId) ;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 留出分割线的位置  每个item控件的下边和右边
        if (isLastRow(parent.getChildLayoutPosition(view), parent)){
            outRect.bottom = 0;
            outRect.right = mDriver.getIntrinsicWidth() ;
        }else if(isLastColum(parent.getChildLayoutPosition(view), parent)){
            outRect.right = 0;
            outRect.bottom = mDriver.getIntrinsicHeight() ;
        }else{
            outRect.bottom = mDriver.getIntrinsicHeight() ;
            outRect.right = mDriver.getIntrinsicWidth() ;
        }
    }

    // 绘制分割线
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        // 绘制分割线

        // 绘制水平方向
        drawHorizontal(canvas,parent) ;
        // 绘制垂直方向
        drawVertical(canvas,parent) ;
    }


    /**
     * 绘制垂直方向的分割线
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        // 第一行，最后一行，第一列，最后一列不绘制
        for (int i = 0; i < childCount; i++) {
            if(isLastColum(i,parent))
                continue;
            View childView = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();

            int top = childView.getTop() - params.topMargin ;
            int bottom = childView.getBottom() + params.bottomMargin ;
            int left = childView.getRight() + params.rightMargin ;
            int right = left + mDriver.getIntrinsicWidth() ;

            //计算水平分割线的位置
            mDriver.setBounds(left , top , right , bottom);
            mDriver.draw(canvas);
        }
    }


    /**
     * 绘制水平方向的分割线
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isLastRow(i,parent))
                continue;
            View childView = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();

            int left = childView.getLeft() - params.leftMargin;
            int right = childView.getRight() + mDriver.getIntrinsicWidth() + params.rightMargin;
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + mDriver.getIntrinsicHeight() ;

            //计算水平分割线的位置
            mDriver.setBounds(left , top , right , bottom);
            mDriver.draw(canvas);
        }
    }

    private int getSpanCount(@NonNull RecyclerView parent){
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }


    //是否最后一行
    private boolean isLastRow(int itemPosition,RecyclerView parent){
        int spanCount = getSpanCount(parent);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if(layoutManager instanceof GridLayoutManager){
            int childCount = parent.getAdapter().getItemCount();
            double count = Math.ceil((double) childCount/spanCount);
            double currentCount = Math.ceil((double) (itemPosition + 1) / spanCount);//当前行数

            if(currentCount<count){
                return false;
            }
        }
        return true;
    }

    //是否最后一列
    private boolean isLastColum(int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //有多少列
        if (layoutManager instanceof GridLayoutManager) {
            int spanCount = getSpanCount(parent);
            if ((itemPosition + 1) % spanCount == 0) {//因为是从0可以所以要将ItemPosition先加1
                return true;
            }
        }
        return false;
    }
}

