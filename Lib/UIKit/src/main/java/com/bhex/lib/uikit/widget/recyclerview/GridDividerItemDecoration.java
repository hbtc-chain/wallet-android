package com.bhex.lib.uikit.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/11
 * Time: 9:49
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    public GridDividerItemDecoration(Context context) {
        TypedArray ta = context.obtainStyledAttributes(ATTRS);
        //mDivider = ta.getDrawable(0);
        mDivider = context.getDrawable(R.color.blue_bg);
        ta.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //super.onDraw(c, parent, state);
        drawHorizontal(canvas, parent);
        drawVertical(canvas, parent);
    }



    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int chilCount = parent.getChildCount();
        for (int i = 0; i <chilCount ; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();

            final int left = child.getLeft() - layoutParams.leftMargin;
            final int right = child.getRight() + layoutParams.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }

    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
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



    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 如果是最后一行，则不需要绘制底部
        if (isLastRow(parent.getChildLayoutPosition(view), parent)){
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }

        // 如果是最后一列，则不需要绘制右边
        if (isLastColum(parent.getChildLayoutPosition(view), parent)){
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

    }
}
