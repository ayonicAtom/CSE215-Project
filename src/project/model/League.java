package project.model;

import java.util.ArrayList;
import java.util.List;

public class League {

    private static int leagueCount = 0;
    private int        leagueId;
    private String     name;
    private String     season;
    private ArrayList<Team> teams;
    private int        maxTeams = 20;

    public League(String name, String season) {
        leagueCount++;
        this.leagueId = leagueCount;
        this.name = name;
        this.season = season;
        this.teams = new ArrayList<>();
    }

    // ---- Getters & Setters -------------------------------------------------------------
    public int        getLeagueId() { return leagueId; }
    public String     getName()     { return name; }
    public String     getSeason()   { return season; }
    public List<Team> getTeams()    { return teams; }
    public int        getMaxTeams() { return maxTeams; }

    public void setName(String name)     { this.name = name; }
    public void setSeason(String season) { this.season = season; }

    public void addTeam(Team team) {
        if (teams.size() < maxTeams) {
            teams.add(team);
        } else {
            System.out.println("League is full! Max teams: " + maxTeams);
        }
    }

    public void removeTeam(Team team) {
        if (teams.contains(team)) {
            teams.remove(team);
            System.out.println(team.getName() + " removed from " + name + ".");
        } else {
            System.out.println(team.getName() + " is not in this league.");
        }
    }

    public void displayLeagueInfo() {
        System.out.println("_".repeat(30));
        System.out.println("League  : " + getName());
        System.out.println("Season  : " + getSeason());
        System.out.println("Teams   : " + teams.size() + "/" + maxTeams);
        for (Team t : teams) {
            System.out.println("  - " + t.getName() + " (" + t.getCity() + ")");
        }
    }
}
