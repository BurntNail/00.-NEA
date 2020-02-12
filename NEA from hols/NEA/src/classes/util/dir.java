package classes.util;

public enum dir {

    N,
    S,
    E,
    W,
    ;

    @Override
    public String toString() {
        switch (this) {
            case W -> {
                return "West";
            }
            case S -> {
                return "South";
            }
            case E -> {
                return "East";
            }
            case N -> {
                return "North";
            }
        }
        return "WHAT! HOW DID YOU DO THIS??????????????";
    }
}
