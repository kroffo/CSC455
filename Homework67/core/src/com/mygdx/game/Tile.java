package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {

    private Room location;
    private Sprite sprite;
    private Occupant occupant;

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
        sprite.draw(batch);
    }

        public void drawOccupant(SpriteBatch batch) {
        if (occupant != null) {
            occupant.draw(batch);
        }
    }
}
