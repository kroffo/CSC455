package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.HashMap;

public class Satchel {

    protected HashMap<String, Weapon> weapons;
    protected HashMap<String, Armor> armors;
    protected HashMap<String, Key> keys;
    
    public Satchel() {
        weapons = new HashMap<String, Weapon>();
	armors = new HashMap<String, Armor>();
	keys = new HashMap<String, Key>();
    }

    public Weapon[] getWeapons() {
        return weapons.values().toArray(new Weapon[weapons.size()]);
    }

    public Armor[] getArmors() {
        return armors.values().toArray(new Armor[armors.size()]);
    }

    public Key[] getKeys() {
        return keys.values().toArray(new Key[keys.size()]);
    }
	
    public void addKey(Key key) {
        if (key != null)
            keys.put(key.getName(), key);
    }
	
    public void addArmor(Armor arm) {
        if (arm != null)
            armors.put(arm.getName(), arm);
    }
	
    public void addWeapon(Weapon weap) {
        if (weap != null)
            weapons.put(weap.getName(), weap);
    }
	
    public Weapon getWeapon(String weaponName) {
        return weapons.get(weaponName);
    }
	
    public Armor getArmor(String armorName) {
        return armors.get(armorName);
    }
	
    public Key getKey(String keyName) {
        return keys.get(keyName);
    }
	
    public Weapon removeWeapon(String weaponName) {
        Weapon weapon = weapons.get(weaponName);
        weapons.remove(weaponName);
        return weapon;
    }
	
    public Armor removeArmor(String armorName) {
        Armor armor = armors.get(armorName);
        armors.remove(armorName);
        return armor;
    }
	
    public Key removeKey(String keyName) {
        Key key = keys.get(keyName);
        keys.remove(keyName);
        return key;
    }
	
    public boolean containsWeapon(String weaponName) {
        if (weapons.get(weaponName) != null) {
            return true;
        }
        return false;
    }
	
    public boolean containsArmor(String armorName) {
        if (armors.get(armorName) != null) {
            return true;
        }
        return false;
    }
	
    public boolean containsKey(String keyName) {
        if (keys.get(keyName) != null) {
            return true;
        }
        return false;
    }
	
    public String displayWeapons() {
        String rv = "  Weapons:\n";
        for (Weapon weap : weapons.values()) {
            rv = rv + "    " + weap + "\n";
        }
        return rv;
    }
	
    public String displayArmors() {
        String rv = "  Armors:\n";
        for (Armor arm : armors.values()) {
            rv = rv + "    " + arm + "\n";
        }
        return rv;
    }
	
    public String displayKeys() {
        String rv = "  Keys:\n";
        for (Key k : keys.values()) {
            rv = rv + "    " + k + "\n";
        }
        return rv;
    }
	
    public String contents() {
        return displayWeapons() + displayArmors() + displayKeys();
    }
}
