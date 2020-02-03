package classes.turret.bullet;

import Gameplay.turrets.turretFrame.Console;
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

        hit = false;
        enemyToHit = enemy;
        dmg = dmg_;
        spd = spd_;
        Console.addText("@Bullet: Bullet Created");

        Runnable r = () -> {
            long current = System.currentTimeMillis();

            while(!hit) {


                if(getXYInArr().equals(enemyToHit.getXYInArr())){
                    if(getXYInTile().distTo(enemyToHit.getXYInTile()) < main.BOUND)
                    {
                        enemyToHit.damage(dmg);
                        hit = true;
                        Console.addText("@Bullet: Enemy Hit");
                        return;
                    }
                }

                if(enemyToHit.isDead() || enemyToHit.hasHit())
                {
                    hit = true;
                    Console.addText("@Bullet: Enemy dead or got past defence.");
                    return;
                }


                distPerFrame = ((int) ((System.currentTimeMillis() - current / 1000) * spd)) / 10;

                distPerFrame = (distPerFrame < 0 ? 1 : distPerFrame);

                current = System.currentTimeMillis();

                try {
                    dir direction = getXYInArr().directionTo(enemyToHit.getXYInArr());

                    int x = getXYOnScrn().getX();
                    int y = getXYOnScrn().getY();

                    switch (direction) {
                        case N:
                            y -= distPerFrame;
                            break;
                        case S:
                            y += distPerFrame;
                            break;
                        case E:
                            x -= distPerFrame;
                            break;
                        case W:
                            x += distPerFrame;
                            break;
                    }

                    changeXYOnScrn(x, y);
                } catch (Exception e) {
                    Console.addText("@Bullet: ERROR " + e.getStackTrace());
                }
            }

            Console.addText("@Bullet: No Longer Needed");

        };

        runThread = new Thread(r);
        runThread.start();
    }

    public boolean isHit() {
        return hit;
    }
}
