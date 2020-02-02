package main;

import CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import Gameplay.turrets.TurretManager;
import Gameplay.waves.waveManager;
import classes.Entity.Entity;
import classes.canvas;
import classes.square.sqaureParser;
import classes.square.squareCollection;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

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

    public static final String ICON_LOCATIONS = IMAGES_LOC + "icns/";
    public static final String AUDIO_LOCATIONS = BASE_LOCATION + "audio/";
    //endregion

    //region UI sizes
    public static int CURRENT_LEVEL = 1;
    public static final CfgReader stage;
    public static final CfgReader level;
    public static final int NUM_OF_TILES_WIDTH;
    public static final int NUM_OF_TILES_HEIGHT;

    public static final int WINDOW_WIDTH = 600; //px
    public static final int WINDOW_HEIGHT = 630; //px

    private static Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static final int TOP_JPANEL_HEIGHT = 0; //px
    public static final int REST_OF_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_JPANEL_HEIGHT; //px

    public static final int TILE_HEIGHT; //px
    public static final int TILE_WIDTH;

    public static final int BOUND;

    //region widths and heights of entities
    public static final int TURRET_WIDTH;
    public static final int TURRET_HEIGHT;

    public static final int BULLET_WIDTH;
    public static final int BULLET_HEIGHT;

    public static final int ENEMY_WIDTH;
    public static final int ENEMY_HEIGHT;
    //endregion

    public static final int TURRET_X_ON_TILE;
    public static final int TURRET_Y_ON_TILE;

    //endregion

    //region fn arrays

    public static final String[] TURRET_FNS = {"wizard.cfg", "dropTower.cfg"};
    public static final String[] ENEMY_FNS = {"fastButWeak.cfg", "slowButStrong.cfg"};

    public static final String[] ENEMY_IMG_FNS = {"skeleton_big.png", "bigButSlow_big.png"};

    //endregion

    //region NAMES


    //endregion

    //region audio

    public static final HashMap<String, Clip> SOUNDS;

    //endregion

    public static void lvl1() {
        int money = Integer.parseInt(level.get("playerGets", "money").toString());
        int hearts = Integer.parseInt(level.get("playerGets", "hp").toString());

        PlayerManager pm = new PlayerManager(money, hearts);
        squareCollection sqc = new squareCollection(new sqaureParser(new CfgReader(main.MAPS_LOC + "stg1.cfg")));

        //region main window
        canvas c = new canvas(CURRENT_LEVEL);

        JFrame window = new JFrame("Apex Turrets");

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setPreferredSize(size);
        window.add(c);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        waveManager waves = new waveManager("lvl1.cfg", sqc, pm);
        TurretManager tm = new TurretManager(sqc, pm);

        Dimension newSize = new Dimension(main.TILE_WIDTH * main.NUM_OF_TILES_WIDTH, main.TILE_HEIGHT * main.NUM_OF_TILES_HEIGHT);
        window.setSize(newSize);
        //endregion

        Thread runThread = new Thread(() -> {

            long current = System.currentTimeMillis();

            long delay = 10;

            while (true) {

                window.pack();

                if (System.currentTimeMillis() - current > delay) {


                    ArrayList<Entity> enemyActuals = waves.getEntites();
                    ArrayList<Entity> turretActuals = tm.step(System.currentTimeMillis() - current, enemyActuals);

                    ArrayList<Entity> finalEnties = new ArrayList<>();
                    finalEnties.addAll(enemyActuals);
                    finalEnties.addAll(turretActuals);

                    c.setEntities(finalEnties);

                    current = System.currentTimeMillis();
                }
            }
        });

        runThread.start();
    }

    public static void main(String[] args) {
        lvl1();
    }

    static {
        stage = new CfgReader(MAPS_LOC + "stg" + CURRENT_LEVEL + ".cfg");
        level = new CfgReader(WAVES_LOC + "lvl" + CURRENT_LEVEL + ".cfg");

        NUM_OF_TILES_WIDTH = Integer.parseInt(stage.get("mapDeets", "rows").toString());
        NUM_OF_TILES_HEIGHT = Integer.parseInt(stage.get("mapDeets", "cols").toString());

        TILE_WIDTH = WINDOW_WIDTH / NUM_OF_TILES_WIDTH;
        TILE_HEIGHT = WINDOW_HEIGHT / NUM_OF_TILES_HEIGHT;

        BOUND = (TILE_WIDTH + TILE_HEIGHT) / 2;


        TURRET_WIDTH = TILE_WIDTH / 3 * 2;
        TURRET_HEIGHT = TILE_HEIGHT / 3 * 2;

        BULLET_WIDTH = TILE_WIDTH / 10;
        BULLET_HEIGHT = TILE_HEIGHT / 10;

        ENEMY_WIDTH = TILE_WIDTH * 3 / 2;
        ENEMY_HEIGHT = TILE_HEIGHT;

        TURRET_X_ON_TILE = (TILE_WIDTH - TURRET_WIDTH) / 2;
        TURRET_Y_ON_TILE = (TILE_HEIGHT - TURRET_HEIGHT) / 2;

        //region sounds
        final String[] sounds = new String[] {"Spawn.wav"};
        SOUNDS = new HashMap<>();

        for (String fn : sounds) {
            Clip c;
            try {
                URL url = new URL(AUDIO_LOCATIONS + fn);

                AudioInputStream AIS = AudioSystem.getAudioInputStream(url);

                c = AudioSystem.getClip();
                c.open(AIS);

                c.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        System.out.println(fn + " has been played...");
                        long frames = AIS.getFrameLength();
                        double l = (frames+0.0) / AIS.getFormat().getFrameRate();

                        long l2 = ((long) Math.floor(l));

                        try {
                            TimeUnit.SECONDS.sleep(l2);
                        } catch (InterruptedException e) {
                            System.out.println("Audio Sleeper interrupted.");
                        }

                        c.stop();
                        c.setMicrosecondPosition(0l);

                    }
                });

                SOUNDS.put(fn, c);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        //endregion
    }
}
