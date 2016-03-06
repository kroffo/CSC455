package com.mygdx.game;

public class LinkAddress {
    private String id;
    private int row;
    private int col;

    public LinkAddress(String id, int r, int c) {
        this.id = id;
        row = r;
        col = c;
    }

    public String getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
