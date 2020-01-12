package main;

import CfgReader.CfgReader;
import classes.turret.turretDictionary;

import java.util.HashMap;

public class NonPrimitiveStaticVars { //Here, counting string as primitive, as it doesn't need a constructor. Unless they are directly relevant

    public static final String[] TURRET_FNS = {"wizard.cfg", "dropTower.cfg"};
    public static final turretDictionary TURRETS = new turretDictionary(TURRET_FNS);

    public static final String[] ENEMY_FNS = {"fastButWeakEnemy.cfg"}

}
