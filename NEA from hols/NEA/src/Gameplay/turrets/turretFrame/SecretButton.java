package Gameplay.turrets.turretFrame;

import classes.util.Coordinate;

import javax.swing.*;

public class SecretButton extends JButton {

    private int mySecretValue;

    public SecretButton(String text, int secret) {
        super(text);
        mySecretValue = secret;
    }

    public int getMySecretValue() {
        return mySecretValue;
    }
}
