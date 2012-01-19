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

//1/13/12 3:43 AM

public class Game extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //test

        super.onCreate(savedInstanceState);
        life = new Life(this);
        view = new GameView(this);
        setContentView(view);

        view.setOnClickListener(this);
    }

    Life life;

    private GameView view = null;

    Handler handler = new Handler();

    Runnable tick = new Runnable() {
        @Override
        public void run() {
            life.update();
            view.invalidate();

            if (life.settings.speed == getResources().getInteger(R.integer.maxSpeed))
                handler.post(tick);
            else
                handler.postDelayed(tick, 1000 - life.settings.speed * 10);
        }
    };

    @Override
    public void onClick(View view) {
        life.randomize();
    }

    class GameView extends View {
        private Paint paint = new Paint();

        public GameView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.GREEN);

            life.draw(canvas, paint);
        }
    }

    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(tick);
    }

    protected void onResume() {
        super.onResume();
        handler.post(tick);
    }
}