package Gameplay.turrets;

import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.turret.*;
import classes.coordinate.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class TurretManager {

    private ArrayList<Entity> turrets;
    private turretDictionary dictionary;

    private squareCollection sqc;

    private ArrayList<Coordinate> turretSquaresAll;
    private ArrayList<Coordinate> turretSquaresFree;
    private ArrayList<Coordinate> turretSquaresUsed;

    private PlayerManager pm;

    private TurretFrame tf;
    private ArrayList<Entity> bullets;
    private ArrayList<Entity> enemies;

    private Thread runThread;


    public TurretManager(squareCollection sqc_, PlayerManager pm, JFrame window) {
        sqc = sqc_;

        turrets = new ArrayList<>();
        dictionary = new turretDictionary(main.TURRET_FNS);

        turretSquaresAll = sqc.getAvailableTurretSquares();
        turretSquaresFree = (ArrayList<Coordinate>) turretSquaresAll.clone();
        turretSquaresUsed = new ArrayList<>();

        tf = new TurretFrame(turretSquaresUsed, turretSquaresFree, new Dimension(main.WINDOW_WIDTH, main.WINDOW_HEIGHT), dictionary.getTurrets().values(), pm, window, this);
        this.pm = pm;

        turrets = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        window.add(tf);

        Runnable r = () -> {
            while(!pm.isDone()) {
                main.quickCoord(turretSquaresUsed);
                main.quickEntity(turrets);


                turrets = tf.getTurretActuals();

                bullets.clear();
                if(turrets.size() > 0) {
                    main.quickEntity(turrets);
                    main.quickCoord(turretSquaresUsed);


                    for (Entity e : ((ArrayList<Entity>) turrets.clone())) {

                        if (e.getType().equals(entityType.turret)) {
                            turretActual t = ((turretActual) e);
                            t.setEnemies(enemies);

                            try {
                                for (Entity b : t.getShotsFired())
                                {
                                    bullets.add(b);
                                }
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }



                ArrayList<Entity> all = (ArrayList<Entity>) turrets.clone();
                all.addAll(bullets);

                tf.setTurrets(((ArrayList<Entity>) turrets.clone()));

            }
        };

        runThread = new Thread(r);
        runThread.start();
    }

    public ArrayList<Entity> setEnemiesAndGetTurretsAndBullets (ArrayList<Entity> enemies) {
        this.enemies = enemies;

        ArrayList<Entity> TABS = (ArrayList<Entity>) turrets.clone();
        TABS.addAll((Collection<Entity>) bullets.clone());

        return TABS;
    }

    protected void buyTurret (Coordinate where, String type) {
        if(!turretSquaresFree.contains(where))
            return;

        turretSquaresUsed.add(where);
        turretSquaresFree.remove(where);

        System.out.println("@TurretManager: " + type + " has been bought.");


        turretActual temp = new turretActual(where, dictionary.getTurret(type), tf.getCurrentIndex(), pm);
        tf.incrementIndex();

        if(pm.buy(temp.getTurret().getCost()))
            turrets.add(temp);

    }

    protected void sellTurret (Coordinate where, turretActual ta) {
        if(!turretSquaresUsed.contains(where))
            return;

        turretSquaresUsed.remove(where);
        turretSquaresFree.add(where);
        turrets.remove(ta);

        int moneyBack = ta.getTurret().getSellValue();
        pm.donateM(moneyBack);
    }
}