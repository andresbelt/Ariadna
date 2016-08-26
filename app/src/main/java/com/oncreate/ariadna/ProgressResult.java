package com.oncreate.ariadna;


import com.oncreate.ariadna.ModelsVO.LessonProgress;
import com.oncreate.ariadna.ModelsVO.Level;

import java.util.ArrayList;

public class ProgressResult extends ExperienceResult {
    private ArrayList<Level> levels;
    private ArrayList<LessonProgress> progress;

    public ArrayList<LessonProgress> getProgress() {
        return this.progress;
    }

    public void setProgress(ArrayList<LessonProgress> progress) {
        this.progress = progress;
    }

    public ArrayList<Level> getLevels() {
        return this.levels;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }
}
