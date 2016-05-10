package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Creature extends Occupant {

    protected boolean transitioning;
    protected float traversalSpeed;
    protected Satchel satchel;
    protected String name;
    private int health, maxHealth;
    private int strength;
    private int attack;
    private int defense;
    private int agility;
    private int luck;
    private boolean stumbled;

    /* Equipped items */
    private Armor shirt;
    private Armor pants;
    private Armor boots;
    private Armor gloves;
    private Armor shield;
    private Armor helmet;
    private Weapon weapon;

    private Sprite fightSprite;

    public Creature(Sprite s, Sprite fs, Tile l, String name, int startHealth, int startStrength, int startDefense,
                    int startAgility, int startLuck, Satchel satchel, int speed) {
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
        this.satchel = satchel;
        initializeEquipment();
    }

    private void initializeEquipment() {
        Weapon[] weapons = satchel.getWeapons();
        for (Weapon w : weapons)
            if (weapon == null || w.getAttack() > weapon.getAttack())
                weapon = w;
        if (weapon != null)
            equipWeapon(weapon.getName());
        
        Armor[] armors = satchel.getShirts();
        for (Armor w : armors)
            if (shirt == null || w.getDefense() > shirt.getDefense())
                shirt = w;
        if (shirt != null)
            equipArmor(shirt.getName());

        armors = satchel.getPants();
        for (Armor w : armors)
            if (pants == null || w.getDefense() > pants.getDefense())
                pants = w;
        if (pants != null)
            equipArmor(pants.getName());

        armors = satchel.getGloves();
        for (Armor w : armors)
            if (gloves == null || w.getDefense() > gloves.getDefense())
                gloves = w;
        if (gloves != null)
            equipArmor(gloves.getName());

        armors = satchel.getBoots();
        for (Armor w : armors)
            if (boots == null || w.getDefense() > boots.getDefense())
                boots = w;
        if (boots != null)
            equipArmor(boots.getName());

        armors = satchel.getShields();
        for (Armor w : armors)
            if (shield == null || w.getDefense() > shield.getDefense())
                shield = w;
        if (shield != null)
            equipArmor(shield.getName());

        armors = satchel.getHelmets();
        for (Armor w : armors)
            if (helmet == null || w.getDefense() > helmet.getDefense())
                helmet = w;
        if (helmet != null)
            equipArmor(helmet.getName());
    }

    public abstract boolean step();

    public boolean transitioning() {
        return transitioning;
    }

    public void setLocation(Tile l) {
        location = l;
        transitioning = true;
    }

    public Tile getLocation() {
        return location;
    }

    public void unequipItems() {
        if (weapon != null)
            weapon.unequip();
        if (shirt != null)
            shirt.unequip();
        if (pants != null)
            pants.unequip();
        if (boots != null)
            boots.unequip();
        if (gloves != null)
            gloves.unequip();
        if (shield != null)
            shield.unequip();
        if (helmet != null)
            helmet.unequip();
    }

    public boolean equipArmor(String armorName) {
        Armor arm = satchel.getArmor(armorName);
        if (arm != null) {
            Armor.Type type = arm.getType();
            if (type == Armor.Type.SHIRT) {
                if (shirt != null)
                    unequipArmor(shirt.getName());
                shirt = arm;
            } else if (type == Armor.Type.PANTS) {
                if (pants != null)
                    unequipArmor(pants.getName());
                pants = arm;
            } else if (type == Armor.Type.BOOTS) {
                if (boots != null)
                    unequipArmor(boots.getName());
                boots = arm;
            } else if (type == Armor.Type.GLOVES) {
                if (gloves != null)
                    unequipArmor(gloves.getName());
                gloves = arm;
            } else if (type == Armor.Type.SHIELD) {
                if (shield != null)
                    unequipArmor(shield.getName());
                shield = arm;
            } else if (type == Armor.Type.HELMET) {
                if (helmet != null)
                    unequipArmor(helmet.getName());
                helmet = arm;
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
            if (arm.getType() == Armor.Type.SHIRT)
                shirt = null;
            else if (arm.getType() == Armor.Type.PANTS)
                pants = null;
            else if (arm.getType() == Armor.Type.GLOVES)
                gloves = null;
            else if (arm.getType() == Armor.Type.BOOTS)
                boots = null;
            else if (arm.getType() == Armor.Type.SHIELD)
                shield = null;
            else if (arm.getType() == Armor.Type.HELMET)
                helmet = null;
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
            weapon = null;
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
    
    protected void levelUp() {
        attack += 5;
        defense += 5;
        strength += 5;
        agility += 3;
        luck += 2;
    }

    protected void increaseHealth(int h) {
        maxHealth += h;
        health = maxHealth;
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

    public String generateAttackString(int damage, boolean criticalHit) {
        String rv = name + " attacks with the " + getEquippedWeaponName() + " dealing ";
        if (criticalHit) {
            rv = rv + "a critical hit worth ";
        }
        rv = rv + damage + " damage!";
        return rv;
    }

    public String generateBlockString(boolean block) {
        if (block)
            return name + " prepares to take a hit.";
        else
            return name + " prepares to take a hit, but is too weak to hold the enemy back!";
    }

    public String generateFleeString() {
        return name + " flees the battle!";
    }

    public String stumble() {
        stumbled = true;
        return name + " stumbles to the ground!";
    }

    public String unstumble() {
        stumbled = false;
        return name + " gets up and is ready to fight!";
    }

    public boolean stumbled() {
        return stumbled;
    }

    public Sprite getFightSprite() {
        return fightSprite;
    }
}
