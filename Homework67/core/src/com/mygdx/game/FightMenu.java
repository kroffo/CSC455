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

public class FightMenu {
    
    private String[] options;
    private FightMenuSelection[] selectables;
    private BitmapFont font;
    private ShapeRenderer renderer;
    private boolean buttonPressed, selectionMade;
    private int selection;

    public FightMenu() {
        
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        options = new String[4];
        selectables = new FightMenuSelection[4];

        options[0] = "Attack";
        options[1] = "Block";
        options[2] = "Dodge";
        options[3] = "Flee";
        
        selectables[0] = new FightMenuSelection("Attack");
        selectables[1] = new FightMenuSelection("Block");
        selectables[2] = new FightMenuSelection("Dodge");
        selectables[3] = new FightMenuSelection("Flee");

        reinitialize();
    }

    public void reinitialize() {
        buttonPressed = true;
        selectionMade = false;
    }

    public void processInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!buttonPressed) {
                buttonPressed = true;
                selectionMade = true;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (!buttonPressed) {
                selection++;
                buttonPressed = true;
                if (selection == options.length)
                    selection = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!buttonPressed) {
                selection--;
                buttonPressed = true;
                if (selection == -1)
                    selection = options.length - 1;
            }
        } else {
            if (selectionMade)
                selectables[selection].select();
            selectionMade = false;
            buttonPressed = false;
        }
    }

    public void draw(SpriteBatch batch, float screenWidth, float screenHeight) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(20, -10, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight()/9 + 10);
        renderer.setColor(Color.GRAY);
        renderer.rect(25, -5, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight()/9);
        renderer.end();
        batch.begin();
        for (int i = 0; i < options.length; i++) {
            if (i == selection) {
                font.setColor(Color.YELLOW);
                font.draw(batch, options[i], 80 + 150*i, screenHeight/12);
                font.setColor(Color.BLACK);
            } else {
                font.draw(batch, options[i], 80 + 150*i, screenHeight/12);
            }
        }
        batch.end();
    }
}
