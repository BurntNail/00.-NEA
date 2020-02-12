package classes.Entity;

import Gameplay.turrets.TurretFrame;
import classes.coordinate.Coordinate;
import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.StringJoiner;

public abstract class Entity implements Comparable<Entity> {

    private BufferedImage img;
    private Coordinate XYInArr, XYInTile;
    private entityType type;
    private String fqdn;

    private Image base;

    public Entity(Coordinate XYInArr, String fn, entityType type, Coordinate XYInTile) {
        this.XYInArr = XYInArr;
        this.XYInTile = XYInTile;
        this.type = type;


        fqdn = "";

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

        try {
            URL url = new URL(fqdn);

            base = ImageIO.read(url);
        } catch (Exception e) {
            base = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        Dimension wh = getWHOnType(type); //wh == width and height
        base = base.getScaledInstance(wh.width, wh.height, Image.SCALE_SMOOTH);
        img = new BufferedImage(wh.width, wh.height, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(base, 0, 0, null);
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

    protected void changeXYOnScrn (int x, int y) {
        int smallX = x % main.TILE_WIDTH;
        int smallY = y % main.TILE_HEIGHT;

        int bigX = (x - smallX) / main.TILE_WIDTH;
        int bigY = (y - smallY) / main.TILE_HEIGHT;

        //region x
        //region small
        if(smallX < 0) {
            smallX = 0;
            bigX--;
        }
        if(smallX > main.TILE_HEIGHT)
        {
            bigX++;
            smallX = 0;
        }
        //endregion
        //region big
        if(bigX < 0)
            bigX = 0;
        if(bigX > main.NUM_OF_TILES_WIDTH)
        {
            bigX = main.NUM_OF_TILES_WIDTH - 1;
        }
        //endregion
        //endregion
        //region y
        //region small
        if(smallY < 0) {
            smallY = 0;
            bigY--;
        }
        if(smallY > main.TILE_WIDTH)
        {
            bigY++;
            smallY = 0;
        }
        //endregion
        //region big
        if(bigY < 0)
            bigY = 0;
        if(bigY > main.NUM_OF_TILES_HEIGHT)
        {
            bigY = main.NUM_OF_TILES_HEIGHT - 1;
        }
        //endregion
        //endregion


        XYInArr = new Coordinate(bigX, bigY);
        XYInTile = new Coordinate(smallX, smallY);
    }
    //endregion

    protected void changeX (int dst) {
        int newXInTile = getXYInTile().getX() + dst;
        int newXInArr = getXYInArr().getX();

        int oldYTile = getXYInTile().getY();
        int oldYArr = getXYInArr().getY();

        if(newXInTile < 0) {
            newXInTile += main.TILE_WIDTH; // We can do this because if XInTile < 0, then by definition the dst is less than 0
            newXInArr--;

            if(newXInTile < 0)
                newXInTile = 0;
        }
        else if (newXInTile > main.TILE_WIDTH)
        {
            newXInTile -= main.TILE_WIDTH;
            newXInArr++;

            if(newXInTile > main.TILE_WIDTH)
                newXInTile = main.TILE_WIDTH;
        }

        if(newXInArr < 0)
            newXInArr = 0;
        else if (newXInArr >= main.NUM_OF_TILES_WIDTH)
            newXInArr = main.NUM_OF_TILES_WIDTH - 1;

        XYInArr = new Coordinate(newXInArr, oldYArr);
        XYInTile = new Coordinate(newXInTile, oldYTile);

//        System.out.println("I like to move it - move it " + (dst < 0 ? "<-" : "->"));
    }

    protected void changeY (int dst) {
        int newYInTile = getXYInTile().getY() + dst;
        int newYInArr = getXYInArr().getY();

        int oldXTile = getXYInTile().getX();
        int oldXArr = getXYInArr().getX();

        if(newYInTile < 0) {
            newYInTile += main.TILE_HEIGHT; // We can do this because if XInTile < 0, then by definition the dst is less than 0
            newYInArr--;

            if(newYInTile < 0)
                newYInTile = main.TILE_HEIGHT;
        }
        else if (newYInTile >= main.TILE_HEIGHT)
        {
            newYInTile -= main.TILE_HEIGHT;
            newYInArr++;

            if(newYInTile > main.TILE_HEIGHT)
                newYInTile = main.TILE_HEIGHT;
        }

        if(newYInArr < 0)
            newYInArr = 0;
        else if (newYInArr >= main.NUM_OF_TILES_HEIGHT)
            newYInArr = main.NUM_OF_TILES_HEIGHT - 1;

        XYInArr = new Coordinate(oldXArr, newYInArr);
        XYInTile = new Coordinate(oldXTile, newYInTile);
    }

    private static Dimension getWHOnType (entityType type) {
        switch (type) {
            case enemy:
                return new Dimension(main.ENEMY_WIDTH, main.ENEMY_HEIGHT); //No break statement needed as it becomes an unreachable statement
            case bullet:
                return new Dimension(main.BULLET_WIDTH, main.BULLET_HEIGHT);
            default:
                return new Dimension(main.TURRET_WIDTH, main.TURRET_HEIGHT);
        }
    }

    public String getFqdn() {
        return fqdn;
    }

    protected void resetImg () {
        int w = img.getWidth();
        int h = img.getHeight();


        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(base, 0, 0, null);
    }


    @Override
    public int compareTo(Entity o) {
        return getXYOnScrn().compareTo(o.getXYOnScrn());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Entity.class.getSimpleName() + "[", "]")
                .add("XYInArr=" + XYInArr)
                .add("XYInTile=" + XYInTile)
                .add("type=" + type)
                .add("fqdn='" + fqdn + "'")
                .toString();
    }

    public void setXYInTile(Coordinate XYInTile) {
        this.XYInTile = XYInTile;
    }
}