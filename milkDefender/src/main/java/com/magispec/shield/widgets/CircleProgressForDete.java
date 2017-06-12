package com.magispec.shield.widgets;

import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.service.BaseApplicaton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressForDete  extends View {
	private int maxProgress = 100;
    private int progress = 0;
    //画圆所在的距形区域
    RectF oval;
    Paint paint;
    public CircleProgressForDete(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        paint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
 
        if(width!=height)
        {
            int min=Math.min(width, height);
            width=min;
            height=min;
        }
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(Color.WHITE); // 设置画笔颜色
        canvas.drawColor(Color.TRANSPARENT); // 透明背景
        int progressStrokeWidth = 200;
        paint.setStrokeWidth(progressStrokeWidth); //线宽
        paint.setStyle(Paint.Style.STROKE);
        oval.left = progressStrokeWidth / 2; // 左上角x
        oval.top = progressStrokeWidth / 2; // 左上角y
        oval.right = width - progressStrokeWidth / 2; // 左下角x
        oval.bottom = height - progressStrokeWidth / 2; // 右下角y
        paint.setColor(Color.WHITE); // 设置画笔颜色;
        canvas.drawArc(oval, -90, 360, false, paint); // 绘制粉色圈圈，即进度条背景
        if(progress<=20){
        	  paint.setColor( Color.rgb(220, 20, 60));
        }else if(progress<=50){
        	paint.setColor(Color.rgb(255,182,193));
        }else if(progress<=75){
        	paint.setColor(Color.rgb(255,215,0));
        }else{
        	paint.setColor(Color.rgb(0, 206, 209));
        }
        canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是蓝色
        Bitmap bitmap= BitmapFactory.decodeResource(BaseApplicaton.getAppContext().getResources(),R.drawable.bg_fragment_detection_circle_in);
        canvas.drawBitmap(bitmap, width/2-bitmap.getWidth()/2,height/2-bitmap.getHeight()/2, paint);;
        String text = Constant.matchingDegree+"%";
      //  canvas.drawText(text, width/2-text.getWidth()/2,height/2-bitmap.getHeight()/2, paint);;
        paint.setStrokeWidth((float) 1);
        paint.setColor(Color.rgb(57, 135, 200));
        int textHeight = height / 8;
        paint.setTextSize(textHeight);
        int textWidth = (int) paint.measureText(text, 0, text.length());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, (float) (height/2 ), paint);
    }
    public int getMaxProgress() {
        return maxProgress;
    }
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }
    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }
}
