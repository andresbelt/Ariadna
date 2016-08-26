package com.oncreate.ariadna;

import com.oncreate.ariadna.loginLearn.ServiceResult;

public class ExperienceResult extends ServiceResult {
    private int level;
    private int points;
    private int xp;

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

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
