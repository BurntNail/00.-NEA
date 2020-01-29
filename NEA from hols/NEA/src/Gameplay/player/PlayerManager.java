package Gameplay.player;

public class PlayerManager {

    private int money;
    private int hearts;

    public PlayerManager(int money, int hearts) {
        this.money = money;
        this.hearts = hearts;

    }

    //region Getters
    public int getMoney() {
        return money;
    }

    public int getHearts() {
        return hearts;
    }

    //endregion

    public boolean buy (int amnt) {
        if(amnt > money)
            return false;


        money -= amnt;
        return true;
    }
    public void donateM (int amnt) {
        money += amnt;
    }

    public void takeHearts (int amnt) {
        hearts -= amnt;
    }
    public void donateH (int amnt) {
        hearts += amnt;
    }

    public boolean isDead () {
        return hearts <= 0;
    }

}
