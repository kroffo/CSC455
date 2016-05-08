package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Key extends Item {
	
	public Key(String keyName) {
		super(keyName);
	}
	
	public String toString() {
		return getName();
	}	
}
