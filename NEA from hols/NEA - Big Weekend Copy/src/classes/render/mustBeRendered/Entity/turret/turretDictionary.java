package classes.render.mustBeRendered.Entity.turret;

import classes.util.CfgReader.CfgReader;
import main.main;

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
            int rangeInt = Integer.parseInt(compSees.get("range").toString());
            String fnImg = compSees.get("file").toString();
            String fnBulletImage = compSees.get("bulletFile").toString();
            int cost = Integer.parseInt(compSees.get("cost").toString());
            int sellValue = Integer.parseInt(compSees.get("sellValue").toString());
            double fireRateDbl = Double.parseDouble(compSees.get("fireRate").toString());
            int bulletSpd = Integer.parseInt(compSees.get("bulletSpd").toString());

            turretTemplate t = new turretTemplate(name, info, dmg, fireRate, range, dmgInt, rangeInt, fnImg, fnBulletImage, cost, sellValue, fireRateDbl, bulletSpd);

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
