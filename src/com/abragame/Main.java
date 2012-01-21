package com.abragame;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //deleteFile(getResources().getString(R.string.filename));
    }

    public void start(View view) {
        Intent myIntent = new Intent(Main.this, GameActivity.class);
        startActivity(myIntent);
    }

    public void settings(View view) {
        Intent myIntent = new Intent(Main.this, SettingsActivity.class);
        startActivity(myIntent);
    }
    
    public void pattern(View view) {
        Intent myIntent = new Intent(Main.this, PatternActivity.class);
        startActivity(myIntent);
    }
}
