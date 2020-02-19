package classes.render.mustBeRendered.Entity.turret;

import Gameplay.player.PlayerManager;
import classes.render.mustBeRendered.Entity.baseEntity.Entity;
import classes.render.mustBeRendered.Entity.baseEntity.entityType;
import classes.render.mustBeRendered.Entity.enemy.enemyActual;
import classes.render.mustBeRendered.Entity.turret.bullet.bulletActual;
import classes.util.coordinate.Coordinate;
import main.main;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class turretActual extends Entity { //turret class

    private turretTemplate turret; //template
    private ArrayList<Entity> shotsFired; //shots that have been fired

    private long differenceMs; //target firing difference

    private ArrayList<Entity> entities; //enemies to hit

    private Thread runThread; //runThread
    private boolean stillWorking;

    public turretActual(Coordinate XYInArr, turretTemplate turret, PlayerManager pm) {
        super(XYInArr, turret.getFn(), entityType.turret, new Coordinate(main.TURRET_X_ON_TILE, main.TURRET_Y_ON_TILE)); //super
        shotsFired = new ArrayList<>(); //init shots
        this.turret = turret; //set template
        stillWorking = true;


        differenceMs = ((long) Math.floor(turret.getDiffBetweenFiring())); //set difference

        entities = new ArrayList<>(); //init entities


        Runnable r = () -> { //runnable for thread

            while(!pm.isDone() && stillWorking) { //whilst the playermanager isn't done
                try {
                    TimeUnit.MILLISECONDS.sleep(differenceMs); //wait for ability to shoot

                } catch (InterruptedException e) {
                    System.out.println("Cooldown violated.");
                }

                ArrayList<enemyActual> enemies = filterEnemies(((ArrayList<Entity>) entities.clone()), turret.getRangeInt(), getXYInArr().clone()); //get enemies, and filter them

                if(enemies.size() != 0) //if there aren't 0 enemies to hit
                {
                    enemyActual e = enemies.get(0); //pick one
                    bulletActual b = new bulletActual(getXYInArr().clone(), turret.getBullet_fn(), e, turret.getDmgInt(), turret.getBulletSpd(), turret.getRangeInt()); //create a bullet
                    shotsFired.add(b); //add bullet to list
                }


                for (Entity entity : ((ArrayList<Entity>) shotsFired.clone())) //for all of the bullets
                {
                    bulletActual ba = ((bulletActual) entity); //temporary variable that is a bulletActual

                    if(ba.isDone()) //if it is done
                    {
                        shotsFired.remove(entity); //remove it from render list
                        System.out.println("Bullet removed");
                    }
                }
            }
        };

        runThread = new Thread(r);
        runThread.start(); //start thread
    }

    public void setEnemies (ArrayList<Entity> enemies) { //update enemies
        entities = enemies;
    }

    public ArrayList<Entity> getShotsFired() { //get render list
        return shotsFired;
    }

    public turretTemplate getTurret() { //get template
        return turret;
    }

    private static ArrayList<enemyActual> filterEnemiesByType (ArrayList<Entity> entities) { //filter by type so we only get enemyActuals
        ArrayList<enemyActual> newOnes = new ArrayList<>(); //new list

        for (Entity e : entities) { //for each of the enemies
            if(e.getType().equals(entityType.enemy)) // if they ARE an enemy
                newOnes.add((enemyActual) e); //add them to the new list
        }

        return newOnes;
    }

    private static ArrayList<enemyActual> filterEnemiesByRange (ArrayList<enemyActual> enemies, int range, Coordinate ici) { //filter by range
        ArrayList<enemyActual> newOnes = new ArrayList<>(); //new list

        for (enemyActual e : enemies) { //for each
            if (e.getXYInArr().distTo(ici) <= range) //if they are in our range
                newOnes.add(e); //add to new list
        }

        return newOnes;
    }

    private static ArrayList<enemyActual> filterEnemiesBySpawned (ArrayList<enemyActual> enemies) { //filter by if they have been spawned
        for(enemyActual e : ((ArrayList<enemyActual>) enemies.clone())) { //for each
            if(!e.haveIBeenSpawnedYet()) //if they haven't been spawned yet
                enemies.remove(e); //remove them
        }

        return enemies; //return original list in case reference wasn't kept
    }

    private static ArrayList<enemyActual> filterEnemies (ArrayList<Entity> entities, int range, Coordinate xy) { //filter by all mechanisms
        ArrayList<enemyActual> enemyActuals = filterEnemiesByType(entities); // filter by type
        enemyActuals = filterEnemiesByRange(enemyActuals, range, xy); //filter by range
        enemyActuals = filterEnemiesBySpawned(enemyActuals); //filter by spawned

        return enemyActuals; //return them
    }

    public void noLongerWorking () {
        stillWorking = false;
    }

    @Override
    public String toString() { //toString with coordinate, name, cost, and resale value
        String coord = getXYInArr().toString();
        String name = getTurret().getName();
        int cost = getTurret().getCost();
        int resaleValue = getTurret().getSellValue();

        StringJoiner joiner = new StringJoiner(", ", turretActual.class.getSimpleName() + "[", "]");

        joiner.add(coord);
        joiner.add("name='" + name + "'");
        joiner.add("initalCost=" + cost);
        joiner.add("sellValue=" + resaleValue);

        return joiner.toString();
    }
}
