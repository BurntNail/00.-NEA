package Gameplay.turrets;

import classes.util.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TurretFrame {

    private Coordinate mostRecent;

    private JFrame window;
    private JPanel panel;

    private JButton[][] btns;
    private ArrayList<Coordinate> usedSquares;
    private ArrayList<Coordinate> freeSquares;

    public static final Coordinate NULL_COORD = new Coordinate(1000, 1000);

    public TurretFrame(ArrayList<Coordinate> usedSquares, ArrayList<Coordinate> freeSquares, Dimension size) {
        this.usedSquares = usedSquares;
        this.freeSquares = freeSquares;

        window = new JFrame("Apex Turrets - Turret Window");
        window.setPreferredSize(size);
        window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); //So it doesn't kill the whole program

        panel = new JPanel();
        panel.setLayout(new GridLayout(main.NUM_OF_TILES_HEIGHT, main.NUM_OF_TILES_WIDTH));

        btns = new JButton[main.NUM_OF_TILES_WIDTH][main.NUM_OF_TILES_HEIGHT];

        for (int y = 0; y < main.NUM_OF_TILES_HEIGHT; y++) {
            for (int x = 0; x < main.NUM_OF_TILES_WIDTH; x++) {
                Coordinate thisOne = new Coordinate(x, y);

                String txt = (freeSquares.contains(thisOne) ?
                        "Purchase Turret" :
                        "...");

                btns[x][y] = new JButton(txt);

                btns[x][y].addActionListener(e -> {
                    setMostRecent(thisOne);
                });

                if(btns[x][y].getText() == "...") {
                    btns[x][y].setVisible(false);
                }

                panel.add(btns[x][y]);
            }
        }

        window.add(panel);

        window.pack();
        window.setVisible(true);

    }

    private void setMostRecent (Coordinate thisOne) {
        mostRecent = thisOne;
        System.out.println(thisOne.toString());
    }

    public Coordinate getMostRecent() {
        if(mostRecent != null)
            return mostRecent;
        else
            return NULL_COORD;
    }
}
