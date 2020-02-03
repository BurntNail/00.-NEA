package Gameplay.player;

import Gameplay.turrets.turretFrame.Console;
import classes.CustomActionListeners.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager {

    private static int money;
    private static int hearts;

    private static boolean needsToUpdate;

    public PlayerManager(int money, int hearts) {
        this.money = money;
        this.hearts = hearts;
        needsToUpdate = true;
    }

    //region Getters
    public static int getMoney() {
        return money;
    }

    public static int getHearts() {
        return hearts;
    }
    //endregion

    //region methods
    public static boolean buy (int amnt) {
        if(amnt > money)
            return false;

        money -= amnt;
        needsToUpdate = true;
        return true;
    }
    public static void donateM (int amnt) {
        money += amnt;
        needsToUpdate = true;
    }

    public static void takeHearts (int amnt) {
        hearts -= amnt;
        needsToUpdate = true;
    }
    public static void donateH (int amnt) {
        hearts += amnt;
        needsToUpdate = true;

    }


    public boolean isDead () {
        return hearts <= 0;
    }
    //endregion


    public static boolean needsToUpdate() { //NB: THIS CAN ONLY HAVE ONE USAGE.
        boolean temp = needsToUpdate;

        if(temp) {
            needsToUpdate = false;
        }

        return temp;
    }
}
