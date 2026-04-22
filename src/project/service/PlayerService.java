package project.service;

import project.model.Player;
import java.util.ArrayList;

public class PlayerService {

    private ArrayList<Player> players = new ArrayList<>();

    public void add(Player player) {
        players.add(player);
    }

    public void delete(int id) {
        players.removeIf(p -> p.getPlayerId() == id);
    }

    public Player findById(int id) {
        for (Player p : players) {
            if (p.getPlayerId() == id) {
                return p;
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void showPlayers() {
        System.out.println("--- Available Players ---");
        for (Player p : players) {
            System.out.println("[ " + p.getPlayerId() + " ] " + p.getName());
        }
    }
}