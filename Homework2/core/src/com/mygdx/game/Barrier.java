package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class Barrier extends Cell {

    private Sprite barrierSprite;
    
    public Barrier() {
        super("F");
        barrierSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/barrier.png")));
    }

    @Override
    public Sprite getSprite() {
        return barrierSprite;
    }

    /* Sets positions of sprites for this cell */
    @Override
    public void setPosition(int row, int col, float maxHeight) {
        float y = maxHeight - (float)((row+1) * barrierSprite.getHeight());
        float x = (float)(col*barrierSprite.getWidth());
        barrierSprite.setPosition(x, y);
        super.setPosition(row, col, maxHeight);
    }

    public int getTerrainValue() {
        return Integer.MAX_VALUE;
    }
}
