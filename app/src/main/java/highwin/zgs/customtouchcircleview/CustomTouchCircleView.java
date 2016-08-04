package highwin.zgs.customtouchcircleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Random;

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
    private final int mPositinoD = 0x004;
    private int mPositionTag = 0x001;
    private Paint mTextArcPaint;

    private boolean mIsHasFirstDraw;
    private boolean mIsSetAngle;

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

    private OnAngleChangeListener mOnAngleChangeListener;

    public interface OnAngleChangeListener {
        void onAngleChange(float angle);
    }

    public OnAngleChangeListener getOnAngleChangeListener() {
        return mOnAngleChangeListener;
    }

    public void setOnAngleChangeListener(OnAngleChangeListener onAngleChangeListener) {
        mOnAngleChangeListener = onAngleChangeListener;
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
        mArcPaint.setStrokeWidth(80);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setDither(true);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setColor(Color.RED);

        mTextArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextArcPaint.setStyle(Paint.Style.STROKE);
        mTextArcPaint.setDither(true);
        mTextArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextArcPaint.setTextSize(50);


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

    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int mCutPositionX;
    private int mCutPositionY;
    private Bitmap starBitMap;
    float angle = 0f;

    @Override
    protected  void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRect(rectF, mRectanglePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4, mCirclePaint);
        //辅助三角形
   /*     Path path = new Path();
        path.moveTo(getWidth() / 2, getHeight() / 2);
        path.lineTo(getWidth() / 2, (getWidth() / 2 - getWidth() / 4) / 2);
        path.lineTo(mMoveX, mMoveY);
        path.close();
        canvas.drawPath(path, mTrianglePaint);*/

        if (!mIsHasFirstDraw) {
            mIsHasFirstDraw = true;
            mMoveX = getWidth() / 2;
            mMoveY = getHeight() / 2;
        }
        if (!mIsSetAngle) {
            double triangleB = Math.sqrt(Math.pow((Math.abs(mMoveX - getWidth() / 2)), 2) + Math.pow((Math.abs(mMoveY - getHeight() / 2)), 2));
            double triangleC = Math.sqrt(Math.pow((Math.abs((getWidth() / 2 - getWidth() / 4) / 2 - getHeight() / 2)), 2));
            double triangleA = Math.sqrt(Math.pow((Math.abs(mMoveX - getWidth() / 2)), 2) + Math.pow((Math.abs(mMoveY - ((getWidth() / 2 - getWidth() / 4) / 2))), 2));
            double cosA = (Math.pow(triangleB, 2) + Math.pow(triangleC, 2) - Math.pow(triangleA, 2)) / (2 * triangleB * triangleC);

            DecimalFormat decimalFormat = new DecimalFormat("#0.#");

            //求反三角函数的度数
            String angleA = decimalFormat.format(Math.acos(cosA) * 180 / Math.PI);
            Log.d("CircleView==>angleA", angleA);

            //余弦角度的值
//        int arcRadius = (getWidth() / 2 - getWidth() / 4) / 2 + getWidth() / 4;
            angle = Float.parseFloat(angleA);

            switch (mPositionTag) {
                //切点，x,y坐标
                case mPositionA:
//                mCutPositionX = (int) (getWidth() / 2 + Double.parseDouble(decimalFormat.format(arcRadius * Math.sin(Math.toRadians(angle)))));
//                mCutPositionY = (int) (getHeight() / 2 - Double.parseDouble(decimalFormat.format(arcRadius * Math.cos(Math.toRadians(angle)))));
                    break;
                case mPositionB:
//                mCutPositionX = (int) (getWidth() / 2 + Double.parseDouble(decimalFormat.format(arcRadius * Math.sin(Math.toRadians(180 - angle)))));
//                mCutPositionY = (int) (getHeight() / 2 + Double.parseDouble(decimalFormat.format(arcRadius * Math.cos(Math.toRadians(180 - angle)))));
                    break;
                case mPositionC:
                    angle = 360 - angle;
//                mCutPositionX = (int) (getWidth() / 2 - Double.parseDouble(decimalFormat.format(arcRadius * Math.cos(Math.toRadians(270 - angle)))));
//                mCutPositionY = (int) (getHeight() / 2 + Double.parseDouble(decimalFormat.format(arcRadius * Math.sin(Math.toRadians(270 - angle)))));
                    break;
                case mPositinoD:
                    angle = 360 - angle;
//                mCutPositionX = (int) (getWidth() / 2 - Double.parseDouble(decimalFormat.format(arcRadius * Math.sin(Math.toRadians(360 - angle)))));
//                mCutPositionY = (int) (getHeight() / 2 - Double.parseDouble(decimalFormat.format(arcRadius * Math.cos(Math.toRadians(360 - angle)))));
                    break;
            }

        }


        RectF acrRectF = new RectF((getWidth() / 2 - getWidth() / 4) / 2,
                (getWidth() / 2 - getWidth() / 4) / 2,
                getWidth() / 2 + (getWidth() / 2 - getWidth() / 4) / 2 + getWidth() / 4,
                getHeight() / 2 + getWidth() / 4 + (getWidth() / 2 - getWidth() / 4) / 2);

        mArcPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, new int[]{Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW}, null));
        canvas.drawArc(acrRectF, -90f, angle, false, mArcPaint);
        String text = angle + "度";


        if (angle % 10 == 0) {
            r = new Random().nextInt(256);
            g = new Random().nextInt(256);
            b = new Random().nextInt(256);
        }

        mTextArcPaint.setARGB(255, r, g, b);
        mTextArcPaint.setTextSize(getTextSize());
        Rect textRectF = new Rect();
        mTextArcPaint.getTextBounds(text, 0, text.length(), textRectF);
        canvas.save();
        canvas.rotate(angle, getWidth() / 2, getHeight() / 2);
        canvas.drawText(text, -textRectF.left + getWidth() / 2 - textRectF.width() / 2, getHeight() / 2 - textRectF.top - textRectF.height() / 2, mTextArcPaint);
        canvas.restore();

        if (mOnAngleChangeListener != null) {
            mOnAngleChangeListener.onAngleChange(angle);
        }
    }

    private float i = 40;
    private boolean isHasUp;
    private boolean isHasBottom = true;

    private float getTextSize() {
        if (!isHasUp) {
            i = i + 0.5f;
            if (i > 50) {
                isHasUp = true;
                isHasBottom = false;
                i = 50;
            }
        }

        if (!isHasBottom) {
            i -= 0.5;
            if (i < 40) {
                isHasBottom = true;
                isHasUp = false;
                i = 40;
            }
        }

        return i;
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIsSetAngle = false;
                mMoveX = event.getX();
                mMoveY = event.getY();
                Log.d("CustomTouchCircleView", "mMoveX:" + mMoveX);
                Log.d("CustomTouchCircleView", "mMoveY:" + mMoveY);
                if (mMoveX > getWidth() / 2) {
                    if (mMoveY < getHeight()) {
                        mPositionTag = mPositionA;
                    } else {
                        mPositionTag = mPositionB;
                    }
                } else {//mMoveX < getWidth()/2
                    if (mMoveY < getHeight() / 2) {  //在第四象限
                        mPositionTag = mPositinoD;
                    } else {   //在第三象限
                        mPositionTag = mPositionC;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        mIsSetAngle = true;
        this.angle = angle;
        invalidate();
    }
}
