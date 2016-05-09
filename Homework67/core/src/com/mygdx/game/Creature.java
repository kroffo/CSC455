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
    private int health, maxHealth;
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

    private Sprite fightSprite;

    public Creature(Sprite s, Sprite fs, Tile l, String name, int startHealth, int startStrength, int startDefense,
                    int startAgility, int startLuck, Armor[] initialArmors, Weapon[] initialWeapons, Key[] initialKeys, int speed) {
        super(s, l);
        this.fightSprite = fs;
        this.name = name;
        this.health = startHealth;
        this.maxHealth = startHealth;
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
        if (transitioning) {
            Vector2 target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
            Vector2 position = new Vector2(sprite.getX(), sprite.getY());
            Vector2 velocity = target.sub(position).nor().scl(traversalSpeed);
            sprite.translate(velocity.x, velocity.y);
            target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
            position = new Vector2(sprite.getX(), sprite.getY());
            if ((target.sub(position)).len() < 0.1)
                transitioning = false;
        } else {
            sprite.setPosition(location.getSprite().getX(), location.getSprite().getY());
        }
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
                if (shirt != null)
                    unequipArmor(shirt.getName());
                shirt = arm;
            } else if (type.equals("Pants")) {
                if (pants != null)
                    unequipArmor(pants.getName());
                pants = arm;
            } else if (type.equals("Boots")) {
                if (boots != null)
                    unequipArmor(boots.getName());
                boots = arm;
            } else if (type.equals("Gloves")) {
                if (gloves != null)
                    unequipArmor(gloves.getName());
                gloves = arm;
            } else if (type.equals("Shield")) {
                if (shield != null)
                    unequipArmor(shield.getName());
                shield= arm;
            }
            arm.equip();
            defense += arm.getDefense();
            return true;
        }
        return false;
    }
	
    public void unequipArmor(String armorName) {
        Armor arm = satchel.getArmor(armorName);
        if (arm != null) {
            arm.unequip();
            defense -= arm.getDefense();
        }
    }

    public boolean equipWeapon(String weaponName) {
        Weapon weap = satchel.getWeapon(weaponName);
        if (weap != null) {
            if (weapon != null)
                unequipWeapon(weapon.getName());
            weap.equip();
            weapon = weap;
            attack += weap.getAttack();
            return true;
        }
        return false;
    }
    
    public void unequipWeapon(String weaponName) {
        Weapon weap = satchel.getWeapon(weaponName);
        if (weap != null) {
            weap.unequip();
            attack -= weap.getAttack();
        }
    }

    public Weapon[] getWeapons() {
        return satchel.getWeapons();
    }

    public Armor[] getArmors() {
        return satchel.getArmors();
    }

    public Key[] getKeys() {
        return satchel.getKeys();
    }
	
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
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
