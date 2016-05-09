package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LootMenu extends Menu {

    public LootMenu(String name, Satchel bag) {
        super(name);
        Selectable[] options = new Selectable[3];

        Menu weaps = new Menu("Weapons");
        Weapon[] weapons = bag.getWeapons();
        Selectable[] weapOpts = new LootItem[weapons.length];
        for (int i = 0; i < weapons.length; i++)
            weapOpts[i] = new LootItem(weapons[i]);
        weaps.setOptions(weapOpts); 

        Menu arms = new Menu("Armors");
        Armor[] armors = bag.getArmors();
        Selectable[] armOpts = new LootItem[armors.length];
        for (int i = 0; i < armors.length; i++)
            armOpts[i] = new LootItem(armors[i]);
        arms.setOptions(armOpts);

        Menu keys = new Menu("Keys");
        Key[] kees = bag.getKeys();
        Selectable[] keyOpts = new LootItem[kees.length];
        for (int i = 0; i < kees.length; i++)
            keyOpts[i] = new LootItem(kees[i]);
        keys.setOptions(keyOpts);

        options[0] = weaps;
        options[1] = arms;
        options[2] = keys;
        super.setOptions(options);
    }

}
