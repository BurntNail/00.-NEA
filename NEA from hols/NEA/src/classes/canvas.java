package classes;

import CfgReader.CfgReader;
import classes.Entity.Entity;
import classes.square.types.Square;
import main.main;

import javax.swing.*;

import classes.square.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class canvas extends JComponent {

    private squareCollection sqc;
    private ArrayList<Entity> entities;

    private Thread runThread;


    public canvas (int stage) {

        CfgReader sqpCfg = new CfgReader(main.MAPS_LOC + "stg" + stage + ".cfg");
        sqaureParser sqp = new sqaureParser(sqpCfg);
        sqc = new squareCollection(sqp);
        entities = new ArrayList<>();

        Runnable r = () -> {
            while(true) {
                paintComponent(getGraphics());
            }
        };

        runThread = new Thread(r);
        runThread.start();
    }

    public void setEntities (ArrayList<Entity> entities) {
        this.entities = entities;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage vCanvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        int xOnScrn = 0;
        int yOnScrn = 0;

        int w = main.TILE_WIDTH;
        int h = main.TILE_HEIGHT;


        Square[][] squares = sqc.getSquares();

        for(int yInArr = 0; yInArr < squares[0].length; yInArr++) {
            for(int xInArr = 0; xInArr < squares.length; xInArr++){

                Image img = squares[xInArr][yInArr].getImg();
                vCanvas.getGraphics().drawImage(img, xOnScrn, yOnScrn, null);

                xOnScrn += w;

            }
            xOnScrn = 0;
            yOnScrn += h;
        }

        if(entities.size() != 0) {
            for (Entity entity : entities) {
                Image img = entity.getImg();

                int x = entity.getXYOnScrn().getX();
                int y = entity.getXYOnScrn().getY();

                vCanvas.getGraphics().drawImage(img, x, y, null);
            }
        }

        g.drawImage(vCanvas, 0, 0, null);

        repaint();


    }


    public squareCollection getSquares () {
        return sqc;
    }
}
