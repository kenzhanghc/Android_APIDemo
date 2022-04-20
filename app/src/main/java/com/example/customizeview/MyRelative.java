package com.example.customizeview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.example.myalldemo.R;

public class MyRelative extends View {
    //圆弧宽度
    private int mBorderWidth;
    //外圆弧默认颜色
    private int mOuterColor = R.color.salmon;
    //内圆弧默认颜色
    private int mInnerColor = R.color.sandy_brown;
    //字体默认颜色
    private int mTextColor = R.color.salmon;
    //字体默认大小
    private int mTextSize = 40;
    //步数
    private int mCurrentStep;
    //创建内圆画笔
    private Paint mInnerPaint;
    //创建外圆画笔
    private Paint mOuterPaint;
    //创建文字画笔
    private Paint mTextPaint;
    //最大步数值
    private int mMaxStep;

    public void setmCurrentStep(int mCurrentStep) {
        this.mCurrentStep = mCurrentStep;
        invalidate();
    }

    public void setmMaxStep(int mMaxStep) {
        this.mMaxStep = mMaxStep;
    }

    public MyRelative(Context context) {
        this(context, null);
    }

    public MyRelative(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //1、分析需求
    //2、自定义属性
    //3、获得自定义属性
    //4、重写onMeasure
    //5、绘制
    //6、其他(动画等等)
    @SuppressLint("ResourceAsColor")
    public MyRelative(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRelative);
        //圆弧宽度
        mBorderWidth = (int) typedArray.getDimension(R.styleable.MyRelative_border_width, 10);
        //外圆弧颜色
        mOuterColor = typedArray.getColor(R.styleable.MyRelative_outer_color, mOuterColor);
        //内圆弧颜色
        mInnerColor = typedArray.getInteger(R.styleable.MyRelative_inner_color, mInnerColor);
        //字体颜色
        mTextColor = typedArray.getColor(R.styleable.MyRelative_step_text_color, mTextColor);
        //字体大小
        mTextSize = (int) typedArray.getDimensionPixelSize(R.styleable.MyRelative_step_text_size, mTextSize);
        //最大步数
        mMaxStep = typedArray.getInteger(R.styleable.MyRelative_max_step, 10000);
        //当前步数
        mCurrentStep = typedArray.getInteger(R.styleable.MyRelative_curren_step, 8000);
        typedArray.recycle();
        //创建画笔
        mInnerPaint = new Paint();
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        //创建画笔
        mOuterPaint = new Paint();
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        //创建文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //将控件截成正方形
        //三目运算符取长度短的一边作为宽高
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制内圆弧
        int center = getWidth() / 2;
        int r = (getWidth() - mBorderWidth) / 2;
        RectF rectF = new RectF(mBorderWidth / 2, mBorderWidth / 2, center + r, center + r);
        canvas.drawArc(rectF, 135, 270, false, mInnerPaint);
        //绘制外圆弧
        if (mMaxStep == 0) {
            return;
        }
        float radio = (float) mCurrentStep / mMaxStep;
        canvas.drawArc(rectF, 135, 270 * radio, false, mOuterPaint);
        //文字
        String mText = mCurrentStep + "";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), rect);
        int dx = getWidth() / 2 - rect.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = fontMetricsInt.bottom - fontMetricsInt.top;
        int baseLine = getHeight() / 2 + dy / 2;
        canvas.drawText(mText, dx, getHeight() / 2 + rect.height() / 2, mTextPaint);
    }

    public void setAnimator(int mMaxStep, int mCurrentStep, int duration) {
        this.mMaxStep = mMaxStep;
        ValueAnimator animator = ObjectAnimator.ofFloat(0, mCurrentStep);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) animator.getAnimatedValue();
                setmCurrentStep((int) value);
            }
        });
        animator.start();
    }
}
