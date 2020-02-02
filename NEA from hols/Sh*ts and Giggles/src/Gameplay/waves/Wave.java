package Gameplay.waves;

import java.util.HashMap;

public class Wave {

    private HashMap<Character, Integer> wave;

    public Wave(HashMap<Character, Integer> wave) {
        this.wave = wave;
    }

    public HashMap<Character, Integer> getWave() {
        return wave;
    }
}
