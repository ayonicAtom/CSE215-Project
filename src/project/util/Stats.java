package project.util;

import project.model.League;
import project.model.Player;
import project.model.Team;

public class Stats {

    // -------- PLAYER COMPARISON --------
    public static void comparePlayer(Player p1, Player p2) {
        int score1 = p1.getGoals() + p1.getAssists();
        int score2 = p2.getGoals() + p2.getAssists();

        System.out.println("--- Player Comparison ---");
        System.out.println("Name    : " + p1.getName()    + " vs " + p2.getName());
        System.out.println("Goals   : " + p1.getGoals()   + " vs " + p2.getGoals() + " Result: " + (p1.getGoals() == p2.getGoals() ? "Tie" : (p1.getGoals() > p2.getGoals() ? p1.getName() + " has more goals" : p2.getName() + " has more goals")));
        System.out.println("Assists : " + p1.getAssists() + " vs " + p2.getAssists() + " Result: " + (p1.getAssists() == p2.getAssists() ? "Tie" : (p1.getAssists() > p2.getAssists() ? p1.getName() + " has more assists" : p2.getName() + " has more assists")));
        System.out.println("Age     : " + p1.getAge()     + " vs " + p2.getAge() + " Result: " + (p1.getAge() == p2.getAge() ? "Tie" : (p1.getAge() < p2.getAge() ? p1.getName() + " is the youngest" : p2.getName() + " is the youngest")));
        System.out.println("Height  : " + p1.getHeight()  + " vs " + p2.getHeight() + " Result: " + (p1.getHeight() == p2.getHeight() ? "Tie" : (p1.getHeight() < p2.getHeight() ? p1.getName() + " is the shortest" : p2.getName() + " is the shortest")));

        System.out.println();

        System.out.print("Overall (Goals + Assists): ");
        if      (score1 > score2) System.out.println(p1.getName() + " wins! (" + score1 + " vs " + score2 + ")");
        else if (score2 > score1) System.out.println(p2.getName() + " wins! (" + score2 + " vs " + score1 + ")");
        else                      System.out.println("It's a tie! (" + score1 + " each)");
        System.out.println("-------------------------");
    }

    // -------- TEAM COMPARISON --------
    public static void compareTeams(Team t1, Team t2) {
        System.out.println("--- Team Comparison ---");
        System.out.println("Team Name : " + t1.getName()    + " vs " + t2.getName());
        System.out.println("Wins      : " + t1.getWins()    + " vs " + t2.getWins() + " Result: " + (t1.getWins() == t2.getWins() ? "Tie" : (t1.getWins() > t2.getWins() ? t1.getName() + " has more wins" : t2.getName() + " has more wins")));
        System.out.println("Losses    : " + t1.getLosses()  + " vs " + t2.getLosses() + " Result: " + (t1.getLosses() == t2.getLosses() ? "Tie" : (t1.getLosses() < t2.getLosses() ? t1.getName() + " has less losses" : t2.getName() + " has less losses")));
        System.out.println("Draws     : " + t1.getDraws()   + " vs " + t2.getDraws() + " Result: " + (t1.getDraws() == t2.getDraws() ? "Tie" : (t1.getDraws() < t2.getDraws() ? t1.getName() + " has less draws" : t2.getName() + " has less draws")));
        System.out.println("Points    : " + t1.getPoints()  + " vs " + t2.getPoints() + " Result: " + (t1.getPoints() == t2.getPoints() ? "Tie" : (t1.getPoints() > t2.getPoints() ? t1.getName() + " has more points" : t2.getName() + " has more points")));
        System.out.println("Players   : " + t1.getPlayers().size() + " vs " + t2.getPlayers().size());

        System.out.println();

        System.out.print("Overall (Points): ");
        if      (t1.getPoints() > t2.getPoints()) System.out.println(t1.getName() + " wins! (" + t1.getPoints() + " vs " + t2.getPoints() + ")");
        else if (t2.getPoints() > t1.getPoints()) System.out.println(t2.getName() + " wins! (" + t2.getPoints() + " vs " + t1.getPoints() + ")");
        else                                      System.out.println("It's a tie! (" + t1.getPoints() + " points each)");
        System.out.println("-----------------------");
    }

    // -------- LEAGUE COMPARISON --------
    public static void compareLeagues(League l1, League l2) {
        int goals1  = getTotalGoals(l1);
        int goals2  = getTotalGoals(l2);
        int assists1 = getTotalAssists(l1);
        int assists2 = getTotalAssists(l2);

        System.out.println("--- League Comparison ---");
        System.out.println("League Titles : " + l1.getName() + " vs " + l2.getName());
        System.out.println("Teams         : " + l1.getTeams().size() + " vs " + l2.getTeams().size());
        System.out.println("Total Goals   : " + goals1  + " vs " + goals2 + " Result: " + (goals1 == goals2 ? "Tie" : (goals1 > goals2 ? l1.getName() + " has more goals" : l2.getName() + " has more goals")));
        System.out.println("Total Assists : " + assists1 + " vs " + assists2 + " Results " + (assists1 == assists2 ? "Tie" : (assists1 > assists2 ? l1.getName() + " has more assists" : l2.getName() + " has more assists")));

        System.out.println();

        int score1 = goals1 + assists1;
        int score2 = goals2 + assists2;

        System.out.print("Overall (Total Goals & Assists): ");
        if      (score1 > score2) System.out.println(l1.getName() + " has more goals & assists! (" + score1 + " vs " + score2 + ")");
        else if (score2 > score1) System.out.println(l2.getName() + " has more goals & assists! (" + score2 + " vs " + score1 + ")");
        else                      System.out.println("It's a tie! ( Combined " + score1 + " goals & assists each)");
        System.out.println("-------------------------");
    }

    // -------- HELPERS --------
    public static int getTotalGoals(League l) {
        int total = 0;
        for (Team t : l.getTeams()) {
            for (Player p : t.getPlayers()) {
                total += p.getGoals();
            }
        }
        return total;
    }

    public static int getTotalAssists(League l) {
        int total = 0;
        for (Team t : l.getTeams()) {
            for (Player p : t.getPlayers()) {
                total += p.getAssists();
            }
        }
        return total;
    }
}