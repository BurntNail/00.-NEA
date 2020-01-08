package classes.square;

import classes.util.Coordinate;
import classes.util.dir;

public class pathSquare{ //Doesn't extend from Square due to fn issues

    public static String fn;

    private Coordinate enterFrom;
    private Coordinate exitTo;
    private Coordinate xy;

    private boolean hasEnemy;


    public pathSquare(Coordinate enterFrom, Coordinate exitTo, Coordinate xy, String fn_) {
        this.enterFrom = enterFrom;
        this.exitTo = exitTo;

        hasEnemy = false;



        fn = fn_;
        this.xy = xy;
    }

    public boolean hasEnemy() {
        return hasEnemy;
    }

    public void setHasEnemy(boolean hasEnemy) {
        this.hasEnemy = hasEnemy;
    }

    public Coordinate getEnterFrom() {
        return enterFrom;
    }

    public Coordinate getExitTo() {
        return exitTo;
    }


}
