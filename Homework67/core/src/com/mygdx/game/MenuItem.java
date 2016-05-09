package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuItem extends Item {

    public MenuItem(String name) {
        super(name);
    }
    
    public String toString() {
        return super.getName();
    }
    
}
