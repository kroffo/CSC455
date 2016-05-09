package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Creature {

    public static enum State {
        PLAY,
        PAUSE,
        LOOT,
        FIGHT
    }
    
    private static Player player;
    private int wins;
    private int maxHealth;
    private State state = State.PLAY;

    private Player(Sprite s, Tile l, String name, Armor[] initialArms, Weapon[] initialWeapons, Key[] initialKeys) {
        super(s, l, name, 10, 4, 2, 20, 20, initialArms, initialWeapons, initialKeys, 2);
        initialArms = null;
    }

    public static void createPlayer(Sprite s, Tile l, String name, Armor[] initialArms, Weapon[] initialWeapons, Key[] initialKeys) {
        if (Player.getPlayer() == null) {
            player = new Player(s, l, name, initialArms, initialWeapons, initialKeys);
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        state = s;
    }

    public void select(Item i) {
        if (i instanceof Weapon) {
            if (state == State.PAUSE) {
                equipWeapon(i.getName());
            } else if (state == State.LOOT) {
                
            }
        } else if (i instanceof Armor) {
            if (state == State.PAUSE) {
                equipArmor(i.getName());
            } else if (state == State.LOOT) {
                
            }
        }
    }
    
    public void takeWeaponFromChest(Chest chest, String weaponName) {
        if (chest.containsWeapon(weaponName)) { //See if chest has the item
            satchel.addWeapon(chest.getContents().removeWeapon(weaponName));
        }
    }
	
    public void takeArmorFromChest(Chest chest, String armorName) {
        if (chest.containsArmor(armorName)) { //See if chest has the item
            satchel.addArmor(chest.getContents().removeArmor(armorName));
        }
    }
	
    public void takeKeyFromChest(Chest chest, String keyName) {
        if (chest.containsKey(keyName)) { //See if chest has the item
            satchel.addKey(chest.getContents().removeKey(keyName));
        }
    }

    public int getLevel() {
        return wins/5;
    }
    
}
