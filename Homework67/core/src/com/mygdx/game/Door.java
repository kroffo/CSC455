package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Door {

    private Room room1;
    private int x1, y1, x2, y2;
    private Room room2;
    private boolean locked;
    private String name;
    private Key correctKey;
    private Sprite sprite;
	
    public Door(String doorName, Sprite s, Room r1, int x1, int y1, int d1, Room r2, int x2, int y2, int d2, boolean lockState, Key key) {
        sprite = s;
        room1 = r1;
        room2 = r2;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        room1.addDoor(this, x1, y1, d1);
        room2.addDoor(this, x2, y2, d2);
        name = doorName;
        locked = lockState;
        correctKey = key;
    }

    public Sprite getSprite() {
        return sprite;
    }
	
    public String toString() {
        if (locked) {
            return name + " (Locked)";
        }
        return name;
    }
	
    public String getCorrectKeyName() {
        return correctKey.getName();
    }
	
    public boolean isLocked() {
        return locked;
    }
	
    public boolean unlock(Key k) {
        if (k.getName().equals(correctKey.getName())) {
            locked = false;
            return true;
        }
        return false;
    }
	
    public Room getNeighbor(Room current) {
        if (room1.equals(current)) {
            return room2;
        } 
        return room1;
    }

    public Tile getOtherTile(Room current) {
        if (room1 == current)
            return room2.getTile(x2,y2);
        return room1.getTile(x1,y1);
    }
	
    public String getName() {
        return name;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
