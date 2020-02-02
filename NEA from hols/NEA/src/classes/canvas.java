package classes;

import CfgReader.CfgReader;
import classes.Entity.Entity;
import main.main;

import javax.swing.*;

import classes.square.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class canvas extends JComponent {

    private sqaureParser sqp;
    private ArrayList<Entity> entities;

    public canvas (int stage) {

        CfgReader sqpCfg = new CfgReader(main.MAPS_LOC + "stg" + stage + ".cfg");
        sqp = new sqaureParser(sqpCfg);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int xOnScrn = 0;
        int yOnScrn = 0;

        int w = main.TILE_WIDTH;
        int h = main.TILE_HEIGHT;


        Square[][] squares = sqp.getSquares();

        for(int yInArr = 0; yInArr < squares[0].length; yInArr++) {
            for(int xInArr = 0; xInArr < squares.length; xInArr++){
                BufferedImage img = squares[xInArr][yInArr].getImg();
                g.drawImage(img, xOnScrn, yOnScrn, null);

                xOnScrn += w;

            }
            xOnScrn = 0;
            yOnScrn += h;
        }



    }


}
