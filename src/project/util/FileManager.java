package project.util;

import project.model.League;
import project.model.Player;
import project.model.Team;
import project.service.LeagueService;
import project.service.PlayerService;
import project.service.TeamService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager — saves and loads Players, Teams, and Leagues to/from .txt files.
 *
 * File formats (pipe-delimited):
 *   players.txt        : id|name|age|height|nationality|position|goals|assists
 *   teams.txt          : id|name|city|wins|losses|draws
 *   leagues.txt        : id|name|season
 *   team_players.txt   : teamId|playerId
 *   league_teams.txt   : leagueId|teamId
 *
 * IDs in the files are stored for reference only.
 * When loading, objects are created in order so their auto-incremented IDs
 * match the saved IDs (assuming clean startup).
 */
public class FileManager {

    private static final String PLAYERS_FILE     = "players.txt";
    private static final String TEAMS_FILE        = "teams.txt";
    private static final String LEAGUES_FILE      = "leagues.txt";
    private static final String TEAM_PLAYERS_FILE = "team_players.txt";
    private static final String LEAGUE_TEAMS_FILE = "league_teams.txt";

    // ================================================================== SAVE

    /** Save all players to players.txt */
    public static void savePlayers(List<Player> players) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PLAYERS_FILE))) {
            for (Player p : players) {
                pw.println(p.getPlayerId() + "|" + p.getName() + "|" + p.getAge() + "|"
                        + p.getHeight() + "|" + p.getNationality() + "|" + p.getPosition() + "|"
                        + p.getGoals() + "|" + p.getAssists());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving players: " + e.getMessage());
        }
    }

    /** Save all teams to teams.txt */
    public static void saveTeams(List<Team> teams) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEAMS_FILE))) {
            for (Team t : teams) {
                pw.println(t.getTeamId() + "|" + t.getName() + "|" + t.getCity() + "|"
                        + t.getWins() + "|" + t.getLosses() + "|" + t.getDraws());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving teams: " + e.getMessage());
        }
    }

    /** Save all leagues to leagues.txt */
    public static void saveLeagues(List<League> leagues) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LEAGUES_FILE))) {
            for (League l : leagues) {
                pw.println(l.getLeagueId() + "|" + l.getName() + "|" + l.getSeason());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving leagues: " + e.getMessage());
        }
    }

    /** Save team–player relationships */
    public static void saveTeamPlayers(List<Team> teams) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEAM_PLAYERS_FILE))) {
            for (Team t : teams) {
                for (Player p : t.getPlayers()) {
                    pw.println(t.getTeamId() + "|" + p.getPlayerId());
                }
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving team-players: " + e.getMessage());
        }
    }

    /** Save league–team relationships */
    public static void saveLeagueTeams(List<League> leagues) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LEAGUE_TEAMS_FILE))) {
            for (League l : leagues) {
                for (Team t : l.getTeams()) {
                    pw.println(l.getLeagueId() + "|" + t.getTeamId());
                }
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving league-teams: " + e.getMessage());
        }
    }

    // ================================================================== LOAD

    /** Load players from players.txt. Returns empty list if file not found. */
    public static List<Player> loadPlayers() {
        List<Player> list = new ArrayList<>();
        File file = new File(PLAYERS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 8) continue;
                // parts[0] is the saved id — used only as reference
                String name        = parts[1];
                int    age         = Integer.parseInt(parts[2]);
                double height      = Double.parseDouble(parts[3]);
                String nationality = parts[4];
                String position    = parts[5];
                int    goals       = Integer.parseInt(parts[6]);
                int    assists     = Integer.parseInt(parts[7]);
                list.add(new Player(name, age, height, nationality, position, goals, assists));
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error loading players: " + e.getMessage());
        }
        return list;
    }

    /** Load teams from teams.txt. Returns empty list if file not found. */
    public static List<Team> loadTeams() {
        List<Team> list = new ArrayList<>();
        File file = new File(TEAMS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue;
                String name   = parts[1];
                String city   = parts[2];
                int    wins   = Integer.parseInt(parts[3]);
                int    losses = Integer.parseInt(parts[4]);
                int    draws  = Integer.parseInt(parts[5]);
                list.add(new Team(name, city, wins, losses, draws));
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error loading teams: " + e.getMessage());
        }
        return list;
    }

    /** Load leagues from leagues.txt. Returns empty list if file not found. */
    public static List<League> loadLeagues() {
        List<League> list = new ArrayList<>();
        File file = new File(LEAGUES_FILE);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                String name   = parts[1];
                String season = parts[2];
                list.add(new League(name, season));
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error loading leagues: " + e.getMessage());
        }
        return list;
    }

    /**
     * Re-link players to teams after loading.
     * Reads team_players.txt and calls team.addPlayer(player).
     */
    public static void loadTeamPlayers(TeamService teamService, PlayerService playerService) {
        File file = new File(TEAM_PLAYERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                int    teamId   = Integer.parseInt(parts[0]);
                int    playerId = Integer.parseInt(parts[1]);
                Team   t        = teamService.findById(teamId);
                Player p        = playerService.findById(playerId);
                if (t != null && p != null) t.addPlayer(p);
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error loading team-players: " + e.getMessage());
        }
    }

    /**
     * Re-link teams to leagues after loading.
     * Reads league_teams.txt and calls league.addTeam(team).
     */
    public static void loadLeagueTeams(LeagueService leagueService, TeamService teamService) {
        File file = new File(LEAGUE_TEAMS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                int    leagueId = Integer.parseInt(parts[0]);
                int    teamId   = Integer.parseInt(parts[1]);
                League l        = leagueService.findById(leagueId);
                Team   t        = teamService.findById(teamId);
                if (l != null && t != null) l.addTeam(t);
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error loading league-teams: " + e.getMessage());
        }
    }
}