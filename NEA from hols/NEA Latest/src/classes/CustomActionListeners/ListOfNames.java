package classes.saver;

import java.io.Serializable;

public class ListOfNames implements Serializable {

    private String[] top10Names;
    private PlayerData[] top10PDs;


    public ListOfNames() {
        super();

        top10Names = new String[10];
        top10PDs = new PlayerData[10];
    }

    public void addPlayer (PlayerData stats) {
        if(stats.compareTo(top10PDs[0]) < 1)
            return;

        int indexOfNewGuy = 0;
        boolean flag = false;

        while(!flag) {
            PlayerData currentOne = top10PDs[indexOfNewGuy];
            if(stats.compareTo(currentOne) > 0)
                indexOfNewGuy++;
            else if (stats.compareTo(currentOne) == 0)
            {
                flag = true;
            }


        }
    }
}
