package Gameplay.waves;

import CfgReader.CfgReader;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.enemy.enemyActual;
import classes.enemy.enemyDictionary;
import classes.enemy.enemyTemplate;
import classes.square.sqaureParser;
import classes.square.squareCollection;
import classes.turret.turretActual;
import main.main;

import java.util.ArrayList;
import java.util.HashMap;

public class waveManager {

    private ArrayList<Wave> waves;

    private ArrayList<ArrayList<Character>> wavesInBetterForm; //Arraylist (all waves) of arraylists (individual waves)

    private ArrayList<Entity> enemyActuals;
    private CfgReader reader;

    private long msSinceLastEnemy;
    private long msSinceLastWave;

    private long enemyDist;
    private long waveDist;

    private boolean waitingForNextWave = false;
    private boolean isFirstOfWave = true;

    private enemyDictionary enemyDictionary;
    private squareCollection sqc;

    private Thread runThread;

    public waveManager(squareCollection sqc, String fnOfWave) {
        reader = new CfgReader(main.WAVES_LOC + fnOfWave);
        waves = WaveParser.enemiesBetweenGaps(reader);
        enemyActuals = new ArrayList<>();

        msSinceLastEnemy = 0;
        msSinceLastWave = 0;
        enemyDist = (Integer.parseInt(reader.get("enemyGaps", "enemyGap").toString())) * 1000;
        waveDist = (Integer.parseInt(reader.get("enemyGaps", "waveGap").toString())) * 1000;

        enemyDictionary = new enemyDictionary(main.ENEMY_FNS, main.ENEMY_IMG_FNS);
        this.sqc = sqc;

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
            @Override
            public void run() {
                for (ArrayList<Character> thisWave : wavesInBetterForm) {
                    try {
                        wait(waveDist);
                    } catch (InterruptedException e) {
                        System.out.println("Wave wait interrupted");
                    }

                    for(Character c : thisWave) {
                        try {
                            wait(enemyDist);
                        } catch (InterruptedException e) {
                            System.out.println("Enemy wait interrupted.");
                        }

                        enemyTemplate eT = enemyDictionary.getEnemy(c);
                        enemyActual eA = new enemyActual(eT, sqc);

                        enemyActuals.add(eA);
                    }

                }
            }
        };

        runThread = new Thread(runnable);

    }

    public Thread getRunThread() {
        return runThread;
    }

    public ArrayList<Entity> getEntites () {
        return enemyActuals;
    }

    //    public ArrayList<Entity> step (long msSinceLast) {
//
//        if(wavesInBetterForm.size() == 0) //We have to make sure we can spawn enemies
//            return enemyActuals;
//
//        if(!waitingForNextWave) { //So we're not waiting for the next wave gap
//            if(msSinceLastEnemy <= enemyDist) { //And there has been sufficient gap, then spawn in an enemy
//                if(isFirstOfWave)
//                    msSinceLastWave = 0;
//                else {
//                    //nothing
//                }
//
//                if(wavesInBetterForm.get(0).size() != 0) { // And we haven't just spawned the last enemy of a wave
//                    ArrayList<Character> currentWave = wavesInBetterForm.get(0);
//                    char currentEnemy = currentWave.get(0);
//                    enemyTemplate template = enemyDictionary.getEnemy(currentEnemy);
//                    wavesInBetterForm.get(0).remove(0);
//
//                    enemyActual eA = new enemyActual(template, sqc);
//                    enemyActuals.add(eA);
//
//                    msSinceLastEnemy = 0;
//
//                    isFirstOfWave = false;
//                }else {
//                    waitingForNextWave = true;
//                }
//            }
//
//            if(waitingForNextWave)
//            {
//                msSinceLastWave += msSinceLast;
//                if(msSinceLastWave >= waveDist)
//                {
//                    waitingForNextWave = false;
//                    isFirstOfWave = true;
//                }
//            }else {
//                msSinceLastEnemy += msSinceLast;
//            }
//
//        }
//
//
//        for (Entity e : enemyActuals) {
//            e.step(msSinceLast);
//            if(e.getType() == entityType.enemy)
//            {
//                enemyActual ea = ((enemyActual) e);
//                if(ea.isHasHit())
//                    enemyActuals.remove(e);
//            }
//        }
//
//
//        return enemyActuals;
//    }
}
