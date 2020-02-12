package classes;

import CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import Gameplay.turrets.turretFrame.Console;
import Gameplay.waves.waveManager;
import classes.Entity.Entity;
import classes.enemy.enemyActual;
import classes.square.types.Square;
import classes.turret.turretActual;
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

    private boolean playerIsDead;
    private int NumOfDarkenings;
    private boolean deathAnimationDone;
    private boolean deathStarted;
    private Thread deadThread;

    private BufferedImage vCanvas;

    public canvas (int stage, PlayerManager pm, waveManager wm) {

        CfgReader sqpCfg = new CfgReader(main.MAPS_LOC + "stg" + stage + ".cfg");
        sqaureParser sqp = new sqaureParser(sqpCfg);
        sqc = new squareCollection(sqp);
        entities = new ArrayList<>();

        finishedRendering = true;

        playerIsDead = false;
        deathAnimationDone = false;
        deathStarted = false;
        NumOfDarkenings = 0;

        pm.addBooleanChangeListener(e -> {
            if(pm.isDead())
                playerIsDead = true;
        });

        Runnable dead = () -> {

            int wCanvas = vCanvas.getWidth();
            int hCanvas = vCanvas.getHeight();

            System.out.println(wCanvas * hCanvas);

            Color bottomRight;

            int score = wm.getEnemiesSpawned() * pm.getMoney();

            StringBuilder lossTxtBuilder = new StringBuilder();
            lossTxtBuilder.append("You lost..." + "\n");
            lossTxtBuilder.append("Score: " + score + "\n");
            lossTxtBuilder.append("Well done tho m8" + "\n");
            String txt = lossTxtBuilder.toString();

            vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);

            do {
                System.out.println("DeadThread started");
                for (int y = 0; y < hCanvas; y++) {
                    for (int x = 0; x < wCanvas; x++) {
                        Color thisPix = new Color(vCanvas.getRGB(x, y));

                        NumOfDarkenings++;
                        for (int i = 0; i < NumOfDarkenings; i++) {
                            thisPix = thisPix.darker();
                        }
                        System.out.println(NumOfDarkenings + "    " + thisPix.getRed() + " " + thisPix.getGreen() + " " + thisPix.getBlue());


                        vCanvas.setRGB(x, y, thisPix.getRGB());

                        vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);
                    }
                }

                bottomRight = new Color(vCanvas.getRGB(wCanvas, hCanvas));
                repaint();
            } while(bottomRight.getRed() > 0 && bottomRight.getGreen() > 0 && bottomRight.getBlue() > 0);
        };
        deadThread = new Thread(dead);
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

        if(!playerIsDead) {
            vCanvas = new BufferedImage(
                    (getWidth() == 0 ? main.WINDOW_WIDTH : getWidth()),
                    (getHeight() == 0 ? main.WINDOW_HEIGHT : getHeight()),
                    BufferedImage.TYPE_INT_ARGB);

            int xOnScrn = 0;
            int yOnScrn = 0;

            int w = main.TILE_WIDTH;
            int h = main.TILE_HEIGHT;


            Square[][] squares = sqc.getSquares();

            for(int yInArr = 0; yInArr < squares[0].length; yInArr++) {
                for (int xInArr = 0; xInArr < squares.length; xInArr++) {

                    Image img = squares[xInArr][yInArr];
                    vCanvas.getGraphics().drawImage(img, xOnScrn, yOnScrn, null);

                    xOnScrn += w;

                }
                xOnScrn = 0;
                yOnScrn += h;
            }

            if(entities.size() != 0) {
                for (Entity entity : ((ArrayList<Entity>) entities.clone())) {
                    try {
                        Image img;

                        switch (entity.getType()) {
                            case enemy -> {
                                enemyActual casted = ((enemyActual) entity);

                                if(casted.isHasBeenSpawned())
                                    img = entity.getImg();
                                else
                                    img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

                                break;
                            }
                            default -> {
                                img = entity.getImg();
                                break;
                            }
                        }

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

        }else { //TODO: Add faster death animation - a bunch of dynamically arranged bufferedImages, that slowly render in.........
            if(!deathStarted)
            {
                deadThread.start();
                deathStarted = true;
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