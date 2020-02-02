package classes.turret;

import java.util.StringJoiner;

public class turretTemplate {

    private String name;
    private String info;
    private String dmg;
    private String fireRate;
    private String range;

    private int dmgInt;
    private int fireRateInt;
    private int rangeInt;
    private String fn;
    private String bullet_fn;
    private int cost;
    private int sellValue;

    public turretTemplate(String name, String info, String dmg, String fireRate, String range, int dmgInt, int fireRateInt, int rangeInt, String fn, String bullet_fn, int cost_, int sellValue_) {
        this.name = name;
        this.info = info;
        this.dmg = dmg;
        this.fireRate = fireRate;
        this.range = range;
        this.dmgInt = dmgInt;
        this.fireRateInt = fireRateInt;
        this.rangeInt = rangeInt;
        this.fn = fn;
        this.bullet_fn = bullet_fn;
        cost = cost_;
        sellValue = sellValue_;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }


    public int getDmgInt() {
        return dmgInt;
    }

    public int getFireRateInt() {
        return fireRateInt;
    }

    public int getRangeInt() {
        return rangeInt;
    }

    public String getFn() {
        return fn;
    }

    public String getBullet_fn() {
        return bullet_fn;
    }

    public int getCost() {
        return cost;
    }

    public int getSellValue() {
        return sellValue;
    }

    @Override
    public String toString() { //TODO: Turn to java.util.StringJoiner
        String fin = name + "\n";
        fin += info + "\n";
        fin += "\n";

        fin += "Damage per shot - " + "\t\t" + dmg + "\n";
        fin += "Seconds between shots - " + "\t\t" + fireRate + "\n";
        fin += "Range in tiles - " + "\t\t" + range + "\n";
        fin += "\n";

        fin += "Cost - " + "\t\t" + cost + "\n";
        fin += "Value back when sold - " + "\t\t" + sellValue + "\n";
        fin += "\n";

        fin += "Would you like to buy this turret?";

        return fin;
    }
}
