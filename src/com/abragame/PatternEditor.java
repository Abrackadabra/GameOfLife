package com.abragame;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

import java.io.*;
import java.util.*;
import java.math.*;

//1/21/12 3:53 AM

public class PatternEditor extends View {
    public PatternEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        size = getResources().getInteger(R.integer.patternSize);

        settings = new Settings((Activity) getContext());
        if (settings.patternSize == 0) {
            cells = new boolean[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++) {
                    cells[i][j] = random.nextBoolean();
                }
        } else {
            cells = settings.pattern;
        }
    }

    Settings settings;

    private Random random = new Random();

    int size;

    boolean[][] cells;

    private double cellWidth, cellHeight;

    private int touchX = -1, touchY = -1;
    
    boolean usingPattern = true;

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN)
            xor(motionEvent.getX(), motionEvent.getY());
        if (action == MotionEvent.ACTION_UP)
            touchX = touchY = -1;
        invalidate();
        return true;
    }

    private void xor(float x, float y) {
        int newX = (int) (x / cellWidth);
        int newY = (int) (y / cellHeight);
        if (newX != touchX || newY != touchY) {
            touchX = newX;
            touchY = newY;
            if (touchX >= 0 && touchX < size && touchY >= 0 && touchY < size)
                cells[touchX][touchY] ^= true;
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.GREEN);
        cellWidth = 1.0 * getWidth() / size;
        cellHeight = 1.0 * getHeight() / size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j]) {
                    int x = (int) Math.round(i * cellWidth);
                    int y = (int) Math.round(j * cellHeight);
                    canvas.drawRect((float) x, (float) y, (float) Math.round(x + cellWidth - 1), (float) Math.round(y + cellHeight - 1), paint);
                }
            }
        }
    }

    void save() {
        if (usingPattern) {
            settings.patternSize = (byte) size;
            settings.pattern = cells;
        } else {
            settings.patternSize = 0;
        }
        settings.save();
    }
}