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

//1/17/12 3:03 AM

/**
 * Class containing user preferences: game type, cellSize
 */

public class Settings {
    boolean[] cellLives = {false, false, true, true, false, false, false, false, false};
    boolean[] cellBorn = {false, false, false, true, false, false, false, false, false};
    byte cellSize;
    byte speed;

    private Activity activity;

    Settings(Activity activity) {
        this.activity = activity;
        cellSize = (byte)activity.getResources().getInteger(R.integer.defaultCellSize);
        speed = (byte)activity.getResources().getInteger(R.integer.defaultSpeed);
        try {
            read();
        } catch (Exception e) {
            save();
        }
    }

    void read() throws IOException {
        FileInputStream in = activity.openFileInput(activity.getString(R.string.filename));
        byte[] buffer = new byte[100];
        in.read(buffer);
        for (int i = 0; i <= 8; i++) {
            cellLives[i] = buffer[i] == '1';
        }
        for (int i = 0; i <= 8; i++) {
            cellBorn[i] = buffer[i + 9] == '1';
        }
        cellSize = buffer[18];
        speed = buffer[19];
        in.close();
    }

    void save() {
        try {
            FileOutputStream out = activity.openFileOutput(activity.getString(R.string.filename), Context.MODE_PRIVATE);
            byte[] buffer = new byte[100];
            for (int i = 0; i <= 8; i++) {
                buffer[i] = (byte) (cellLives[i] ? '1' : '0');
            }
            for (int i = 0; i <= 8; i++) {
                buffer[i + 9] = (byte) (cellBorn[i] ? '1' : '0');
            }
            buffer[18] = cellSize;
            buffer[19] = speed;
            out.write(buffer);
            out.close();
        } catch (Exception e) {
            Log.e("GameOfLife", "Unsuccessful saving settings.");
            activity.finish();
        }
    }
}