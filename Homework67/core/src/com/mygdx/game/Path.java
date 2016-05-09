package com.mygdx.game;

import java.util.ArrayList;

public class Path implements Comparable<Path>{

    private Cell head;
    private Cell start;
    private ArrayList<Cell> cells = new ArrayList<Cell>(0);
    private int totalTerrain = 0;
    
    public Path(Cell start) {      
        cells.add(start);
        head = start;
    }

    public Path(Path basePath, Cell addition) {
        for (Cell c : basePath.getOrderedArray())
            cells.add(c);
        totalTerrain = basePath.getTotalTerrain();
        addCell(addition);
    }

    public ArrayList<Cell> getCellList() {
        return cells;
    }

    public void addCell(Cell addition) {
        head = addition;
        cells.add(addition);
        totalTerrain += addition.getTerrainValue();
    }

    public Cell getHead() {
        return head;
    }

    public Cell getFirstStep() {
        return cells.get(1);
    }

    public Cell[] getOrderedArray() {
        return cells.toArray(new Cell[cells.size()]);
    }

    public int getTotalTerrain() {
        return totalTerrain;
    }
    
    public int compareTo(Path y) {
        int xval = this.getTotalTerrain(), yval = y.getTotalTerrain();
        if (xval - yval < 0)
            return -1;
        else if (xval == yval)
            return 0;
        return 1;
    }
}
