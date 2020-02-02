package classes.Entity;

import Gameplay.turrets.TurretFrame;
import classes.util.Coordinate;
import main.main;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public abstract class Entity {

    private BufferedImage img;
    private Coordinate XYInArr, XYInTile;
    private entityType type;

    public Entity(Coordinate XYInArr, String fn, entityType type, Coordinate XYInTile) {
        this.XYInArr = XYInArr;
        this.XYInTile = XYInTile;
        this.type = type;


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
            URL url = new URL(fqdn);

            temp = ImageIO.read(url);
        } catch (Exception e) {
            temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        Coordinate wh = getWHOnType(type); //wh == width and height
        temp = temp.getScaledInstance(wh.getX(), wh.getY(), Image.SCALE_SMOOTH);
        img = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(temp, 0, 0, null);
    }

    //region getters and setters
    public entityType getType() {
        return type;
    }

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
        int x = 0;
        int y = 0;
        try {
            int bigX = main.TILE_WIDTH * XYInArr.getX();
            int bigY = main.TILE_HEIGHT * XYInArr.getY();

            int smallX = XYInTile.getX();
            int smallY = XYInTile.getY();

            x = bigX + smallX;
            y = bigY + smallY;
        } catch (Exception e) {
            return TurretFrame.NULL_COORD;
        }

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

    protected void N (int dst) {
        XYInTile.setY(XYInTile.getY() - dst);

        if(XYInTile.getY() < 0) {
            if(XYInTile.getY() <= (main.TILE_HEIGHT * -1)){
                XYInTile.setY(0);
                XYInArr.setY(XYInArr.getY() - 2);
            }else{
                int y = (XYInTile.getY() - main.TILE_HEIGHT) * -1;
                XYInTile.setY(y);
                XYInArr.setY(XYInArr.getY() - 1);
            }
        }
    }
    protected void S (int dst) {
        XYInTile.setY(XYInTile.getY() + dst);

        if(XYInTile.getY() > main.TILE_HEIGHT) {
            if(XYInTile.getY() >= (main.TILE_HEIGHT * 2)){
                XYInTile.setY(main.TILE_HEIGHT);
                XYInArr.setY(XYInArr.getY() + 2);
            }else{
                int y = (main.TILE_HEIGHT - XYInTile.getY());
                XYInTile.setY(y);
                XYInArr.setY(XYInArr.getY() + 1);
            }
        }
    }

    protected void W (int dst) {
        XYInTile.setX(XYInTile.getX() - dst);

        if(XYInTile.getX() < 0) {
            if(XYInTile.getX() <= (main.TILE_WIDTH * -1)){
                XYInTile.setX(0);
                XYInArr.setX(XYInArr.getX() - 2);
            }else{
                int x = (XYInTile.getX() - main.TILE_WIDTH) * -1;
                XYInTile.setX(x);
                XYInArr.setX(XYInArr.getX() - 1);
            }
        }
    }
    protected void E (int dst) {
        XYInTile.setX(XYInTile.getX() + dst);

        if(XYInTile.getX() > main.TILE_WIDTH) {
            if(XYInTile.getX() >= (main.TILE_WIDTH * 2)){
                XYInTile.setX(main.TILE_WIDTH);
                XYInArr.setX(XYInArr.getX() + 2);
            }else{
                int x = (main.TILE_WIDTH - XYInTile.getX());
                XYInTile.setX(x);
                XYInArr.setX(XYInArr.getX() + 1);
            }
        }
    }
}