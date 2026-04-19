import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner s = new Scanner(System.in);

    static ArrayList<Player> players = new ArrayList<>();
    static ArrayList<Team>   teams = new ArrayList<>();
    static ArrayList<League> leagues = new ArrayList<>();

    public static void main(String[] args) {
        menu();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(s.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Please enter a whole number.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Double.parseDouble(s.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Please enter a number");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return s.nextLine().trim();
    }

    public static void menu() {
        while (true) {
            System.out.println("_".repeat(5) + "MENU" + "_".repeat(5));
            System.out.println("1. Create Player Profile");
            System.out.println("2. Create Team");
            System.out.println("3. Create League");
            System.out.println("4. Add a Player To a Team");
            System.out.println("5. Add a Team To a League");
            System.out.println("0. Exit");

            int option = readInt("Enter Option: ");

            switch (option) {
                case 1 -> createPlayer();
                case 2 -> createTeam();
                case 3 -> createLeague();
                case 4 -> addPlayerToTeam();
                case 5 -> addTeamToLeague();
                case 0 -> { return; }
                default -> System.out.println("Invalid Option. Try Again. ");
            }
        }
    }

    public static void createPlayer() {
        String name        = readString("Enter Name: ");
        int age            = readInt("Enter Age: ");
        double height      = readDouble("Enter Height (cm): ");
        String nationality = readString("Enter Nationality: ");
        int goals          = readInt("Enter Goals: ");
        int assists        = readInt("Enter Assists: ");

        Player p = new Player(name, age, height, nationality, goals, assists);
        players.add(p);
        p.displayPlayerInfo();
    }

    public static void createTeam() {
        String clubName = readString("Enter Club Name: ");
        String cityName = readString("Enter City Name: ");
        int wins        = readInt("Enter Wins: ");
        int losses      = readInt("Enter Losses: ");
        int draws       = readInt("Enter Draws: ");

        Team t = new Team(clubName, cityName, wins, losses, draws);
        teams.add(t);
        t.displayTeamInfo();
    }

    public static void createLeague() {
        String leagueName   = readString("Enter League Name: ");
        String leagueSeason = readString("Enter Season: ");

        League l = new League(leagueName, leagueSeason);
        leagues.add(l);
        l.displayLeagueInfo();
    }

    public static void addPlayerToTeam() {

        /* Checks if my player and team lists is empty */
        if (players.isEmpty()) {
            System.out.println("No players exist. Create a player first.");
            return;
        }

        if (teams.isEmpty()) {
            System.out.println("No teams exists. Create a team first.");
            return;
        }

        /* Shows available players and teams */
        System.out.println("--- Available Players ---");
        for (Player p : players) {
            System.out.println("[" + p.getPlayerId() + "]" + p.getName());
        }

        System.out.println("--- Available Teams ---");
        for (Team t : teams) {
            System.out.println("[" + t.getTeamId() + "]" + t.getName());
        }

        /* The player I want to put inside team */
        int playerId = readInt("Enter Player ID: ");
        int teamId   = readInt("Enter Team ID: ");

        /* When ID's match player and team is selected */
        Player selectedPlayer = null;
        Team   selectedTeam = null;

        for (Player p : players) {
            if (p.getPlayerId() == playerId) {
                selectedPlayer = p;
                break;
            }
        }

        for (Team t : teams) {
            if (t.getTeamId() == teamId) {
                selectedTeam = t;
                break;
            }
        }

        /* If the selected variables are still null that means
        player and team were not found. */
        if (selectedPlayer == null) {
            System.out.println("Player ID " + playerId + " not found.");
            return;
        }

        if (selectedTeam == null) {
            System.out.println("Team ID " + teamId + " not found");
            return;
        }

        selectedTeam.addPlayer(selectedPlayer);
        System.out.println(selectedPlayer.getName() + " added to " + selectedTeam.getName());
        selectedTeam.displayTeamInfo();
    }

    public static void addTeamToLeague() {

        /* Checks if my teams and leagues lists is empty */
        if (teams.isEmpty()) {
            System.out.println("No teams exists. Create a team first.");
            return;
        }

        if (leagues.isEmpty()) {
            System.out.println("No leagues exists. Create a league first.");
            return;
        }

        /* Shows available teams and leagues */
        System.out.println("--- Available Teams ---");
        for (Team t : teams) {
            System.out.println("[" + t.getTeamId() + "]" + t.getName());
        }

        System.out.println("--- Available Leagues ---");
        for (League l : leagues) {
            System.out.println("[" + l.getLeagueId() + "]" + l.getName());
        }

        /* The team I want to put inside league */
        int teamId   = readInt("Enter Team ID: ");
        int leagueId = readInt("Enter League ID: ");

        /* When ID's match team and league is selected */
        Team selectedTeam = null;
        League selectedLeague = null;

        for (Team t : teams) {
            if (t.getTeamId() == teamId) {
                selectedTeam = t;
                break;
            }
        }

        for (League l : leagues) {
            if (l.getLeagueId() == leagueId) {
                selectedLeague = l;
                break;
            }
        }

        /* If the selected variables are still null that means
        team and league were not found. */
        if (selectedTeam == null) {
            System.out.println("Team ID " + teamId + " not found.");
        }

        if (selectedLeague == null) {
            System.out.println("League ID " + leagueId + " not found. ");
        }
    }
}
