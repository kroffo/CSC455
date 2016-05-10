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
    
    private String[] options;
    private Selectable[] selectables;
    private BitmapFont font;
    private ShapeRenderer renderer;
    private String title, selectionTitle;
    private boolean buttonPressed, exitChosen, selectionMade;
    private Menu selectedMenu = null;
    
    /* Use an int to represent the index of the item selected */
    private int selection;
    
    public Menu(String t) {
        title = t;
        options = new String[1];
        options[0] = "Exit";
        selectables = new Selectable[0];
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
        updateValues();
    }

    public void updateValues() {
        for (int i = 0; i < selectables.length; i++) {
            options[i] = selectables[i].toString();
            if (selectables[i] instanceof StatsMenu)
                ((StatsMenu)selectables[i]).updateValues();
        }
    }

    public void removeIndex(int i) {
        if (i < 0 || i >= selectables.length)
            return;
        String[] opts = new String[options.length - 1];
        Selectable[] selects = new Selectable[selectables.length - 1];
        boolean passed = false;
        for (int j = 0; j < selects.length; j++) {
            if (j == i)
                passed = true;
            if (passed) {
                opts[j] = options[j+1];
                selects[j] = selectables[j+1];
            } else {
                opts[j] = options[j];
                selects[j] = selectables[j];
            }
        }
        opts[opts.length-1] = "Exit";
        options = opts;
        selectables = selects;
        updateValues();
    }

    public void selectByMenu(String prefix) {
        selectionTitle = prefix + title;
    }

    public void setOptions(Selectable[] ops) {
        options = new String[ops.length + 1];
        selectables = new Selectable[ops.length];
        for (int i = 0; i < ops.length; i++) {
            options[i] = ops[i].toString();
            selectables[i] = ops[i];
        }
        options[options.length - 1] = "Exit";
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
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (!buttonPressed) {
                    buttonPressed = true;
                    selectionMade = true;
                    if (selection == options.length - 1)
                        exitChosen = true;
                    else { /* Handle other options. */
                        Selectable s = selectables[selection];
                        if (s instanceof Menu) {
                            selectedMenu = (Menu)s;
                            selectedMenu.reinitialize();
                            selectedMenu.selectByMenu(this.toString() + " - ");
                        } else if (s instanceof MenuItem) {
                            Player.getPlayer().select(((MenuItem)s));
                            updateValues();
                        } else if (s instanceof LootItem) {
                            if (!((LootItem)s).taken()) {
                                Player.getPlayer().select(((LootItem)s));
                                removeIndex(selection);
                            }
                        } else if (s instanceof FightOption) {
                            reinitialize();
                            Player.getPlayer().startFight();
                        }
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (!buttonPressed) {
                    selection++;
                    buttonPressed = true;
                    if (selection == options.length)
                        selection = 0;
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (!buttonPressed) {
                    selection--;
                    buttonPressed = true;
                    if (selection == -1)
                        selection = options.length - 1;
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
            renderer.end();
            batch.begin();
            font.draw(batch, selectionTitle, 40, screenHeight - 40);
            for (int i = 0; i < options.length; i++) {
                if (i == selection) {
                    font.setColor(Color.YELLOW);
                    font.draw(batch, options[i], 60, screenHeight - 70 - 20*i);
                    font.setColor(Color.BLACK);
                } else {
                    font.draw(batch, options[i], 60, screenHeight - 70 - 20*i);
                }
            }
            batch.end();
        }
    }

    public String toString() {
        return title;
    }
}
