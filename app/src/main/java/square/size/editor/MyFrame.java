package square.size.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyFrame extends FrameLayout {
    public MyFrame(@NonNull Context context) {
        super(context);
    }

    public MyFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int highSize = MeasureSpec.getSize(heightMeasureSpec);

        int highMode = MeasureSpec.getMode(heightMeasureSpec);

//        int width = Math.min(widthSize, highSize); //获取最小值来实现正方形布局

        //防止出现尺寸为0不显示
        //记录如果是wrap_content时设置的宽和高
        int width, height;
        //左右的高度
        int lHeight = 0,rHeight = 0;
        //上下的宽度
        int tWidth = 0, bWidth = 0;
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams mParams = (MarginLayoutParams) childView.getLayoutParams();

            if(i ==0 || i == 1){
                tWidth += childWidth + mParams.leftMargin + mParams.rightMargin;
            }

            if(i == 2 || i == 3){
                bWidth += childWidth + mParams.leftMargin + mParams.rightMargin;
            }

            if(i == 0 || i== 2){
                lHeight += childHeight + mParams.topMargin + mParams.bottomMargin;
            }

            if(i == 1 || i == 3){
                rHeight += childHeight + mParams.topMargin + mParams.bottomMargin;
            }
        }


//
//        if (widthMode == MeasureSpec.UNSPECIFIED) {
//
//            width = highSize;
//
//        } else if (highMode == MeasureSpec.UNSPECIFIED) {
//
//            width = widthSize;
//
//        }

        setMeasuredDimension(widthSize, widthSize); //设置宽高 即正方形布局
    }

    @Override
    protected void onLayout(boolean changed, int l, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            int childWidth = getWidth();
            int childHeight = getHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            int childLeft = 0,childTop = 0,childRight = 0,childBottom = 0;
            switch (i){
                case 0:
                    childLeft = cParams.leftMargin;
                    childTop = cParams.topMargin+ getPaddingTop();

                    break;
                case 1:
                    childLeft = getMeasuredWidth() - childWidth - cParams.rightMargin;
                    childTop = cParams.topMargin + getPaddingTop();
                    break;
                case 2:
                    childLeft = cParams.leftMargin;
                    childTop =  getMeasuredHeight() - childHeight - cParams.bottomMargin
                            - getPaddingBottom() - getPaddingTop();
                    break;
                case 3:
                    childLeft = getMeasuredWidth() - childWidth - cParams.rightMargin;
                    childTop =  getMeasuredHeight() - childHeight - cParams.bottomMargin
                            -getPaddingTop() - getPaddingBottom();
                    break;
                default:
            }

            childRight = getWidth()-cParams.rightMargin;
            childBottom = getHeight()-cParams.bottomMargin;

            childView.layout(childLeft,childTop,childRight,childBottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
