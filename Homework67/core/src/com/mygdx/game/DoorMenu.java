package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DoorMenu extends Menu {

    public DoorMenu(Door door) {
        super(door.toString());
        Selectable[] options = new Selectable[2];
        
        boolean unlockable = !door.isLocked();

        Player p = Player.getPlayer();

        if (door.isLocked()) {
            Key[] keys = p.getKeys();
            for (Key k : keys)
                if (k.getName().equals(door.getCorrectKeyName())) {
                    unlockable = true;
                }
        }

        if (unlockable) {
            options[0] = new MenuItem("Enter");
            options[1] = new MenuItem("Peek");
        } else {
            options = new Selectable[0];
        }
        super.setOptions(options);
    }

}
