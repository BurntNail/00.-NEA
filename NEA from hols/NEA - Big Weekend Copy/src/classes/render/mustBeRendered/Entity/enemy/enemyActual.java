package classes.render.mustBeRendered.Entity.enemy;

import Gameplay.player.PlayerManager;
import classes.render.mustBeRendered.Entity.baseEntity.Entity;
import classes.render.mustBeRendered.Entity.baseEntity.entityType;
import classes.render.mustBeRendered.square.squareCollection;
import classes.util.coordinate.Coordinate;
import classes.util.coordinate.dir;
import main.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class enemyActual extends Entity {

    private static final Coordinate IN_TILE_TARGET = addHitBoxTolerances(Entity.turnFromArrToScrnPlusHalfTile(new Coordinate(0, 0)), new Coordinate(main.ENEMY_WIDTH / 2, main.ENEMY_HEIGHT / 2));

    public static final long START_GAP = 30; //S
    public static final long MOVE_GAP = 20; //MS

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
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, IN_TILE_TARGET);

        template = eTemplate;
        currentHP = template.getHp();
        baseHP = currentHP;

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        int av = (main.TILE_WIDTH + main.TILE_HEIGHT) / 2;

        distInPx = (currentSpd * av) / SPEED_DIVISOR;
        System.out.println("DIP: " + distInPx);
        if(distInPx <= 0)
            distInPx = currentSpd;

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
//            System.out.println("Enemy created, and ready to go.");

            hasBeenSpawned = true;

            if(pm.isDone())
                return;

            main.SOUNDS.get("Spawn.wav").start();


            while(!hasHit && !isDead && !pm.isDone()){
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

                    //region move gap
                    long diff = System.currentTimeMillis() - current;
                    current = System.currentTimeMillis();

                    if(diff < 0)
                        diff = 0;
                    if(diff > MOVE_GAP)
                        diff = MOVE_GAP;


                    TimeUnit.MILLISECONDS.sleep(MOVE_GAP - diff);
                    //endregion

                    currentCoord = squares.getEnemyPath().get(currentStep);
                    Coordinate onScrnTarget = Entity.turnFromArrToScrnPlusHalfTile(currentCoord);
                    onScrnTarget = Entity.addHitBoxTolerances(onScrnTarget, CENTRE_OF_HITBOX);



                    dir direction = getXYOnScrn().directionTo(onScrnTarget);
                    double dist = getXYOnScrn().distTo(onScrnTarget);

                    if(distInPx >= dist) { //If the dist we can go is greater than the dist to go, then we know we can get there
                        setXYInArr(currentCoord);
                        currentStep++;
                        setXYInTile(IN_TILE_TARGET);
//                        System.out.println(code + " has reached the target");
                        continue;
                    }

                    switch (direction) {
                        case N -> changeY(-distInPx);
                        case S -> changeY(distInPx);
                        case E -> changeX(distInPx);
                        case W -> changeX(-distInPx);
                    }
//                    System.out.println(code + " is just moving");



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


    public boolean haveIBeenSpawnedYet () {
        return hasBeenSpawned;
    }

    @Override
    public BufferedImage getImg() {
        BufferedImage base = super.getImg();
        BufferedImage hpBar = new BufferedImage(base.getWidth(), 15, BufferedImage.TYPE_INT_ARGB);

        BufferedImage newOne = new BufferedImage(base.getWidth(), base.getHeight() + 15, BufferedImage.TYPE_INT_ARGB);

        double HPFraction = currentHP / baseHP;
        int cutOff = ((int) Math.floor(newOne.getWidth() * HPFraction));

        if(HPFraction != 1)
            System.out.println(HPFraction);



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

    public Coordinate getCentreOfHitbox () {
        return CENTRE_OF_HITBOX;
    }
}