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

//1/17/12 9:56 PM

public class Preferences extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("GameOfLife", "Opening preferences screen.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        settings = new Settings(this);

        updateCheckBoxes();

        SeekBar seekBarCellSize = (SeekBar) findViewById(R.id.seekBarCellSize);
        seekBarCellSize.setOnSeekBarChangeListener(new SeekBarListener());
        seekBarCellSize.setProgress(settings.cellSize - 1);

        SeekBar seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBarListener());
        seekBarSpeed.setProgress(settings.speed);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.gameVariations, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        findFittingRule();
    }

    void updateCheckBoxes() {
        for (int i = 0; i <= 8; i++) {
            ((CheckBox) findViewById(checkBoxesLivesIds[i])).setChecked(settings.cellLives[i]);
            ((CheckBox) findViewById(checkBoxesBornIds[i])).setChecked(settings.cellBorn[i]);
        }
    }

    Settings settings;

    public void checkBoxClick(View view) {
        Log.v("GameOfLife", "Checkbox clicked.");
        boolean value = ((CheckBox) view).isChecked();
        for (int i = 0; i <= 8; i++) {
            if (checkBoxesLivesIds[i] == view.getId()) settings.cellLives[i] = value;
            if (checkBoxesBornIds[i] == view.getId()) settings.cellBorn[i] = value;
        }
        settings.save();
        ((Spinner) findViewById(R.id.spinner)).setSelection(0);
        findFittingRule();
    }

    void findFittingRule() {
        String[] rules = getResources().getStringArray(R.array.gameRules);
        for (int pos = 1; pos < ((Spinner) findViewById(R.id.spinner)).getCount(); pos++) {
            boolean[] lives = new boolean[9];
            boolean[] born = new boolean[9];
            String rule = rules[pos - 1];
            for (int i = 0; i < rule.indexOf("/"); i++) {
                lives[rule.charAt(i) - '0'] = true;
            }
            for (int i = rule.indexOf("/") + 1; i < rule.length(); i++) {
                born[rule.charAt(i) - '0'] = true;
            }
            boolean ok = true;
            for (int i = 0; i <= 8; i++) {
                ok &= lives[i] == settings.cellLives[i];
                ok &= born[i] == settings.cellBorn[i];
            }
            if (ok)
                ((Spinner) findViewById(R.id.spinner)).setSelection(pos);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.v("GameOfLife", "Chosen " + parent.getItemAtPosition(pos).toString());
        if (pos != 0) {
            String[] rules = getResources().getStringArray(R.array.gameRules);
            settings.cellLives = new boolean[9];
            settings.cellBorn = new boolean[9];
            String rule = rules[pos - 1];
            for (int i = 0; i < rule.indexOf("/"); i++) {
                settings.cellLives[rule.charAt(i) - '0'] = true;
            }
            for (int i = rule.indexOf("/") + 1; i < rule.length(); i++) {
                settings.cellBorn[rule.charAt(i) - '0'] = true;
            }
            updateCheckBoxes();
            settings.save();
        }
    }

    public void onNothingSelected(AdapterView parent) {}

    int[] checkBoxesLivesIds = {
            R.id.checkBoxLives0,
            R.id.checkBoxLives1,
            R.id.checkBoxLives2,
            R.id.checkBoxLives3,
            R.id.checkBoxLives4,
            R.id.checkBoxLives5,
            R.id.checkBoxLives6,
            R.id.checkBoxLives7,
            R.id.checkBoxLives8
    };

    int[] checkBoxesBornIds = {
            R.id.checkBoxBorn0,
            R.id.checkBoxBorn1,
            R.id.checkBoxBorn2,
            R.id.checkBoxBorn3,
            R.id.checkBoxBorn4,
            R.id.checkBoxBorn5,
            R.id.checkBoxBorn6,
            R.id.checkBoxBorn7,
            R.id.checkBoxBorn8
    };

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (seekBar.getId() == R.id.seekBarCellSize)
                settings.cellSize = (byte) (progress + 1);
            if (seekBar.getId() == R.id.seekBarSpeed)
                settings.speed = (byte) (progress);
            settings.save();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}