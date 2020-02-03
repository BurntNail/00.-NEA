package Gameplay.waves;

import CfgReader.CfgReader;
import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.enemy.enemyActual;
import classes.enemy.enemyDictionary;
import classes.enemy.enemyTemplate;
import classes.square.squareCollection;
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

    private long enemyDist;
    private long waveDist;

    private enemyDictionary enemyDictionary;
    private squareCollection sqc;

    private Thread runThread;


    public waveManager(String fnOfWave, squareCollection sqc_) {
        reader = new CfgReader(main.WAVES_LOC + fnOfWave);
        waves = WaveParser.enemiesBetweenGaps(reader);
        enemyActuals = new ArrayList<>();

        msSinceLastEnemy = 0;
        msSinceLastWave = 0;
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
                        enemyActual eA = new enemyActual(eT, sqc.clone(), co);

                        eA.addBooleanChangeListener(e -> {
                            enemyActuals.remove(eA);

                            boolean isDead = eA.isDead();
                            if(!isDead)
                                PlayerManager.takeHearts(eA.getTemplate().getHeartsCost());
                            else
                            {
                                int moneyGained = eA.getTemplate().getMoneyBack();
                                PlayerManager.donateM(moneyGained);
                            }

                        });

                        eA.start();

                        enemyActuals.add(eA);
                        co++;
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
}
