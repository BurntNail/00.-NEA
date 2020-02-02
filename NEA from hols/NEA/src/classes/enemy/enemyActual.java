package classes.enemy;

import classes.CustomActionListeners.BooleanChangeDispatcher;
import classes.CustomActionListeners.BooleanChangeEvent;
import classes.CustomActionListeners.BooleanChangeListener;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class enemyActual extends Entity implements BooleanChangeDispatcher {

    public static final long MS_GAP = 10;

    private enemyTemplate template;

    private int currentHP, currentSpd;
    private boolean isDead;

    private boolean hasHit;

    private squareCollection squares;
    private int currentStep;
    private Coordinate currentCoord;

    private Thread runThread;
    private boolean hasStarted;

    private int distInPx;

    private ArrayList<BooleanChangeListener> listeners;

    public enemyActual(enemyTemplate eTemplate, squareCollection squares, int code) {
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, 5));

        listeners = new ArrayList<>();

        template = eTemplate;
        currentHP = template.getHp();

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

        distInPx = (currentSpd * av) / 100;
        hasHit = false;

        Runnable r = () -> {
            main.SOUNDS.get("Spawn.wav").start();
            while(!hasHit && !isDead){
                try {
                    TimeUnit.MILLISECONDS.sleep(MS_GAP);
                    if(currentStep == squares.getEnemyPath().size())
                    {
                        distInPx = 0;
                        hasHit = true;
                        dispatchEvent(); //TODO: Add Game Over Scene
                        return;
                    }

                    currentCoord = squares.getEnemyPath().get(currentStep);

                    dir direction = getXYInArr().directionTo(currentCoord);
                    System.out.println(direction + " - " + getXYInArr().toString() + " -> " + currentCoord.toString() + "\t\t" + code);


                    switch (direction) {
                        case N:
                            N(distInPx);
                            break;
                        case E:
                            E(distInPx);
                            break;
                        case S:
                            S(distInPx);
                            break;
                        case W:
                            W(distInPx);
                            break;
                        default:
                            System.out.println("NULL" + code);
                            break;
                    }

                    if(getXYInArr().equals(currentCoord) && getXYInTile().isWithinBounds(main.BOUND, new Coordinate(main.TILE_WIDTH / 2, main.TILE_HEIGHT / 2), direction))
                        currentStep++;


                } catch (InterruptedException e) {
                    System.out.println("Enemy mover stooped.");
                }
            }
        };
        runThread = new Thread(r);
        hasStarted = false;
    }

    public void start () {
        if(hasStarted)
            return;

        runThread.start();
        hasStarted = true;
    }

    public boolean isHasHit() {
        return hasHit;
    }

    public void damage (int dmg) {
        currentHP -= dmg;
        dispatchEvent();
    }

    public enemyTemplate getTemplate() {
        return template;
    }

    @Override
    public void addBooleanChangeListener(BooleanChangeListener listener) {
        listeners.add(listener);
    }

    private void dispatchEvent() {
        final BooleanChangeEvent event = new BooleanChangeEvent(this);
        for (BooleanChangeListener l : listeners) {
            dispatchRunnableOnEventQueue(l, event);
        }
    }

    private void dispatchRunnableOnEventQueue(final BooleanChangeListener listener, final BooleanChangeEvent event) {
        EventQueue.invokeLater(() -> listener.stateChanged(event));
    }
}