package rockpaperscissors;

public enum Gesture {
    SCISSORS("scissors", "rock", "paper"),
    ROCK("rock", "paper", "scissors"),
    PAPER("paper", "scissors", "rock");

    private String name;
    private String losesAgainst;
    private String winsAgainst;

    Gesture (String name, String loses, String beats){
        this.name = name;
        this.losesAgainst = loses;
        this.winsAgainst = beats;
    }

    public String getName() {
        return name;
    }

    public String getLosesAgainst() {
        return losesAgainst;
    }

    public String getWinsAgainst() {
        return winsAgainst;
    }

    public static Gesture fromString(String name) {
        for (Gesture gesture: Gesture.values()) {
            if (gesture.name.equals(name)) {
                return gesture;
            }
        }
        //throw new IllegalArgumentException("No gesture found with the name " + name + ".");
        throw new IllegalArgumentException("Invalid input");
    }

}
