package classes.saver;


import java.io.*;

public class SaveSystem {

//    public static void SavePlayer (PlayerData playerStats){ //Turns out that System.get or setProperty is not permanent and does not stay through closes and re-opens...
//        String name = playerStats.getName();
//        String value = playerStats.toString();
//
//        System.setProperty(name, value);
//    }
//
//    public static PlayerData getPlayer (String name) {
//        String val = System.getProperty(name);
//        if(val == null)
//            return null;
//
//        return PlayerData.parse(val);
//
//    }

    public static void SavePlayer (PlayerData p) {
        try {
            String whereAmI = System.getProperty("user.home") + "/Apex Turrets";
            String fn = whereAmI + "/usrs/" + p.getName() + ".ser";

            BufferedWriter writer = new BufferedWriter(new FileWriter(fn));
            writer.write("");
            writer.close();



            FileOutputStream fileOut = new FileOutputStream(whereAmI + "/urs/" + p.getName() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(p);
//            addToList(p.getName());

            out.close();
            fileOut.close();
        } catch (Exception e) {
            System.err.println("FILE SAVE FAILED");
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayer (String name) {
        String whereIAm = System.getProperty("user.home") + "/Apex Turrets";
        try {
            FileInputStream fileIn = new FileInputStream("/usrs/" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            PlayerData val = ((PlayerData) in.readObject());

            return val;

        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        PlayerData idk = new PlayerData(578346595, "ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha ha");
        SavePlayer(idk);


        PlayerData letsCheckBro = getPlayer(System.getProperty("user.name"));
        if(letsCheckBro == null)
            return;
        System.out.println(letsCheckBro.toString());
    }

}
