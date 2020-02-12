package classes.render.mustBeRendered.square;

import classes.util.CfgReader.CfgReader;
import classes.render.mustBeRendered.square.types.*;
import classes.util.coordinate.Coordinate;
import main.main;

import java.util.HashMap;

public class sqaureParser {

    private Square[][] squares;
    private HashMap<Character, String> fns;
    private CfgReader r;
    private Coordinate satan;
    private Coordinate home;

    private sqaureParser (CfgReader r, Square[][] squares, HashMap<Character, String> fns, Coordinate satan, Coordinate home)
    {
        this.r = r.clone();
        this.squares = squares.clone();
        this.fns = ((HashMap<Character, String>) fns.clone());
        this.satan = satan.clone();
        this.home = home.clone();
    }


    public sqaureParser(CfgReader r) {
        this.r = r;

        int w = main.NUM_OF_TILES_WIDTH;
        int h = main.NUM_OF_TILES_HEIGHT;
        fns = new HashMap<>();
        setUpHashMap();

        char[] tbp = r.get("mapDeets", "map").toString().toCharArray();
        squares = new Square[w][h];


        int x = 0;
        int y = 0;

        for (char c : tbp) {
            String fn = fns.get(c);
            Square newBoi;

            Coordinate ici = new Coordinate(x, y);

            try {
                switch (fn) {
                    case "general_big.png":
                        newBoi = new pathSquare();
                        break;
                    case "happy_big.png":
                        newBoi = new homeBase();
                        home = ici;
                        break;
                    case "satan_big.png":
                        newBoi = new enemyStartSquare();
                        satan = ici;
                        break;
                    case "turret_base_big.png":
                        newBoi = new TurretSquare();
                        break;
                    case "nothing_big.png":
                        newBoi = new nothingSquare();
                        break;
                    default:
                        newBoi = new cornerSquare(fn);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();

                newBoi = new nothingSquare();
            }

            squares[x][y] = newBoi;

            x++;

            if(x == w)
            {
                x = 0;
                y++;
            }
        }
    }


    public sqaureParser clone () {
        return new sqaureParser(r, squares, fns, satan, home);
    }

    private void setUpHashMap () {
        HashMap<String, Object> mod = r.getModule("refs");

        Object[] chars = mod.keySet().toArray();
        Object[] fnsFromR = mod.values().toArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i].toString().charAt(0);
            String fn = fnsFromR[i].toString();

            fns.put(c, fn);
        }
    }

    public Square[][] getSquares() {
        return squares;
    }

    public CfgReader getR() {
        return r;
    }
}
