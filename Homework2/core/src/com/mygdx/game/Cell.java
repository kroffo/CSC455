package com.mygdx.game;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public abstract class Cell {

    private ArrayList<Cell> neighbors = new ArrayList<Cell>(0);
    private String text;
    protected boolean visited, crossed, selected, badSelect;
    protected Sprite unvisitedSprite, visitedSprite, crossedSprite, selectedSprite, badSelectSprite;

    public Cell(String text) {
        unvisitedSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/unvisited.png")));
        visitedSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/visited.png")));
        crossedSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/crossed.png")));
        selectedSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/selected.png")));
        badSelectSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/badselect.png")));
        this.text = text;
    }

    /* Sets positions of sprites for this cell */
    public void setPosition(int row, int col, float maxHeight) {
        float y = maxHeight - (float)((row+1) * unvisitedSprite.getHeight());
        float x = (float)(col*unvisitedSprite.getWidth());
        unvisitedSprite.setPosition(x, y);
        visitedSprite.setPosition(x, y);
        crossedSprite.setPosition(x, y);
        selectedSprite.setPosition(x, y);
        badSelectSprite.setPosition(x, y);
    }

    /* returns the correct sprite to draw based on the last path found */
    public Sprite getSprite() {
        if (selected)
            return selectedSprite;
        else if (badSelect)
            return badSelectSprite;
        else if (!visited)
            return unvisitedSprite;
        else if (crossed)
            return crossedSprite;
        else
            return visitedSprite;
    }

    /* selects or deselects cell -- returns true if selected */
    public boolean select() {
        selected = !selected;
        return selected;
    }

    public void badSelect() {
        selected = false;
        badSelect = true;
    }

    public void deselect() {
        selected = false;
        badSelect = false;
    }

    public void addNeighbor(Cell neighbor) {
        neighbors.add(neighbor);
    }

    public Cell[] getNeighbors() {
        return neighbors.toArray(new Cell[neighbors.size()]);
    }

    /* returns the string to be displayed inside this cell */
    public String getText() {
        return text;
    }

    public void visit() {
        visited = true;
    }

    public void cross() {
        crossed = true;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void resetPathFindingVariables() {
        visited = false;
        crossed = false;
    }

    public abstract int getTerrainValue();
}
