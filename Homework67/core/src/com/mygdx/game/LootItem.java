package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LootItem extends Item {

    private Item item;
    private String name;
    private boolean taken;

    public LootItem(Item i) {
        super(i.toString());
        item = i;
    }
    
    public String toString() {
        String rv = item.toString();
        if (taken)
            rv = rv + " (taken)";
        return rv;
    }
    
    public Item getItem() {
        return item;
    }

    public void take() {
        taken = true;
    }

    public boolean taken() {
        return taken;
    }

}
