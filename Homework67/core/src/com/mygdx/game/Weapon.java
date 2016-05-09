package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Weapon extends Item {
    private int attack;
    private int block;
    
    public Weapon(String weaponName, int startAttack, int startBlock) {
        super(weaponName);
        attack = startAttack;
        block = startBlock;
    }
	
    public String toString() {
        String rv = getName() + " ~ ATK: " + attack + "  Block: " + block;
        if (this.isEquipped()) {
            rv = rv + "  (EQUIPPED)";
        }
        return rv;
    }
	
    public int getAttack() {
        return attack;
    }
	
    public int getBlock() {
        return block;
    }

}
