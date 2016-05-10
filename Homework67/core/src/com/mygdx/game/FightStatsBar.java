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

public class FightStatsBar {

    private Creature combatant1;
    private Creature combatant2;
    private BitmapFont font;
    private ShapeRenderer renderer;

    public FightStatsBar(Creature c1, Creature c2) {
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        combatant1 = c1;
        combatant2 = c2;
    }

    public void draw(SpriteBatch batch, float screenWidth, float screenHeight) {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(20, Gdx.graphics.getHeight()*7/9, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() + 10);
            renderer.setColor(Color.GRAY);
            renderer.rect(25, Gdx.graphics.getHeight()*7/9 + 5, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() + 10);   
            renderer.end();
            batch.begin();

            font.draw(batch, combatant1.getName() + ":", 80, screenHeight - 10);
            font.draw(batch, "Health: " + combatant1.getHealth() + "/" + combatant1.getMaxHealth(), 90, screenHeight - 30);
            font.draw(batch, "Weapon: " + combatant1.getEquippedWeaponName(), 90, screenHeight - 50);

            
            font.draw(batch, combatant2.getName() + ":", screenWidth - 320, screenHeight - 10);
            font.draw(batch, "Health: " + combatant2.getHealth() + "/" + combatant2.getMaxHealth(), screenWidth - 310, screenHeight - 30);
            font.draw(batch, "Weapon: " + combatant2.getEquippedWeaponName(), screenWidth - 310, screenHeight - 50);
            batch.end();
    }
}
