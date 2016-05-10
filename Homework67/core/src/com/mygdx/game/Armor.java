package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Armor extends Item {
    private int defense;
    private Type type;

    public enum Type {
        SHIRT,
        PANTS,
        GLOVES,
        BOOTS,
        SHIELD,
        HELMET
    }

    public Armor(String armorName, int startDefense, Type t) {
        super(armorName);
        defense = startDefense;
        type = t;
    }
	
    public String toString() {
        String rv = getName() + " ~ " + "DEF: " + getDefense();
        if (this.isEquipped()) {
            rv = rv + "  (EQUIPPED)";
        }
        return rv;
    }
	
    public Type getType() {
        return type;
    }

    public String getTypeString() {
        if (type == Type.SHIRT)
            return "Shirt";
        else if (type == Type.PANTS)
            return "Pants";
        else if (type == Type.GLOVES)
            return "Gloves";
        else if (type == Type.BOOTS)
            return "Boots";
        else if (type == Type.SHIELD)
            return "Shield";
        else if (type == Type.HELMET)
            return "Helmet";
        else
            return null;
    }
	
    public int getDefense() {
        return defense;
    }
}
