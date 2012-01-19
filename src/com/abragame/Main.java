package com.abragame;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.util.*;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        deleteFile(getResources().getString(R.string.filename));
    }

    public void start(View view) {
        Intent myIntent = new Intent(Main.this, Game.class);
        startActivity(myIntent);
    }

    public void settings(View view) {
        Intent myIntent = new Intent(Main.this, Preferences.class);
        startActivity(myIntent);
    }
}
