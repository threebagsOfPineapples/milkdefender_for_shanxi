package com.magispec.shield.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.view.View;
import android.widget.Toast;

public class XpToast extends View{
	private Toast toast;
	private Paint paint = new Paint();
    //属性
	private String text;
	private int textSize;
	private int textColor;
	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;
	private int cornerRadius;
	private int backgroundColor;
	
	public XpToast(Context context ,String s) {
		super(context);
		text = "";
		textSize = sp2px(this.getContext(), 16);
		textColor = 0xFFFFFFFF;
		paddingLeft = dip2px(this.getContext(), 10);
		paddingTop = dip2px(this.getContext(), 10);
		paddingRight = dip2px(this.getContext(), 10);
		paddingBottom = dip2px(this.getContext(), 10);
		cornerRadius = dip2px(this.getContext(), 4);
		backgroundColor = 0xFF2A2A2A;		
		toast = Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT);
	
	}
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }	
	/**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
        	result = getDefaultWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * 计算默认宽度
     */
    private int getDefaultWidth(){
    	int width = 0;
		paint.setTextSize(this.textSize);
		int txtWidth = (int)this.paint.measureText(this.text);
		width = paddingLeft + txtWidth + paddingRight;
    	return width;
    }
    /**
     * 计算默认宽度
     */
    private int getDefaultHeight(){
    	int height = 0;
    	paint.setTextSize(this.textSize);
    	FontMetricsInt fontMetrics = paint.getFontMetricsInt();
    	int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
    	height = paddingTop + txtHeight + paddingBottom;
    	return height;
    }
    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackground(canvas);
		drawText(canvas);
    }
    /**
     * 画背景
     */
    private void drawBackground(Canvas canvas){
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(this.backgroundColor);
		RectF rectF = new RectF(0, 0, this.getWidth() , this.getHeight());
		canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
    }
    /**
     * 画文字
     */
    private void drawText(Canvas canvas){
    	paint.setTextSize(this.textSize);
		paint.setColor(this.textColor);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
		int txtWidth = (int)this.paint.measureText(this.text);
		int left = this.paddingLeft + ((this.getWidth() - this.paddingLeft - this.paddingRight)/2-txtWidth/2);
		int top = this.paddingTop;
		canvas.drawText(text, left, top-fontMetrics.ascent, paint);	
    }
	/**
	 * 打开
	 */
	public void show(){
		toast.setView(this);
		toast.show();
	}
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    /** 
     * 从 sp 的单位 转成为 px(像素) 
     */  
    public static int sp2px(Context context, float spValue) {  
    	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    }
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	public int getTextColor() {
		return textColor;
	}
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	public int getPaddingLeft() {
		return paddingLeft;
	}
	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	public int getPaddingTop() {
		return paddingTop;
	}
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}
	public int getPaddingRight() {
		return paddingRight;
	}
	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}
	public int getPaddingBottom() {
		return paddingBottom;
	}
	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	public int getCornerRadius() {
		return cornerRadius;
	}
	public void setCornerRadius(int cornerRadius) {
		this.cornerRadius = cornerRadius;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
