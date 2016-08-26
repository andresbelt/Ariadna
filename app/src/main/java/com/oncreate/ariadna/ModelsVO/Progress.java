package com.oncreate.ariadna.ModelsVO;

import android.util.SparseArray;

public class Progress {
    private int level;
    private SparseArray<Level> levels;
    private SparseArray<LessonProgress> localProgress;
    private int points;
    private int xp;

    public Progress() {
        this.level = 1;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean addXp(int xp) {
        this.xp = Math.max(0, this.xp + xp);
        boolean levelUp = false;
        if (this.levels != null) {
            while (((Level) this.levels.get(this.level)).getMaxXp() <= this.xp) {
                this.level++;
                levelUp = true;
            }
        }
        return levelUp;
    }

    public void addPoints(int points) {
        this.points = Math.max(0, this.points + points);
    }

    public SparseArray<Level> getLevels() {
        if (this.levels == null) {
            this.levels = new SparseArray();
            for (int i = 0; i < 10; i++) {
                this.levels.put(i, new Level(i, (i * i) * 50));
            }
        }
        return this.levels;
    }

    public void setLevels(SparseArray<Level> levels) {
        this.levels = levels;
    }

    public SparseArray<LessonProgress> getLocalProgress() {
        if (this.localProgress == null) {
            this.localProgress = new SparseArray();
        }
        return this.localProgress;
    }

    public void setLocalProgress(SparseArray<LessonProgress> localProgress) {
        this.localProgress = localProgress;
    }

    public void reset() {
        this.localProgress = new SparseArray();
        this.xp = 0;
        this.level = 1;
        this.points = 0;
    }

    public Level getLevelForXp(int xp) {
        for (int i = 1; i < getLevels().size(); i++) {
            if (((Level) this.levels.valueAt(i)).getMaxXp() > xp) {
                return (Level) this.levels.valueAt(i);
            }
        }
        return null;
    }
}
