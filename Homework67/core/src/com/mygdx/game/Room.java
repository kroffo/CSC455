package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class Room {

    private Tile[][] tiles = new Tile[9][7];
    private ArrayList<Occupant> occupants = new ArrayList<Occupant>(0);
    private ArrayList<Room> neighboringRooms = new ArrayList<Room>(0);
    
    public Room(String tile_image_name, float width, float height) {
        initializeTiles(tile_image_name, width, height);
    } 

    private void initializeTiles(String image_name, float width, float height) {
        double startX = (width - (new Sprite(new Texture(image_name)).getWidth()*tiles.length))/2;
        double startY = (height - (new Sprite(new Texture(image_name)).getWidth()*tiles[0].length))/2;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Sprite s = new Sprite(new Texture(image_name));
                s.setOrigin(s.getWidth()/2, s.getHeight()/2);
                s.setPosition((float)(startX + i*s.getWidth()), (float)(startY + j*s.getHeight()));
                tiles[i][j] = new Tile(this, s, 1);                
            }
        }
        tiles[0][0].setNeighbors(tiles[0][1], tiles[1][0], null, null);
        tiles[0][tiles[0].length - 1].setNeighbors(null, tiles[1][tiles[0].length - 1], tiles[0][tiles[0].length - 2], null);
        tiles[tiles.length - 1][0].setNeighbors(tiles[tiles.length - 1][1], null, null, tiles[tiles.length - 2][0]);
        tiles[tiles.length - 1][tiles[0].length - 1].setNeighbors(null, null, tiles[tiles.length - 1][tiles[0].length - 2], tiles[tiles.length - 2][tiles[0].length - 1]);
        for (int i = 1; i < tiles[0].length - 1; i++) {
            tiles[0][i].setNeighbors(tiles[0][i+1], tiles[1][i], tiles[0][i-1], null);
        }
        for (int i = 1; i < tiles[0].length - 1; i++) {
            tiles[tiles.length - 1][i].setNeighbors(tiles[tiles.length - 1][i+1], null, tiles[tiles.length - 1][i-1], tiles[tiles.length - 2][i]);
        }
        for (int i = 1; i < tiles.length - 1; i++) {
            tiles[i][0].setNeighbors(tiles[i][1], tiles[i+1][0], null, tiles[i-1][0]);
        }
        for (int i = 1; i < tiles.length - 1; i++) {
            tiles[i][tiles[0].length - 1].setNeighbors(null, tiles[i+1][tiles[0].length - 1], tiles[i][tiles[0].length - 2], tiles[i-1][tiles[0].length - 1]);
        }
        
        for (int i = 1; i < tiles.length - 1; i++) {
            for (int j = 1; j < tiles[0].length - 1; j++) {
                tiles[i][j].setNeighbors(tiles[i][j+1], tiles[i+1][j], tiles[i][j-1], tiles[i-1][j]);
            }
        }
    }

    public boolean containsTile(Tile t) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == t)
                    return true;
            }
        }
        return false;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x > tiles.length || y < 0 || y > tiles[0].length)
            return null;
        return tiles[x][y];
    }

    public int getTileX(Tile t) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == t)
                    return i;
            }
        }
        return -1;
    }

    public int getTileY(Tile t) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == t)
                    return j;
            }
        }
        return -1;
    }

    protected Tile[][] getTiles() {
        return tiles;
    }

    public void addDoor(Door d, int x, int y, int direction) {
        tiles[x][y].setDoor(d, direction);
        neighboringRooms.add(d.getNeighbor(this));
    }

    public Player addPlayer(String imageName, String fightImageName, int x, int y, String name, Satchel satchel) {
        Player addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());

            Sprite fs = new Sprite(new Texture(fightImageName));
            fs.setOrigin(fs.getWidth()/2, fs.getHeight()/2);
            fs.setPosition(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);

            Armor[] initialArms = new Armor[3];
            Player.createPlayer(s, fs, placement, this, name, satchel);
            addition = Player.getPlayer();
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Enemy addEnemy(String imageName, String fightImageName, int x, int y, String name, int startHealth, int startStrength, int startDefense, int startAgility,
                          int startLuck, Satchel satchel, int speed, String race, String behavior) {
        Enemy addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            
            Sprite fs = new Sprite(new Texture(imageName));
            fs.setOrigin(fs.getWidth()/2, fs.getHeight()/2);
            fs.setPosition(Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()*2/3);

            addition = new Enemy(s, fs, placement, name, startHealth, startStrength, startDefense, startAgility,
                                 startLuck, satchel, speed, race, behavior);
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Chest addChest(String imageName, int x, int y, String name, boolean lockState, Key key, Satchel satchel) {
        Chest addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            addition = new Chest(s, placement, name, lockState, key, satchel);
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Occupant addBarrier(String imageName, int x, int y) {
        Occupant addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            addition = new Occupant(s, placement);
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Occupant[] getOccupants() {
        return occupants.toArray(new Occupant[occupants.size()]);
    }
    
    public void removeOccupant(Occupant o) {
        occupants.remove(o);
    }

    public void addOccupant(Occupant o) {
        occupants.add(o);
    }

    public Room[] getNeighboringRooms() {
        return neighboringRooms.toArray(new Room[neighboringRooms.size()]);
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].draw(batch);
            }
        }
        for (Occupant o : occupants)
            o.draw(batch);
    }
}
