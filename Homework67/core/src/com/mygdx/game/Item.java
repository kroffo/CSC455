package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Item implements Selectable {
    private String name;
    private boolean equipped;
	
    public Item(String itemName) {
        name = itemName;
    }
	
    public void equip() {
        equipped = true;
    }
	
    public void unequip() {
        equipped = false;
    }
	
    public boolean isEquipped() {
        return equipped;
    }
	
    public String getName() {
        return name;
    }
	
    public abstract String toString();
}
