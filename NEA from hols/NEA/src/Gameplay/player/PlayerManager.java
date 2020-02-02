package Gameplay.player;

import classes.CustomActionListeners.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager implements BooleanChangeDispatcher {

    private int money;
    private int hearts;

    private int prevHearts;
    private int prevMoney;

    private List<BooleanChangeListener> listeners;
    private boolean hasChanged;


    public PlayerManager(int money, int hearts) {
        this.money = money;
        this.hearts = hearts;
        listeners = new ArrayList<>();

        prevMoney = 0;
        prevHearts = 0;

        hasChanged = false;
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


        prevMoney = money;
        money -= amnt;

        hasChanged = true;
        dispatchEvent();

        return true;
    }
    public void donateM (int amnt) {
        prevMoney = money;
        money += amnt;

        hasChanged = true;
        dispatchEvent();
    }

    public void takeHearts (int amnt) {
        prevHearts = hearts;
        hearts -= amnt;

        hasChanged = true;
        dispatchEvent();
    }
    public void donateH (int amnt) {
        prevHearts = hearts;
        hearts += amnt;

        hasChanged = true;
        dispatchEvent();
    }

    public boolean isDead () {
        return hearts <= 0;
    }
    //endregion


    @Override
    public void addBooleanChangeListener(BooleanChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean getFlag() {
        return hasChanged;
    }

    private void dispatchEvent() {
        final BooleanChangeEvent event = new BooleanChangeEvent(this);
        for (BooleanChangeListener l : listeners) {
            dispatchRunnableOnEventQueue(l, event);
        }
        hasChanged = false;
    }

    private void dispatchRunnableOnEventQueue(final BooleanChangeListener listener, final BooleanChangeEvent event) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                listener.stateChanged(event);
            }
        });
    }




}
