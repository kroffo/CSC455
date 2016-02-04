package com.mygdx.helloworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    //    TextureAtlas invader1Atlas, invader2Atlas, invader3Atlas;
    Sprite[][][] invaderSprites;
    Sprite playerShip;
    Sprite bullet;
    Sprite alienShip;
    boolean[][] invaders;
    float elapsedTime = 0, nextTime;
    int screenWidth, screenHeight, i = 1, position = 0, state = 0,
        currentFrame = 0, horizontalBase = 120, verticalBase = 400;
    String currentAtlasKey = new String("0001");
    boolean headingLeft = true, bulletShot = false, alienCrossedLeft = false, alienTraveling = false;
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        playerShip = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/PlayerShip.png")));
        playerShip.setScale(2,2);
        playerShip.setOrigin(15,5);
        bullet = new Sprite(new Texture(Gdx.files.internal("Sprites/Bullet.png")));
        alienShip = new Sprite(new Texture(Gdx.files.internal("Sprites/SpaceShip/SpaceShip.png")));
        alienShip.setScale(2,2);
        invaderSprites = new Sprite[5][11][2];
        invaders = new boolean[5][11];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader3/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader3/0002.png")));
                invaderSprites[i][j][0].setScale(1.5f,1.5f);
                invaderSprites[i][j][1].setScale(1.5f,1.5f);
                invaders[i][j] = true;
            }
        }
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader1/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader1/0002.png")));
                invaderSprites[i][j][0].setScale(1.5f,1.5f);
                invaderSprites[i][j][1].setScale(1.5f,1.5f);
                invaders[i][j] = true;
            }
        }
        for (int i = 3; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader2/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader2/0002.png")));
                invaderSprites[i][j][0].setScale(1.5f,1.5f);
                invaderSprites[i][j][1].setScale(1.5f,1.5f);
                invaders[i][j] = true;
            }
        }
        nextTime = getSpeed();
    }

    @Override
    public void dispose() {
        batch.dispose();
        // invader1Atlas.dispose();
        // invader2Atlas.dispose();
        // invader3Atlas.dispose();
    }
    
    @Override
    public void render () {
        if (currentFrame == 0) playerShip.setPosition(screenWidth/2, 150);
        currentFrame++;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime > nextTime) {
            if (++state == 2) state = 0;
            if (headingLeft) {
                horizontalBase -= 5;
                if (horizontalBase < 0) {
                    horizontalBase += 10;
                    headingLeft = false;
                    verticalBase -= 13;
                }
            } else {
                horizontalBase += 5;
                if (horizontalBase > screenWidth - 20*invaders[0].length) {
                    horizontalBase -= 10;
                    headingLeft = true;
                    verticalBase -= 13;
                }
            }
            nextTime += getSpeed();
            
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                if (invaders[i][j]) {
                    invaderSprites[i][j][state].setPosition(horizontalBase + j*25, verticalBase - 20*i);            
                    invaderSprites[i][j][state].draw(batch);
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            if (playerShip.getX() > 10)
                playerShip.translateX(-2f);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            if (playerShip.getX() < screenWidth - 25)
                playerShip.translateX(2f);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!bulletShot) {
                bullet.setPosition(playerShip.getX(), playerShip.getY());
                bulletShot = true;
            }
        }
        if (bulletShot) {
            bullet.translateY(4f);
            if (bullet.getY() > 440)
                bulletShot = false;
            bullet.draw(batch);
        }
        playerShip.draw(batch);
        if (currentFrame % 1000 == 0) {
            alienTraveling = true;
            if (alienCrossedLeft)
                alienShip.setPosition(-30,420);
            else
                alienShip.setPosition(screenWidth + 30, 420);
            alienCrossedLeft = !alienCrossedLeft;
        }
        if (alienTraveling) {
            boolean stopAlien = false;
            if (!alienCrossedLeft) {
                alienShip.translateX(1.0f);
                stopAlien = alienShip.getX() > screenWidth;
            } else {
                alienShip.translateX(-1.0f);
                stopAlien = alienShip.getX() < 0;
            }
            if (stopAlien) {
                alienTraveling = false;
            }
            alienShip.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = /*width*/640;
        screenHeight = height;
    }
    
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private float getSpeed() {
        int numberAlive = 0;
        for (int i = 0; i < invaders.length; i++) {
            for (int j = 0; j < invaders[0].length; j++) {
                if (invaders[i][j]) numberAlive++;
            }
        }
        if (numberAlive < 11)
            return 1/5;
        return numberAlive/55;
    }
}
