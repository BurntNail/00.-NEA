package classes.turret.bullet;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

public class bulletActual extends Entity {

    private int distPerFrame = 0; //Travels a fifteenth of a tile per frame, or 2 tiles per second, assuming 30 fps

    private boolean hit;
    private enemyActual enemyToHit;
    private int dmg;
    private int spd;

    private Thread runThread;

    public bulletActual(Coordinate XYInArr, String fn, enemyActual enemy, int dmg_, int spd_) {
        super(XYInArr.clone(), fn, entityType.bullet, new Coordinate(main.TILE_WIDTH / 2, main.TILE_HEIGHT / 2));
//        super(XYInArr, fn, entityType.bullet, null);
        hit = false;
        enemyToHit = enemy;
        dmg = dmg_;
        spd = spd_;

        Runnable r = () -> {
            long current;

            while(!hit) {
                current = System.currentTimeMillis();

                if(getXYInArr().equals(enemyToHit.getXYInArr())){
                    if(getXYInTile().distTo(enemyToHit.getXYInTile()) < 100)
                    {
                        enemyToHit.damage(dmg);
                        hit = true;
                    }
                }


                distPerFrame = ((int) ((System.currentTimeMillis() - current / 1000) * spd));

                dir direction = getXYInArr().directionTo(enemyToHit.getXYInArr());

                switch (direction) {
                    case N:
                        N(distPerFrame);
                        break;
                    case S:
                        S(distPerFrame);
                        break;
                    case E:
                        E(distPerFrame);
                        break;
                    case W:
                        W(distPerFrame);
                        break;
                }
            }


        };

        runThread = new Thread(r);
        runThread.start();
    }

    public boolean isHit() {
        return hit;
    }
}
