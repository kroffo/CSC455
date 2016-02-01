package com.mygdx.helloworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    TextureAtlas invader1Atlas, invader2Atlas, invader3Atlas;
    Sprite[][] invaders = new Sprite[5][11];
    int screenWidth, screenHeight, i = 1, position = 0,
        currentFrame = 0;
    String currentAtlasKey = new String("0001");
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        invader1Atlas = new TextureAtlas(Gdx.files.internal("Sprites/Invader" + 1 + ".atlas"));
        invader2Atlas = new TextureAtlas(Gdx.files.internal("Sprites/Invader" + 2 + ".atlas"));
        invader3Atlas = new TextureAtlas(Gdx.files.internal("Sprites/Invader" + 3 + ".atlas"));
        generateRow(1);
        generateRow(2);
        generateRow(3);
        generateRow(4);
        generateRow(5);
    }

    @Override
    public void dispose() {
        batch.dispose();
        //font.dispose();
        //texture.dispose();
        invader1Atlas.dispose();
        invader2Atlas.dispose();
        invader3Atlas.dispose();
    }
    
    @Override
    public void render () {
        currentFrame++;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (int i = 0; i < 5; i++)
            for (Sprite sprite : invaders[i]) {
                sprite.draw(batch);
                
            }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
    
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    public void generateRow(int row) {
        final TextureAtlas textureAtlas;
        switch (row) {
        case 1:
            textureAtlas = invader3Atlas;
            break;
        case 2:
            textureAtlas = invader1Atlas;
            break;
        case 3:
            textureAtlas = invader1Atlas;
            break;
        case 4:
            textureAtlas = invader2Atlas;
            break;
        case 5:
            textureAtlas = invader2Atlas;
            break;
        default: return;
        }
        AtlasRegion region = textureAtlas.findRegion("0001");
        for (int i = 0; i < 11; i++) {
            invaders[row-1][i] = new Sprite(region);
            final Sprite sprite = invaders[row-1][i];
            sprite.setPosition(120 + i*40, 500 - 40*row);
            sprite.scale(1.5f);
            Timer.schedule(new Task() {
                    @Override
                    public void run() {
                        if (currentFrame > 19)
                            currentFrame = 0;
                        currentAtlasKey = "000" + (currentFrame/10 + 1);
                        sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
                    }
                },0,1/30.0f);
            // font = new BitmapFont();
            // font.setColor(Color.RED);
        }
    }
}
