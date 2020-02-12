package Gameplay.player;

import Gameplay.waves.waveManager;
import classes.util.CustomActionListeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class PlayerManager implements BooleanChangeDispatcher, KeyListener {

    private int money;
    private int hearts;

    private int startHearts;

    private List<BooleanChangeListener> listeners;

    private boolean hasWon;

//    private Thread checkThread;
//    private boolean checkThreadStarted;

    public PlayerManager(int money, int hearts, JFrame win) {
        this.money = money;
        this.hearts = hearts;
        startHearts = hearts;

        listeners = new ArrayList<>();

        win.addKeyListener(this);

        hasWon = false;
    }

//    public void setWaves (waveManager wm) {
//        if(checkThreadStarted)
//            return;
//
//        Runnable r = () -> {
//            while(true) {
//                if (wm.getCurrentEnemies() == 0) {
////                    System.out.println("0 enemies");
//                    if (wm.getEnemiesSpawned() != 0) {
//                        hasWon();
//                        System.out.println("I've won");
//                        break;
//                    }
//                }
//            }
//        };
//
//        checkThread = new Thread(r);
//        checkThread.start();
//
//        checkThreadStarted = true;
//    }

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


    public boolean isDead () {
        return hearts <= 0;
    }
    public void hasWon () {
        hasWon = true;
        dispatchEvent();
    }
    public boolean haveIWon () {
        return hasWon;
    }

    public boolean isDone () {
        return hasWon || isDead();
    }

    public int startHearts () {
        return startHearts;
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
