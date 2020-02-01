package Gameplay.turrets.turretFrame;

import Gameplay.player.PlayerManager;
import classes.util.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TurretFrame {

    private Coordinate mostRecent;
    private String mostRecentType;

    private JFrame window;
    private JPanel panel;

    private ArrayList<SecretButton> btns;
    private ArrayList<Coordinate> usedSquares;
    private ArrayList<Coordinate> freeSquares;

    public static final Coordinate NULL_COORD = new Coordinate(100000, 10000);
    public static final String NULL_STR = "NOPE!";

    private ArrayList<String> names;

    private Icon messageIcn;

    public TurretFrame(ArrayList<Coordinate> usedSquares, ArrayList<Coordinate> freeSquares, Dimension size, ArrayList<String> turret_names, PlayerManager pm) {
        try {
            URL url = this.getClass().getResource(main.ICON_LOCATIONS + "XYIcon.png");

            if(url == null)
                throw new MalformedURLException("No URL found...");

            messageIcn = new ImageIcon(url, "Icon showing X and Y with crosshairs circle.");
        }catch (Exception e) {
            messageIcn = new ImageIcon();
        }


        this.usedSquares = usedSquares;
        this.freeSquares = freeSquares;

        window = new JFrame("Apex Turrets - Turret Window");
        window.setLocation(size.width, 0);
        window.setPreferredSize(size);
        window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE); //So it doesn't kill the whole program

        panel = new JPanel();
        panel.setAutoscrolls(true);


        btns = new ArrayList<>();
        names = turret_names;
        panel.setLayout(new GridLayout(names.size() + 1, 1));

        for (int i = 0; i < names.size(); i++) {
            SecretButton sBtn = new SecretButton("Buy " + names.get(i), i);
            btns.add(sBtn);
            panel.add(sBtn);

            sBtn.addActionListener(e -> {

                if(freeSquares.size() == 0)
                {
                    mostRecent = NULL_COORD;
                    return;
                }

                Object location = JOptionPane.showInputDialog(panel, "Please enter a location", "Where would you like your tower?", JOptionPane.QUESTION_MESSAGE, messageIcn, ((Object[]) freeSquares.toArray()), 0);

                String resInStr = location + "";
                //TODO: Add null checks here

                mostRecent = Coordinate.parseFromTS(resInStr);
                mostRecentType = sBtn.getText().substring(4);
            });
        }

        JTextArea label = new JTextArea(getLabel(pm));
        label.setEditable(false);
        panel.add(label);


        window.add(panel);

        window.pack();
        window.setVisible(true);

        pm.addBooleanChangeListener(e -> {
            label.setText(getLabel(pm));
            window.pack();
        });

//        while(true) {
//            label.setText(getLabel(pm));
//            window.pack();
//        }
    }

    private static String getLabel (PlayerManager pm) {
        return "Money: " + pm.getMoney() + "\nHearts remaining: " + pm.getHearts();
    }

    private void setMostRecent (Coordinate thisOne) {
        mostRecent = thisOne;
        System.out.println(thisOne.toString());
    }

    public Coordinate getMostRecent() {
        if(mostRecent != null)
            return mostRecent;
        else
            return NULL_COORD;
    }
    public String getMostRecentType() {
        if(mostRecentType != null && mostRecentType != "")
            return mostRecentType;
        else
            return NULL_STR;
    }
}
