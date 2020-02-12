package Gameplay.player;

import classes.CustomActionListeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager implements BooleanChangeDispatcher, KeyListener {

    private int money;
    private int hearts;

    private List<BooleanChangeListener> listeners;

    public PlayerManager(int money, int hearts, JFrame win) {
        this.money = money;
        this.hearts = hearts;

        listeners = new ArrayList<>();

        win.addKeyListener(this);
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
        dispatchEvent();
        return true;
    }
    public void donateM (int amnt) {
        money += amnt;
        dispatchEvent();
    }

    public void takeHearts (int amnt) {
        hearts -= amnt;
        dispatchEvent();
    }
    public void donateH (int amnt) {
        hearts += amnt;
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

    private void dispatchEvent() {
        final BooleanChangeEvent event = new BooleanChangeEvent(this);
        for (BooleanChangeListener l : listeners) {
            dispatchRunnableOnEventQueue(l, event);
        }
    }

    private void dispatchRunnableOnEventQueue(final BooleanChangeListener listener, final BooleanChangeEvent event) {
        EventQueue.invokeLater(() -> listener.stateChanged(event));
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D: //down
                hearts--;
                dispatchEvent();
                break;
            case KeyEvent.VK_U: //up
                hearts++;
                dispatchEvent();
                break;
            case KeyEvent.VK_R: //Rich
                money += 100;
                dispatchEvent();
                break;
            case KeyEvent.VK_P: //Poor
                money -= 100;
                dispatchEvent();
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
