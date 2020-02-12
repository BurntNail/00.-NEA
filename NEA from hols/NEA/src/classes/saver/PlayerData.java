package classes.saver;

import classes.censor.Censorer;

import java.io.Serializable;

public class PlayerData implements Serializable, Comparable<PlayerData> {

    private int score;
    private String name;
    private String msg;

    public PlayerData(int score, String msg) {
        super();
        this.score = score;
        name = System.getProperty("user.name");
        this.msg = msg;
    }

    public PlayerData (int score, String user, String msg) {
        super();
        this.score = score;
        this.name = user;
        try {
            this.msg = Censorer.censorWord(msg);
        } catch (Exception e) {
            this.msg = "MSG FINDING ERROR";
        }
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "score=" + score +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public int compareTo(PlayerData o) {
        return Integer.compare(score, o.score);
    }

//    public static PlayerData parse (String tbp) {
//        int scoreStartIndex = tbp.indexOf("score=") + 6;
//        int scoreEndIndex = tbp.indexOf(", name") - 1;
//
//        int nameStartIndex = tbp.indexOf("name=") + 6;
//        int nameEndIndex = tbp.indexOf(", msg") - 1;
//
//        int msgStartIndex = tbp.indexOf("msg=") + 5;
//        int msgEndIndex = tbp.indexOf('}') - 1;
//
//        int score = Integer.parseInt(tbp.substring(scoreStartIndex, scoreEndIndex));
//        String name = tbp.substring(nameStartIndex, nameEndIndex);
//        String msg = tbp.substring(msgStartIndex, msgEndIndex);
//
//        return new PlayerData(score, name, msg);
//    }
}
