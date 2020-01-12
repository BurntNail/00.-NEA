package classes.enemy;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.util.Coordinate;
import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

public class enemyActual extends Entity {

    private enemyTemplate template;

    private int currentHP, currentSpd;

    public enemyActual(enemyTemplate eTemplate, Coordinate XYInArr, String fn) {
        super(XYInArr, fn, entityType.enemy);

        int offsetY = main.TILE_HEIGHT / 2;
        int offsetX = main.TILE_WIDTH / 2;

        template = eTemplate;
        currentHP = template.getHp();
        currentSpd = template.getSpd();

    }
}