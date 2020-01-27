package Gameplay.turrets;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.turret.*;
import classes.util.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TurretManager {

    private ArrayList<Entity> turrets;
    private turretDictionary dictionary;

    private squareCollection sqc;

    private ArrayList<Coordinate> turretSquaresAll;
    private ArrayList<Coordinate> turretSquaresFree;
    private ArrayList<Coordinate> turretSquaresUsed;

    private Coordinate prevClickedCoordinate;
    private Coordinate coordBefore;

    private TurretFrame tf;
    private ArrayList<Entity> bullets;


    public TurretManager (squareCollection sqc_) {
        sqc = sqc_;

        turrets = new ArrayList<>();
        dictionary = new turretDictionary(main.TURRET_FNS);

        turretSquaresAll = sqc.getAvailableTurretSquares();
        turretSquaresFree = (ArrayList<Coordinate>) turretSquaresAll.clone();
        turretSquaresUsed = new ArrayList<>();

        tf = new TurretFrame(turretSquaresUsed, turretSquaresFree, new Dimension(main.WINDOW_WIDTH, main.WINDOW_HEIGHT));
        prevClickedCoordinate = TurretFrame.NULL_COORD;
        coordBefore = TurretFrame.NULL_COORD;

        turrets = new ArrayList<>();
        turrets.add(new turretActual(new Coordinate(2, 1), dictionary.getTurret("Wizard's tower")));
        bullets = new ArrayList<>();
    }

    public ArrayList<Entity> step (long msPrev, ArrayList<Entity> enemies) {
        prevClickedCoordinate = tf.getMostRecent();

        if (prevClickedCoordinate != TurretFrame.NULL_COORD && prevClickedCoordinate != coordBefore && !turretSquaresUsed.contains(prevClickedCoordinate)) {
            turretSquaresUsed.add(prevClickedCoordinate);
            turretSquaresFree.remove(prevClickedCoordinate);

//            String type = TurretInput.GetInitialTurretInput(dictionary.getNames());
            String type = "Wizard's tower.";

            turretActual temp = new turretActual(prevClickedCoordinate, dictionary.getTurret("Wizard's tower"));

            turrets.add(temp);
        }

        bullets.clear();
        if(turrets.size() > 0) {
            for (Entity e : turrets) {
                e.step(msPrev);

                if (e.getType().equals(entityType.turret)) {
                    turretActual t = ((turretActual) e);
                    t.setEnemies(enemies);

                    for (Entity b : t.getShotsFired())
                        bullets.add(b);
                }
            }
        }

        return turrets;
    }

}
