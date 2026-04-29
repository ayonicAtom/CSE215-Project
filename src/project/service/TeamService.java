package project.service;

import project.model.Player;
import project.model.Team;

import java.util.ArrayList;

public class TeamService {

    private ArrayList<Team> teams = new ArrayList<>();

    public void add(Team team) {
        teams.add(team);
    }

    public void delete(int id) {
        teams.removeIf(t -> t.getTeamId() == id);
    }

    public Team findById(int id) {
        for (Team t : teams) {
            if (t.getTeamId() == id) {
                return t;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return teams.isEmpty();
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void showTeams() {
        System.out.println("--- Available Teams ---");
        for (Team t : teams) {
            System.out.println("[ " + t.getTeamId() + " ] " + t.getName());

            System.out.println("Players in " + t.getName());
            for (Player p : t.getPlayers()) {
                System.out.println(" - [ " + p.getPlayerId() + " ] " + p.getName());
            }
        }
    }
}