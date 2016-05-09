package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuItem extends Item {

    Item item;
    String name;

    public MenuItem(Item i) {
        super(i.toString());
        item = i;
    }

    public MenuItem(String name) {
        super(name);
        this.name = name;
    }
    
    public String toString() {
        if (item == null)
            return name;
        return item.toString();
    }
    
    public Item getItem() {
        return item;
    }

}
