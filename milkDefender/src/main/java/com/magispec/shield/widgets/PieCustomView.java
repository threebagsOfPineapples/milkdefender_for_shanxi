package com.magispec.shield.widgets;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.magispec.shield.domain.BaseMessage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class PieCustomView  extends View {

    private Context ctx;
    private DecimalFormat format;
    private List<BaseMessage> mList;
    private ArrayList<Point>points;

    private Paint arcPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Paint fanPaint;

    private float centerX;
    private float centerY;
    private float radius;
    private float total;
    private float startAngle;
    private float textAngle;
    private float lastAngle;

    private List<PointF[]> lineList;
    private List<PointF> textList;

    public PieCustomView(Context context) {
        this(context, null);
    }
    public PieCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PieCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        this.ctx = context;
        this.lineList = new ArrayList<>();
        this.textList = new ArrayList<>();
        this.mList = new ArrayList<>();
        this.points=new ArrayList<>();
        this.format = new DecimalFormat("##0.00");

        this.arcPaint = new Paint();
        this.arcPaint.setColor(Color.rgb(225, 226, 226));
        this.arcPaint.setAntiAlias(true);
        this.arcPaint.setDither(true);
        this.arcPaint.setStyle(Paint.Style.STROKE);
        this.fanPaint=new Paint();
        this.fanPaint.setAntiAlias(true);
        this.fanPaint.setDither(true);
        this.fanPaint.setStyle(Paint.Style.FILL);

        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setDither(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeWidth(dip2px(ctx, 2));
        this.linePaint.setColor(Color.parseColor("#FFFFFF"));

        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setDither(true);
        this.textPaint.setStyle(Paint.Style.FILL);
        this.textPaint.setColor(Color.parseColor("#FFFFFF"));
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.EXACTLY) {
            height = heightSpecSize;
            width = Math.min(heightSpecSize, Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]));
        } else if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.AT_MOST) {
            width = widthSpecSize;
            height = Math.min(widthSpecSize, Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]));
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            width = height = Math.min(getScreenSize(ctx)[0], getScreenSize(ctx)[1]);
        } else {
            width = widthSpecSize;
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(centerX, centerY) * 0.6f;
        arcPaint.setStrokeWidth(radius /4);
        textPaint.setTextSize(radius / 7);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textList.clear();
        lineList.clear();
        lineList = new ArrayList<>();
        textList = new ArrayList<>();
       canvas.drawCircle(centerX, centerY, radius, arcPaint); 
        if (mList != null) {
            RectF mRectF = new RectF(centerX - radius+radius/8, centerY - radius+radius/8, centerX + radius-radius/8, centerY + radius-radius/8);
            for (int i = 0; i < mList.size(); i++) {
            	System.out.println("查看i的值："+i+mList.get(i).percent);
            	
                fanPaint.setColor(mList.get(i).color);
                canvas.drawArc(mRectF, startAngle, mList.get(i).percent / total * 360f, true, fanPaint);
                lineList.add(getLinePointFs());//获取直线 开始坐标 结束坐标
                textAngle = startAngle + mList.get(i).percent / total * 360f / 2;
                textList.add(getTextPointF());   //获取文本文本
                startAngle += mList.get(i).percent / total * 360f;
              //  totalAngle+=mList.get(i).percent / total * 360f;
                System.out.println("startAngle:"+startAngle);
              //绘制直线
                float angle= mList.get(i).percent / total * 360f;
                float spanwidth=(float) ((float) Math.cos(Math.toRadians(startAngle-angle/2))*(radius*1.5));
                float spanheight=(float) ((float) Math.sin(Math.toRadians(startAngle-angle/2))*(radius*1.5));
                lastAngle+= mList.get(i).percent / total * 360f;
                fanPaint.setColor(mList.get(i).color);
                canvas.drawLine(centerX, centerY, centerX+spanwidth, centerY+spanheight, fanPaint);
                canvas.drawLine(centerX+spanwidth, centerY+spanheight, points.get(i).x, points.get(i).y, fanPaint);
                System.out.println( "试验得到——point_x"+points.get(i).x+"point_y"+points.get(i).y);
            }
            //绘制间隔空白线
            //  drawSpacingLine(canvas, lineList);
            //绘制文字
            drawText(canvas);
        }
    }
    /**
     * 获取文本文本
     *
     * @return
     */
    private PointF getTextPointF() {
        float textPointX = (float) (centerX + 0.5*radius * Math.cos(Math.toRadians(textAngle)));
        float textPointY = (float) (centerY + 0.5*radius * Math.sin(Math.toRadians(textAngle)));
        return new PointF(textPointX, textPointY);
    }
    /**
     * 获取直线 开始坐标 结束坐标
     */
    private PointF[] getLinePointFs() {
        float stopX = (float) (centerX + (radius + arcPaint.getStrokeWidth() / 2) * Math.cos(Math.toRadians(startAngle)));
        float stopY = (float) (centerY + (radius + arcPaint.getStrokeWidth() / 2) * Math.sin(Math.toRadians(startAngle)));
        float startX = (float) (centerX + (radius - arcPaint.getStrokeWidth() / 2) * Math.cos(Math.toRadians(startAngle)));
        float startY = (float) (centerY + (radius - arcPaint.getStrokeWidth() / 2) * Math.sin(Math.toRadians(startAngle)));
        PointF startPoint = new PointF(startX, startY);
        PointF stopPoint = new PointF(stopX, stopY);
        return new PointF[]{startPoint, stopPoint};
    }
    /**
     * 画间隔线
     *
     * @param canvas
     */
    private void drawSpacingLine(Canvas canvas, List<PointF[]> pointFs) {
        for (PointF[] fp : pointFs) {
            canvas.drawLine(fp[0].x, fp[0].y, fp[1].x, fp[1].y, linePaint);
        }
    }
    /**
     * 画文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        for (int i = 0; i < textList.size(); i++) {
            textPaint.setTextAlign(Paint.Align.CENTER);
           // String text = mList.get(i).content;
           // canvas.drawText(text, textList.get(i).x, textList.get(i).y, textPaint);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            canvas.drawText(format.format(mList.get(i).percent * 100 / total) + "%", textList.get(i).x, textList.get(i).y + (fm.descent - fm.ascent), textPaint);
        }
    }
    /**
     * 设置间隔线的颜色
     *
     * @param color
     */
    public void setSpacingLineColor(int color) {
        linePaint.setColor(color);
    }
    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    /**
     * 设置开始角度
     *
     * @param startAngle
     */
    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }
    /**
     * 设置饼的宽度
     *
     * @param width
     */
    public void setCakeStrokeWidth(int width) {
        arcPaint.setStrokeWidth(dip2px(ctx, width));
    }

    /**
     * 设置饼的数据
     *
     * @param mList
     */
    public void setCakeData(List<BaseMessage> mList, ArrayList<Point>pionts) {
        total = 0;
        if (mList == null) {
            return;
        }
        for (int i = 0; i < mList.size(); i++) {
            total += mList.get(i).percent;
        }
        this.mList.clear();
        this.points.clear();
        this.mList = mList;
        this.points=pionts;
        invalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }
}
