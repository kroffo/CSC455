package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.HashMap;
import java.util.ArrayList;

public class Satchel {

    protected HashMap<String, Weapon> weapons;
    protected HashMap<String, Armor> armors;
    protected HashMap<String, Key> keys;
    
    public Satchel() {
        weapons = new HashMap<String, Weapon>();
	armors = new HashMap<String, Armor>();
	keys = new HashMap<String, Key>();
    }

    public Satchel(ArrayList<Weapon> weapons, ArrayList<Armor> armors, ArrayList<Key> keys) {

        this.weapons = new HashMap<String, Weapon>();
	this.armors = new HashMap<String, Armor>();
	this.keys = new HashMap<String, Key>();

        for (Weapon w : weapons)
            addWeapon(w);
        for (Armor a : armors)
            addArmor(a);
        for (Key k : keys)
            addKey(k);
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

    public Armor[] getShirts() {
        ArrayList<Armor> shirts = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.SHIRT)
                shirts.add(a);
        }
        return shirts.toArray(new Armor[shirts.size()]);
    }

    public Armor[] getPants() {
        ArrayList<Armor> pants = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.PANTS)
                pants.add(a);
        }
        return pants.toArray(new Armor[pants.size()]);
    }

    public Armor[] getGloves() {
        ArrayList<Armor> gloves = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.GLOVES)
                gloves.add(a);
        }
        return gloves.toArray(new Armor[gloves.size()]);
    }

    public Armor[] getBoots() {
        ArrayList<Armor> boots = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.BOOTS)
                boots.add(a);
        }
        return boots.toArray(new Armor[boots.size()]);
    }

    public Armor[] getShields() {
        ArrayList<Armor> shields = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.SHIELD)
                shields.add(a);
        }
        return shields.toArray(new Armor[shields.size()]);
    }

    public Armor[] getHelmets() {
        ArrayList<Armor> helmets = new ArrayList<Armor>(0);
        for (Armor a : armors.values().toArray(new Armor[armors.size()])) {
            if (a.getType() == Armor.Type.HELMET)
                helmets.add(a);
        }
        return helmets.toArray(new Armor[helmets.size()]);
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
