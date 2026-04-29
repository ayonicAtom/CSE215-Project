package project.model;

import java.util.ArrayList;

public class Team {

    private static int teamCount = 0;
    private int        teamId;
    private String     name;
    private String     city;
    private Coach      coach;
    private ArrayList<Player> players;
    private int        wins;
    private int        losses;
    private int        draws;
    private int        points;

    public Team(String name, String city, int wins, int losses, int draws) {

        teamCount++;
        this.teamId = teamCount;
        this.name = name;
        this.city = city;
        this.players = new ArrayList<>();
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public int               getTeamId()  { return teamId; }
    public String            getName()    { return name; }
    public String            getCity()    { return city; }
    public Coach             getCoach()   { return coach; }
    public ArrayList<Player> getPlayers() { return players; }
    public int               getWins()    { return wins; }
    public int               getLosses()  { return losses; }
    public int               getDraws()   { return draws; }
    public int               getPoints()  { return (getWins() * 3) + getDraws(); }

    public void setName(String name)  { this.name = name; }
    public void setCity(String city)  { this.city = city; }
    public void setWins(int wins)     { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setDraws(int draws)   { this.draws = draws; }

    public void addPlayer(Player player)    { players.add(player); }
    public void addCoach(Coach c)           { this.coach = c; }
    public void removePlayer(Player player) { players.remove(player); }
    public void removeCoach()               { this.coach = null; }

    public void displayTeamInfo() {
        System.out.println("-".repeat(30));
        System.out.println("Team ID    : " + getTeamId());
        System.out.println("Team Name  : " + getName());
        System.out.println("City       : " + getCity());

        System.out.println("Members    : ");
        if (coach != null) {
            System.out.println("  - " + coach.getName() + " [ COACH ]");
        } else {
            System.out.println("  - No coach assigned");
        }

        for (Player p : players) {
            System.out.println("  - " + p.getName());
        }
        System.out.println("Wins: " + getWins() + " | Losses: " + getLosses() + " | Draws: " + getDraws());

    }
}
