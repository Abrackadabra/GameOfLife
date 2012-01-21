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

//1/20/12 6:56 PM

public class PatternActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern);

        Spinner spinner = (Spinner) findViewById(R.id.patternSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.patternNames, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        PatternEditor patternEditor = (PatternEditor)findViewById(R.id.patternEditor);
        if (pos == 1)
            patternEditor.usingPattern = false;
        else
            patternEditor.usingPattern = true;
        if (pos > 1) {
            patternEditor.settings.patternSize = (byte) getResources().getInteger(R.integer.patternSize);
            patternEditor.settings.pattern = new boolean[patternEditor.settings.patternSize][patternEditor.settings.patternSize];
            String[] patterns = getResources().getStringArray(R.array.patterns);
            String[] patternCode = patterns[pos - 2].split(" ");
            patternEditor.cells = new boolean[patternEditor.size][patternEditor.size];
            for (int i = 0; i < patternCode.length; i += 2) {
                patternEditor.cells[Integer.parseInt(patternCode[i])][Integer.parseInt(patternCode[i + 1])] = true;
            }
        }
        patternEditor.save();
        patternEditor.invalidate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void onPause() {
        super.onPause();
        ((PatternEditor)(findViewById(R.id.patternEditor))).save();
    }
}