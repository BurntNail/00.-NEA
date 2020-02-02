package classes.turret;

import CfgReader.CfgReader;
import classes.enemy.enemyTemplate;
import main.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class turretDictionary {

    private HashMap<String, turretTemplate> turrets;
    private ArrayList<String> names;

    public turretDictionary(String[] fns) {

        turrets = new HashMap<>();
        names = new ArrayList<>();

        for(String fnTurret : fns) {
            CfgReader reader = new CfgReader(main.TURRETS_LOC + fnTurret);

            HashMap<String, Object> playerSees = reader.getModule("playerSees");
            HashMap<String, Object> compSees = reader.getModule("compSees");

            String name = playerSees.get("name").toString();
            String info = playerSees.get("info").toString();
            String dmg = playerSees.get("dmg").toString();
            String fireRate = playerSees.get("fireRate").toString();
            String range = playerSees.get("range").toString();

            int dmgInt = Integer.parseInt(compSees.get("dmg").toString());
            int fireRateInt = Integer.parseInt(compSees.get("fireRate").toString());
            int rangeInt = Integer.parseInt(compSees.get("range").toString());
            String fnImg = compSees.get("file").toString();
            String fnBulletImage = compSees.get("bulletFile").toString();
            int cost = Integer.parseInt(compSees.get("cost").toString());
            int sellValue = Integer.parseInt(compSees.get("sellValue").toString());

            turretTemplate t = new turretTemplate(name, info, dmg, fireRate, range, dmgInt, fireRateInt, rangeInt, fnImg, fnBulletImage, cost, sellValue);

            turrets.put(name, t);
            names.add(name);

        }

    }

    public HashMap<String, turretTemplate> getTurrets() {
        return turrets;
    }
    public turretTemplate getTurret (String key) {
        return turrets.get(key);
    }
    public ArrayList<String> getNames() {
        return names;
    }
}
