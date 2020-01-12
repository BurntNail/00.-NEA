package classes.Entity;

import classes.util.Coordinate;
import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public abstract class Entity {

    private BufferedImage img;
    private Coordinate XYInArr, XYInTile;

    public Entity(Coordinate XYInArr, String fn, entityType type) {
        this.XYInArr = XYInArr;
        int x = main.TILE_WIDTH / 2 + XYInArr.getX();
        int y = main.TILE_WIDTH / 2 + XYInArr.getY();
        XYInTile = new Coordinate(x, y);

        String fqdn = "";

        switch (type) {
            case enemy:
                fqdn = main.ENEMY_IMAGES_LOC + fn;
                break;
            case bullet:
                fqdn = main.BULLET_IMAGE_LOC + fn;
                break;
            default: //Use default rather than 'case turret' as otherwise ide throws error
                fqdn = main.TURRET_IMAGES_LOC + fn;
        }

        Image temp;

        try {
            temp = ImageIO.read(new URL(fqdn));
        } catch (IOException e) {
            temp = null;
        }

        Coordinate wh = getWHOnType(type); //wh == width and height
        temp = temp.getScaledInstance(wh.getX(), wh.getY(), Image.SCALE_SMOOTH);
    }

    //region getters and setters
    public BufferedImage getImg() {
        return img;
    }

    public Coordinate getXYInArr() {
        return XYInArr;
    }

    public Coordinate getXYInTile() {
        return XYInTile;
    }

    public Coordinate getXYOnScrn () {
        int bigX = main.TILE_WIDTH * XYInArr.getX();
        int bigY = main.TILE_HEIGHT * XYInArr.getY();

        int smallX = XYInTile.getX();
        int smallY = XYInTile.getY();

        int x = bigX + smallX;
        int y = bigY + smallY;

        return new Coordinate(x, y);
    }

    public void changeTile (Coordinate newOne) {
        XYInArr = newOne;
    }

    public void changePosInTile (Coordinate newOne) {
        XYInTile = newOne;
    }
    //endregion

    private static Coordinate getWHOnType (entityType type) {
        switch (type) {
            case enemy:
                return new Coordinate(main.ENEMY_WIDTH, main.ENEMY_HEIGHT); //No break statement needed as it becomes an unreachable statement
            case bullet:
                return new Coordinate(main.BULLET_WIDTH, main.BULLET_HEIGHT);
            default:
                return new Coordinate(main.TURRET_WIDTH, main.TURRET_HEIGHT);
        }
    }
}
