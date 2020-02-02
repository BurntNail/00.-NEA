package main;

import CfgReader.CfgReader;
import Gameplay.waves.waveManager;
import classes.Entity.Entity;
import classes.canvas;
import classes.square.squareCollection;
import classes.util.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class main {

    //TODO: Go onto the git and add more data

    //For ease of file locations and packages, I have uploaded all images to a public github repo, the URL of which can be found as BASE_LOCATION on line 10.

    //region URL locations
    private static final String BASE_LOCATION = "https://raw.githubusercontent.com/Epacnoss/NEAAssets/master/Actual/";
    private static final String IMAGES_LOC = BASE_LOCATION + "images/";

    public static final String ENEMIES_LOC = BASE_LOCATION + "enemies/";
    public static final String TURRETS_LOC = BASE_LOCATION + "turrets/";
    public static final String WAVES_LOC = BASE_LOCATION + "waves/";
    public static final String MAPS_LOC = BASE_LOCATION + "maps/";

    public static final String ENEMY_IMAGES_LOC = IMAGES_LOC + "enemies/";
    public static final String TURRET_IMAGES_LOC = IMAGES_LOC + "turrets/";
    public static final String PATHS_IMAGES_LOC = IMAGES_LOC + "paths/";
    public static final String BULLET_IMAGE_LOC = IMAGES_LOC + "bullets/";
    //endregion

    //region UI sizes
    public static int CURRENT_LEVEL = 1;
    private static final CfgReader stage = new CfgReader(MAPS_LOC + "stg" + CURRENT_LEVEL + ".cfg");
    public static final int NUM_OF_TILES_WIDTH = 10;
    public static final int NUM_OF_TILES_HEIGHT = 3;

    public static final int WINDOW_WIDTH = 600; //px
    public static final int WINDOW_HEIGHT = 630; //px

    private static Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static final int TOP_JPANEL_HEIGHT = 30; //px
    public static final int REST_OF_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_JPANEL_HEIGHT; //px

    public static final int TILE_HEIGHT = REST_OF_FRAME_HEIGHT / NUM_OF_TILES_HEIGHT; //px
    public static final int TILE_WIDTH = WINDOW_WIDTH / NUM_OF_TILES_WIDTH;

    public static final int BOUND = ((TILE_WIDTH + TILE_HEIGHT) / 2);

    //region widths and heights of entities
    public static final int TURRET_WIDTH = TILE_HEIGHT * 2 / 3;
    public static final int TURRET_HEIGHT = TILE_HEIGHT * 2 / 3;

    public static final int BULLET_WIDTH = TILE_WIDTH / 10;
    public static final int BULLET_HEIGHT = TILE_HEIGHT / 20;

    public static final int ENEMY_WIDTH = TILE_WIDTH * 2 / 3;
    public static final int ENEMY_HEIGHT = TILE_HEIGHT * 4 / 5;
    //endregion

    public static final int TURRET_X_ON_TILE = TILE_WIDTH / 2;
    public static final int TURRET_Y_ON_TILE = TILE_HEIGHT / 10 + 3;

    //endregion

    //region fn arrays

    public static final String[] TURRET_FNS = {"wizard.cfg", "dropTower.cfg"};
    public static final String[] ENEMY_FNS = {"fastButWeak.cfg", "slowButStrong.cfg"};

    public static final String[] TURRET_IMG_FNS = {"dropper_big.png", "sorcerer_big.png"};
    public static final String[] ENEMY_IMG_FNS = {"skeleton_big.png", "bigButSlow_big.png"};

    //endregion

    public static void lvl1 () {
        //region main window
        canvas c = new canvas(CURRENT_LEVEL);

        JFrame window = new JFrame("Apex Turrets");

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setPreferredSize(size);
        window.add(c);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        waveManager waves = new waveManager("lvl1.cfg", "stg1.cfg");

        long timeSinceLast = 0;
        long currentTime = 0;
        long previousLoopTime = System.currentTimeMillis();
        long delay = 100;

        Dimension newSize = new Dimension(main.TILE_WIDTH * main.NUM_OF_TILES_WIDTH, main.TILE_HEIGHT * main.NUM_OF_TILES_HEIGHT);
        window.setSize(newSize);
        //endregion

        JFrame turretWin = new JFrame("Turret Window");
        JButton[][] buttons = new JButton[NUM_OF_TILES_WIDTH][NUM_OF_TILES_HEIGHT];
        GridLayout gl = new GridLayout(NUM_OF_TILES_WIDTH, NUM_OF_TILES_HEIGHT);
        turretWin.setLayout(gl);
        turretWin.setPreferredSize(newSize);

        squareCollection squares = c.getSquares();

        for (int j = 0; j < NUM_OF_TILES_HEIGHT; j++) {
            for (int i = 0; i < NUM_OF_TILES_WIDTH; i++) {
                boolean isTurretSquare = squares.getAvailableTurretSquares().contains(new Coordinate(i, j));
                String txt = "";

                if(isTurretSquare)
                    txt = "Turret Here fam";

                buttons[i][j] = new JButton(txt);
                buttons[i][j].setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
                turretWin.add(buttons[i][j]);
            }
        }

        turretWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        turretWin.pack();
        turretWin.setVisible(true);

        while (true) {

            currentTime = System.currentTimeMillis();
            timeSinceLast += currentTime - previousLoopTime;
            previousLoopTime = System.currentTimeMillis();



            if(timeSinceLast <= delay)
                continue;





            ArrayList<Entity> enemyActuals = waves.step(timeSinceLast);

            c.setEntities(enemyActuals);

            while(!c.isFinishedRendering()) {
                continue;
            }

            window.pack();

            timeSinceLast = 0;
        }
    }

    public static void main(String[] args) {
        lvl1();
    }


}
