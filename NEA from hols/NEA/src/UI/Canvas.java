package UI;

import classes.square.Square;
import main.main;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JComponent {

    private Square[][] squares;
    private ArrayList<enemyActual> enemies;

    public Canvas(Square[][] squares_) {
        squares = squares_;
    }
}
