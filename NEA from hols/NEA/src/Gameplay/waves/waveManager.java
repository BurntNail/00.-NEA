package Gameplay.waves;

import CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.enemy.enemyActual;
import classes.enemy.enemyDictionary;
import classes.enemy.enemyTemplate;
import classes.square.squareCollection;
import classes.turret.turretActual;
import main.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class waveManager {

    private ArrayList<Wave> waves;

    private ArrayList<ArrayList<Character>> wavesInBetterForm; //Arraylist (all waves) of arraylists (individual waves)

    private ArrayList<Entity> enemyActuals;
    private CfgReader reader;

    private long msSinceLastEnemy;
    private long msSinceLastWave;

    private int enemiesSpawned;

    private long enemyDist;
    private long waveDist;

    private enemyDictionary enemyDictionary;
    private squareCollection sqc;

    private Thread runThread;


    public waveManager(String fnOfWave, squareCollection sqc_, PlayerManager pm) {
        reader = new CfgReader(main.WAVES_LOC + fnOfWave);
        waves = WaveParser.enemiesBetweenGaps(reader);
        enemyActuals = new ArrayList<>();

        msSinceLastEnemy = 0;
        msSinceLastWave = 0;
        enemiesSpawned = 0;
        enemyDist = (Integer.parseInt(reader.get("enemyGaps", "enemyGap").toString())) * 1000;
        waveDist = (Integer.parseInt(reader.get("enemyGaps", "waveGap").toString())) * 1000;

        enemyDictionary = new enemyDictionary(main.ENEMY_FNS, main.ENEMY_IMG_FNS);
        sqc = sqc_;

        wavesInBetterForm = new ArrayList<>();

        for (int i = 0; i < waves.size(); i++) {
            HashMap<Character, Integer> oldWave = waves.get(i).getWave();
            Object[] keySet = oldWave.keySet().toArray(); //now we have an obj array

            ArrayList<Character> thisWave = new ArrayList<>();

            for (int j = 0; j < keySet.length; j++) {
                char c = keySet[j].toString().charAt(0); //Obj to char

                int no = oldWave.get(c);

                for (int k = 0; k < no; k++) {
                    thisWave.add(c);
                }
            }

            wavesInBetterForm.add(thisWave);
        }

        Runnable runnable = new Runnable() {

            long now = 0;
            long then = 0;

            @Override
            public void run() {
                Thread checkThread = new Thread(() -> {
                    while(true) {
                        check(enemyActuals, pm);
                    }
                });
                checkThread.start();


                now = System.currentTimeMillis();

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

                        while(!eA.isHasBeenSpawned()) {
                            //do nothing, wait
                        }

                        enemyActuals.add(eA);
                        co++;
                        enemiesSpawned++;

                        if(pm.isDead())
                            break;
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
}
