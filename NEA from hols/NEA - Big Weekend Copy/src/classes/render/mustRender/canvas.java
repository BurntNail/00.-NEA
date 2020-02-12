package classes.render.mustRender;

import classes.util.CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import Gameplay.waves.waveManager;
import classes.render.mustBeRendered.Entity.Entity;
import classes.render.mustBeRendered.Entity.enemy.enemyActual;
import classes.render.mustBeRendered.square.Square;
import main.main;

import javax.swing.*;

import classes.render.mustBeRendered.square.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class canvas extends JComponent {

    private squareCollection sqc;
    private ArrayList<Entity> entities;

    private boolean finishedRendering;

    private boolean playerIsDead;
    private boolean deathStarted;
    private Thread deadThread;

    private boolean playerHasWon;
    private boolean winStarted;
    private Thread winThread;


    private BufferedImage vCanvas;

    private static final BufferedImage[] RED_IMGS;
    private static final BufferedImage[] GREEN_IMGS;
    private static final long DIFFERENCE_BETWEEN_COLOUR_CHANGE = 1;
    private static final long DIFFERENCE_BETWEEN_SQUARES = 1;

    static {
        Color[] reds = new Color[256];
        Color[] grns = new Color[256];
        for (int i = 0; i < reds.length; i++)
            reds[i] = new Color(i, 0, 0);
        for (int i = 0; i < grns.length; i++)
            grns[i] = new Color(0, i, 0);

        RED_IMGS = new BufferedImage[256];
        GREEN_IMGS = new BufferedImage[256];

        for (int i = 0; i < RED_IMGS.length; i++) {
            BufferedImage red = new BufferedImage(main.TILE_WIDTH, main.TILE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            BufferedImage grn = new BufferedImage(main.TILE_WIDTH, main.TILE_HEIGHT, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < main.TILE_HEIGHT; y++) {
                for (int x = 0; x < main.TILE_WIDTH; x++) {
                    red.setRGB(x, y, reds[i].getRGB());
                    grn.setRGB(x, y, grns[i].getRGB());
                }
            }

            RED_IMGS[i] = red;
            GREEN_IMGS[i] = grn;
        }

        System.out.println("IMGS CREATED");
    }

    public canvas (int stage, PlayerManager pm, waveManager wm) {

        CfgReader sqpCfg = new CfgReader(main.MAPS_LOC + "stg" + stage + ".cfg");
        sqaureParser sqp = new sqaureParser(sqpCfg);
        sqc = new squareCollection(sqp);
        entities = new ArrayList<>();

        finishedRendering = true;

        playerIsDead = false;
        deathStarted = false;

        pm.addBooleanChangeListener(e -> {
            if(pm.isDead())
                playerIsDead = true;
            if(pm.haveIWon())
                playerHasWon = true;
        });

        Runnable dead = () -> {

            int wCanvas = vCanvas.getWidth();
            int hCanvas = vCanvas.getHeight();

            System.out.println(wCanvas * hCanvas);


            int score = wm.getEnemiesSpawned() * (pm.startHearts() - pm.getHearts());

            StringBuilder lossTxtBuilder = new StringBuilder();
            lossTxtBuilder.append("You lost..." + "\n");
            lossTxtBuilder.append("Score: " + score + "\n");
            lossTxtBuilder.append("Well done tho m8" + "\n");
            String txt = lossTxtBuilder.toString();

            vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);


            for (int y = 0; y < main.NUM_OF_TILES_HEIGHT; y++) {
                for (int x = 0; x < main.NUM_OF_TILES_WIDTH; x++) {
                    for (int i = 0; i < 256; i++) {
                        vCanvas.getGraphics().drawImage(RED_IMGS[i], x * main.TILE_WIDTH, y * main.TILE_HEIGHT, null);
                        vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);

                        repaint();

                        try {
                            TimeUnit.MILLISECONDS.sleep(DIFFERENCE_BETWEEN_COLOUR_CHANGE);
                        } catch (InterruptedException e) {
                            System.out.println("DIFF BETWEEN COLOUR CHANGE INTERRUPTED");
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(DIFFERENCE_BETWEEN_SQUARES);
                    } catch (InterruptedException e) {
                        System.out.println("DIFF BETWEEN SQUARES CHANGE INTERRUPTED");
                    }
                }
            }

        };
        deadThread = new Thread(dead);

        Runnable won = () -> {
            int wCanvas = vCanvas.getWidth();
            int hCanvas = vCanvas.getHeight();

            System.out.println(wCanvas * hCanvas);


            int score = pm.getMoney() + pm.getHearts() * 10;

            StringBuilder winTxtBuilder = new StringBuilder();
            winTxtBuilder.append("You won!" + "\n");
            winTxtBuilder.append("Score: " + score + "\n");
            winTxtBuilder.append("Well done m8" + "\n");
            String txt = winTxtBuilder.toString();

            vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);


            for (int y = 0; y < main.NUM_OF_TILES_HEIGHT; y++) {
                for (int x = 0; x < main.NUM_OF_TILES_WIDTH; x++) {
                    for (int i = 0; i < 256; i++) {
                        vCanvas.getGraphics().drawImage(GREEN_IMGS[i], x * main.TILE_WIDTH, y * main.TILE_HEIGHT, null);
                        vCanvas.getGraphics().drawString(txt, main.TILE_WIDTH, main.TILE_HEIGHT);

                        repaint();

                        try {
                            TimeUnit.MILLISECONDS.sleep(DIFFERENCE_BETWEEN_COLOUR_CHANGE);
                        } catch (InterruptedException e) {
                            System.out.println("DIFF BETWEEN COLOUR CHANGE INTERRUPTED");
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(DIFFERENCE_BETWEEN_SQUARES);
                    } catch (InterruptedException e) {
                        System.out.println("DIFF BETWEEN SQUARES CHANGE INTERRUPTED");
                    }
                }
            }
        };
        winThread = new Thread(won);
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

        if(!playerIsDead && !playerHasWon) {
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

            ArrayList<Entity> temp = ((ArrayList<Entity>) entities.clone());

            if(temp.size() != 0) {
                for (Entity entity : temp) {
                        if (entity == null){
                            System.out.println("WHAT A PAIN");
                            continue;
                        }


                    try {
                        Image img;

                        switch (entity.getType()) {
                            case enemy -> {
                                enemyActual casted = ((enemyActual) entity);

                                if(casted.haveIBeenSpawnedYet())
                                    img = entity.getImg();
                                else
                                    img = null;
//                                System.out.println("ENEMY");

                                break;
                            }
                            default -> {
                                img = entity.getImg();
//                                System.out.println("Default skin");
                                break;
                            }
                        }

                        if (img != null) {
                            int x = entity.getXYOnScrn().getX();
                            int y = entity.getXYOnScrn().getY();

                            vCanvas.getGraphics().drawImage(img, x, y, null);
                        }
                    } catch (Exception e) {
                        System.err.println("ENTITY NOT FOUND - ");
                        e.printStackTrace();
                    }
                }
            }

        }else {
            if(!deathStarted && !winStarted)
            {
                if(playerIsDead) {
                    deathStarted = true;
                    deadThread.start();
                } else if (playerHasWon) {
                    winStarted = true;
                    winThread.start();
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