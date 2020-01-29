package Gameplay.turrets.turretFrame;

import classes.util.Coordinate;

import javax.swing.*;
import java.util.ArrayList;

public class XYPanel extends JFrame {

    private ArrayList<JButton> btns;

    private Coordinate theOne;
    private boolean hasBeenFound;

    public XYPanel (ArrayList<Coordinate> values) {
        super("Pick X and Y for New Tower (starts from 0 at top-left)");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing (java.awt.event.WindowEvent windowEvent) {
                theOne = values.get(values.size() - 1);
                setVisible(false);
                dispose();
            }
        });


        btns = new ArrayList<>();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        theOne = TurretFrame.NULL_COORD;

        for (Coordinate value : values)
        {
            JButton temp = new JButton(value.toString());
            btns.add(temp);
            panel.add(temp);
        }

        for(int i = 0; i < btns.size(); i++) {
            JButton btn = btns.get(i);

            btn.addActionListener(e -> {
                hasBeenFound = true;
                theOne = Coordinate.parseFromTS(btn.getText());
                setVisible(false);
            });
        }



        add(panel);

        pack();
        setVisible(true);

    }

    public Coordinate getTheOne() {
        if(hasBeenFound) {
            return theOne;
        }
        else
            return TurretFrame.NULL_COORD;
    }
}
