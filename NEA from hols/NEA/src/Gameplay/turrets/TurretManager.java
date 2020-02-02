package Gameplay.turrets;

import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.turret.*;
import classes.util.Coordinate;
import main.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class TurretManager {

    private ArrayList<Entity> turrets;
    private turretDictionary dictionary;
    private PlayerManager pm;

    private squareCollection sqc;

    private ArrayList<Coordinate> turretSquaresAll;
    private ArrayList<Coordinate> turretSquaresFree;
    private ArrayList<Coordinate> turretSquaresUsed;

    private Coordinate prevClickedCoordinate;
    private Coordinate coordBefore;
    private String prevClickedType;

    private TurretFrame tf;
    private ArrayList<Entity> bullets;
    private ArrayList<Entity> enemies;

    private Thread runThread;


    public TurretManager (squareCollection sqc_, PlayerManager pm) {
        this.pm = pm;
        sqc = sqc_;

        turrets = new ArrayList<>();
        dictionary = new turretDictionary(main.TURRET_FNS);

        turretSquaresAll = sqc.getAvailableTurretSquares();
        turretSquaresFree = (ArrayList<Coordinate>) turretSquaresAll.clone();
        turretSquaresUsed = new ArrayList<>();

        tf = new TurretFrame(turretSquaresUsed, turretSquaresFree, new Dimension(main.WINDOW_WIDTH, main.WINDOW_HEIGHT), dictionary.getTurrets().values(), pm);
        prevClickedCoordinate = TurretFrame.NULL_COORD;
        coordBefore = TurretFrame.NULL_COORD;
        prevClickedType = TurretFrame.NULL_STR;

        turrets = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        Runnable r = () -> {
            while(true) {
                prevClickedCoordinate = tf.getMostRecent();
                prevClickedType = tf.getMostRecentType();

                if (prevClickedCoordinate != TurretFrame.NULL_COORD && prevClickedCoordinate != coordBefore && !turretSquaresUsed.contains(prevClickedCoordinate)) {
                    turretSquaresUsed.add(prevClickedCoordinate);
                    turretSquaresFree.remove(prevClickedCoordinate);

                    String type = prevClickedType;

                    turretActual temp = new turretActual(prevClickedCoordinate, dictionary.getTurret(type), tf.getCurrentIndex());
                    tf.incrementIndex();

                    if(pm.buy(temp.getTurret().getCost()))
                    {
                        turrets.add(temp);
                        coordBefore = prevClickedCoordinate;
                    }
                }

                bullets.clear();
                if(turrets.size() > 0) {
                    for (Entity e : turrets) {

                        if (e.getType().equals(entityType.turret)) {
                            turretActual t = ((turretActual) e);
                            t.setEnemies(enemies);

                            for (Entity b : t.getShotsFired())
                                bullets.add(b);
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
}