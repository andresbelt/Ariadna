package com.oncreate.ariadna;

import android.util.SparseArray;
import java.util.Arrays;

public class ShortcutManager {
    private int attempts;
    private int correctCount;
    private int count;
    private int moduleId;
    private SparseArray<Boolean> results;

    public ShortcutManager(int moduleId, int count) {
        this.moduleId = moduleId;
        this.count = count;
        this.attempts = (int) Math.max(1.0d, ((double) count) * 0.3d);
        this.results = new SparseArray(count);
    }

    public int getModuleId() {
        return this.moduleId;
    }

    public int getCount() {
        return this.count;
    }

    public int getCorrectCount() {
        return this.correctCount;
    }

    public int getAttempts() {
        return this.attempts;
    }

    public void setResult(int index, boolean isCorrect) {
        if (this.results.get(index) == null) {
            this.results.put(index, Boolean.valueOf(isCorrect));
            if (isCorrect) {
                this.correctCount++;
            } else {
                this.attempts--;
            }
        }
    }

    public boolean[] getResults() {
        boolean[] resultArray = new boolean[this.count];
        int answerCount = 0;
        for (int i = 0; i < this.count; i++) {
            Boolean result = (Boolean) this.results.get(i);
            if (result == null) {
                break;
            }
            resultArray[i] = result.booleanValue();
            answerCount++;
        }
        return Arrays.copyOfRange(resultArray, 0, answerCount);
    }

    public boolean isCompleted() {
        return this.attempts > 0 && this.results.size() == this.count;
    }
}
