package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Armor extends Item {
	private int defense;
	private String type;

    public Armor(String armorName, int startDefense, String itemType) {
		super(armorName);
		defense = startDefense;
		type = itemType;
	}
	
	public String toString() {
		String rv = getName() + "\t~ " + "DEF:" + getDefense();
		if (this.isEquipped()) {
			rv = rv + "  (EQUIPPED)";
		}
		return rv;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDefense() {
		return defense;
	}
}
