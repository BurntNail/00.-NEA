package classes.turret;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.turret.bullet.bulletActual;
import classes.util.Coordinate;
import main.main;

import java.util.ArrayList;

public class turretActual extends Entity {

    private turretTemplate turret;
    private ArrayList<Entity> shotsFired;

    private int currentDmg, currentFireRate;

    private long msSinceLast;
    private long differenceMs;

    private ArrayList<Entity> entities;

    public turretActual(Coordinate XYInArr, turretTemplate turret) {
        super(XYInArr, turret.getFn(), entityType.turret, new Coordinate(main.TURRET_X_ON_TILE, main.TURRET_Y_ON_TILE));
        shotsFired = new ArrayList<>();
        this.turret = turret;

        msSinceLast = 0;
        differenceMs = turret.getFireRateInt() * 1000;

        entities = new ArrayList<>();
    }

    public void setEnemies (ArrayList<Entity> enemies) {
        entities = enemies;
    }

    public ArrayList<Entity> getShotsFired() {
        return shotsFired;
    }


    public void step (long msSinceLastFrame) {
        for (int i = 0; i < shotsFired.size(); i++) {
            if(shotsFired.get(i) != null) //TODO: here
                System.out.println("Weelll");
        }
        
        if(msSinceLast < differenceMs)
            return;

        //From here on, we assume we can shoot
        boolean nothingFired = false;

        ArrayList<enemyActual> enemies = filterEnemiesByType(entities);
        enemies = filterEnemiesByRange(enemies, turret.getRangeInt(), getXYInArr());
        if(enemies.size() == 0)
        {
            nothingFired = true;


            //From here on we assume we have an enemy to shoot
        }else {
            enemyActual enemy = enemies.get(0);
            bulletActual b = new bulletActual(getXYInArr(), turret.getBullet_fn(), enemy, turret.getDmgInt(), 2); //TODO: Add Speed Var in Turret.cfg files
            shotsFired.add(b);
            msSinceLast = 0;
        }

        msSinceLast += msSinceLastFrame;
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
}
