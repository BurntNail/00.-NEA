package classes.enemy;

import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.coordinate.Coordinate;
import classes.coordinate.dir;
import main.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class enemyActual extends Entity {

    public static final long START_GAP = 30; //S
    public static final long MOVE_GAP = 20; //MS
    private static final long MAX_TIME_ON_A_TILE = 1500; //MS

    public static final Coordinate TARGET_IN_TILE = new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 4, main.TILE_HEIGHT / 2 - main.ENEMY_HEIGHT / 4); //Div by 4, as ENEMY_WIDth and HEIGHT have whistepace included

    private enemyTemplate template;

    private int currentHP, currentSpd;
    private int baseHP;

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
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, TARGET_IN_TILE);

        template = eTemplate;
        currentHP = template.getHp();
        baseHP = currentHP;

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

        distInPx = (currentSpd * av) / 800;
        hasHit = false;
        isDead = false;
        hasBeenSpawned = false;


        Runnable r = () -> {
            if(pm.isDone())
                return;

            long current = System.currentTimeMillis();
            if(code == 0) {
                try {

                    for (long l = START_GAP; l > 0; l--) {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(l + "s remaining, in start wait");
                    }

                } catch (InterruptedException e) {
                    System.out.println("ENEMY START GAP INTERRUPTED");
                }
            }
            System.out.println("Enemy created, and ready to go.");

            hasBeenSpawned = true;

            if(pm.isDone())
                return;

            main.SOUNDS.get("Spawn.wav").start();

            long timeOnTile = 0;
            long prevTimeHitTile = 0;

            while(!hasHit && !isDead && !pm.isDone()){
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

                    long diff = System.currentTimeMillis() - current;
                    current = System.currentTimeMillis();

                    if(diff < 0)
                        diff = 0;
                    if(diff > MOVE_GAP)
                        diff = MOVE_GAP;


                    TimeUnit.MILLISECONDS.sleep(MOVE_GAP - diff);


                    currentCoord = squares.getEnemyPath().get(currentStep);

                    Coordinate currentOnScrn = new Coordinate(currentCoord.getX() * main.TILE_WIDTH + av * 2 / 3, currentCoord.getY() * main.TILE_HEIGHT + av * 2 / 3);


                    dir direction = getXYOnScrn().directionTo(currentOnScrn);
                    double dist = getXYOnScrn().distTo(currentOnScrn);

                    /*Coordinate rightNow = getXYOnScrn();
                    int x = rightNow.getX();
                    int y = rightNow.getY();

                    switch (direction) {
                        case N:
                            y -= distInPx;
                            break;
                        case E:
                            x += distInPx;
                            break;
                        case S:
                            y += distInPx;
                            break;
                        case W:
                            x -= distInPx;
                            break;
                        default:
                            System.out.println("NULL" + code);
                            break;
                    }

                    changeXYOnScrn(x, y);*/


                    if(!getXYInArr().equals(currentCoord) && distInPx < dist) {

                        switch (direction) {
                            case N -> changeY(-distInPx);
                            case E -> changeX(distInPx);
                            case S -> changeY(distInPx);
                            case W -> changeX(-distInPx);
                            default -> System.out.println("NULL" + code);
                        }
                    }

                    else if(!getXYInTile().isWithinBounds(av / 2, TARGET_IN_TILE) && distInPx < dist)
                    {
                        System.out.println("I hath reached thine tile, and so am one stepeth closereth to Romeo.");

                        if(prevTimeHitTile == 0)
                            prevTimeHitTile = System.currentTimeMillis();

                        timeOnTile += System.currentTimeMillis() - prevTimeHitTile;
                        prevTimeHitTile = System.currentTimeMillis();
                        System.out.println("TOT: " + timeOnTile + ", CODE=" + code);

                        if(dist <= (distInPx + av/2))
                            setXYInTile(TARGET_IN_TILE);
                        else {
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
                    }
                    else if(getXYInTile().isWithinBounds(av / 2, TARGET_IN_TILE) || timeOnTile >= MAX_TIME_ON_A_TILE) {
                        timeOnTile = 0;
                        currentStep++;
                    }


                } catch (Exception e) {
                    System.out.println("Enemy mover stooped.");
                }



            }

            System.out.println("Enemy hath beeneth `dn`");
        };
        runThread = new Thread(r);
        hasStarted = false;

    }

    public void start () {
        if(hasStarted)
            return;

        runThread.start();
        hasStarted = true;
        System.out.println("Enemy Thread Started");
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

    @Override
    public BufferedImage getImg() {
        BufferedImage base = super.getImg();
        BufferedImage hpBar = new BufferedImage(base.getWidth(), 15, BufferedImage.TYPE_INT_ARGB);

        BufferedImage newOne = new BufferedImage(base.getWidth(), base.getHeight() + 15, BufferedImage.TYPE_INT_ARGB);



        double HPFraction = currentHP / baseHP;
        int cutOff = ((int) Math.floor(newOne.getWidth() * HPFraction));

        for (int x = 0; x < hpBar.getWidth(); x++) {
            for (int y = 0; y < 10; y++) {
                if(x <= cutOff)
                    hpBar.setRGB(x, y, Color.GREEN.getRGB());
                else
                    hpBar.setRGB(x, y, Color.RED.getRGB());
            }
        }



        newOne.getGraphics().drawImage(hpBar, 0, 0, null);
        newOne.getGraphics().drawImage(base, 0, 15, null);


        return newOne;
    }
}