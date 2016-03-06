package com.mygdx.game;

import java.util.ArrayList;

public class Path implements Comparable<Path>{

    private Cell head;
    private ArrayList<Cell> cells = new ArrayList<Cell>(0);
    private int totalTerrain = 0;
    private Grid grid;
    
    public Path(Cell start, Grid grid) {
        this.grid = grid;        
        cells.add(start);
        head = start;
    }

    public Path(Path basePath, Cell addition, Grid grid) {
        this.grid = grid;
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

    public Cell[] getOrderedArray() {
        return cells.toArray(new Cell[cells.size()]);
    }

    public int getTotalTerrain() {
        return totalTerrain;
    }

    private int getTotalTerrainWithHeuristic() {
        return totalTerrain + grid.getHeuristic(head);
    }
    
    public int compareTo(Path y) {
        int xval = this.getTotalTerrainWithHeuristic(), yval = y.getTotalTerrainWithHeuristic();
        if (xval - yval < 0)
            return -1;
        else if (xval == yval)
            return 0;
        return 1;
    }
}
