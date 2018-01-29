package com.example.customview;

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

	private int topColor = Color.GRAY;//����������ɫĬ��
	private int centerColor = Color.GRAY;//�м仭����ɫĬ��
	private int bottomColor = Color.GRAY;//�ײ�������ɫĬ��
	private Paint mBottomPaint, mCenterPaint, mTopPaint;//����
	
	private int dimension = 1;//�Զ���view�ߴ磨��͸���ͬ��
	private int radius = 1;//�뾶
	private float scale = (float) 2.2;//�����뾶�Ŵ�ߴ�
	
	private boolean isAnimate = false;//�Ƿ�ʼ����
	private int duration = 2000;//��������ʱ��
	private int startAngle = 135;//��ʼ�Ƕ�
	private int currentAngle = 0;//��ǰ�Ƕ�
	private int endAngle = 270;//��Խ�ĽǶ�

	public LoadingDialog(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		Log.i("info", "con 1");
	}

	public LoadingDialog(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
		Log.i("info", "con 2");
		//��attrs�ļ��л�ȡ��ɫ����
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.CustomView);
		setBottomColor(array.getColor(R.styleable.CustomView_bottomColor,
				bottomColor));
		setCenterColor(array.getColor(R.styleable.CustomView_centerColor,
				centerColor));
		setTopColor(
				array.getColor(R.styleable.CustomView_topColor, topColor));
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
	 * �Գߴ紦��
	 * @param defultSize	С�ڸ�ֵʱ�����Ϊ��ֵ
	 * @param widthMeasureSpec	����ģʽ�ͳߴ����
	 * @param heightMeasureSpec	����
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
		/*��ֵ��Ϊwrap_contentʱ������ͬʱ�ж�ģʽ�Ͳ���*/
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isAnimate = false;
			startAnimation();
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
			startAnimation();
		} else {
			onDrawBottom(canvas, bottomColor);
			onDrawCenter(canvas, centerColor);
			onDrawTop(canvas, topColor);
		}
	}

	/**
	 * ���Ƶײ�Բ��
	 * @param canvas
	 * @param p_color���뻭����ɫ
	 */
	protected void onDrawBottom(Canvas canvas, int p_color) {
		mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBottomPaint.setAntiAlias(true);//�����
		mBottomPaint.setDither(true);//������
		mBottomPaint.setColor(p_color);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2,
				mBottomPaint);
	}
	/**
	 * �����м�����
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
				radius, colors, null, TileMode.REPEAT);	//���ʽ�����
		mCenterPaint.setShader(lg);
		RectF rectf = new RectF(0, 0, getWidth(), getHeight());
		canvas.drawArc(rectf, startAngle, currentAngle, true,
				mCenterPaint);	//��������
	}
	/**
	 * ���ƶ������ְ�ɫԲ
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
	 * ��������
	 */
	protected void startAnimation() {
		ValueAnimator animator = ValueAnimator.ofInt(0,endAngle + 20,
				endAngle - 20, endAngle);	//���Զ��������ֵ�ı仯
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				currentAngle = (Integer) animation.getAnimatedValue();
				//�÷����൱��ˢ�£�����ִ��onDraw����
				invalidate();
			}
		});
		animator.setDuration(duration);
		animator.start();
	}
	/**
	 * �ӿ�-���ö���ʱ��
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
