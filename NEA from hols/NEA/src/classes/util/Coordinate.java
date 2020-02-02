package classes.util;

public class Coordinate {

    private int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public dir directionTo (Coordinate other) {
        int yDist = dist(y, other.y);
        double dist = distTo(other);

        double angle = Math.acos(yDist/dist);

        int mult = 45;
        double half = mult / 2;
        int whichOne = 0;

        for (int i = 0; i < 8; i++) {
            double a = mult * i;
            double b = mult * (i - 1);

            a -= half;
            b -= half;

            if (a < 0)
                a += 360;
            else if (b < 0)
                b += 360;

            if(between(angle, a, b))
                whichOne = i;

        }

        switch (whichOne) {
            case 0:
                return dir.N;
            case 1:
                return dir.NW;
            case 2:
                return dir.W;
            case 3:
                return dir.SW;
            case 4:
                return dir.S;
            case 5:
                return dir.SE;
            case 6:
                return dir.E;
            case 7:
                return dir.NE;
        }

        return dir.N;
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

    private int dist (int a, int b) {
        boolean value1Bigger = (a > b);

        return value1Bigger ? a - b : b - a;

    }
}
