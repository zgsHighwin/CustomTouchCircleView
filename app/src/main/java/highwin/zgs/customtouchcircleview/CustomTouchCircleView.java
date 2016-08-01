package highwin.zgs.customtouchcircleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;

public class CustomTouchCircleView extends View {
    private Paint mRectanglePaint;
    private Paint mCirclePaint;
    private Paint mTrianglePaint;
    private float mMoveX;
    private float mMoveY;
    private Paint mArcPaint;
    private final int mPositionA = 0x001;
    private final int mPositionB = 0x002;
    private final int mPositionC = 0x003;
    private int mPositionTag = 0x001;

    public CustomTouchCircleView(Context context) {
        this(context, null);
    }

    public CustomTouchCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTouchCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mRectanglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectanglePaint.setStrokeWidth(5);
        mRectanglePaint.setStyle(Paint.Style.STROKE);
        mRectanglePaint.setDither(true);
        mRectanglePaint.setStrokeCap(Paint.Cap.ROUND);
        mRectanglePaint.setStrokeJoin(Paint.Join.ROUND);
        mRectanglePaint.setColor(Color.BLUE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setDither(true);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setColor(Color.BLACK);

        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setStrokeWidth(5);
        mTrianglePaint.setStyle(Paint.Style.STROKE);
        mTrianglePaint.setDither(true);
        mTrianglePaint.setStrokeCap(Paint.Cap.ROUND);
        mTrianglePaint.setStrokeJoin(Paint.Join.ROUND);
        mTrianglePaint.setColor(Color.YELLOW);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStrokeWidth(5);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setDither(true);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setColor(Color.RED);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int realWidth = 0;
        int realHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            realWidth = widthSize;
        } else {
            realWidth = 50;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = heightSize;
        } else {
            realHeight = 50;
        }

        //取最大值作为最为图形最外侧的面积
        int max = Math.max(realWidth, realHeight);
        setMeasuredDimension(max, max);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRect(rectF, mRectanglePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4, mCirclePaint);
        Path path = new Path();
        path.moveTo(getWidth() / 2, getHeight() / 2);
        path.lineTo(getWidth() / 2, (getWidth() / 2 - getWidth() / 4) / 2);
        path.lineTo(mMoveX, mMoveY);
        path.close();
        canvas.drawPath(path, mTrianglePaint);

        double triangleB = Math.sqrt(Math.pow((Math.abs(mMoveX - getWidth() / 2)), 2) + Math.pow((Math.abs(mMoveY - getHeight() / 2)), 2));
        double triangleC = Math.sqrt(Math.pow((Math.abs((getWidth() / 2 - getWidth() / 4) / 2 - getHeight() / 2)), 2));
        double triangleA = Math.sqrt(Math.pow((Math.abs(mMoveX - getWidth() / 2)), 2) + Math.pow((Math.abs(mMoveY - ((getWidth() / 2 - getWidth() / 4) / 2))), 2));
        double cosA = (Math.pow(triangleB, 2) + Math.pow(triangleC, 2) - Math.pow(triangleA, 2)) / (2 * triangleB * triangleC);

        DecimalFormat decimalFormat = new DecimalFormat("#0.#");

        //求反三角函数的度数
        String angleA = decimalFormat.format(Math.acos(cosA) * 180 / Math.PI);
        float angle = 0f;
        Log.d("CircleView==>angleA", angleA);
        angle = Float.parseFloat(angleA);
        if (mPositionTag == mPositionB || mPositionTag == mPositionC) {
            angle = 360 - angle;
        }

        RectF acrRectF = new RectF((getWidth() / 2 - getWidth() / 4) / 2,
                (getWidth() / 2 - getWidth() / 4) / 2,
                getWidth() / 2 + (getWidth() / 2 - getWidth() / 4) / 2 + getWidth() / 4,
                getHeight() / 2 + getWidth() / 4 + (getWidth() / 2 - getWidth() / 4) / 2);

        canvas.drawArc(acrRectF, -90f, angle, false, mArcPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mMoveX = event.getX();
                mMoveY = event.getY();
                Log.d("CustomTouchCircleView", "mMoveX:" + mMoveX);
                Log.d("CustomTouchCircleView", "mMoveY:" + mMoveY);
                if (mMoveX > getWidth() / 2) {
                    mPositionTag = mPositionA;
                } else {//mMoveX < getWidth()/2
                    if (mMoveY < getHeight() / 2) {  //在第四象限
                        mPositionTag = mPositionC;
                    } else {   //在第三象限
                        mPositionTag = mPositionB;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
