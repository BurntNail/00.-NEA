package main;

import classes.enemy.enemyDictionary;

public class main {

    //TODO: Go onto the git and add more data

    //For ease of file locations and packages, I have uploaded all images to a public github repo, the URL of which can be found as BASE_LOCATION on line 10.

    //region URL locations
    private static final String BASE_LOCATION = "https://raw.githubusercontent.com/Epacnoss/NEAAssets/master/Actual/";
    private static final String IMAGES_LOC = BASE_LOCATION + "images/";

    public static final String ENEMIES_LOC = BASE_LOCATION + "enemies/";
    public static final String TURRETS_LOC = BASE_LOCATION + "turrets/";
    public static final String WAVES_LOC = BASE_LOCATION + "waves/";

    public static final String ENEMY_IMAGES_LOC = IMAGES_LOC + "enemies/";
    public static final String TURRET_IMAGES_LOC = IMAGES_LOC + "turrets/";
    public static final String PATHS_IMAGES_LOC = IMAGES_LOC + "paths/";
    //endregion

    //region UI sizes
    public static final int NUM_OF_TILES_WIDTH = 20;
    public static final int NUM_OF_TILES_HEIGHT = 20;

    public static final int WINDOW_WIDTH = 600; //px
    public static final int WINDOW_HEIGHT = 630; //px

    public static final int TOP_JPANEL_HEIGHT = 30; //px
    public static final int REST_OF_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_JPANEL_HEIGHT; //px

    public static final int TILE_HEIGHT = REST_OF_FRAME_HEIGHT / NUM_OF_TILES_HEIGHT; //px
    public static final int TILE_WIDTH = WINDOW_WIDTH / NUM_OF_TILES_WIDTH;
    //endregion


    public static void main(String[] args) {
        enemyDictionary e = new enemyDictionary(new String[]{"fastButWeakEnemy.cfg", "slowButStrong.cfg"});
    }


}
