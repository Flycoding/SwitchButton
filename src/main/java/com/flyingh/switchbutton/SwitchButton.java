package com.flyingh.switchbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SwitchButton extends View {

    private final Bitmap onOffBitmap;
    private final Bitmap slideBitmap;
    private final int MAX_LEFT;
    private Paint paint;
    private boolean on;
    private int left;
    private int lastX;
    private boolean isDragging;

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        onOffBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDragging) {
                    left = on ? 0 : MAX_LEFT;
                    invalidate();
                    on = !on;
                }
            }
        });
        paint = new Paint();
        paint.setAntiAlias(true);
        MAX_LEFT = onOffBitmap.getWidth() - slideBitmap.getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float delta = event.getX() - lastX;
                if (Math.abs(delta) > 5) {
                    isDragging = true;
                }
                left += delta;
                left = Math.min(left, MAX_LEFT);
                left = Math.max(0, left);
                break;
            case MotionEvent.ACTION_UP:
                left = left > MAX_LEFT / 2 ? MAX_LEFT : 0;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(onOffBitmap.getWidth(), onOffBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(onOffBitmap, 0, 0, paint);
        canvas.drawBitmap(slideBitmap, left, 0, paint);
    }
}