package com.oncreate.ariadna.ModelsVO;

public class Level {
    private int maxXp;
    private int number;

    public Level(int number, int maxXp) {
        this.number = number;
        this.maxXp = maxXp;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMaxXp() {
        return this.maxXp;
    }

    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }
}
