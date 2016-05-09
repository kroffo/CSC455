package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {

    private Room location;
    private Sprite sprite;
    private Occupant occupant;
    private Door door;
    private int doorLocation;

    /* Array of neighbors, north, south, east and west */
    private Tile[] neighbors = new Tile[4];

    public Tile(Room l, Sprite s) {
        location = l;
        sprite = s;
    }

    public void setNeighbors(Tile n, Tile e, Tile s, Tile w) {
        neighbors[0] = n;
        neighbors[1] = e;
        neighbors[2] = s;
        neighbors[3] = w;
    }

    public void setDoor(Door d, int direction) {
        door = d;
        doorLocation = direction;
    }

    public boolean doorAt(int direction) {
        return (door != null && doorLocation == direction);
    }

    public Door getDoor() {
        return door;
    }

    public Room getRoom() {
        return location;
    }
    
    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public Occupant getOccupant() {
        return occupant;
    }
    
    public void setOccupant(Occupant o) {
        occupant = o;
    }

    public void removeOccupant() {
        occupant = null;
    }
    
    public boolean occupied() {
        return occupant != null;
    }

    public boolean chestAt(int d) {
        if (d < 0 || d > 3)
            return false;
        if (neighbors[d] != null)
            return (neighbors[d].getOccupant() instanceof Chest);
        return false;
    }

    public boolean enemyAt(int d) {
        if (d < 0 || d > 3)
            return false;
        if (neighbors[d] != null)
            return (neighbors[d].getOccupant() instanceof Enemy);
        return false;
    }

    public Occupant getNeighboringOccupant(int d) {
        if (d < 0 || d > 3)
            return null;
        if (neighbors[d] != null)
            return neighbors[d].getOccupant();
        return null;
    }

    /* Tests whether the specified neighbor is unoccupied */
    /*                                                    */
    /* north=0  east=1  south=2  west=3                   */
    public boolean neighborAvailable(int i) {
        if (i < 0 || i > 3)
            return false;
        if (neighbors[i] != null)
            return !(neighbors[i].occupied());
        return false;
    }

    public void moveOccupant(int i) {
        if (occupant instanceof Creature) {
            if (i < 0 || i > 3)
                return;
            Tile neighbor = neighbors[i];
            neighbor.setOccupant(occupant);
            ((Creature)occupant).setLocation(neighbor);
            this.removeOccupant();
        }
    }

    public void draw(SpriteBatch batch) {
        if (door != null) {
            float x = sprite.getX();
            float y = sprite.getY();
            if (doorLocation == 0)
                y += sprite.getHeight()/4;
            else if (doorLocation == 1)
                x += sprite.getHeight()/4;
            else if (doorLocation == 2)
                y -= sprite.getHeight()/4;
            else if (doorLocation == 3)
                x -= sprite.getWidth()/4;
            door.getSprite().setPosition(x,y);
            door.draw(batch);
        }
        sprite.draw(batch);
    }

    public void drawOccupant(SpriteBatch batch) {
        if (occupant != null) {
            occupant.draw(batch);
        }
    }
}
