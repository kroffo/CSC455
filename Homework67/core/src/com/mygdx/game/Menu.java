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

public class Menu implements Selectable {
    
    private ArrayList<String> options;
    private ArrayList<Selectable> selectables;
    private BitmapFont font;
    private ShapeRenderer renderer;
    private String title, selectionTitle;
    private boolean buttonPressed, exitChosen, selectionMade;
    private Menu selectedMenu = null;
    
    /* Use an int to represent the index of the item selected */
    private int selection;
    
    public Menu(String t) {
        title = t;
        options = new ArrayList<String>(0);
        options.add("Exit");
        selectables = new ArrayList<Selectable>(0);
        selection = 0;
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        reinitialize();
    }

    public void reinitialize() {
        buttonPressed = true;
        selection = 0;
        exitChosen = false;
        selectionMade = false;
        selectedMenu = null;
        selectionTitle = title;
    }

    public void selectByMenu(String prefix) {
        selectionTitle = prefix + title;
    }

    public void setOptions(Selectable[] ops) {
        options = new ArrayList<String>(0);
        selectables = new ArrayList<Selectable>(0);
        for (Selectable o : ops) {
            options.add(o.toString());
            selectables.add(o);
        }
        options.add("Exit");
    }

    public boolean exited() {
        return ((!buttonPressed) && exitChosen);
    }

    public void processInput() {
        if (selectedMenu != null) {
            selectedMenu.processInput();
            if (selectedMenu.exited()) {
                selectedMenu.reinitialize();
                selectedMenu = null;
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                if (!buttonPressed) {
                    buttonPressed = true;
                    selectionMade = true;
                    if (selection == options.size() - 1)
                        exitChosen = true;
                    else { /* Handle other options. */
                        Selectable s = selectables.get(selection);
                        if (s instanceof Menu) {
                            selectedMenu = (Menu)s;
                            selectedMenu.reinitialize();
                            selectedMenu.selectByMenu(this.toString() + " - ");
                        } else if (s instanceof Item) {
                            Player.getPlayer().select((Item)(selectables.get(selection)));
                        }
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (!buttonPressed) {
                    selection++;
                    buttonPressed = true;
                    if (selection == options.size())
                        selection = 0;
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (!buttonPressed) {
                    selection--;
                    buttonPressed = true;
                    if (selection == -1)
                        selection = options.size() - 1;
                }
            } else  {
                buttonPressed = false;
            }
        }
    }

    public void draw(SpriteBatch batch, float screenWidth, float screenHeight) {
        if (selectedMenu != null) {
            selectedMenu.draw(batch, screenWidth, screenHeight);
        } else {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(20, 20, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 40);
            renderer.setColor(Color.GRAY);
            renderer.rect(25, 25, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50);
            // renderer.setColor(Color.VIOLET);
            // renderer.rect(55, Gdx.graphics.getHeight() - 63 - 20*(selection + 1), Gdx.graphics.getWidth() - 130, 15);
            renderer.end();
            batch.begin();
            font.draw(batch, selectionTitle, 40, screenHeight - 40);
            for (int i = 0; i < options.size(); i++) {
                if (i == selection) {
                    font.setColor(Color.YELLOW);
                    font.draw(batch, options.get(i), 60, screenHeight - 70 - 20*i);
                    font.setColor(Color.BLACK);
                } else {
                    font.draw(batch, options.get(i), 60, screenHeight - 70 - 20*i);
                }
            }
            batch.end();
        }
    }

    public String toString() {
        return title;
    }
}
