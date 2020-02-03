package classes.enemy;

public class enemyTemplate {

    private String name;
    private int spd;
    private String info;
    private String heartsLost;
    private int hp;

    private int heartsCost;
    private char initial;
    private String fn;
    private int moneyBack;

    public enemyTemplate(String name, int spd, String info, String heartsLost, int hp, int heartsCost, char initial, String fn, int moneyBack) {
        this.name = name;
        this.spd = spd;
        this.info = info;
        this.heartsLost = heartsLost;
        this.hp = hp;
        this.heartsCost = heartsCost;
        this.initial = initial;
        this.fn = fn;
        this.moneyBack = moneyBack;
    }

    public String getName() {
        return name;
    }

    public String getFn() {
        return fn;
    }

    public int getSpd() {
        return spd;
    }

    public String getInfo() {
        return info;
    }

    public String getHeartsLost() {
        return heartsLost;
    }

    public int getHp() {
        return hp;
    }

    public int getHeartsCost() {
        return heartsCost;
    }

    public char getInitial() {
        return initial;
    }

    public int getMoneyBack() {
        return moneyBack;
    }
}
