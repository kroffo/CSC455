package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ChestMenu extends Menu {

    public ChestMenu(Chest chest) {
        super(chest.toString());
        Selectable[] options = new Selectable[1];
        
        boolean unlockable = !chest.isLocked();

        Player p = Player.getPlayer();

        if (chest.isLocked()) {
            Key[] keys = p.getKeys();
            for (Key k : keys)
                if (k.getName().equals(chest.getCorrectKeyName())) {
                    unlockable = true;
                }
        }

        if (unlockable) {
            options[0] = new MenuItem("Unlock");
        } else {
            options = new Selectable[0];
        }
        super.setOptions(options);
    }

}
