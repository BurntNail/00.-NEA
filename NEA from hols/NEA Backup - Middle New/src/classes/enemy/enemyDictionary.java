package classes.enemy;

import CfgReader.CfgReader;
import main.main;

import java.util.ArrayList;
import java.util.HashMap;

public class enemyDictionary {

    private HashMap<Character, enemyTemplate> enemies;

    public enemyDictionary(String[] fns, String[] imgFns) {

        enemies = new HashMap<>();

        CfgReader[] readers = new CfgReader[fns.length];

        for (int i = 0; i < readers.length; i++) {
            readers[i] = new CfgReader(main.ENEMIES_LOC + fns[i]);
        }

        for (int i = 0; i < fns.length; i++) {
            CfgReader reader = readers[i];

            HashMap<String, Object> playerSees = reader.getModule("playerSees");
            HashMap<String, Object> compSees = reader.getModule("compSees");

            String name = playerSees.get("name").toString();
            String info = playerSees.get("info").toString();
            String heartsLost = playerSees.get("heartsLost").toString();

            int spd = Integer.parseInt(compSees.get("spd").toString());
            int hp = Integer.parseInt(compSees.get("hp").toString());
            int heartsCost = Integer.parseInt(compSees.get("heartsLost").toString());
            char intial = compSees.get("initial").toString().charAt(0);


            String fn = imgFns[i];

            enemyTemplate e = new enemyTemplate(name, spd, info, heartsLost, hp, heartsCost, intial, fn);

            enemies.put(intial, e);
        }
    }

    public HashMap<Character, enemyTemplate> getEnemies() {
        return enemies;
    }
    public enemyTemplate getEnemy(char key) {
        return enemies.get(key);
    }


}
