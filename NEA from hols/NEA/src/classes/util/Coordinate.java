package classes.util;

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

    private boolean between (double angle, double a, double b) {
        boolean bigThA = angle > a;
        boolean smolThB = angle < b;

        return bigThA && smolThB;
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
}
