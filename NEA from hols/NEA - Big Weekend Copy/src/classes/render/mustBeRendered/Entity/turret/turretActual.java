package classes.render.mustBeRendered.Entity.turret;

import Gameplay.player.PlayerManager;
import classes.render.mustBeRendered.Entity.baseEntity.Entity;
import classes.render.mustBeRendered.Entity.baseEntity.entityType;
import classes.render.mustBeRendered.Entity.enemy.enemyActual;
import classes.render.mustBeRendered.Entity.turret.bullet.bulletActual;
import classes.util.coordinate.Coordinate;
import main.main;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class turretActual extends Entity {

    private turretTemplate turret;
    private ArrayList<Entity> shotsFired;

    private long differenceMs;

    private ArrayList<Entity> entities;

    private Thread runThread;


    public turretActual(Coordinate XYInArr, turretTemplate turret, PlayerManager pm) {
        super(XYInArr, turret.getFn(), entityType.turret, new Coordinate(main.TURRET_X_ON_TILE, main.TURRET_Y_ON_TILE));
        shotsFired = new ArrayList<>();
        this.turret = turret;



        differenceMs = ((long) Math.floor(turret.getDiffBetweenFiring()));

        entities = new ArrayList<>();



        Runnable r = () -> {

            while(!pm.isDone()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(differenceMs);

                } catch (InterruptedException e) {
                    System.out.println("Cooldown violated.");
                }

                ArrayList<enemyActual> enemies = filterEnemies(((ArrayList<Entity>) entities.clone()), turret.getRangeInt(), getXYInArr().clone());

//                System.out.println("Well matey, we have precisely " + enemies.size() + " thing(s) to kill.");
                if(enemies.size() != 0)
                {
                    enemyActual e = enemies.get(0);
                    bulletActual b = new bulletActual(getXYInArr().clone(), turret.getBullet_fn(), e, turret.getDmgInt(), turret.getBulletSpd(), turret.getRangeInt());
                    shotsFired.add(b);
                }


                for (Entity entity : ((ArrayList<Entity>) shotsFired.clone()))
                {
                    bulletActual ba = ((bulletActual) entity);

                    if(ba.isDone())
                    {
                        shotsFired.remove(entity);
                        System.out.println("Bullet removed");
                    }
                }
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

    private static ArrayList<enemyActual> filterEnemiesBySpawned (ArrayList<enemyActual> enemies) {
        for(enemyActual e : ((ArrayList<enemyActual>) enemies.clone())) {
            if(!e.haveIBeenSpawnedYet())
                enemies.remove(e);
        }

        return enemies;
    }

    private static ArrayList<enemyActual> filterEnemies (ArrayList<Entity> entities, int range, Coordinate xy) {
        ArrayList<enemyActual> enemyActuals = filterEnemiesByType(entities);
        enemyActuals = filterEnemiesByRange(enemyActuals, range, xy);
        enemyActuals = filterEnemiesBySpawned(enemyActuals);

        return enemyActuals;
    }


    @Override
    public String toString() {
        String coord = getXYInArr().toString();
        String name = getTurret().getName();


        return "turretActual{XYInArr='" + coord + "', Name='" + name + "'}";
    }
}
