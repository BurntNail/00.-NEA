package Gameplay.player;

import classes.CustomActionListeners.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager {

    private int money;
    private int hearts;

    private int prevMoney;
    private int prevHearts;


    private List<BooleanChangeListener> listeners;


    public PlayerManager(int money, int hearts) {
        this.money = money;
        this.hearts = hearts;
        listeners = new ArrayList<>();

        prevHearts = 0;
        prevMoney = 0;

    }

    //region Getters
    public int getMoney() {
        return money;
    }

    public int getHearts() {
        return hearts;
    }
    //endregion

    //region methods
    public boolean buy (int amnt) {
        if(amnt > money)
            return false;


        money -= amnt;
        return true;
    }
    public void donateM (int amnt) {
        money += amnt;
    }

    public void takeHearts (int amnt) {
        hearts -= amnt;
    }
    public void donateH (int amnt) {
        hearts += amnt;
    }

    public boolean isDead () {
        return hearts <= 0;
    }
    //endregion

    public boolean hasChanedSinceLastCheck () {
        boolean ans = prevHearts != hearts || prevMoney != money;

        prevMoney = money;
        prevHearts = hearts;

        return ans;
    }



}
