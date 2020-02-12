package classes.enemy;

import Gameplay.player.PlayerManager;
import Gameplay.turrets.turretFrame.Console;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

import java.util.concurrent.TimeUnit;

public class enemyActual extends Entity {

    public static final long START_GAP = 10000;
//    public static final long START_GAP = 1000;
    public static final long MOVE_GAP = 20;

    public static final Coordinate TARGET_IN_TILE = new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, main.TILE_HEIGHT / 2 - main.ENEMY_HEIGHT / 2);

    private enemyTemplate template;

    private int currentHP, currentSpd;

    private boolean hasHit;
    private boolean isDead;

    private squareCollection squares;
    private int currentStep;
    private Coordinate currentCoord;

    private Thread runThread;
    private boolean hasStarted;

    private int distInPx;

    private boolean hasBeenSpawned;

    public enemyActual(enemyTemplate eTemplate, squareCollection squares, int code, PlayerManager pm) {
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, 5));

        template = eTemplate;
        currentHP = template.getHp();

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

        distInPx = (currentSpd * av) / 200;
        hasHit = false;
        isDead = false;
        hasBeenSpawned = false;

        Runnable r = () -> {
            if(pm.isDead())
                return;

            long current = System.currentTimeMillis();
            Console.addText( "@EnemySpawner: " + eTemplate.getName() + " has been spawned.");
            if(code == 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(START_GAP);
                } catch (InterruptedException e) {
                    Console.addText("@Enemy: START_GAP WAIT HAS BEEN INTERRUPTED: " + e.getStackTrace());
                }
            }

            hasBeenSpawned = true;

            if(pm.isDead())
                return;

            main.SOUNDS.get("Spawn.wav").start();

            while(!hasHit && !isDead && !pm.isDead()){
//                System.out.println(currentHP);
                try {
                    if(currentStep == squares.getEnemyPath().size())
                    {
                        distInPx = 0;
                        hasHit = true;
                        System.out.println("Ah-Ha! It appeareth that Tybalteth hath won this round, and Romeo hath beeneth 'pwned'");
                        return;
                    }
                    if(currentHP <= 0)
                    {
                        isDead = true;
                        System.out.println("Ah-Ha! Tybalt hath been 'pwned', but noteth by fairest Romeo, but by Ninja, the newesth recruitth of Mixerth.");
                        return;
                    }

                    currentCoord = squares.getEnemyPath().get(currentStep);

                    if(!getXYInArr().equals(currentCoord)) {
                        dir direction = getXYInArr().directionTo(currentCoord);

//                    Coordinate rightNow = getXYOnScrn();
//                    int x = rightNow.getX();
//                    int y = rightNow.getY();
//
//                    switch (direction) {
//                        case N:
//                            y -= distInPx;
//                            break;
//                        case E:
//                            x += distInPx;
//                            break;
//                        case S:
//                            y += distInPx;
//                            break;
//                        case W:
//                            x -= distInPx;
//                            break;
//                        default:
//                            System.out.println("NULL" + code);
//                            break;
//                    }
//
//                    changeXYOnScrn(x, y);
                        long diff = System.currentTimeMillis() - current;
                        current = System.currentTimeMillis();

                        if(diff < 0)
                            diff = 0;
                        if(diff > MOVE_GAP)
                            diff = MOVE_GAP;


                        TimeUnit.MILLISECONDS.sleep(MOVE_GAP - diff);


                        switch (direction) {
                            case N:
                                changeY(-distInPx);
                                break;
                            case E:
                                changeX(distInPx);
                                break;
                            case S:
                                changeY(distInPx);
                                break;
                            case W:
                                changeX(-distInPx);
                                break;
                            default:
                                System.out.println("NULL" + code);
                                break;
                        }
                    }else if(getXYInTile().isWithinBounds(60, new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, main.TILE_HEIGHT / 2 - main.ENEMY_HEIGHT / 2)))
                    {
                        System.out.println("I hath reached thine tile, and so am one stepeth closereth to Romeo.");

                        long diff = System.currentTimeMillis() - current;
                        current = System.currentTimeMillis();

                        if(diff < 0)
                            diff = 0;
                        if(diff > MOVE_GAP)
                            diff = MOVE_GAP;


                        TimeUnit.MILLISECONDS.sleep(MOVE_GAP - diff);

                        dir direction = getXYInTile().directionTo(new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, main.TILE_HEIGHT / 2 - main.ENEMY_WIDTH / 2));


                        switch (direction) {
                            case N:
                                changeY(-distInPx);
                                break;
                            case E:
                                changeX(distInPx);
                                break;
                            case S:
                                changeY(distInPx);
                                break;
                            case W:
                                changeX(-distInPx);
                                break;
                            default:
                                System.out.println("NULL" + code);
                                break;
                        }

                    }


                } catch (InterruptedException e) {
                    System.out.println("Enemy mover stooped.");
                }



            }

            System.out.println("Enemy hath beeneth 'pwned'");
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

    public boolean hasHit () {
        return hasHit;
    }
    public boolean isDead () {
        return isDead;
    }
    public boolean isDone () {
        return isDead || hasHit;
    }

    public void damage (int dmg) {
        currentHP -= dmg;
        System.out.println("ah, alas fare game, i may soonest be speaketh my last farewell. " + currentHP);
    }

    public enemyTemplate getTemplate() {
        return template;
    }


    public boolean isHasBeenSpawned() {
        return hasBeenSpawned;
    }
}