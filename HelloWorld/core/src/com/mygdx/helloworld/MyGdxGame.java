package com.mygdx.helloworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture texture;
    Pixmap pixmap;
    BitmapFont font;
    Sprite sprite;
    int screenWidth, screenHeight, i = 1, position = 0;;
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);

        
        pixmap.setColor(Color.BLACK);
        
        pixmap.fillRectangle(6,8,2,6);
        pixmap.fillRectangle(8,12,2,4);
        pixmap.fillRectangle(10,8,2,10);
        pixmap.fillRectangle(10,20,2,2);
        pixmap.fillRectangle(12,6,4,2);
        pixmap.fillRectangle(18,6,4,2);
        pixmap.fillRectangle(12,10,10,4);
        pixmap.fillRectangle(14,10,6,2);
        pixmap.fillRectangle(12,10,10,2);
        pixmap.fillRectangle(12,18,2,2);
        pixmap.fillRectangle(20,18,2,2);
        pixmap.fillRectangle(22,20,2,2);
        pixmap.fillRectangle(14,14,6,2);
        pixmap.fillRectangle(12,16,10,2);
        pixmap.fillRectangle(22,8,2,10);
        pixmap.fillRectangle(24,12,2,4);
        pixmap.fillRectangle(26,8,2,6);
        
        texture = new Texture(pixmap);
        pixmap.dispose();
        sprite = new Sprite(texture);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        texture.dispose();
    }
    
    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.setPosition(100,100);
        sprite.draw(batch);
        sprite.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        sprite.draw(batch);
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
    
}
