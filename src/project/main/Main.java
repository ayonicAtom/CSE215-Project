package project.main;

import project.model.League;
import project.model.Player;
import project.model.Team;
import project.service.LeagueService;
import project.service.PlayerService;
import project.service.TeamService;
import project.util.InputHelper;
import project.util.Stats;

public class Main {

    static PlayerService playerService = new PlayerService();
    static TeamService   teamService   = new TeamService();
    static LeagueService leagueService = new LeagueService();

    public static void main(String[] args) {
        String password = InputHelper.readString("Create Password (CAUTION!!! PASSWORD CANNOT BE CHANGED.): ");
        Login user = new Login(password);

        if (user.startLogin()) {
            menu();
        } else {
            System.out.println("Program exited.");
        }
    }

    public static void menu() {
        while (true) {
            System.out.println("_".repeat(5) + "MENU" + "_".repeat(5));
            System.out.println("1. Create Player");
            System.out.println("2. Create Team");
            System.out.println("3. Create League");
            System.out.println("4. Show Player List");
            System.out.println("5. Show Team List");
            System.out.println("6. Show League List");
            System.out.println("7. Delete Player");
            System.out.println("8. Delete Team");
            System.out.println("9. Delete League");
            System.out.println("10. Add Player to Team");
            System.out.println("11. Add Team To League");
            System.out.println("12. Edit Information");
            System.out.println("13. Remove Player From Team");
            System.out.println("14. Remove Team From League");
            System.out.println("15. See Statistics");
            System.out.println("0. Exit");

            int option = InputHelper.readInt("Enter Option: ");

            switch (option) {
                case 1 -> createPlayer();
                case 2 -> createTeam();
                case 3 -> createLeague();
                case 4 -> playerService.showPlayers();
                case 5 -> teamService.showTeams();
                case 6 -> leagueService.showLeagues();
                case 10 -> addPlayerToTeam();
                case 11 -> addTeamToLeague();
                case 12 -> editMenu();
                case 13 -> removePlayerFromTeam();
                case 14 -> removeTeamFromLeague();
                case 15 -> Statistics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Option. Try Again. ");
            }
        }
    }

    public static void editMenu() {
        System.out.println("Edit - ");
        System.out.println("1. Player Info");
        System.out.println("2. Team Info");
        System.out.println("3. League Info");
        System.out.println("0. Back");

        int subOption = InputHelper.readInt("Enter Option: ");

        switch (subOption) {
            case 1 -> editPlayer();
            case 2 -> editTeam();
            case 3 -> editLeague();
            case 0 -> {
                return;
            }
            default -> System.out.println("Invalid Option. Try Again");
        }
    }

    // ----- Create ---------------------------------------------------

    public static void createPlayer() {
        String name        = InputHelper.readString("Enter Name: ");
        int    age         = InputHelper.readInt("Enter Age: ");
        double height      = InputHelper.readDouble("Enter Height (cm): ");
        String nationality = InputHelper.readString("Enter Nationality: ");
        int    goals       = InputHelper.readInt("Enter Goals: ");
        int    assists     = InputHelper.readInt("Enter Assists: ");
        String position    = InputHelper.readString("Enter Position: ");

        Player p = new Player(name, age, height, nationality, position, goals, assists);
        playerService.add(p);
        p.displayPlayerInfo();
    }

    public static void createTeam() {
        String clubName = InputHelper.readString("Enter Club Name: ");
        String cityName = InputHelper.readString("Enter City Name: ");
        int    wins     = InputHelper.readInt("Enter Wins: ");
        int    losses   = InputHelper.readInt("Enter Losses: ");
        int    draws    = InputHelper.readInt("Enter Draws: ");

        Team t = new Team(clubName, cityName, wins, losses, draws);
        teamService.add(t);
        t.displayTeamInfo();
    }

    public static void createLeague() {
        String leagueName   = InputHelper.readString("Enter League Name: ");
        String leagueSeason = InputHelper.readString("Enter Season: ");

        League l = new League(leagueName, leagueSeason);
        leagueService.add(l);
        l.displayLeagueInfo();
    }

    // ----- Delete ---------------------------------------------------
    public static void deletePlayer() {
        playerService.showPlayers();
        int playerId = InputHelper.readInt("Enter Player ID to Delete: ");
        playerService.delete(playerId);
        System.out.println("Player With ID " + playerId + " deleted.");
        playerService.showPlayers();
    }

    public static void deleteTeam() {
        teamService.showTeams();
        int teamId = InputHelper.readInt("Enter Team ID to delete: ");
        teamService.delete(teamId);
        System.out.println("Team With ID " + teamId + " deleted.");
        teamService.showTeams();
    }

    public static void deleteLeague() {
        leagueService.showLeagues();
        int leagueId = InputHelper.readInt("Enter League ID to delete: ");
        leagueService.delete(leagueId);
        System.out.println("League with ID " + leagueId + " deleted.");
        leagueService.showLeagues();
    }

    // ----- Add ---------------------------------------------------

    public static void addPlayerToTeam() {

        /* Checks if my player and team lists is empty */
        if (playerService.isEmpty()) {
            System.out.println("No players exist. Create a player first.");
            return;
        }
        if (teamService.isEmpty()) {
            System.out.println("No teams exists. Create a team first.");
            return;
        }

        /* Shows available players and teams */
        playerService.showPlayers();
        teamService.showTeams();

        /* The player I want to put inside team */
        int playerId = InputHelper.readInt("Enter Player ID: ");
        int teamId   = InputHelper.readInt("Enter Team ID: ");

        /* When ID's match, player and team is selected */
        Player p = playerService.findById(playerId);
        Team   t = teamService.findById(teamId);

        /* If the selected variables are still null that means
        player and team were not found. */
        if (p == null) {
            System.out.println("Player ID " + playerId + " not found.");
            return;
        }
        if (t == null) {
            System.out.println("Team ID " + teamId + " not found");
            return;
        }

        t.addPlayer(p);
        System.out.println(p.getName() + " added to " + t.getName());
        t.displayTeamInfo();
    }

    public static void addTeamToLeague() {

        /* Checks if my teams and leagues lists is empty */
        if (teamService.isEmpty()) {
            System.out.println("No teams exists. Create a team first.");
            return;
        }
        if (leagueService.isEmpty()) {
            System.out.println("No leagues exists. Create a league first.");
            return;
        }

        /* Shows available teams and leagues */
        teamService.showTeams();
        leagueService.showLeagues();

        /* The team I want to put inside league */
        int teamId   = InputHelper.readInt("Enter Team ID: ");
        int leagueId = InputHelper.readInt("Enter League ID: ");

        /* When ID's match, team and league is selected */
        Team   t = teamService.findById(teamId);
        League l = leagueService.findById(leagueId);

        /* If the selected variables are still null that means
        team and league were not found. */
        if (t == null) {
            System.out.println("Team ID " + teamId + " not found.");
            return;
        }
        if (l == null) {
            System.out.println("League ID " + leagueId + " not found. ");
            return;
        }

        l.addTeam(t);
        System.out.println(t.getName() + " added to " + l.getName());
        l.displayLeagueInfo();
    }

    // ----- Edit ---------------------------------------------------

    public static void editPlayer() {
        if (playerService.isEmpty()) {
            System.out.println("No players exists.");
            return;
        }

        playerService.showPlayers();
        int playerId = InputHelper.readInt("Enter Player ID to edit: ");

        Player p = playerService.findById(playerId);
        if (p == null) {
            System.out.println("Player ID " + playerId + " not found.");
            return;
        }

        System.out.println("Editing: " + p.getName());
        System.out.println("(Press Enter to keep current value)");

        String name = InputHelper.read("Name [" + p.getName() + "]: ");
        if (!name.isEmpty()) p.setName(name);

        String age = InputHelper.read("Age [" + p.getAge() + "]: ");
        if (!age.isEmpty()) {
            try {
                p.setAge(Integer.parseInt(age));
            } catch (NumberFormatException e) {
                System.out.println("Invalid age, kept original.");
            }
        }

        String height = InputHelper.read("Height [" + p.getHeight() + "]: ");
        if (!height.isEmpty()) {
            try {
                p.setHeight(Double.parseDouble(height));
            } catch (NumberFormatException e) {
                System.out.println("Invalid height, kept original.");
            }
        }

        String nationality = InputHelper.read("Nationality [" + p.getNationality() + "]: ");
        if (!nationality.isEmpty()) p.setNationality(nationality);

        String goals = InputHelper.read("Goals [" + p.getGoals() + "]: ");
        if (!goals.isEmpty()) {
            try {
                p.setGoals(Integer.parseInt(goals));
            } catch (NumberFormatException e) {
                System.out.println("Invalid goals, kept the original.");
            }
        }

        String assists = InputHelper.read("Assists [" + p.getAssists() + "]: ");
        if (!assists.isEmpty()) {
            try {
                p.setAssists(Integer.parseInt(assists));
            } catch (NumberFormatException e) {
                System.out.println("Invalid assists, kept the original");
            }
        }

        System.out.println("Player details updated.");
        p.displayPlayerInfo();
    }

    public static void editTeam() {
        if (teamService.isEmpty()) {
            System.out.println("No teams exists.");
            return;
        }

        teamService.showTeams();
        int teamId = InputHelper.readInt("Enter Team ID to edit: ");

        Team t = teamService.findById(teamId);
        if (t == null) {
            System.out.println("Team ID " + teamId + " not found.");
            return;
        }

        System.out.println("Editing: " + t.getName());
        System.out.println("(Press Enter to keep current value)");

        String name = InputHelper.read("Name [" + t.getName() + "]: ");
        if (!name.isEmpty()) t.setName(name);

        String city = InputHelper.read("City [" + t.getCity() + "]: ");
        if (!city.isEmpty()) t.setCity(city);

        String wins = InputHelper.read("Wins [" + t.getWins() + "]: ");
        if (!wins.isEmpty()) {
            try {
                t.setWins(Integer.parseInt(wins));
            } catch (NumberFormatException e) {
                System.out.println("Invalid wins, kept original.");
            }
        }

        String losses = InputHelper.read("Losses [" + t.getLosses() + "]: ");
        if (!losses.isEmpty()) {
            try {
                t.setLosses(Integer.parseInt(losses));
            } catch (NumberFormatException e) {
                System.out.println("Invalid losses, kept original.");
            }
        }

        String draws = InputHelper.read("Draws [" + t.getDraws() + "]: ");
        if (!draws.isEmpty()) {
            try {
                t.setDraws(Integer.parseInt(draws));
            } catch (NumberFormatException e) {
                System.out.println("Invalid draws, kept original.");
            }
        }

        System.out.println("Team updated.");
        t.displayTeamInfo();

    }

    public static void editLeague() {
        if (leagueService.isEmpty()) {
            System.out.println("No leagues exist.");
            return;
        }

        leagueService.showLeagues();
        int leagueId = InputHelper.readInt("Enter League ID to edit: ");

        League l = leagueService.findById(leagueId);
        if (l == null) {
            System.out.println("League ID " + leagueId + " not found. ");
            return;
        }

        System.out.println("Editing: " + l.getName());
        System.out.println("(Press Enter to keep current value)");

        String name = InputHelper.read("Name [" + l.getName() + "]: ");
        if (!name.isEmpty()) l.setName(name);

        String season = InputHelper.read("Season [" + l.getSeason() + "]: ");
        if (!season.isEmpty()) l.setSeason(season);

        System.out.println("League updated.");
        l.displayLeagueInfo();
    }

    // ----- Remove ---------------------------------------------------
    public static void removePlayerFromTeam() {
        /* Checks if my player and team lists is empty */
        if (playerService.isEmpty()) {
            System.out.println("No players exist. Create a player first.");
            return;
        }
        if (teamService.isEmpty()) {
            System.out.println("No teams exists. Create a team first.");
            return;
        }

        teamService.showTeams();
        int teamId = InputHelper.readInt("Enter Team ID: ");

        Team t = teamService.findById(teamId);
        if (t == null) {
            System.out.println("Team ID " + teamId + " not found.");
            return;
        }

        if (t.getPlayers().isEmpty()) {
            System.out.println(t.getName() + " has no players");
            return;
        }

        t.displayTeamInfo();
        int playerId = InputHelper.readInt("Enter Player ID to remove: ");

        Player p = playerService.findById(playerId);
        if (p == null) {
            System.out.println("Player ID " + playerId + " not found.");
            return;
        }

        t.removePlayer(p);
        System.out.println(p.getName() + " removed from " + t.getName() + ".");
        t.displayTeamInfo();
    }

    public static void removeTeamFromLeague() {
        /* Checks if my team and league lists exists */
        if (teamService.isEmpty()) {
            System.out.println("No teams exists.");
            return;
        }
        if (leagueService.isEmpty()) {
            System.out.println("No leagues exists.");
            return;
        }

        leagueService.showLeagues();
        int leagueId = InputHelper.readInt("Enter League ID: ");

        League l = leagueService.findById(leagueId);
        if (l == null) {
            System.out.println("League ID " + leagueId + " not found. ");
            return;
        }

        if (l.getTeams().isEmpty()) {
            System.out.println(l.getName() + " has no teams.");
            return;
        }

        l.displayLeagueInfo();
        int teamId = InputHelper.readInt("Enter Team ID to remove: ");

        Team t = teamService.findById(teamId);
        if (t == null) {
            System.out.println("Team ID " + teamId + " not found.");
            return;
        }

        l.removeTeam(t);
        System.out.println(t.getName() + " removed from " + l.getName());
        l.displayLeagueInfo();
    }

    // ----- Functions --------------------------------------------------
    public static void Statistics() {
        while (true) {
            System.out.println("Compare -");
            System.out.println("1. Players");
            System.out.println("2. Teams");
            System.out.println("3. Leagues");
            System.out.println("0. Exit");

            int option = InputHelper.readInt("Enter Option: ");

            switch (option) {
                case 1 -> {
                    if (playerService.isEmpty()) {
                        System.out.println("No players exists.");
                        break;
                    }

                    playerService.showPlayers();

                    int playerOneId = InputHelper.readInt("Enter Player One ID: ");
                    int playerTwoId = InputHelper.readInt("Enter Player Two ID: ");

                    Player targetOne = playerService.findById(playerOneId);
                    if (targetOne == null) {
                        System.out.println("Player ID " + playerOneId + " not found.");
                        break;
                    }

                    Player targetTwo = playerService.findById(playerTwoId);
                    if (targetTwo == null) {
                        System.out.println("Player ID " + playerTwoId + " not found.");
                        break;
                    }

                    Stats.comparePlayer(targetOne, targetTwo);
                }

                case 2 -> {
                    if (teamService.isEmpty()) {
                        System.out.println("No teams exists.");
                        break;
                    }

                    teamService.showTeams();

                    int teamOneId = InputHelper.readInt("Enter Team One ID: ");
                    int teamTwoId = InputHelper.readInt("Enter Team Two ID: ");

                    Team targetOne = teamService.findById(teamOneId);
                    if (targetOne == null) {
                        System.out.println("Team ID " + teamOneId + " not found.");
                        break;
                    }

                    Team targetTwo = teamService.findById(teamTwoId);
                    if (targetTwo == null) {
                        System.out.println("Team ID " + teamTwoId + " not found.");
                        break;
                    }

                    Stats.compareTeams(targetOne, targetTwo);
                }

                case 3 -> {
                    if (leagueService.isEmpty()) {
                        System.out.println("No leagues exists.");
                        break;
                    }

                    leagueService.showLeagues();

                    int leagueOneId = InputHelper.readInt("Enter League One ID: ");
                    int leagueTwoId = InputHelper.readInt("Enter League Two ID: ");

                    League targetOne = leagueService.findById(leagueOneId);
                    if (targetOne == null) {
                        System.out.println("League ID " + leagueOneId + " not found");
                        break;
                    }

                    League targetTwo = leagueService.findById(leagueTwoId);
                    if (targetTwo == null) {
                        System.out.println("League ID " + leagueTwoId + " not found");
                        break;
                    }

                    Stats.compareLeagues(targetOne, targetTwo);
                }

                case 0 -> {
                    return;
                }

                default -> System.out.println("Invalid Option.");
            }
        }
    }
}