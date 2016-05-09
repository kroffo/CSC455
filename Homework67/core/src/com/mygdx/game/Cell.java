package com.mygdx.game;

public class Cell {
    
    private Tile tile;
    private boolean visited;
    private Cell[] neighbors;

    public Cell(Tile t) {
        tile = t;
        visited = false;
        neighbors = new Cell[4];
    }

    public Tile getTile() {
        return tile;
    }

    public void setNeighbors(Cell n, Cell e, Cell s, Cell w) {
        neighbors[0] = n;
        neighbors[1] = e;
        neighbors[2] = s;
        neighbors[3] = w;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public int getTerrainValue() {
        return tile.getTerrain();
    }

    public boolean occupied () {
        return tile.occupied();
    }

    public Cell[] getNeighbors() {
        return neighbors;
    }

}
