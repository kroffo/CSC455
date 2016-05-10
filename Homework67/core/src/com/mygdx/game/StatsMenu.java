package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StatsMenu extends Menu {

    public StatsMenu() {
        super("Stats");
        updateValues();
    }

    @Override
    public void updateValues() {
        Selectable[] statOpts = new MenuItem[9];
        Player p = Player.getPlayer();
        statOpts[0] = new MenuItem("Name: " + p.getName());
        statOpts[1] = new MenuItem("Level: " + p.getLevel());
        statOpts[2] = new MenuItem("Health: " + p.getHealth() + "/" + p.getMaxHealth());
        String weaponString = "Weapon: " + p.getEquippedWeaponName();
        if (p.getEquippedWeapon() != null)
            weaponString = weaponString + " (+" + p.getEquippedWeapon().getAttack() + ")";
        statOpts[3] = new MenuItem(weaponString);
        statOpts[4] = new MenuItem("Attack: " + p.getAttack());
        statOpts[5] = new MenuItem("Defense: " + p.getDefense());
        statOpts[6] = new MenuItem("Strength: " + p.getStrength());
        statOpts[7] = new MenuItem("Agility: " + p.getAgility());
        statOpts[8] = new MenuItem("Luck: " + p.getLuck());
        this.setOptions(statOpts);
    }

}
