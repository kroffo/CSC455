package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class FightOutputDisplay {

    private ArrayList<String> lines;
    private BitmapFont font;
    private ShapeRenderer renderer;

    public FightOutputDisplay(ArrayList<String> outputs) {
        lines = outputs;
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    public void draw(SpriteBatch batch, float screenWidth, float screenHeight) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(20, -10, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight()*2/9 + 10);
        renderer.setColor(Color.GRAY);
        renderer.rect(25, -5, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight()*2/9);   
        renderer.end();
        batch.begin();
        for (int i = 0; i < lines.size(); i++) {
            font.draw(batch, lines.get(i), 80,screenHeight*2/9 - 10 - i*20);
        }
        batch.end();
    }
}
