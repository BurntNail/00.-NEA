package Gameplay.player;

import classes.CustomActionListeners.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager implements BooleanChangeDispatcher {

    private int money;
    private int hearts;


    private List<BooleanChangeListener> listeners;
    private boolean hasChanged;


    public PlayerManager(int money, int hearts) {
        this.money = money;
        this.hearts = hearts;
        listeners = new ArrayList<>();


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


        money -= amnt;

        hasChanged = true;
        dispatchEvent();

        return true;
    }
    public void donateM (int amnt) {
        money += amnt;

        hasChanged = true;
        dispatchEvent();
    }

    public void takeHearts (int amnt) {
        hearts -= amnt;

        hasChanged = true;
        dispatchEvent();
    }
    public void donateH (int amnt) {
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
