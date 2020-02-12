package classes.square;

import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.net.URL;

public abstract class Square extends BufferedImage{

    public Square (String fn) {
        super(main.TILE_WIDTH, main.TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        try {
            URL earl = new URL(main.PATHS_IMAGES_LOC + fn);
            Image temp = ImageIO.read(earl).getScaledInstance(main.TILE_WIDTH, main.TILE_HEIGHT, Image.SCALE_SMOOTH);


            getGraphics().drawImage(temp, 0, 0, null);

        } catch (Exception e) {
            System.out.println("tHe ImAgE mAkEr iN tHe SqUaRe ClAsS hAs BeEn ViOlAtEd");
            e.printStackTrace();
            System.exit(1);
        }

    }

}
