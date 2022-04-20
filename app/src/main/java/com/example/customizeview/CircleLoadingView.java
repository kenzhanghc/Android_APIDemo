package com.example.customizeview;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myalldemo.R;

public class CircleLoadingView extends View {
    private Context mContext;
    //内圆颜色
    private int mInnerColor;
    //外圆颜色
    private int mOuterColor;
    //圆点颜色
    private int mDotColor;
    //字体颜色
    private int mTextColor;
    //字体大小
    private int mTextSize;
    //创建内圆画笔
    private Paint mInnerPaint;
    //view的宽度
    private int mWidth;
    //view的高度
    private int mHeight;
    //当前进度
    private int mProgress = 0;
    //创建文字画笔
    private Paint mTextPaint;
    //创建小圆圈画笔
    private Paint mDotPaint;
    //小圆圈起点位置
    private int mDotProgress;

    //仿华为圆形加载框
    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //1、自定义属性
    //2、测量控件大小
    //3、绘制内圆
    //4、绘制外圆
    @SuppressLint("ResourceAsColor")
    public CircleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        mInnerColor = typedArray.getColor(R.styleable.CircleLoadingView_in_color, R.color.antique_white);
        mOuterColor = typedArray.getColor(R.styleable.CircleLoadingView_out_color, R.color.aqua);
        mTextColor = typedArray.getColor(R.styleable.CircleLoadingView_text_color, R.color.aqua);
        mDotColor = typedArray.getColor(R.styleable.CircleLoadingView_dot_color, R.color.blue_violet);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleLoadingView_text_size, 20);
        typedArray.recycle();
        //创建画笔
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(DensityUtil.dip2px(mContext, 3));
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        //创建文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        //创建小圆圈画笔
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStrokeWidth(DensityUtil.dip2px(mContext, 10));
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(mDotColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            //当宽度为精准值或match_parent时直接使用
            mWidth = widthSize;
        } else {
            //当宽度为wrap_content设置控件大小为120dp
            mWidth = DensityUtil.dip2px(mContext, 220);
        }
        //获取高度
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DensityUtil.dip2px(mContext, 220);
        }
        setMeasuredDimension(mWidth > mHeight ? mHeight : mWidth, mWidth > mHeight ? mHeight : mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆形
        canvas.save();
        for (int i = 0; i < 100; i++) {
            if (mProgress > i) {
                mInnerPaint.setColor(mInnerColor);
            } else {
                mInnerPaint.setColor(mOuterColor);
            }
            canvas.drawLine(mWidth / 2, 0, mWidth / 2, DensityUtil.dip2px(mContext, 10), mInnerPaint);
            canvas.rotate(3.6f, mWidth / 2, mHeight / 2);
        }
        canvas.restore();
        //绘制文字
        String progreStr = mProgress + "";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(progreStr, 0, progreStr.length(), rect);
        int dx = getWidth() / 2 - rect.width() / 2;
        canvas.drawText(progreStr, dx, getHeight() / 2 + rect.height() / 2, mTextPaint);
        //绘制小圆圈
        canvas.save();
        canvas.rotate(mDotProgress * 3.6f, mWidth / 2, mHeight / 2);
        canvas.drawCircle(mWidth / 2 - (mInnerPaint.getStrokeWidth() * 2), DensityUtil.dip2px(mContext, 10) + DensityUtil.dip2px(mContext, 8), DensityUtil.dip2px(mContext, 3), mDotPaint);
        canvas.restore();
    }

    public void setProgress(int progress) {
        mProgress = progress;
        mDotProgress = progress;
        invalidate();
    }

    public void setAnimation(int progress, int time) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, progress);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                setProgress((int) value);
            }
        });
        valueAnimator.start();
    }

}
