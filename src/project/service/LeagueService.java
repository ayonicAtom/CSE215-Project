package project.service;

import project.model.League;
import java.util.ArrayList;

public class LeagueService {

    private ArrayList<League> leagues = new ArrayList<>();

    public void add(League league) {
        leagues.add(league);
    }

    public void delete(int id) {
        leagues.removeIf(l -> l.getLeagueId() == id);
    }

    public League findById(int id) {
        for (League l : leagues) {
            if (l.getLeagueId() == id) {
                return l;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return leagues.isEmpty();
    }

    public void showLeagues() {
        System.out.println("--- Available Leagues ---");
        for (League l : leagues) {
            System.out.println("[ " + l.getLeagueId() + " ] " + l.getName() + " (" + l.getSeason() + ")");
        }
    }
}