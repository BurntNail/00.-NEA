package classes;

import CfgReader.CfgReader;
import Gameplay.turrets.turretFrame.Console;
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

    private boolean finishedRendering;

    public canvas (int stage) {

        CfgReader sqpCfg = new CfgReader(main.MAPS_LOC + "stg" + stage + ".cfg");
        sqaureParser sqp = new sqaureParser(sqpCfg);
        sqc = new squareCollection(sqp);
        entities = new ArrayList<>();
    }

    public void setEntities (ArrayList<Entity> entities_) {
        finishedRendering = false;
        entities.clear();
        entities = (ArrayList<Entity>) entities_.clone();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        finishedRendering = false;

        BufferedImage vCanvas = new BufferedImage(
                (getWidth() == 0 ? main.WINDOW_WIDTH : getWidth()),
                (getHeight() == 0 ? main.WINDOW_HEIGHT : getHeight()),
                BufferedImage.TYPE_INT_ARGB);

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
                try {
                    Image img = entity.getImg();

                    int x = entity.getXYOnScrn().getX();
                    int y = entity.getXYOnScrn().getY();

                    vCanvas.getGraphics().drawImage(img, x, y, null);
                } catch (Exception e) {
                    try {
                        Console.addText("@Canvas: Entity Img Not Found at: " + entity.getFqdn());
                    } catch (Exception ex) {
                        Console.addText("@Canvas - Entity not found.");
                    }
                }
            }
        }

        if(g != null)
            g.drawImage(vCanvas, 0, 0, null);

        repaint();

        finishedRendering = true;
    }

    public boolean hasFinishedRendering () {
        return finishedRendering;
    }
}