//package classes.enemy;
//
//import CfgReader.CfgReader;
//import main.main;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class enemyDictionary {
//
//    private HashMap<Character, enemyTemplate> enemies;
//
//    public enemyDictionary(String[] fns) {
//        for (String fn : fns) {
//            CfgReader reader = new CfgReader(main.ENEMIES_LOC + fn);
//
//            String name = reader.get("playerSees", "name").toString();
//            String info = reader.get("playerSees", "info").toString();
//            String heartsLost = reader.get("playerSees", "heartsLost").toString();
//
//            int spd = Integer.parseInt(reader.get("compSees", "spd").toString());
//            int hp = Integer.parseInt(reader.get("compSees", "hp").toString());
//            int heartsCost = Integer.parseInt(reader.get("compSees", "heartsCost").toString());
//            char intial = reader.get("compSees", "initial").toString().charAt(0);
//
//
//
//
//            enemyTemplate e = new enemyTemplate(name, spd, info, heartsLost, hp, heartsCost, intial, fn);
//
//            enemies.put(intial, e);
//        }
//    }
//
//    public HashMap<Character, enemyTemplate> getEnemies() {
//        return enemies;
//    }
//    public enemyTemplate getEnemy(char key) {
//        return enemies.get(key);
//    }
//
//
//}
