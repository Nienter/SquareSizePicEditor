package square.size.editor;

import android.content.Context;
import android.util.AttributeSet;
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

        int width = Math.min(widthSize, highSize); //获取最小值来实现正方形布局

        //防止出现尺寸为0不显示

        if (widthMode == MeasureSpec.UNSPECIFIED) {

            width = highSize;

        } else if (highMode == MeasureSpec.UNSPECIFIED) {

            width = widthSize;

        }

        setMeasuredDimension(widthSize, widthSize); //设置宽高 即正方形布局
    }
}
