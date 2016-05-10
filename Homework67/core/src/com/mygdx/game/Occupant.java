package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Occupant {

    protected Sprite sprite;
    protected Tile location;
    
    /* north:0 east:1 south:2 west:3 */
    protected int orientation;


    public Occupant(Sprite s, Tile l) {
        sprite = s;
        location = l;
    }

    public Tile getLocation() {
        return location;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean setOrientation(int d) {
        if (d < 0 || d > 3)
            return false;
        while (d != orientation) {
            orientation += 1;
            sprite.rotate90(true);
            if (orientation == 4)
                orientation = 0;
        }
        return true;            
    }

    public void fixPosition() {
        sprite.setPosition(location.getSprite().getX(), location.getSprite().getY());
    }
    
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
