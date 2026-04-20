package project.model;

public class Player {

    private static int playerCount = 0;
    private int playerId;
    private String name;
    private int age;
    private double height;
    private String nationality;
    private int goals;
    private int assists;

    public Player(String name, int age, double height, String nationality, int goals, int assists) {
        playerCount++;
        this.playerId = playerCount;
        this.name = name;
        this.age = age;
        this.height = height;
        this.nationality = nationality;
        this.goals = goals;
        this.assists = assists;
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public int    getPlayerId()    { return playerId; }
    public String getName()        { return name; }
    public int    getAge()         { return age; }
    public double getHeight()      { return height; }
    public String getNationality() { return nationality; }
    public int    getGoals()       { return goals; }
    public int    getAssists()     { return assists; }


    public void setName(String name)               { this.name = name; }
    public void setAge(int age)                    { this.age = age; }
    public void setHeight(double height)           { this.height = height; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setGoals(int goals)                { this.goals = goals; }
    public void setAssists(int assists)            { this.assists = assists; }

    public void displayPlayerInfo() {
        System.out.println("_".repeat(30));
        System.out.println("Player ID    : " + getPlayerId());
        System.out.println("Name         : " + getName());
        System.out.println("Age          : " + getAge());
        System.out.println("Height       : " + getHeight());
        System.out.println("Nationality  : " + getNationality());
        System.out.println("Goals        : " + getGoals());
        System.out.println("Assists      : " + getAssists());
    }
}
