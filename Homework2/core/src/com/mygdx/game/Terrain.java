package com.mygdx.game;

public class Terrain extends Cell {

    private int level;
    
    public Terrain(int level) {
        super(level + "");
        this.level = level;
    }

    public int getTerrainValue() {
        return level;
    }
}
