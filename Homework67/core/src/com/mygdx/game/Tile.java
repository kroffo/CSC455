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
    private int terrain;

    /* Array of neighbors, north, south, east and west */
    private Tile[] neighbors = new Tile[4];

    public Tile(Room l, Sprite s, int t) {
        location = l;
        sprite = s;
        terrain = t;
    }

    public void setNeighbors(Tile n, Tile e, Tile s, Tile w) {
        neighbors[0] = n;
        neighbors[1] = e;
        neighbors[2] = s;
        neighbors[3] = w;
    }

    public int getTerrain() {
        return terrain;
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

    public int getFurthestAvailableNeighbor(Vector2 pos) {
        int neighbor = -1;
        Tile far = this;
        Vector2 farPos = new Vector2(far.getSprite().getX(), far.getSprite().getY());
        float farDist = farPos.dst(pos);
        for (int i = 0; i < neighbors.length; i++) {
            Tile n = neighbors[i];
            if (n != null && (!n.occupied())) {
                Vector2 nPos = new Vector2(n.getSprite().getX(), n.getSprite().getY());
                float nDist = nPos.dst(pos);
                if (nDist > farDist) {
                    neighbor = i;
                    far = n;
                    farPos = nPos;
                    farDist = nDist;
                }
            }
        }
        return neighbor;
    }

    public int getNeighborIndex(Tile t) {
        for (int i = 0; i < neighbors.length; i++)
            if (neighbors[i] == t)
                return i;
        return -1;
    }

    public int getNextPursuitTile(Tile target) {
        if (location.containsTile(target)) {
            Tile[][] tiles = location.getTiles();
            Cell[][] cells = new Cell[tiles.length][tiles[0].length];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    cells[i][j] = new Cell(tiles[i][j]);
                }
            }
            cells[0][0].setNeighbors(cells[0][1], cells[1][0], null, null);
            cells[0][cells[0].length - 1].setNeighbors(null, cells[1][cells[0].length - 1], cells[0][cells[0].length - 2], null);
            cells[cells.length - 1][0].setNeighbors(cells[cells.length - 1][1], null, null, cells[cells.length - 2][0]);
            cells[cells.length - 1][cells[0].length - 1].setNeighbors(null, null, cells[cells.length - 1][cells[0].length - 2], cells[cells.length - 2][cells[0].length - 1]);
            for (int i = 1; i < cells[0].length - 1; i++) {
                cells[0][i].setNeighbors(cells[0][i+1], cells[1][i], cells[0][i-1], null);
            }
            for (int i = 1; i < cells[0].length - 1; i++) {
                cells[cells.length - 1][i].setNeighbors(cells[cells.length - 1][i+1], null, cells[cells.length - 1][i-1], cells[cells.length - 2][i]);
            }
            for (int i = 1; i < cells.length - 1; i++) {
                cells[i][0].setNeighbors(cells[i][1], cells[i+1][0], null, cells[i-1][0]);
            }
            for (int i = 1; i < cells.length - 1; i++) {
                cells[i][cells[0].length - 1].setNeighbors(null, cells[i+1][cells[0].length - 1], cells[i][cells[0].length - 2], cells[i-1][cells[0].length - 1]);
            }
        
            for (int i = 1; i < cells.length - 1; i++) {
                for (int j = 1; j < cells[0].length - 1; j++) {
                    cells[i][j].setNeighbors(cells[i][j+1], cells[i+1][j], cells[i][j-1], cells[i-1][j]);
                }
            }
            Grid g = new Grid(cells);
            Cell current = cells[location.getTileX(this)][location.getTileY(this)];
            Cell targetCell =  cells[location.getTileX(target)][location.getTileY(target)];
            Path p = g.findPath(current, targetCell);
            if (p != null) {
                int neighborIndex = getNeighborIndex(p.getFirstStep().getTile());
                return neighborIndex;
            }
        }
        return -1;
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
