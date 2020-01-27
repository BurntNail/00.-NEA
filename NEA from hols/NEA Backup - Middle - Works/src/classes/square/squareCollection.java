package classes.square;

import CfgReader.CfgReader;
import classes.square.types.Square;
import classes.util.Coordinate;
import main.main;

import java.util.ArrayList;

public class squareCollection {

    private Square[][] squares;
    private ArrayList<Coordinate> enemyPath;
    private ArrayList<Coordinate> availableTurretSquares;
    private Coordinate start, end;
    private CfgReader reader;

    public squareCollection(sqaureParser sqp) {
        squares = sqp.getSquares();
        reader = sqp.getR();
        enemyPath = getCoordinates(reader.get("importantLocations", "path").toString());
        availableTurretSquares = getCoordinates(reader.get("importantLocations", "turrets").toString());
        start = enemyPath.get(0);
        end = enemyPath.get(enemyPath.size() - 1);

    }

    public Square[][] getSquares() {
        return squares;
    }

    public ArrayList<Coordinate> getEnemyPath() {
        return enemyPath;
    }

    public ArrayList<Coordinate> getAvailableTurretSquares() {
        return availableTurretSquares;
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public CfgReader getReader() {
        return reader;
    }

    private static ArrayList<Coordinate> getCoordinates (String toBeParsed) {
        ArrayList<Coordinate> path = new ArrayList<>();

        char[] chars = toBeParsed.toCharArray();

        for (int i = 0; i < chars.length; i+=4) { //The Coords are 2 digits long each, with a full one being 4, so we go in 4-length bits
            String xStr = chars[i] + "" + chars[i + 1];
            String yStr = chars[i + 2] + "" + chars[i + 3];

            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            Coordinate it = new Coordinate(x, y);
            path.add(it);

        }

        return path;
    }
}
