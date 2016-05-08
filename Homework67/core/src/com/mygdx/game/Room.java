package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class Room {

    private Tile[][] tiles = new Tile[9][7];
    private ArrayList<Occupant> occupants = new ArrayList<Occupant>(0);
    
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
                tiles[i][j] = new Tile(this, s);                
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

    public Player addPlayer(String imageName, int x, int y, String name) {
        Player addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            Armor[] initialArms = new Armor[3];
            initialArms[0] = new Armor("Cloth Shirt", 0, "Shirt");
            initialArms[1] = new Armor("Cloth Shoes", 0, "Boots");
            initialArms[2] = new Armor("Cloth Pants", 0, "Pants");
            addition = new Player(s, placement, name, initialArms, null, null);
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Enemy addEnemy(String imageName, int x, int y, String name, int startHealth, int startStrength, int startDefense, int startAgility,
                 int startLuck, Armor[] initialArmors, Weapon[] initialWeapons, Key[] initialKeys, int speed, String race) {
        Enemy addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            addition = new Enemy(s, placement, name, startHealth, startStrength, startDefense, startAgility,
                                 startLuck, initialArmors, initialWeapons, initialKeys, speed, race);
            placement.setOccupant(addition);
            occupants.add(addition);
            return addition;
        }
    }

    public Chest addChest(String imageName, int x, int y, String name, boolean lockState, Key key) {
        Chest addition;
        Tile placement = tiles[x][y];
        if (placement.occupied())
            return null;
        else {
            Sprite s = new Sprite(new Texture(imageName));
            s.setOrigin(s.getWidth()/2, s.getHeight()/2);
            s.setPosition(placement.getSprite().getX(), placement.getSprite().getY());
            addition = new Chest(s, placement, name, lockState, key);
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
