package Gameplay.turrets;

import Gameplay.player.PlayerManager;
import classes.render.mustBeRendered.Entity.baseEntity.Entity;
import classes.render.mustBeRendered.Entity.baseEntity.entityType;
import classes.render.mustBeRendered.Entity.turret.turretActual;
import classes.render.mustBeRendered.Entity.turret.turretTemplate;
import classes.util.coordinate.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;

public class TurretFrame extends JPanel{

    private JPanel panel;

    private ArrayList<JButton> btns;
    private ArrayList<Coordinate> usedSquares;
    private ArrayList<Coordinate> freeSquares;
    private int currentIndex;

    public static final Coordinate NULL_COORD = new Coordinate(100000, 10000);

    private Icon messageIcn;

    private ArrayList<Entity> turretActuals;

    public TurretFrame(ArrayList<Coordinate> usedSquares, ArrayList<Coordinate> freeSquares, Dimension size, Collection<turretTemplate> templates_collection, PlayerManager pm, JFrame toPack, TurretManager tm) {
        super();


        currentIndex = 0;
        try {
            URL url = new URL(main.ICON_LOCATIONS + "XYIcon.png");

            if(url == null)
                throw new MalformedURLException("No URL found...");

            messageIcn = new ImageIcon(url, "Icon showing X and Y with crosshairs circle.");
        }catch (Exception e) {
            messageIcn = new ImageIcon();
        }

        ArrayList<turretTemplate> turrets = new ArrayList<>();
        turrets.addAll(templates_collection);

        this.usedSquares = usedSquares;
        this.freeSquares = freeSquares;

        setPreferredSize(size);
        setLayout(new GridLayout(turrets.size() + 1, 1));

        panel = new JPanel();
        panel.setAutoscrolls(true);
        panel.setPreferredSize(new Dimension(main.WINDOW_WIDTH / 2, main.WINDOW_HEIGHT));

        turretActuals = new ArrayList<>();


        btns = new ArrayList<>();
        panel.setLayout(new GridLayout(turrets.size() + 2, 1));

        for (int i = 0; i < turrets.size(); i++) {
            turretTemplate tt = turrets.get(i);
            String name = tt.getName();


            JButton sBtn = new JButton("Buy " + name);
            btns.add(sBtn);
            panel.add(sBtn);

            sBtn.addActionListener(e -> {

                if(freeSquares.size() == 0)
                {
                    JOptionPane.showMessageDialog(panel, "Unfortunately, there are no turret spaces left. Good luck!", "No free space.", JOptionPane.ERROR_MESSAGE, messageIcn);
                    return;
                }

                int result = JOptionPane.showConfirmDialog(panel, tt.toString(), "Confirm buy Turret: " + tt.getName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, messageIcn);

                if(JOptionPane.YES_OPTION == result) {

                    main.quickEntity(turretActuals);
                    main.quickCoord(usedSquares);
                    main.quickCoord(freeSquares);


                    Object location = JOptionPane.showInputDialog(panel, "Please enter a location", "Where would you like your tower?", JOptionPane.QUESTION_MESSAGE, messageIcn, ((Object[]) freeSquares.toArray()), 0);

                    String resInStr = location + "";

                    if(resInStr == null)
                        return;


                    String type = sBtn.getText().substring(4);
                    Coordinate loc = Coordinate.parseFromTS(resInStr);

                    tm.buyTurret(loc, type);

                    main.quickEntity(turretActuals);
                    main.quickCoord(usedSquares);
                    main.quickCoord(freeSquares);
                }
            });
        }

        JButton sellBtn = new JButton("Sell tower?");
        sellBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(panel, "Sell turret?", "Do you want to sell a turret - Beware, you may not get back the full investment.", JOptionPane.OK_CANCEL_OPTION);

            if(result == JOptionPane.OK_OPTION) {

                main.quickCoord(usedSquares);
                main.quickCoord(freeSquares);
                main.quickEntity(turretActuals);


                ArrayList<String> sellableTowers = new ArrayList<>();
                ArrayList<turretActual> turretsThatGoWith = new ArrayList<>();
                int i = 0;
                for (Coordinate coordinate : usedSquares) {
                    turretActual ta = null;
                    String fin;

                    try {
                        for(Entity entity : turretActuals) {
                            if(entity.getXYInArr().equals(coordinate))
                            {
                                ta = ((turretActual) entity);
                                break;
                            }
                        }

                        if(ta == null)
                            throw new TurretNotFoundException();

                        fin = "#" + (i + 1) + " ";
                        fin += coordinate.toString() + " - " + ta.getTurret().getSellValue();
                        turretsThatGoWith.add(ta);

                    } catch (TurretNotFoundException ex) {
                        fin = "";
                    }

                    sellableTowers.add(fin);
                    i++;
                }

                Object location = JOptionPane.showInputDialog(panel, "Which Tower would you like to sell. Resale values and coordinates listed.", "Please enter a location", JOptionPane.QUESTION_MESSAGE, messageIcn, ((Object[]) sellableTowers.toArray()), 0);

                if(location == null)
                    return;
                else if (turretActuals.size() == 0)
                {
                    JOptionPane.showMessageDialog(panel, "No TURRETS LEFT.", "Unfortunately, if there are no turrets to sell, you cannot sell a turret." ,JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String strVersion = location.toString();

                System.out.println("Selling: " + strVersion);


                int indexStart = 1;
                int indexEnd = strVersion.indexOf("Coordinate") - 2;

                int index = Integer.parseInt(strVersion.substring(indexStart, indexEnd));
                turretActual turretToSell = turretsThatGoWith.get(index);



                tm.sellTurret(turretToSell);

            }
        });

        JTextArea label = new JTextArea(getLabel(pm, turretActuals));
        label.setPreferredSize(new Dimension(size.width, size.height / 2));
        label.setAutoscrolls(true);
//        label.setEditable(false);


        panel.add(sellBtn);
        panel.add(label);


        add(panel);
        toPack.pack();

        pm.addBooleanChangeListener(e -> {
            label.setText(getLabel(pm, turretActuals));
            toPack.pack();

            if(pm.isDone()) {
                for(JButton btn : btns)
                    btn.setEnabled(false);
                sellBtn.setEnabled(false);

                toPack.pack();
            }
        });

    }

    private static String getLabel(PlayerManager pm, ArrayList<Entity> turrets) {
        String pmPart = "Money: " + pm.getMoney() + "\nHearts remaining: " + pm.getHearts();

        if(turrets.size() == 0)
            return pmPart;

        StringJoiner turretJoiner = new StringJoiner("\n", "Turret Statuses: \n\n", "");

        for (Entity turret : turrets) {
            if(turret.getType() != entityType.turret)
                continue;

            turretActual casted = ((turretActual) turret);
            String st = casted.toString();

            turretJoiner.add(st);
        }

        return pmPart + "\n\n" + turretJoiner.toString();
    }

    public void setTurrets (ArrayList<Entity> newTurrets) {
        turretActuals = ((ArrayList<Entity>) newTurrets.clone());
    }
    public ArrayList<Entity> getTurretActuals () {
        return turretActuals;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    public void incrementIndex () {
        currentIndex++;
    }
}
