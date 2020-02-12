package Gameplay.waves;

import classes.util.CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import classes.render.mustBeRendered.Entity.baseEntity.Entity;
import classes.render.mustBeRendered.Entity.enemy.enemyActual;
import classes.render.mustBeRendered.Entity.enemy.enemyDictionary;
import classes.render.mustBeRendered.Entity.enemy.enemyTemplate;
import classes.render.mustBeRendered.square.squareCollection;
import main.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class waveManager {

    private ArrayList<Wave> waves;

    private ArrayList<ArrayList<Character>> wavesInBetterForm; //Arraylist (all waves) of arraylists (individual waves)

    private ArrayList<Entity> enemyActuals;
    private CfgReader reader;

    private int enemiesSpawned;

    private long enemyDist;
    private long waveDist;

    private enemyDictionary enemyDictionary;
    private squareCollection sqc;

    private Thread runThread;
    private Thread checkThread;

    private long totalTime;
    private long startTime;
    private long mostRecentTime;


    public waveManager(String fnOfWave, squareCollection sqc_, PlayerManager pm) {
        reader = new CfgReader(main.WAVES_LOC + fnOfWave);
        waves = WaveParser.enemiesBetweenGaps(reader);
        enemyActuals = new ArrayList<>();

        enemiesSpawned = 0;
        totalTime = enemyActual.START_GAP * 1000 + 10000;
        enemyDist = (Integer.parseInt(reader.get("enemyGaps", "enemyGap").toString())) * 1000;
        waveDist = (Integer.parseInt(reader.get("enemyGaps", "waveGap").toString())) * 1000;

        enemyDictionary = new enemyDictionary(main.ENEMY_FNS, main.ENEMY_IMG_FNS);
        sqc = sqc_;

        wavesInBetterForm = new ArrayList<>();
        System.out.println("Lets get the party started in here!");

        for (int i = 0; i < waves.size(); i++) {
            HashMap<Character, Integer> oldWave = waves.get(i).getWave();
            Object[] keySet = oldWave.keySet().toArray(); //now we have an obj array

            ArrayList<Character> thisWave = new ArrayList<>();

            for (int j = 0; j < keySet.length; j++) {
                char c = keySet[j].toString().charAt(0); //Obj to char

                int no = oldWave.get(c);

                for (int k = 0; k < no; k++) {
                    thisWave.add(c);
                    totalTime += enemyDist;
                }

                totalTime += waveDist;
            }

            wavesInBetterForm.add(thisWave);
        }

        Runnable checker = () -> {
            while(true) {
                check(enemyActuals, pm);
            }
        };

        checkThread = new Thread(checker);
        checkThread.start();

        Runnable runnable = new Runnable() {

            long now = 0;
            long then = 0;

            @Override
            public void run() {

                System.out.println("running");


                now = System.currentTimeMillis();


                startTime = System.currentTimeMillis();
                int co = 0;
                for (ArrayList<Character> thisWave : wavesInBetterForm) {
                    try {

                        long gap = then - now;
                        long dist = (gap > waveDist ?
                                0 : gap - waveDist);

                        TimeUnit.MILLISECONDS.sleep(dist);

                    } catch (InterruptedException e) {
                        System.out.println("Wave wait interrupted");
                    }

                    for(Character c : thisWave) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(enemyDist);
                        } catch (InterruptedException e) {
                            System.out.println("Enemy wait interrupted.");
                        }

                        enemyTemplate eT = enemyDictionary.getEnemy(c);
                        enemyActual eA = new enemyActual(eT, sqc.clone(), co, pm);

                        eA.start();
                        System.out.println("Enemy Started");

                        while(!eA.haveIBeenSpawnedYet())
                        {
                            try {
                                TimeUnit.MILLISECONDS.sleep(200);
//                                System.out.println("have i been spwned yet");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
//                                System.out.println("have i been spwned yet");
                            }
                        }

                        enemyActuals.add(eA);
                        co++;
                        enemiesSpawned++;


                        if(pm.isDead())
                            break;
                        if(hasThePCWon())
                            pm.hasWon();

                        mostRecentTime = System.currentTimeMillis();
                    }

                    then = System.currentTimeMillis();
                }

            }
        };

        runThread = new Thread(runnable);
        runThread.start();
    }

    public ArrayList<Entity> getEntites () {
        return enemyActuals;
    }

    private static void check (ArrayList<Entity> enemyActuals, PlayerManager pm) {
        for(Entity e : ((ArrayList<Entity>) enemyActuals.clone())) //Cloned to avoid ConcurrentModificationExceptions
        {
            enemyActual eA = ((enemyActual) e);
            if(eA.isDone()) {
                enemyActuals.remove(e);

                boolean isDead = eA.isDead();
                if(!isDead) {
                    pm.takeHearts(eA.getTemplate().getHeartsCost());
                    System.out.println("Tybalteth's victory was reported, and we no got him :-(");
                }
                else
                {
                    int moneyGained = eA.getTemplate().getMoneyBack();
                    pm.donateM(moneyGained);
                    System.out.println("Ladies and Gentlemen. We got him!");
                }
            }
        }
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    private boolean hasThePCWon () {
        if(mostRecentTime == 0 || startTime == 0 || enemiesSpawned == 0)
            return false;

        long timeSoFar = mostRecentTime - startTime;
        long timeLeft = totalTime - timeSoFar;
        System.out.println(timeLeft);
        System.out.println(timeSoFar);

        return timeSoFar >= totalTime;
    }
}