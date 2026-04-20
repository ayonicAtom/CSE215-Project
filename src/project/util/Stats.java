package project.util;

import project.model.League;
import project.model.Player;
import project.model.Team;

public class Stats {

    // -------- PLAYER COMPARISON --------
    public static void comparePlayer(Player p1, Player p2) {
        System.out.println("PLAYER COMPARISON");

        printRow("Goals", p1.getGoals(), p2.getGoals(), true);
        printRow("Assists", p1.getAssists(), p2.getAssists(), true);
        printRow("Age", p1.getAge(), p2.getAge(), false);

        double h1 = p1.getHeight();
        double h2 = p2.getHeight();
        System.out.println("Height: " + h1 + " vs " + h2 + " -> " + getWinner(h1, h2, false));

        int score1 = p1.getGoals() + p1.getAssists();
        int score2 = p2.getGoals() + p2.getAssists();

        System.out.println("Overall: " + score1 + " vs " + score2);
    }

    // -------- TEAM COMPARISON --------
    public static void compareTeams(Team t1, Team t2) {
        System.out.println("TEAM COMPARISON");

        printRow("Wins", t1.getWins(), t2.getWins(), true);
        printRow("Losses", t1.getLosses(), t2.getLosses(), false);
        printRow("Points", t1.getPoints(), t2.getPoints(), true);

        System.out.println("Players: " + t1.getPlayers().size() + " vs " + t2.getPlayers().size());
    }

    // -------- LEAGUE COMPARISON --------
    public static void compareLeagues(League l1, League l2) {
        System.out.println("LEAGUE COMPARISON");

        int goals1 = getTotalGoals(l1);
        int goals2 = getTotalGoals(l2);

        int points1 = getTotalPoints(l1);
        int points2 = getTotalPoints(l2);

        printRow("Total Goals", goals1, goals2, true);
        printRow("Total Points", points1, points2, true);
    }

    // -------- SIMPLE HELPER METHODS --------
    private static void printRow(String label, int v1, int v2, boolean higherIsBetter) {
        System.out.println(label + ": " + v1 + " vs " + v2 + " -> " + getWinner(v1, v2, higherIsBetter));
    }

    private static String getWinner(double v1, double v2, boolean higherIsBetter) {
        if (v1 == v2) return "Tie";

        if (higherIsBetter) {
            return (v1 > v2) ? "P1 wins" : "P2 wins";
        } else {
            return (v1 < v2) ? "P1 wins" : "P2 wins";
        }
    }

    private static int getTotalGoals(League l) {
        int total = 0;

        for (Team team : l.getTeams()) {
            for (Player player : team.getPlayers()) {
                total += player.getGoals();
            }
        }

        return total;
    }

    private static int getTotalPoints(League l) {
        int total = 0;

        for (Team team : l.getTeams()) {
            total += team.getPoints();
        }

        return total;
    }
}