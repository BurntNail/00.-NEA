package classes.turret;

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

    public turretTemplate(String name, String info, String dmg, String fireRate, String range, int dmgInt, int fireRateInt, int rangeInt, String fn, String bullet_fn, int cost_) {
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
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDmg() {
        return dmg;
    }

    public String getFireRate() {
        return fireRate;
    }

    public String getRange() {
        return range;
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
}
