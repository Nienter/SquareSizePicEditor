package square.size.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 使用矩阵（Matrix）来设置图片的属性
 * 平移、旋转、放大缩小
 * <p>
 * 本类的效果：旋转
 * Created by lby on 2017/7/24.
 */
public class VaryImageView extends AppCompatImageView {
    private Context mContext;
    private static final String TAG = "VaryImageView";

    private Matrix matrix = new Matrix();//变换矩阵
    private Matrix currentMatrix = new Matrix(); //临时矩阵
    private Bitmap bitmap;
    private double oldRotate; // 按下时的两根手指的角度
    private float oldLength; //  按下時兩個落點的距離

    private PointF midPoint = new PointF();
    private boolean isMorePoint; // 是否是多根手指

    private float downX; // 手指按下时的 X 坐标
    private float downY; // 手指按下时的 Y 坐标
    private MODEL currentModel = MODEL.ACTION_NONE;
    private boolean isInit = true;

    private enum MODEL {
        ACTION_DOUBLE,
        ACTION_TO_SINGLE,
        ACTION_SINGLE,
        ACTION_NONE;
    }

    public VaryImageView(Context context) {
        this(context, null);
        mContext = context;
    }

    public VaryImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public VaryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bitmap = bm;
    }

    private void init() {
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Log.d(TAG, "setBitmap: "+bitmap.getWidth()+"**"+getWidth());
            matrix.reset();
            matrix.postTranslate((getWidth()-bitmap.getWidth())/2, 0);
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, matrix, null);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN: ");
                isMorePoint = false;
                currentModel = MODEL.ACTION_SINGLE;
                downX = event.getX();
                downY = event.getY();
                // 记录此时矩阵的数据，也就是记录此时图片的属性，比如：位置
                currentMatrix.set(matrix);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "ACTION_POINTER_DOWN: ");
                isMorePoint = true;
                currentMatrix.set(matrix);
                currentModel = MODEL.ACTION_DOUBLE;
                oldRotate = getRotate(event);
                oldLength = getLength(event);
                getMidPoint(midPoint, event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: ");
                if (isMorePoint) {
                    matrix.set(currentMatrix);
                    double rotate = getRotate(event) - oldRotate;
                    matrix.postRotate((float) rotate, midPoint.x, midPoint.y);
                    // 计算缩放比例
                    float length = getLength(event) / oldLength;
                    matrix.postScale(length, length, midPoint.x, midPoint.y);
                } else if (currentModel == MODEL.ACTION_SINGLE) {
                    matrix.set(currentMatrix);
                    float x = event.getX() - downX;
                    float y = event.getY() - downY;
                    matrix.postTranslate(x, y);
                } else if (currentModel == MODEL.ACTION_TO_SINGLE) {
                    matrix.set(currentMatrix);
//                    float x = event.getX()-downX;
//                    float y = event.getY() - downY;
//                    matrix.postTranslate(x, y);
//                    currentModle = MODEL.ACTION_SINGLE;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isMorePoint = false;
                currentModel = MODEL.ACTION_TO_SINGLE;
                currentMatrix.set(matrix);

                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP: ");
                currentModel = MODEL.ACTION_NONE;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 取手势中心点
     */
    private void getMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private double getRotate(MotionEvent event) {
        // 获取两个手指的点,相当于两个向量
        float pointX1 = event.getX(0);
        float pointX2 = event.getX(1);
        float pointY1 = event.getY(0);
        float pointY2 = event.getY(1);

        // 获取旋转角度
        // 这里有两个注意点，首先是Math.toDegrees，这个是把小数点转换成角度的函数，比自己*180/3.14更加的方便，
        // 还有就是Math.atan2这个函数，一开始我用的是Math.atan((pointY1 - pointY2)/(pointX1 - pointX2))
        //但是这样的话，当pointX1 - pointX2==0的时候会有问题，然后图片旋转到两个手指上下或水平平行的时候
        //会突然跳转一个很大的角度，根本原因就是因为pointX1 - pointX2==0或则pointY1 - pointY2==0，
        //而Math.atan2内部有对这些事件的处理，这样就可以了
        return Math.toDegrees(Math.atan2((pointY1 - pointY2), (pointX1 - pointX2)));
    }

    /**
     * 获取两个落点之间的距离
     */
    private float getLength(MotionEvent event) {
        // 获取两个手指的点
        float pointX1 = event.getX(0);
        float pointX2 = event.getX(1);
        float pointY1 = event.getY(0);
        float pointY2 = event.getY(1);

        return (float) Math.sqrt(Math.pow(pointX1 - pointX2, 2) + Math.pow(pointY1 - pointY2, 2));
    }
}
