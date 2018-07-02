package com.hossain.ju.bus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class CustomLoginBackGroundDesign extends View {
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint3 = new Paint();

    RectF r1 = new RectF(30, 30, 400, 500);
    RectF r2 = new RectF(40, 50, 410, 510);
    RectF r3 = new RectF(50, 60, 420, 520);
    int rad = 20;

    public CustomLoginBackGroundDesign(Context context) {
        super(context);
    }

    public CustomLoginBackGroundDesign(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLoginBackGroundDesign(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomLoginBackGroundDesign(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint1.setColor(Color.RED);
        paint1.setAlpha(70);
        canvas.drawRoundRect(r1, rad, rad, paint1);
        paint2.setColor(Color.YELLOW);
        paint2.setAlpha(60);
        canvas.drawRoundRect(r2, rad, rad, paint2);

        paint3.setColor(Color.BLUE);
        paint3.setAlpha(50);
        canvas.drawRoundRect(r3, rad, rad, paint3);

    }
}
