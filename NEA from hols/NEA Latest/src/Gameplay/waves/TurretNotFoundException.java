package Gameplay.turrets;

public class TurretNotFoundException extends Exception{

    public TurretNotFoundException() {
        super("TURRET LOCATION NOT FOUND");
    }
}
