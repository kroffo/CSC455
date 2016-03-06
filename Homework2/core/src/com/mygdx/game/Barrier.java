package com.mygdx.game;

public class Barrier extends Cell {
    
    public Barrier() {
        super("F");
    }

    public int getTerrainValue() {
        return Integer.MAX_VALUE;
    }
}
