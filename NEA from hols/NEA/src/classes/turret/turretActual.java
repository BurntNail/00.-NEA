package classes.turret;

import Gameplay.player.PlayerManager;
import Gameplay.turrets.turretFrame.Console;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.turret.bullet.bulletActual;
import classes.util.Coordinate;
import main.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class turretActual extends Entity {

    private static final int BULLET_SPD = 1;

    private turretTemplate turret;
    private ArrayList<Entity> shotsFired;

    private int currentDmg, currentFireRate;

    private long differenceMs;

    private ArrayList<Entity> entities;

    private Thread runThread;
    private int code;

    public turretActual(Coordinate XYInArr, turretTemplate turret, int index, PlayerManager pm) {
        super(XYInArr, turret.getFn(), entityType.turret, new Coordinate(main.TURRET_X_ON_TILE, main.TURRET_Y_ON_TILE));
        shotsFired = new ArrayList<>();
        this.turret = turret;
        code = index;


        differenceMs = ((long) Math.floor(turret.getDiffBetweenFiring()));

        entities = new ArrayList<>();



        Runnable r = () -> {

            while(!pm.isDead()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(differenceMs);
                } catch (InterruptedException e) {
                    System.out.println("Cooldown violated.");
                }

                ArrayList<enemyActual> enemies = filterEnemies(((ArrayList<Entity>) entities.clone()), turret.getRangeInt(), getXYInArr().clone());
                if(enemies.size() != 0)
                {
                    enemyActual e = enemies.get(0);
                    bulletActual b = new bulletActual(getXYInArr().clone(), turret.getBullet_fn(), e, turret.getDmgInt(), BULLET_SPD, turret.getRangeInt());
                    shotsFired.add(b);
                }


                for (Entity entity : ((ArrayList<Entity>) shotsFired.clone()))
                {
                    bulletActual ba = ((bulletActual) entity);

                    if(ba.isHit())
                    {
                        shotsFired.remove(entity);
                        Console.addText("@Turret: Bullet removed.");
                    }
                }
            }
        };

        runThread = new Thread(r);
        runThread.start();

//        Runnable temp1 = () -> {
//            while (true){
//                try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                } catch (InterruptedException e) {
//
//                }
////                int y = getXYInArr().getY(); //We know that the y goes nuts
////                int x = getXYInArr().getX() + 1; // The x doesn't work either
//
////                changeXYOnScrn(x, y);
//
////                changeX(-5); //Works
////                changeY(-5); // Works
//
//                System.out.println("Turret moved");
//
//            }
//        };
//        Thread temp = new Thread(temp1);
//        temp.start();
//        System.out.println(temp.getName());
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
            if(!e.isHasBeenSpawned())
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
}
