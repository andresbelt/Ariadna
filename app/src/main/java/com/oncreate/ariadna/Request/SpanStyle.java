package com.oncreate.ariadna.Request;

public class SpanStyle {
    private int color;
    private int end;
    private int start;

    public SpanStyle(int start, int end, int color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getColor() {
        return this.color;
    }

    public void shift(int shift) {
        this.start += shift;
        this.end += shift;
    }
}
