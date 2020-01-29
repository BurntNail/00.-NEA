package classes.enemy;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

import java.util.concurrent.TimeUnit;

public class enemyActual extends Entity {

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

    public enemyActual(enemyTemplate eTemplate, squareCollection squares) {
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, 5));

        template = eTemplate;
        currentHP = template.getHp();

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

        distInPx = (currentSpd * av) / 100;
        hasHit = false;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(!hasHit){
                    try {
                        TimeUnit.MILLISECONDS.sleep(MS_GAP);
                        if(currentStep == squares.getEnemyPath().size())
                        {
                            distInPx = 0;
                            hasHit = true;
                            return;
                        }

                        currentCoord = squares.getEnemyPath().get(currentStep);

                        dir direction = getXYInArr().directionTo(currentCoord);
//                        System.out.println(direction + " - " + getXYInArr().toString() + " -> " + currentCoord.toString() + "\t\t" + currentStep);


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
                            default:
                                W(distInPx);
                        }

                        if(getXYInArr().equals(currentCoord) && getXYInTile().isWithinBounds(main.BOUND, new Coordinate(main.TILE_WIDTH / 2, main.TILE_HEIGHT / 2), direction))
                            currentStep++;


                    } catch (InterruptedException e) {
                        System.out.println("Enemy mover stooped.");
                    }
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
    }

    public enemyTemplate getTemplate() {
        return template;
    }
}