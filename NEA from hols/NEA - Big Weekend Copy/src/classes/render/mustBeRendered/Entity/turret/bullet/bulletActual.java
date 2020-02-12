package classes.render.mustBeRendered.Entity.turret.bullet;

import classes.render.mustBeRendered.Entity.Entity;
import classes.render.mustBeRendered.Entity.entityType;
import classes.render.mustBeRendered.Entity.enemy.enemyActual;
import classes.util.coordinate.Coordinate;
import classes.util.coordinate.dir;
import main.main;

import java.util.concurrent.TimeUnit;

public class bulletActual extends Entity {


    private static final long MOVE_GAP = 10;

    private boolean hit;
    private boolean dead;
    private enemyActual enemyToHit;
    private int dmg;
    private int spd;

    private Thread runThread;

    public bulletActual(Coordinate XYInArr, String fn, enemyActual enemy, int dmg_, int spd_, int range) {
        super(XYInArr, fn, entityType.bullet, new Coordinate(main.TILE_WIDTH / 2, main.TILE_HEIGHT / 2));
        System.out.println("BULLET CREATED: " + hashCode());

        hit = false;
        enemyToHit = enemy;
        dmg = dmg_;
        spd = spd_;


        Runnable r = () -> {

            int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;
            int distInPx = ((spd * av) / 20) / 5;

            while(!hit && !dead) {

                if(!enemyToHit.haveIBeenSpawnedYet())
                {
                    dead = true;
                    System.out.println("Wait - the what! Maybe I can see the future.");
                    return;
                }

                if(getXYInArr().equals(enemyToHit.getXYInArr()) || hit){
                    if(getXYInTile().distTo(enemyToHit.getXYInTile()) < main.BOUND || hit)
                    {
                        enemyToHit.damage(dmg);
                        hit = true;
                        System.out.println("Bullet and Enemy in a tree, K-I-L-L-I-N-G");
                        return;
                    }
                }

                if(enemyToHit.isDead() || enemyToHit.hasHit())
                {
                    dead = true;
                    System.out.println("Oof size: Medium");
                    return;
                }

                if(getXYInArr().distTo(enemyToHit.getXYInArr()) > range + 1)
                {
                    dead = true;
                    System.out.println("Oof size: Large");
                    return;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(MOVE_GAP);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                Coordinate onScrnTarget = Entity.turnFromArrToScrnPlusHalfTile(enemyToHit.getXYInArr(), enemyToHit.getCentreOfHitbox());
                onScrnTarget = Entity.addHitBoxTolerances(onScrnTarget, CENTRE_OF_HITBOX);



                dir direction = getXYOnScrn().directionTo(onScrnTarget);
                double dist = getXYOnScrn().distTo(onScrnTarget);

                if(distInPx >= dist) { //If the dist we can go is greater than the dist to go, then we know we can get there
                    setXYInArr(enemyToHit.getXYInArr());
                    setXYInTile(enemyToHit.getCentreOfHitbox());
                    hit = true;
                    continue;
                }

                switch (direction) {
                    case N -> changeY(-distInPx);
                    case S -> changeY(distInPx);
                    case E -> changeX(distInPx);
                    case W -> changeX(-distInPx);
                }

            }

        };

        runThread = new Thread(r);
        runThread.start();
    }

    public boolean isDone() {
        return hit || dead;
    }
}
