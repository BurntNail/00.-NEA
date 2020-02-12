package classes.turret.bullet;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.coordinate.Coordinate;
import classes.coordinate.dir;
import main.main;

public class bulletActual extends Entity {

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

            while(!hit && !dead) {

                if(enemyToHit.isHasBeenSpawned())
                {
                    dead = true;
                    return;
                }

                if(getXYInArr().equals(enemyToHit.getXYInArr())){
                    if(getXYInTile().distTo(enemyToHit.getXYInTile()) < main.BOUND)
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


                int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

                int distInPx = ((spd * av) / 20);

                double dist = getXYOnScrn().distTo(enemyToHit.getXYOnScrn());
                dir direction = getXYOnScrn().directionTo(getXYOnScrn());

                System.out.println(distInPx);

                try {
                    if(!getXYInArr().equals(enemyToHit.getXYInArr())) {

                        switch (direction) {
                            case N:
                                changeY(-distInPx);
                                break;
                            case S:
                                changeY(distInPx);
                                break;
                            case E:
                                changeX(-distInPx);
                                break;
                            case W:
                                changeX(distInPx);
                                break;
                        }
                    } else if (getXYInArr().equals(enemyToHit.getXYInArr()) && dist > main.BOUND) {
                        switch (direction) {
                            case N:
                                changeY(-distInPx);
                                break;
                            case S:
                                changeY(distInPx);
                                break;
                            case E:
                                changeX(-distInPx);
                                break;
                            case W:
                                changeX(distInPx);
                                break;
                        }
                    } else
                        setXYInTile(enemyToHit.getXYInTile());

                } catch (Exception e) {
                    e.printStackTrace();
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
