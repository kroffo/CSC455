package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Occupant {

    protected Sprite sprite;
    protected Tile location;
    
    public Occupant(Sprite s, Tile l) {
        sprite = s;
        location = l;
    }

    public Tile getLocation() {
        return location;
    }
    
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
