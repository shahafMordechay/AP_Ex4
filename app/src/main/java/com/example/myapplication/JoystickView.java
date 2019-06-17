package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    private void setDimensions() {
        centerX = (float)getWidth() / 2;
        centerY = (float)getHeight() / 2;
        baseRadius = (float) (Math.min(getWidth(), getHeight()) / 2.5);
        hatRadius = (float)Math.min(getWidth(), getHeight()) / 5;
    }

    private void drawJoystick(float x, float y) {
        if (getHolder().getSurface().isValid()) {
            setDimensions();
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 0, 0, 255);
            canvas.drawCircle(x, y, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        drawJoystick(centerX, centerY);
        drawJoystick(centerX, centerY);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private float normalize(float num, float min, float max) {
        return (((num - min) / (max - min)) * 2) - 1;
    }

    public boolean onTouch(View v, MotionEvent e) {
        if (v.equals(this)) {
            if (e.getAction() != e.ACTION_UP) {
                double xPow = (Math.pow(e.getX() - centerX, 2));
                double yPow = (Math.pow(e.getY() - centerY, 2));
                float displacement = (float)Math.sqrt(xPow + yPow) + hatRadius;
                if (displacement < baseRadius) {
                    drawJoystick(e.getX(), e.getY());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);

                    float normalizedX = normalize(constrainedX,
                            centerX  - baseRadius, centerX + baseRadius);

                    float normalizedY = normalize(constrainedY,
                            centerY - baseRadius, centerY + baseRadius);

                    joystickCallback.onJoystickMoved(normalizedX, normalizedY);
                }
            }
            else {
                drawJoystick(centerX, centerY);
            }
        }

        return true;
    }
}


interface JoystickListener {
    void onJoystickMoved(float x, float y);
}