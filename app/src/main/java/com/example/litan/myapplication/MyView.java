package com.example.litan.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

/**
 * Created by litan on 2017/4/5.
 */

public class MyView extends View {

    Context m_context;
    public MyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        m_context=context;
    }
    //重写OnDraw（）函数，在每次重绘时自主实现绘图
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setAntiAlias(true);//抗锯齿功能
        paint.setColor(Color.RED);
        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(5);
        paint.setShadowLayer(10, 15, 15, Color.GREEN);
        canvas.drawRGB(255, 255,255);
        canvas.drawCircle(190, 200, 150, paint);
    }

}
