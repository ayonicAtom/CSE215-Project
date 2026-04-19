import java.util.ArrayList;
import java.util.List;

public class League {

    public static int leagueCount = 0;
    public int leagueId;
    private String name;
    private String season;
    private ArrayList<Team> teams;
    private int maxTeams = 20;

    public League(String name, String season) {
        this.name = name;
        this.season = season;
        this.teams = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (teams.size() < maxTeams) {
            teams.add(team);
        } else {
            System.out.println("League is full! Max teams: " + maxTeams);
        }
    }

    public int getLeagueId()      { return leagueId; }
    public String getName()       { return name; }
    public String getSeason()     { return season; }
    public List<Team> getTeams()  { return teams; }
    public int getMaxTeams()      { return maxTeams; }

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
