package classes.turret.bullet;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;
import sun.jvm.hotspot.opto.MachIfNode;

public class bulletActual extends Entity {

    private int distPerFrame = 0; //Travels a fifteenth of a tile per frame, or 2 tiles per second, assuming 30 fps

    private boolean hit;
    private enemyActual enemyToHit;
    private int dmg;
    private int spd;

    public bulletActual(Coordinate XYInArr, String fn, enemyActual enemy, int dmg_, int spd_) {
        super(XYInArr, fn, entityType.bullet, null);
        hit = false;
        enemyToHit = enemy;
        dmg = dmg_;
        spd = spd_;
    }

    public void step (long msSinceLast) {
        if(getXYInArr().equals(enemyToHit.getXYInArr())){
            if(getXYInTile().distTo(enemyToHit.getXYInTile()) < 10)
            {
                enemyToHit.damage(dmg);
                hit = true;
            }
        }

        distPerFrame = ((int) ((msSinceLast / 1000) * spd));

        dir direction = getXYInArr().directionTo(enemyToHit.getXYInArr());

        switch (direction) {
            case N:
                //region N
                Coordinate newOneInTileN = new Coordinate(getXYInTile().getX(), getXYInTile().getY() - distPerFrame);
                Coordinate newOneInArrN = getXYInArr().clone();
                if(newOneInTileN.getY() < 0)
                {
                    int overflow = newOneInTileN.getY() * -1;
                    newOneInArrN.setY(newOneInArrN.getY() - 1);
                    newOneInTileN.setY(overflow);
                }

                changePosInTile(newOneInTileN);
                changeTile(newOneInArrN);
                //endregion
                break;
            case S:
                //region S
                Coordinate newOneInTileY = new Coordinate(getXYInTile().getX(), getXYInTile().getY() + distPerFrame);
                Coordinate newOneInArrY = getXYInArr().clone();

                if(newOneInTileY.getY() > main.TILE_HEIGHT)
                {
                    int overflow = newOneInTileY.getY() - main.TILE_HEIGHT;
                    newOneInArrY.setY(newOneInArrY.getY() + 1);
                    newOneInTileY.setY(overflow);
                }

                changePosInTile(newOneInTileY);
                changeTile(newOneInArrY);
                //endregion
                break;
            case E:
                //region E
                Coordinate newOneInTileE = new Coordinate(getXYInTile().getX() + distPerFrame, getXYInTile().getY());
                Coordinate newOneInArrE = getXYInArr().clone();
                if(newOneInTileE.getX() > main.TILE_WIDTH)
                {
                    int overflow = newOneInTileE.getX() - main.TILE_WIDTH;
                    newOneInArrE.setX(newOneInArrE.getX() + 1);
                    newOneInTileE.setX(overflow);
                }

                changePosInTile(newOneInTileE);
                changeTile(newOneInArrE);
                //endregion
                break;
            case W:
                //region S
                Coordinate newOneInTileW = new Coordinate(getXYInTile().getX() - distPerFrame, getXYInTile().getY());
                Coordinate newOneInArrW = getXYInArr().clone();

                if(newOneInTileW.getX() < 0)
                {
                    int overflow = newOneInTileW.getX() * -1;
                    newOneInArrW.setX(newOneInArrW.getX() - 1);
                    newOneInTileW.setX(overflow);
                }

                changePosInTile(newOneInTileW);
                changeTile(newOneInArrW);
                //endregion
                break;
        }
    }

    public boolean isHit() {
        return hit;
    }
}
