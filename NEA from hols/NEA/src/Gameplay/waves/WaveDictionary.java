package Gameplay.waves;

import java.util.ArrayList;

public class WaveDictionary {

    private ArrayList<Wave> waves;

    public WaveDictionary (String[] fns) {

    }

    public Wave getCurrent () {
        return waves.remove(0);
    }



}
