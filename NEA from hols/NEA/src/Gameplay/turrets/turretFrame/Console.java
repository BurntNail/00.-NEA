package Gameplay.turrets.turretFrame;

import javax.swing.*;

public class Console extends JTextArea {

    private static String txtFull;
    private static boolean needsToUpdate;

    private Thread updateThread;

    public Console(String text) {
        super((txtFull == null ? "" : txtFull) + text + "\n");
        setEditable(false);


        txtFull = text + "\n";

        needsToUpdate = true;

        Runnable r = () -> {
            while(true) {
                if(needsToUpdate) {
                    this.setText(txtFull);
                    needsToUpdate = false;
                }
            }
        };

        updateThread = new Thread(r);
        updateThread.start();
        addText("@Console: I have been created.");
    }

    public static void addText (String msg) {
        txtFull += msg + "\n";
//        System.out.println("@Console: " + msg);
        needsToUpdate = true;
    }
}
