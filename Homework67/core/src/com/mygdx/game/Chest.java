package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Chest extends Occupant {
    private String name; 
    private boolean locked;
    private Key correctKey;
    private boolean searched;
    private Satchel contents;
	
    public Chest(Sprite sprite, Tile location, String chestName, boolean lockState, Key key, Satchel satchel) {
        super(sprite, location);
        name = chestName;
        locked = lockState;
        correctKey = key;
        searched = false;
        contents = satchel;
    }
	
    public String toString() {
        if (locked) {
            return name + "\t(Locked)";
        }
        if (searched) {
            return name + "\t(Searched)";
        }
        return name;
    }
	
    public String getName() {
        return name;
    }

    public boolean isLocked() {
        return locked;
    }
	
    public String getCorrectKeyName() {
        return correctKey.getName();
    }

    public boolean unlock(Key k) {
        if (k.getName().equals(correctKey.getName())) {
            locked = false;
            return true;
        }
        return false;
    }
	
    public boolean containsWeapon(String weaponName) {
       return contents.containsWeapon(weaponName);
    }
	
    public boolean containsArmor(String armorName) {
        return contents.containsArmor(armorName);
    }
	
    public boolean containsKey(String keyName) {
        return contents.containsKey(keyName);
    }

    public Satchel getContents() {
        return contents;
    }
    
}
