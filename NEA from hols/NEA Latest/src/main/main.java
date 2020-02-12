package main;

import CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import Gameplay.turrets.TurretManager;
import Gameplay.waves.waveManager;
import classes.Entity.Entity;
import classes.canvas;
import classes.saver.PlayerData;
import classes.square.sqaureParser;
import classes.square.squareCollection;
import classes.coordinate.Coordinate;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class main {

    //For ease of file locations and packages, I have uploaded all images to a public github repo, the URL of which can be found as BASE_LOCATION on line 28.

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

    public static final int WINDOW_WIDTH;
    public static final int WINDOW_HEIGHT;

    private static Dimension size;

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

        //region main window




        JFrame window = new JFrame("Apex Turrets");

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setLayout(new GridLayout(1, 2));

        System.out.println("WINDOW CREATED: " + new Timestamp(System.currentTimeMillis()));


        PlayerManager pm = new PlayerManager(money, hearts, window);
        System.out.println("PM DONE: " + new Timestamp(System.currentTimeMillis()));
        squareCollection sqc = new squareCollection(new sqaureParser(new CfgReader(main.MAPS_LOC + "stg1.cfg")));
        System.out.println("SQC DONE: " + new Timestamp(System.currentTimeMillis()));

        waveManager waves = new waveManager("lvl1.cfg", sqc, pm);
        System.out.println("WAVES DONE: " + new Timestamp(System.currentTimeMillis()));

        canvas c = new canvas(CURRENT_LEVEL, pm, waves);
        window.add(c);

        System.out.println("CANVAS DONE: " + new Timestamp(System.currentTimeMillis()));

        TurretManager tm = new TurretManager(sqc, pm, window);
        System.out.println("TM DONE: " + new Timestamp(System.currentTimeMillis()));

        window.pack();
        window.setVisible(true);
        window.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        System.out.println("WINDOW DONE: " + new Timestamp(System.currentTimeMillis()));

        Dimension newSize = new Dimension(main.TILE_WIDTH * main.NUM_OF_TILES_WIDTH, main.TILE_HEIGHT * main.NUM_OF_TILES_HEIGHT);
        window.setSize(newSize);
        //endregion

        Thread runThread = new Thread(() -> {

            long current = System.currentTimeMillis();

            long delay = 10;

            System.out.println("RUN THREAD STARTED - ACTUAL");

            while (true) {

                c.paint(c.getGraphics());

                while(!c.hasFinishedRendering()) {
//                    System.out.print("RENDERING");
                }

                if (System.currentTimeMillis() - current > delay) {
//                    System.out.println();

                    ArrayList<Entity> enemyActuals = ((ArrayList<Entity>) waves.getEntites().clone());
                    ArrayList<Entity> turretActuals = ((ArrayList<Entity>) tm.setEnemiesAndGetTurretsAndBullets(enemyActuals).clone());

                    ArrayList<Entity> finalEnties = new ArrayList<>();
                    finalEnties.addAll(turretActuals);
                    finalEnties.addAll(enemyActuals);

                    c.setEntities(finalEnties);

                    current = System.currentTimeMillis();
                }
            }
        });

        runThread.start();

        System.out.println("RUN THREAD STARTED - CONTROL: " + new Timestamp(System.currentTimeMillis()));
    }

    public static void main(String[] args) {
        long current = System.currentTimeMillis();
        lvl1();
    }

    static {
        System.out.println("MAIN STATIC STARTING");

        stage = new CfgReader(MAPS_LOC + "stg" + CURRENT_LEVEL + ".cfg");
        level = new CfgReader(WAVES_LOC + "lvl" + CURRENT_LEVEL + ".cfg");

        NUM_OF_TILES_WIDTH = Integer.parseInt(stage.get("mapDeets", "rows").toString());
        NUM_OF_TILES_HEIGHT = Integer.parseInt(stage.get("mapDeets", "cols").toString());


        Dimension wholeScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int wTemp = wholeScreenSize.width / 2;
        int hTemp = wholeScreenSize.height;


        WINDOW_WIDTH = wTemp - (wTemp % NUM_OF_TILES_WIDTH);
        WINDOW_HEIGHT = hTemp - (hTemp % NUM_OF_TILES_WIDTH);


        size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);


        TILE_WIDTH = Math.floorDiv(WINDOW_WIDTH, NUM_OF_TILES_WIDTH);
        TILE_HEIGHT = Math.floorDiv(WINDOW_HEIGHT, NUM_OF_TILES_HEIGHT);

        BOUND = (TILE_WIDTH + TILE_HEIGHT) / 2;

        TURRET_WIDTH = TILE_WIDTH / 3 * 2;
        TURRET_HEIGHT = TILE_HEIGHT / 3 * 2;

        BULLET_WIDTH = TURRET_WIDTH * 2 / 3;
        BULLET_HEIGHT = TURRET_HEIGHT * 2 / 3;

        ENEMY_WIDTH = TILE_WIDTH * 3 / 2;
        ENEMY_HEIGHT = TILE_HEIGHT * 3 / 2;

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

                c.addLineListener(event -> {
//                    System.out.println(fn + " has been played...");
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

                });

                SOUNDS.put(fn, c);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        //endregion

        System.out.println("MAIN STATIC DONE");
    }

    //region sorters
    public static ArrayList<Coordinate> quickCoord (ArrayList<Coordinate> listBase)
    {
        ArrayList<Coordinate> list = ((ArrayList<Coordinate>) listBase.clone());

        if(list.size() > 1)
        {

            Coordinate pivot = list.get(list.size() / 2);
            ArrayList<Coordinate> less, more, equal;
            less = new ArrayList<>();
            more = new ArrayList<>();
            equal = new ArrayList<>();

            for(Coordinate x : list)
            {
                if(x.compareTo(pivot) < 0)
                    less.add(x);
                else if (x.compareTo(pivot) == 0)
                    equal.add(x);
                else
                    more.add(x);

            }

            less = quickCoord(less);
            more = quickCoord(more);

            list.clear();

            list.addAll(less);
            list.addAll(equal);
            list.addAll(more);
        }

        return list;
    }

    public static List<? extends Entity> quickEntity (List<Entity> list)
    {
        if(list.size() > 1)
        {

            Entity pivot = list.get(list.size() / 2);
            List<Entity> less, more, equal;
            less = new ArrayList<>();
            more = new ArrayList<>();
            equal = new ArrayList<>();

            for(Entity x : list)
            {
                if(x.compareTo(pivot) < 0)
                    less.add(x);
                else if (x.compareTo(pivot) == 0)
                    equal.add(x);
                else
                    more.add(x);

            }

            less = (List<Entity>) quickEntity(less);
            more = (List<Entity>) quickEntity(more);
            list.clear();

            list.addAll(less);
            list.addAll(equal);
            list.addAll(more);
        }

        return list;
    }

    public static PlayerData[] quickPlayerData (PlayerData[] list)
    {
        if(list.length > 1)
        {

            PlayerData pivot = list[list.length / 2];
            PlayerData[] less, more, equal;
            less = new PlayerData[list.length];
            more = new PlayerData[list.length];
            equal = new PlayerData[list.length];

            int lessIndex = 0;
            int moreIndex = 0;
            int equalIndex = 0;

            for(PlayerData x : list)
            {
                if(x.compareTo(pivot) < 0)
                {
                    less[lessIndex] = x;
                    lessIndex++;
                }
                else if (x.compareTo(pivot) == 0)
                {
                    equal[equalIndex] = x;
                    equalIndex++;
                }
                else
                {
                    more[moreIndex] = x;
                    moreIndex++;
                }

            }

            less = quickPlayerData(less);
            more = quickPlayerData(more);

            list = new PlayerData[list.length];

            int index = 0;

            for(PlayerData p : less)
            {
                list[index] = p;
                index++;
            }
            for(PlayerData p : equal)
            {
                list[index] = p;
                index++;
            }
            for(PlayerData p : more)
            {
                list[index] = p;
                index++;
            }
        }

        return list;
    }
    //endregion
}
