package com.example.customview;

import java.util.Random;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class LoadingDialog extends View {

	private int topColor = Color.GRAY;//顶部画笔颜色默认
	private int centerColor = Color.GRAY;//中间画笔颜色默认
	private int bottomColor = Color.GRAY;//底部画笔颜色默认
	private Paint mBottomPaint, mCenterPaint, mTopPaint;//画笔
	
	private int dimension = 1;//自定义view尺寸（宽和高相同）
	private int radius = 1;//半径
	private float scale = (float) 2.2;//顶部半径放大尺寸
	
	//数据刷新监听,json可为数据
	public interface OnFlashListener{
		void onFlash(String json);
	}
	private OnFlashListener mOnFlashListener;
	
	private boolean isAnimate = false;//是否开始动画
	private int duration = 2000;//动画持续时间
	private int startAngle = 135;//开始角度
	private int currentAngle = 0;//当前角度
	private int endAngle = 270;//跨越的角度

	public LoadingDialog(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		Log.i("info", "con 1");
	}

	public LoadingDialog(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
		Log.i("info", "con 2");
		//从attrs文件中获取颜色数据
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.CustomView);
		setBottomColor(array.getColor(R.styleable.CustomView_bottomColor,
				bottomColor));
		setCenterColor(array.getColor(R.styleable.CustomView_centerColor,
				centerColor));
		setTopColor(
				array.getColor(R.styleable.CustomView_topColor, topColor));
		array.recycle();
	}

	public LoadingDialog(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		Log.i("info", "con 3");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		dimension = getSpecSize(200, widthMeasureSpec, heightMeasureSpec);
		radius = dimension / 2;
		setMeasuredDimension(dimension, dimension);
	}
	/**
	 * 对尺寸处理
	 * @param defultSize	小于该值时宽高设为此值
	 * @param widthMeasureSpec	包含模式和尺寸参数
	 * @param heightMeasureSpec	类上
	 * @return
	 */
	protected int getSpecSize(int defultSize, int widthMeasureSpec,
			int heightMeasureSpec) {
		int dimension = 0;
		LayoutParams param = getLayoutParams();
		int mwidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int mwidthSize = MeasureSpec.getSize(widthMeasureSpec);
		int mheightMode = MeasureSpec.getMode(heightMeasureSpec);
		int mheightSize = MeasureSpec.getSize(heightMeasureSpec);
		/*当值设为wrap_content时，必须同时判断模式和参数*/
		if (mwidthMode == MeasureSpec.AT_MOST
				&& param.width == LayoutParams.WRAP_CONTENT) { 
			mwidthSize = defultSize;
		}
		if (mheightMode == MeasureSpec.AT_MOST
				&& param.height == LayoutParams.WRAP_CONTENT) {
			mheightSize = defultSize;
		}
		Log.i("info", "widht" + mwidthSize + "heit" + mheightSize);
		dimension = mwidthSize > mheightSize ? mheightSize : mwidthSize;
		return dimension;
	}

	public void setOnFlashListener(OnFlashListener listener){
		mOnFlashListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isAnimate = false;
			mOnFlashListener.onFlash("update");
			//load a new endAngle from net
			Random random = new Random();
			endAngle = random.nextInt(270);
			Log.i("info", "endAngle:--"+endAngle);
			if(endAngle<30 || endAngle>270)
				endAngle = 270;
			startAnimation(endAngle);
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (!isAnimate) {
			isAnimate = true;
			onDrawBottom(canvas, bottomColor);
			onDrawCenter(canvas, centerColor);
			onDrawTop(canvas, topColor);
			startAnimation(endAngle);
		} else {
			onDrawBottom(canvas, bottomColor);
			onDrawCenter(canvas, centerColor);
			onDrawTop(canvas, topColor);
		}
	}

	/**
	 * 绘制底部圆形
	 * @param canvas
	 * @param p_color传入画笔颜色
	 */
	protected void onDrawBottom(Canvas canvas, int p_color) {
		mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBottomPaint.setAntiAlias(true);//抗锯齿
		mBottomPaint.setDither(true);//防抖动
		mBottomPaint.setColor(p_color);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2,
				mBottomPaint);
	}
	/**
	 * 绘制中间扇形
	 * @param canvas
	 * @param p_color
	 */
	protected void onDrawCenter(Canvas canvas, int p_color) {
		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setAntiAlias(true);
		mCenterPaint.setDither(true);
		mCenterPaint.setColor(p_color);
		int[] colors = { Color.GREEN, Color.YELLOW, Color.YELLOW, Color.RED };
		LinearGradient lg = new LinearGradient(0, radius, getWidth(),
				radius, colors, null, TileMode.REPEAT);	//画笔渐变类
		mCenterPaint.setShader(lg);
		RectF rectf = new RectF(0, 0, getWidth(), getHeight());
		canvas.drawArc(rectf, startAngle, currentAngle, true,
				mCenterPaint);	//绘制扇形
	}
	/**
	 * 绘制顶部遮罩白色圆
	 * @param canvas
	 * @param p_color
	 */
	protected void onDrawTop(Canvas canvas, int p_color) {
		mTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTopPaint.setAntiAlias(true);
		mTopPaint.setColor(p_color);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2,
				getWidth() / scale, mTopPaint);
	}
	/**
	 * 动画设置
	 */
	protected void startAnimation(int endAngle) {
		ValueAnimator animator = ValueAnimator.ofInt(0,endAngle + 20,
				endAngle - 20, endAngle);	//属性动画，针对值的变化
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				currentAngle = (Integer) animation.getAnimatedValue();
				//该方法相当于刷新，重新执行onDraw方法
				invalidate();
			}
		});
		animator.setDuration(duration);
		animator.start();
	}
	/**
	 * 接口-设置动画时间
	 * @param duration
	 */
	protected void setDuration(int duration) {
		this.duration = duration;
	}
	
	protected void setTopColor(int topColor) {
		this.topColor = topColor;
	}
	
	protected void setCenterColor(int centerColor) {
		this.centerColor = centerColor;
	}
	
	protected void setBottomColor(int bottomColor) {
		this.bottomColor = bottomColor;
	}
}
