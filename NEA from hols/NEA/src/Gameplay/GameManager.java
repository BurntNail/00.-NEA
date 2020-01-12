package Gameplay;


import CfgReader.CfgReader;
import classes.enemy.enemyDictionary;

public class GameManager {

    private enemyDictionary enemyDictionary;

    public GameManager(String[] enemyFNs, String[] lvlFNs, String[] turretFNs) {
        enemyDictionary = new enemyDictionary(enemyFNs);

    }
}
