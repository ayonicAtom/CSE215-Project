package project.model;

public class Player extends Person {

    private static int playerCount = 0;
    private int        playerId;
    private int        goals;
    private int        assists;
    private String     position;

    public Player(String name, int age, double height, String nationality, String position, int goals, int assists) {
        super(name, age, height, nationality);
        playerCount++;
        this.playerId = playerCount;
        this.position = position;
        this.goals = goals;
        this.assists = assists;
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public int    getPlayerId()    { return playerId; }
    public int    getGoals()       { return goals; }
    public int    getAssists()     { return assists; }
    public String getPosition()    { return position; }

    public void setGoals(int goals)                { this.goals = goals; }
    public void setAssists(int assists)            { this.assists = assists; }
    public void setPosition(String position)       { this.position = position; }

    public void displayInfo() {
        System.out.println("_".repeat(30));
        System.out.println("Player ID    : " + getPlayerId());
        System.out.println("Name         : " + getName());
        System.out.println("Age          : " + getAge());
        System.out.println("Height       : " + getHeight());
        System.out.println("Nationality  : " + getNationality());
        System.out.println("Position     : " + getPosition());
        System.out.println("Goals        : " + getGoals());
        System.out.println("Assists      : " + getAssists());
    }
}
