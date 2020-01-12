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
        temp = temp.getScaledInstance()
    }

    private Coordinate getWHOnType (entityType type) {
        switch (type) {
            case enemy:
                return new Coordinate(main.ENEMY_WIDTH, main.ENEMY_HEIGHT);
                break;
            case bullet:
                return new Coordinate(main.BULLET_WIDTH, main.BULLET_HEIGHT);
        }
    }
}
