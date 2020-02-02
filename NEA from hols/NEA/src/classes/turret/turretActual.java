package classes.turret;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.turret.bullet.bulletActual;
import classes.util.Coordinate;
import main.main;

import java.util.ArrayList;

public class turretActual extends Entity {

    private static final int BULLET_SPD = 1;

    private turretTemplate turret;
    private ArrayList<Entity> shotsFired;

    private int currentDmg, currentFireRate;

    private long differenceMs;

    private ArrayList<Entity> entities;

    private Thread runThread;
    private int code;

    public turretActual(Coordinate XYInArr, turretTemplate turret, int index) {
        super(XYInArr, turret.getFn(), entityType.turret, new Coordinate(main.TURRET_X_ON_TILE, main.TURRET_Y_ON_TILE));
        shotsFired = new ArrayList<>();
        this.turret = turret;
        code = index;

        differenceMs = turret.getFireRateInt() * 1000;

        entities = new ArrayList<>();


        Runnable r = () -> {
            long current = 0;
            long msSinceLastShot = 0;

            while(true) {
                current = System.currentTimeMillis();
                long diff = System.currentTimeMillis() - current + msSinceLastShot;
                msSinceLastShot += diff;

                if(diff < differenceMs)
                    continue;

                boolean nothingFired = false;

                ArrayList<enemyActual> enemies = filterEnemiesByRange(filterEnemiesByType(entities), turret.getRangeInt(), getXYInArr());
                if(enemies.size() == 0)
                    nothingFired = true;
                else {
                    enemyActual e = enemies.get(0);
                    bulletActual b = new bulletActual(getXYInArr(), turret.getBullet_fn(), e, turret.getDmgInt(), BULLET_SPD);
                    shotsFired.add(b);
                }

                if(!nothingFired)
                    msSinceLastShot = 0;
            }
        };

        runThread = new Thread(r);
        runThread.start();
    }

    public void setEnemies (ArrayList<Entity> enemies) {
        entities = enemies;
    }

    public ArrayList<Entity> getShotsFired() {
        return shotsFired;
    }

    public turretTemplate getTurret() {
        return turret;
    }

    private static ArrayList<enemyActual> filterEnemiesByType (ArrayList<Entity> entities) {
        ArrayList<enemyActual> newOnes = new ArrayList<>();

        for (Entity e : entities) {
            if(e.getType().equals(entityType.enemy))
                newOnes.add((enemyActual) e);
        }

        return newOnes;
    }

    private static ArrayList<enemyActual> filterEnemiesByRange (ArrayList<enemyActual> enemies, int range, Coordinate ici) {
        ArrayList<enemyActual> newOnes = new ArrayList<>();

        for (enemyActual e : enemies) {
            if (e.getXYInArr().distTo(ici) <= range)
                newOnes.add(e);
        }

        return newOnes;
    }

    public int getCode() {
        return code;
    }
}
