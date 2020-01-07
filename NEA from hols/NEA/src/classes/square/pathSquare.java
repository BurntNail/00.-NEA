package classes.square;

import classes.util.Coordinate;
import classes.util.dir;

public class pathSquare{ //Doesn't extend from Square due to fn issues

    public static String fn;

    private Coordinate enterFrom;
    private Coordinate exitTo;
    private Coordinate xy;

    private boolean hasEnemy;


    public pathSquare(Coordinate enterFrom, Coordinate exitTo, Coordinate xy) {
        this.enterFrom = enterFrom;
        this.exitTo = exitTo;

        hasEnemy = false;

        setFN();

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

    private void setFN () {
        dir d = enterFrom.directionTo(exitTo);

        switch (d) {
            case E:
                fn = "horiz.png";
            case N:
                fn = "vert.png";
            case S:
                fn = "vert.png";
            case W:
                fn = "horiz.png";
            case NE:
                fn = "topToLeft.png";
            case NW:
                fn = "topToRight.png";
            case SE:
                fn = "bottomToLeft.png";
            case SW:
                fn = "bottomToRight.png";
        }
    }


}
