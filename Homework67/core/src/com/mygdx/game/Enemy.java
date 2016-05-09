package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy extends Creature {
    private String race;
    private boolean searched;

    public enum Behavior {
        FLEE,
        CHASE,
        RANDOMSLOW,
        RANDOMFAST
    }

    public Enemy(Sprite s, Sprite fs, Tile l, String name, int startHealth, int startStrength, int startDefense, int startAgility,
                 int startLuck, Armor[] initialArmors, Weapon[] initialWeapons, Key[] initialKeys, int speed, String race) {
        super(s, fs, l, name, startHealth, startStrength, startDefense, startAgility, startLuck, initialArmors, initialWeapons, initialKeys, speed);
        this.race = race;
        searched = false;
    }

    public String getRace() {
        return race;
    }
	
    public String toString() {
        String rv = race + ":\t" + getName();
        if (isDead()) {
            rv = rv + "\t[DEAD]";
            if (searched) {
                rv = rv + "  (Searched)";
            }
        }
        return rv;
    }
	
    public void searched() {
        searched = true;
    }
    
}
