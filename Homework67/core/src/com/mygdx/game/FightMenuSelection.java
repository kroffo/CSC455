package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FightMenuSelection {

    String name;

    public FightMenuSelection(String n) {
        name = n;
    }
    
    public String toString() {
        return name;
    }

    public void select() {
        Player.getPlayer().selectFightOption(name);
    }
}
