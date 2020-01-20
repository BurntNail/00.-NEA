package classes.square.types;

import classes.util.Coordinate;
import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class Square {

    protected Coordinate xAndY;
    protected BufferedImage img;

    public Square (String fn, Coordinate xY) {
        img = new BufferedImage(main.TILE_WIDTH, main.TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        try {
            URL earl = new URL(main.PATHS_IMAGES_LOC + fn);
            URLConnection c = earl.openConnection();
            Image temp = ImageIO.read(c.getInputStream()).getScaledInstance(main.TILE_WIDTH, main.TILE_HEIGHT, Image.SCALE_SMOOTH);


            img.getGraphics().drawImage(temp, 0, 0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        xAndY = xY;

    }

    public Coordinate getxAndY() {
        return xAndY;
    }

    public BufferedImage getImg() {
        return img;
    }
}
