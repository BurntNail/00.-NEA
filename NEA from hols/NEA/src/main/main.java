package main;

import classes.canvas;
import classes.enemy.enemyDictionary;

import javax.swing.*;
import java.awt.*;

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
    public static final int NUM_OF_TILES_WIDTH = 5;
    public static final int NUM_OF_TILES_HEIGHT = 3;

    public static final int WINDOW_WIDTH = 600; //px
    public static final int WINDOW_HEIGHT = 630; //px

    private static Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static final int TOP_JPANEL_HEIGHT = 30; //px
    public static final int REST_OF_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_JPANEL_HEIGHT; //px

    public static final int TILE_HEIGHT = REST_OF_FRAME_HEIGHT / NUM_OF_TILES_HEIGHT; //px
    public static final int TILE_WIDTH = WINDOW_WIDTH / NUM_OF_TILES_WIDTH;

    //region widths and heights of entities
    public static final int TURRET_WIDTH = TILE_HEIGHT * 2 / 3;
    public static final int TURRET_HEIGHT = TILE_HEIGHT * 2 / 3;

    public static final int BULLET_WIDTH = TILE_WIDTH / 10;
    public static final int BULLET_HEIGHT = TILE_HEIGHT / 20;

    public static final int ENEMY_WIDTH = TILE_WIDTH * 2 / 5;
    public static final int ENEMY_HEIGHT = TILE_HEIGHT * 2 / 5;
    //endregion

    public static final int TURRET_X_ON_TILE = TILE_WIDTH / 2;
    public static final int TURRET_Y_ON_TILE = TILE_HEIGHT / 10 + 3;

    //endregion

    //region fileNameArrays

    public static final String[] levelFns = {"lvl01.cfg"};


    //endregion

    //region GameData
//    public static final enemyDictionary e = new enemyDictionary(new String[]{"fastButWeakEnemy.cfg", "slowButStrong.cfg"});

    //endregion


    public static void main(String[] args) {
        canvas c = new canvas(1);

        JFrame window = new JFrame("apex turrets");
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setPreferredSize(size);
        window.add(c);
        window.pack();
        window.setVisible(true);
    }


}
