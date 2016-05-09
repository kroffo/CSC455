package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseMenu extends Menu {

    public PauseMenu() {
        super("Pause Menu");
        Selectable[] options = new Selectable[4];

        Player p = Player.getPlayer();

        Menu weaps = new Menu("Weapons");
        Weapon[] weapons = p.getWeapons();
        Selectable[] weapOpts = new MenuItem[weapons.length];
        for (int i = 0; i < weapons.length; i++)
            weapOpts[i] = new MenuItem(weapons[i]);
        weaps.setOptions(weapOpts); 

        Menu arms = new Menu("Armors");
        Armor[] armors = p.getArmors();
        Selectable[] armOpts = new MenuItem[armors.length];
        for (int i = 0; i < armors.length; i++)
            armOpts[i] = new MenuItem(armors[i]);
        arms.setOptions(armOpts);

        Menu keys = new Menu("Keys");
        Key[] kees = p.getKeys();
        Selectable[] keyOpts = new MenuItem[kees.length];
        for (int i = 0; i < kees.length; i++)
            keyOpts[i] = new MenuItem(kees[i]);
        keys.setOptions(keyOpts);

        options[0] = new StatsMenu();
        options[1] = weaps;
        options[2] = arms;
        options[3] = keys;
        super.setOptions(options);
    }
}
