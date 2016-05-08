package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Creature extends Occupant {

    private boolean transitioning;
    private float traversalSpeed;
    protected Satchel satchel;
    private String name;
    private int health;
    private int strength;
    private int attack;
    private int defense;
    private int agility;
    private int luck;

    /* Equipped items */
    private Armor shirt;
    private Armor pants;
    private Armor boots;
    private Armor gloves;
    private Armor shield;
    private Weapon weapon;

    public Creature(Sprite s, Tile l, String name, int startHealth, int startStrength, int startDefense,
                    int startAgility, int startLuck, Armor[] initialArmors, Weapon[] initialWeapons, Key[] initialKeys, int speed) {
        super(s, l);
        this.name = name;
        this.health = startHealth;
        this.strength = startStrength;
        this.attack = startStrength;
        this.defense = startDefense;
        this.agility = startAgility;
        this.luck = startLuck;
        this.traversalSpeed = speed;
        satchel = new Satchel();
        if (initialArmors != null) {
            for (Armor a : initialArmors) {
                addArmor(a);
                equipArmor(a.getName());
            }
        }
        if (initialWeapons != null) {
            for (Weapon w : initialWeapons) {
                addWeapon(w);
                equipWeapon(w.getName());
            }
        }
        if (initialKeys != null) {
            for (Key k : initialKeys) {
                addKey(k);
            }
        }
    }

    public boolean step() {
        Vector2 target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
        Vector2 position = new Vector2(sprite.getX(), sprite.getY());
        Vector2 velocity = target.sub(position).nor().scl(traversalSpeed);
        sprite.translate(velocity.x, velocity.y);
        target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
        position = new Vector2(sprite.getX(), sprite.getY());
        if ((target.sub(position)).len() < 0.1)
            transitioning = false;
        return !transitioning;
    }

    public boolean transitioning() {
        return transitioning;
    }

    public void setLocation(Tile l) {
        location = l;
        transitioning = true;
    }

    public boolean equipArmor(String armorName) {
        Armor arm = satchel.getArmor(armorName);
        if (arm != null) {
            String type = arm.getType();
            if (type.equals("Shirt")) {
                unequipArmor(shirt);
                shirt = arm;
            } else if (type.equals("Pants")) {
                unequipArmor(pants);
                pants = arm;
            } else if (type.equals("Boots")) {
                unequipArmor(boots);
                boots = arm;
            } else if (type.equals("Gloves")) {
                unequipArmor(gloves);
                gloves = arm;
            } else if (type.equals("Shield")) {
                unequipArmor(shield);
                shield= arm;
            }
            arm.equip();
            defense += arm.getDefense();
            return true;
        }
        return false;
    }
	
    public void unequipArmor(Armor arm) {
        if (arm != null) {
            arm.unequip();
            defense -= arm.getDefense();
        }
    }

    public boolean equipWeapon(String weaponName) {
        Weapon weap = satchel.getWeapon(weaponName);
        if (weap != null) {
            unequipWeapon(weapon);
            weap.equip();
            weapon = weap;
            attack += weap.getAttack();
            return true;
        }
        return false;
    }
    
    public void unequipWeapon(Weapon weap) {
        if (weap != null) {
            weap.unequip();
            attack -= weap.getAttack();
        }
    }
	
    public int getHealth() {
        return health;
    }
	
    public int getAttack() {
        return attack;
    }
	
    public int getStrength() {
        return strength;
    }
		
    public int getDefense() {
        return defense;
    }	
	
    public int getAgility() {
        return agility;
    }
	
    public int getLuck() {
        return luck;
    }
		
    public void addWeapon(Weapon weap) {
        satchel.addWeapon(weap);
    }
	
    public void addArmor(Armor arm) {
        satchel.addArmor(arm);
    }
	
    public void addKey(Key key) {
        satchel.addKey(key);
    }
	
    public void removeWeapon(String weaponName) {
        satchel.removeWeapon(weaponName);
    }
	
    public void removeArmor(String armorName) {
        satchel.removeArmor(armorName);
    }
	
    public void removeKey(String keyName) {
        satchel.removeKey(keyName);
    }

    public String satchelContents() {
        return "\n" + getName() + "'s satchel:\n" + satchel.contents();
    }

    public String getName() {
        return name;
    }
	
    public Weapon takeWeaponFromSatchel(String weaponName) {
        satchel.getWeapon(weaponName).unequip();
        return satchel.removeWeapon(weaponName);
    }
	
    public Armor takeArmorFromSatchel(String armorName) {
        satchel.getArmor(armorName).unequip();
        return satchel.removeArmor(armorName);
    }
	
    public Key takeKeyFromSatchel(String keyName) {
        return satchel.removeKey(keyName);
    }
	
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
	
    public String getEquippedWeaponName() {
        if (weapon != null) {
            return weapon.getName();
        } else {
            return "hands";
        }
    }
	
    public Weapon getEquippedWeapon() {
        return weapon;
    }
	
    public boolean isDead() {
        return health <= 0;
    }
}
