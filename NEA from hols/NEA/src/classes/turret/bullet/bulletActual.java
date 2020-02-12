package classes.turret.bullet;

import Gameplay.turrets.turretFrame.Console;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

public class bulletActual extends Entity {

    private boolean hit;
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
        Console.addText("@Bullet: Bullet Created");

        Runnable r = () -> {

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

                if(getXYInArr().distTo(enemyToHit.getXYInArr()) > range + 1)
                {
                    hit = true;
                    Console.addText("@Bullet: Enemy left range.");
                    return;
                }


                int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

                int fudgeFactor = 10;

                int distInPx = ((spd * av) / 200) / fudgeFactor;


                try {
                    dir direction = getXYInArr().directionTo(enemyToHit.getXYInArr());

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
