package com.abragame;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.graphics.*;

import java.io.*;
import java.util.*;
import java.math.*;

//1/17/12 3:01 AM

public class Life {
    private int screenWidth, screenHeight, cellWidth, cellHeight, cellSize;

    private boolean[][] cells;

    Settings settings;

    private Random random = new Random();

    Life(Activity activity) {
        settings = new Settings(activity);

        Display display = activity.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        cellSize = settings.cellSize;
        cellWidth = screenWidth / cellSize;
        cellHeight = screenHeight / cellSize;

        initCells();
    }

    void initCells() {
        cells = new boolean[cellWidth][cellHeight];
        if (settings.patternSize > 0) {
            int startX = cellWidth / 2 - settings.patternSize / 2, startY = cellHeight / 2 - settings.patternSize / 2;
            for (int i = 0; i < settings.patternSize; i++) {
                for (int j = 0; j < settings.patternSize; j++) {
                    cells[startX + i][startY + j] = settings.pattern[i][j];
                }
            }
        } else {
            for (int i = 0; i < cellWidth; i++)
                for (int j = 0; j < cellHeight; j++)
                    cells[i][j] = random.nextBoolean();
        }
    }

    void draw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.GREEN);
        for (int x = 0; x < cellWidth; x++) {
            for (int y = 0; y < cellHeight; y++) {
                if (cells[x][y])
                    canvas.drawRect((float) (x * cellSize), (float) (y * cellSize),
                            (float) (x * cellSize + cellSize), (float) (y * cellSize + cellSize), paint);
            }
        }
    }

    void update() {
        int[][] sums = new int[cellWidth][cellHeight];
        for (int i = 0; i < cellWidth; i++) {
            for (int j = 0; j < cellHeight; j++) {
                if (cells[i][j]) sums[i][j]++;
                if (i == 0) {
                    if (j != 0) sums[i][j] += sums[i][j - 1];
                } else {
                    if (j == 0)
                        sums[i][j] += sums[i - 1][j];
                    else
                        sums[i][j] += sums[i - 1][j] + sums[i][j - 1] - sums[i - 1][j - 1];
                }
            }
        }
        boolean[][] newCells = new boolean[cellWidth][cellHeight];
        for (int i = 0; i < cellWidth; i++) {
            for (int j = 0; j < cellHeight; j++) {
                int neighbours = cells[i][j] ? -1 : 0;
                if (i == 0 || j == 0 || i == cellWidth - 1 || j == cellHeight - 1) {
                    for (int dx = -1; dx <= 1; dx++)
                        for (int dy = -1; dy <= 1; dy++) {
                            int x = (i + dx + cellWidth) % cellWidth;
                            int y = (j + dy + cellHeight) % cellHeight;
                            if (cells[x][y]) neighbours++;
                        }
                } else {
                    neighbours += sums[i + 1][j + 1];
                    if (i > 1) neighbours -= sums[i - 2][j + 1];
                    if (j > 1) neighbours -= sums[i + 1][j - 2];
                    if (i > 1 && j > 1) neighbours += sums[i - 2][j - 2];
                }
                if ((cells[i][j] && settings.cellLives[neighbours]) ||
                        (!cells[i][j] && settings.cellBorn[neighbours]))
                    newCells[i][j] = true;
            }
        }
        cells = newCells;
    }
}