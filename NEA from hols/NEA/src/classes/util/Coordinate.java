package classes.util;

import Gameplay.turrets.TurretFrame;

import java.util.StringJoiner;

public class Coordinate {

    private int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //region gs
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    //endregion

    public dir directionTo (Coordinate other) {
        int yDist = y - other.y;
        int xDist = x - other.x;
        double dist = distTo(other);

        if(dist == 0)
            return null;

        if(yDist == 0) { //So if the yDist is 0, they are E or W
            if(xDist > 0)
                return dir.W;
            else
                return dir.E;
        }else {
            if (yDist > 0)
                return dir.N;
            else
                return dir.S;
        }

    }

    public double distTo (Coordinate other) {
            int theirX = other.x;
            int theirY = other.y;

            int run = dist(x, theirX) ^ 2;
            int rise = dist(y, theirY) ^ 2;

            double dist = Math.sqrt(rise + run);

            return dist;

    }

    public Coordinate clone () {
        return new Coordinate(x, y);
    }

    private int dist (int a, int b) {
        boolean value1Bigger = (a > b);

        return value1Bigger ? a - b : b - a;

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coordinate.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }

    public static Coordinate parseFromTS (String tbp) {
        if(tbp.length() <= 5 || tbp == null)
            return TurretFrame.NULL_COORD;

        int xIndexStart = tbp.indexOf('x') + 2;
        int xIndexEnd = tbp.indexOf('y') - 2;

        int yIndexStart = tbp.indexOf('y') + 2;
        int yIndexEnd = tbp.length() - 1;

        int x = Integer.parseInt(tbp.substring(xIndexStart, xIndexEnd));
        int y = Integer.parseInt(tbp.substring(yIndexStart, yIndexEnd));

        return new Coordinate(x, y);
    }

    @Override
    public boolean equals(Object obj) {

        try {
            Coordinate obj2 = ((Coordinate) obj);

            return obj2.x == x && obj2.y == y;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean isWithinBounds (int bound, Coordinate other, dir direction) { //We need to know direction, becuase otherwise enemies may get locked in a cycle of back and forth to get closer
        switch (direction) {
            case N:
                return y > other.y || other.y - y <= bound;
            case S:
                return y < other.y || other.y - y >= bound;
            case W:
                return x > other.x || other.x - x <= bound;
            case E:
                return x < other.x || other.x - x >= bound;
        }

        return false;
    }
}
