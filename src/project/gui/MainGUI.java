package project.gui;

import project.model.Coach;
import project.model.League;
import project.model.Player;
import project.model.Team;
import project.service.CoachService;
import project.service.LeagueService;
import project.service.PlayerService;
import project.service.TeamService;
import project.util.FileManager;
import project.util.Stats;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MainGUI — Swing front-end for the League Management project.
 *
 * IMPORTANT: This class does NOT modify any existing source files.
 * It creates its own instances of the three service classes, loads
 * saved data via FileManager on startup, and saves on every change.
 *
 * To launch the GUI instead of the console app, run MainGUI.main().
 */
public class MainGUI extends JFrame {

    // ---------------------------------------------------------------- services
    private final PlayerService  playerService  = new PlayerService();
    private final TeamService    teamService    = new TeamService();
    private final LeagueService  leagueService  = new LeagueService();
    private final CoachService   coachService   = new CoachService();

    // Helper lists (mirrors of what's in the services) so we can pass to FileManager
    private final List<Player> playerList  = new ArrayList<>();
    private final List<Team>   teamList    = new ArrayList<>();
    private final List<League> leagueList  = new ArrayList<>();
    private final List<Coach>  coachList   = new ArrayList<>();

    // ---------------------------------------------------------------- output area
    private final JTextArea outputArea = new JTextArea(20, 60);

    // ================================================================ MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // --- Password dialog on startup ---
            JPasswordField pwField = new JPasswordField(20);
            int result = JOptionPane.showConfirmDialog(
                    null,
                    new Object[]{"Create / Enter Password:", pwField},
                    "Login",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                System.exit(0);
            }

            String password = new String(pwField.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty.");
                System.exit(0);
            }

            // Second prompt — verify password
            JPasswordField confirmField = new JPasswordField(20);
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    new Object[]{"Confirm Password:", confirmField},
                    "Login",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (confirm != JOptionPane.OK_OPTION) System.exit(0);

            if (!password.equals(new String(confirmField.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Passwords do not match. Exiting.");
                System.exit(0);
            }

            new MainGUI().setVisible(true);
        });
    }

    // ================================================================ CONSTRUCTOR
    public MainGUI() {
        super("League Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        loadFromFiles();  // load saved data on startup
        buildUI();
    }

    // ================================================================ UI BUILD
    private void buildUI() {
        setLayout(new BorderLayout(5, 5));

        // ---------- output panel (right side) ----------
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(new TitledBorder("Output"));

        // ---------- button panel (left side) ----------
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Groups
        btnPanel.add(sectionLabel("--- Create ---"));
        btnPanel.add(makeBtn("Create Player",       this::createPlayer));
        btnPanel.add(makeBtn("Create Team",         this::createTeam));
        btnPanel.add(makeBtn("Create League",       this::createLeague));
        btnPanel.add(makeBtn("Create Coach",        this::createCoach));

        btnPanel.add(sectionLabel("--- View ---"));
        btnPanel.add(makeBtn("Show Players",        this::showPlayers));
        btnPanel.add(makeBtn("Show Teams",          this::showTeams));
        btnPanel.add(makeBtn("Show Leagues",        this::showLeagues));
        btnPanel.add(makeBtn("Show Coaches",        this::showCoaches));

        btnPanel.add(sectionLabel("--- Link ---"));
        btnPanel.add(makeBtn("Add Player to Team",  this::addPlayerToTeam));
        btnPanel.add(makeBtn("Add Team to League",  this::addTeamToLeague));
        btnPanel.add(makeBtn("Assign Coach to Team", this::assignCoachToTeam));

        btnPanel.add(sectionLabel("--- Unlink ---"));
        btnPanel.add(makeBtn("Remove Player from Team",  this::removePlayerFromTeam));
        btnPanel.add(makeBtn("Remove Team from League",  this::removeTeamFromLeague));
        btnPanel.add(makeBtn("Remove Coach from Team",   this::removeCoachFromTeam));

        btnPanel.add(sectionLabel("--- Edit ---"));
        btnPanel.add(makeBtn("Edit Player",         this::editPlayer));
        btnPanel.add(makeBtn("Edit Team",           this::editTeam));
        btnPanel.add(makeBtn("Edit League",         this::editLeague));
        btnPanel.add(makeBtn("Edit Coach",          this::editCoach));

        btnPanel.add(sectionLabel("--- Delete ---"));
        btnPanel.add(makeBtn("Delete Player",       this::deletePlayer));
        btnPanel.add(makeBtn("Delete Team",         this::deleteTeam));
        btnPanel.add(makeBtn("Delete League",       this::deleteLeague));
        btnPanel.add(makeBtn("Delete Coach",        this::deleteCoach));

        btnPanel.add(sectionLabel("--- Stats ---"));
        btnPanel.add(makeBtn("Compare Players",     this::comparePlayers));
        btnPanel.add(makeBtn("Compare Teams",       this::compareTeams));
        btnPanel.add(makeBtn("Compare Leagues",     this::compareLeagues));

        btnPanel.add(Box.createVerticalGlue());

        JScrollPane btnScroll = new JScrollPane(btnPanel);
        btnScroll.setPreferredSize(new Dimension(220, 0));

        add(btnScroll, BorderLayout.WEST);
        add(scroll,    BorderLayout.CENTER);

        // ---------- bottom bar ----------
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton clearBtn = new JButton("Clear Output");
        clearBtn.addActionListener(e -> outputArea.setText(""));
        JButton saveBtn = new JButton("Save to Files");
        saveBtn.addActionListener(e -> { saveToFiles(); print("[Saved] All data saved to txt files."); });
        bottom.add(clearBtn);
        bottom.add(saveBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // helper — uniform button
    private JButton makeBtn(String label, Runnable action) {
        JButton btn = new JButton(label);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // helper — section label
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ================================================================ OUTPUT
    private void print(String msg) {
        outputArea.append(msg + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    /** Redirects System.out to the output area for one operation. */
    private void captureOutput(Runnable op) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            op.run();
        } finally {
            System.setOut(oldOut);
        }
        print(baos.toString());
    }

    // ================================================================ FILE I/O
    private void loadFromFiles() {
        // Load in order: players → coaches → teams → leagues → relations
        List<Player> loadedPlayers = FileManager.loadPlayers();
        for (Player p : loadedPlayers) {
            playerService.add(p);
            playerList.add(p);
        }

        List<Coach> loadedCoaches = FileManager.loadCoaches();
        for (Coach c : loadedCoaches) {
            coachService.add(c);
            coachList.add(c);
        }

        List<Team> loadedTeams = FileManager.loadTeams();
        for (Team t : loadedTeams) {
            teamService.add(t);
            teamList.add(t);
        }

        List<League> loadedLeagues = FileManager.loadLeagues();
        for (League l : loadedLeagues) {
            leagueService.add(l);
            leagueList.add(l);
        }

        FileManager.loadTeamPlayers(teamService, playerService);
        FileManager.loadTeamCoach(teamService, coachService);
        FileManager.loadLeagueTeams(leagueService, teamService);

        System.out.println("[FileManager] Loaded: "
                + playerList.size()  + " players, "
                + coachList.size()   + " coaches, "
                + teamList.size()    + " teams, "
                + leagueList.size()  + " leagues.");
    }

    private void saveToFiles() {
        FileManager.savePlayers(playerList);
        FileManager.saveCoaches(coachList);
        FileManager.saveTeams(teamList);
        FileManager.saveLeagues(leagueList);
        FileManager.saveTeamPlayers(teamList);
        FileManager.saveTeamCoach(teamList);
        FileManager.saveLeagueTeams(leagueList);
    }

    // ================================================================ CREATE

    private void createPlayer() {
        JTextField nameField        = new JTextField();
        JTextField ageField         = new JTextField();
        JTextField heightField      = new JTextField();
        JTextField nationalityField = new JTextField();
        JTextField positionField    = new JTextField();
        JTextField goalsField       = new JTextField();
        JTextField assistsField     = new JTextField();

        Object[] fields = {
                "Name:",        nameField,
                "Age:",         ageField,
                "Height (cm):", heightField,
                "Nationality:", nationalityField,
                "Position:",    positionField,
                "Goals:",       goalsField,
                "Assists:",     assistsField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Create Player", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            String name        = nameField.getText().trim();
            int    age         = Integer.parseInt(ageField.getText().trim());
            double height      = Double.parseDouble(heightField.getText().trim());
            String nationality = nationalityField.getText().trim();
            String position    = positionField.getText().trim();
            int    goals       = Integer.parseInt(goalsField.getText().trim());
            int    assists     = Integer.parseInt(assistsField.getText().trim());

            Player p = new Player(name, age, height, nationality, position, goals, assists);
            playerService.add(p);
            playerList.add(p);
            captureOutput(p::displayInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    private void createTeam() {
        JTextField nameField   = new JTextField();
        JTextField cityField   = new JTextField();
        JTextField winsField   = new JTextField();
        JTextField lossesField = new JTextField();
        JTextField drawsField  = new JTextField();

        Object[] fields = {
                "Club Name:", nameField,
                "City:",      cityField,
                "Wins:",      winsField,
                "Losses:",    lossesField,
                "Draws:",     drawsField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Create Team", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            String name   = nameField.getText().trim();
            String city   = cityField.getText().trim();
            int    wins   = Integer.parseInt(winsField.getText().trim());
            int    losses = Integer.parseInt(lossesField.getText().trim());
            int    draws  = Integer.parseInt(drawsField.getText().trim());

            Team t = new Team(name, city, wins, losses, draws);
            teamService.add(t);
            teamList.add(t);
            captureOutput(t::displayTeamInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    private void createLeague() {
        JTextField nameField   = new JTextField();
        JTextField seasonField = new JTextField();

        Object[] fields = {
                "League Name:", nameField,
                "Season:",      seasonField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Create League", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String name   = nameField.getText().trim();
        String season = seasonField.getText().trim();

        League l = new League(name, season);
        leagueService.add(l);
        leagueList.add(l);
        captureOutput(l::displayLeagueInfo);
        saveToFiles();
    }

    private void createCoach() {
        JTextField nameField        = new JTextField();
        JTextField ageField         = new JTextField();
        JTextField nationalityField = new JTextField();
        JTextField specialityField  = new JTextField();

        Object[] fields = {
                "Name:",        nameField,
                "Age:",         ageField,
                "Nationality:", nationalityField,
                "Speciality:",  specialityField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Create Coach", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            String name        = nameField.getText().trim();
            int    age         = Integer.parseInt(ageField.getText().trim());
            String nationality = nationalityField.getText().trim();
            String speciality  = specialityField.getText().trim();

            Coach c = new Coach(name, age, nationality, speciality);
            coachService.add(c);
            coachList.add(c);
            captureOutput(c::displayInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    // ================================================================ SHOW

    private void showPlayers() { captureOutput(playerService::showPlayers); }
    private void showTeams()   { captureOutput(teamService::showTeams); }
    private void showLeagues() { captureOutput(leagueService::showLeagues); }
    private void showCoaches() { captureOutput(coachService::showCoaches); }

    // ================================================================ DELETE

    private void deletePlayer() {
        if (playerList.isEmpty()) { print("No players exist."); return; }
        Player p = pickPlayer("Select Player to Delete");
        if (p == null) return;
        playerService.delete(p.getPlayerId());
        playerList.remove(p);
        // also remove from any team
        for (Team t : teamList) t.removePlayer(p);
        print("Player '" + p.getName() + "' deleted.");
        saveToFiles();
    }

    private void deleteTeam() {
        if (teamList.isEmpty()) { print("No teams exist."); return; }
        Team t = pickTeam("Select Team to Delete");
        if (t == null) return;
        teamService.delete(t.getTeamId());
        teamList.remove(t);
        // also remove from any league
        for (League l : leagueList) l.removeTeam(t);
        print("Team '" + t.getName() + "' deleted.");
        saveToFiles();
    }

    private void deleteLeague() {
        if (leagueList.isEmpty()) { print("No leagues exist."); return; }
        League l = pickLeague("Select League to Delete");
        if (l == null) return;
        leagueService.delete(l.getLeagueId());
        leagueList.remove(l);
        print("League '" + l.getName() + "' deleted.");
        saveToFiles();
    }

    private void deleteCoach() {
        if (coachList.isEmpty()) { print("No coaches exist."); return; }
        Coach c = pickCoach("Select Coach to Delete");
        if (c == null) return;
        // also remove from any team that has this coach assigned
        for (Team t : teamList) {
            if (t.getCoach() != null && t.getCoach().getCoachId() == c.getCoachId()) {
                t.removeCoach();
            }
        }
        coachService.delete(c.getCoachId());
        coachList.remove(c);
        print("Coach '" + c.getName() + "' deleted.");
        saveToFiles();
    }

    // ================================================================ LINK

    private void addPlayerToTeam() {
        if (playerList.isEmpty()) { print("No players exist."); return; }
        if (teamList.isEmpty())   { print("No teams exist.");   return; }

        Player p = pickPlayer("Select Player to Add");
        if (p == null) return;
        Team t = pickTeam("Select Team");
        if (t == null) return;

        if (t.getPlayers().contains(p)) {
            print("Player already in " + t.getName());
            captureOutput(t::displayTeamInfo);
            return;
        }
        t.addPlayer(p);
        print(p.getName() + " added to " + t.getName());
        captureOutput(t::displayTeamInfo);
        saveToFiles();
    }

    private void addTeamToLeague() {
        if (teamList.isEmpty())   { print("No teams exist.");   return; }
        if (leagueList.isEmpty()) { print("No leagues exist."); return; }

        Team t = pickTeam("Select Team to Add");
        if (t == null) return;
        League l = pickLeague("Select League");
        if (l == null) return;

        if (l.getTeams().contains(t)) {
            print("Team already in " + l.getName());
            captureOutput(l::displayLeagueInfo);
            return;
        }
        l.addTeam(t);
        print(t.getName() + " added to " + l.getName());
        captureOutput(l::displayLeagueInfo);
        saveToFiles();
    }

    private void assignCoachToTeam() {
        if (coachList.isEmpty()) { print("No coaches exist."); return; }
        if (teamList.isEmpty())  { print("No teams exist.");   return; }

        Team t = pickTeam("Select Team");
        if (t == null) return;
        Coach c = pickCoach("Select Coach to Assign");
        if (c == null) return;

        if (t.getCoach() != null && t.getCoach().getCoachId() == c.getCoachId()) {
            print(c.getName() + " is already the coach of " + t.getName());
            return;
        }
        t.addCoach(c);
        print(c.getName() + " assigned as coach of " + t.getName());
        captureOutput(t::displayTeamInfo);
        saveToFiles();
    }

    // ================================================================ UNLINK

    private void removePlayerFromTeam() {
        if (teamList.isEmpty()) { print("No teams exist."); return; }
        Team t = pickTeam("Select Team");
        if (t == null) return;
        if (t.getPlayers().isEmpty()) { print(t.getName() + " has no players."); return; }

        Player p = pickPlayerFromList("Select Player to Remove", t.getPlayers());
        if (p == null) return;

        t.removePlayer(p);
        print(p.getName() + " removed from " + t.getName());
        captureOutput(t::displayTeamInfo);
        saveToFiles();
    }

    private void removeTeamFromLeague() {
        if (leagueList.isEmpty()) { print("No leagues exist."); return; }
        League l = pickLeague("Select League");
        if (l == null) return;
        if (l.getTeams().isEmpty()) { print(l.getName() + " has no teams."); return; }

        Team t = pickTeamFromList("Select Team to Remove", new ArrayList<>(l.getTeams()));
        if (t == null) return;

        captureOutput(() -> l.removeTeam(t));
        captureOutput(l::displayLeagueInfo);
        saveToFiles();
    }

    private void removeCoachFromTeam() {
        if (teamList.isEmpty()) { print("No teams exist."); return; }
        Team t = pickTeam("Select Team");
        if (t == null) return;
        if (t.getCoach() == null) { print(t.getName() + " has no coach assigned."); return; }

        String coachName = t.getCoach().getName();
        t.removeCoach();
        print(coachName + " removed from " + t.getName());
        captureOutput(t::displayTeamInfo);
        saveToFiles();
    }

    // ================================================================ EDIT

    private void editPlayer() {
        if (playerList.isEmpty()) { print("No players exist."); return; }
        Player p = pickPlayer("Select Player to Edit");
        if (p == null) return;

        JTextField nameField        = new JTextField(p.getName());
        JTextField ageField         = new JTextField(String.valueOf(p.getAge()));
        JTextField heightField      = new JTextField(String.valueOf(p.getHeight()));
        JTextField nationalityField = new JTextField(p.getNationality());
        JTextField positionField    = new JTextField(p.getPosition());
        JTextField goalsField       = new JTextField(String.valueOf(p.getGoals()));
        JTextField assistsField     = new JTextField(String.valueOf(p.getAssists()));

        Object[] fields = {
                "Name:",        nameField,
                "Age:",         ageField,
                "Height (cm):", heightField,
                "Nationality:", nationalityField,
                "Position:",    positionField,
                "Goals:",       goalsField,
                "Assists:",     assistsField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Edit Player - " + p.getName(), JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            if (!nameField.getText().trim().isEmpty())        p.setName(nameField.getText().trim());
            if (!ageField.getText().trim().isEmpty())         p.setAge(Integer.parseInt(ageField.getText().trim()));
            if (!heightField.getText().trim().isEmpty())      p.setHeight(Double.parseDouble(heightField.getText().trim()));
            if (!nationalityField.getText().trim().isEmpty()) p.setNationality(nationalityField.getText().trim());
            if (!positionField.getText().trim().isEmpty())    p.setPosition(positionField.getText().trim());
            if (!goalsField.getText().trim().isEmpty())       p.setGoals(Integer.parseInt(goalsField.getText().trim()));
            if (!assistsField.getText().trim().isEmpty())     p.setAssists(Integer.parseInt(assistsField.getText().trim()));

            print("Player updated.");
            captureOutput(p::displayInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    private void editTeam() {
        if (teamList.isEmpty()) { print("No teams exist."); return; }
        Team t = pickTeam("Select Team to Edit");
        if (t == null) return;

        JTextField nameField   = new JTextField(t.getName());
        JTextField cityField   = new JTextField(t.getCity());
        JTextField winsField   = new JTextField(String.valueOf(t.getWins()));
        JTextField lossesField = new JTextField(String.valueOf(t.getLosses()));
        JTextField drawsField  = new JTextField(String.valueOf(t.getDraws()));

        Object[] fields = {
                "Name:",   nameField,
                "City:",   cityField,
                "Wins:",   winsField,
                "Losses:", lossesField,
                "Draws:",  drawsField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Edit Team - " + t.getName(), JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            if (!nameField.getText().trim().isEmpty())   t.setName(nameField.getText().trim());
            if (!cityField.getText().trim().isEmpty())   t.setCity(cityField.getText().trim());
            if (!winsField.getText().trim().isEmpty())   t.setWins(Integer.parseInt(winsField.getText().trim()));
            if (!lossesField.getText().trim().isEmpty()) t.setLosses(Integer.parseInt(lossesField.getText().trim()));
            if (!drawsField.getText().trim().isEmpty())  t.setDraws(Integer.parseInt(drawsField.getText().trim()));

            print("Team updated.");
            captureOutput(t::displayTeamInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    private void editLeague() {
        if (leagueList.isEmpty()) { print("No leagues exist."); return; }
        League l = pickLeague("Select League to Edit");
        if (l == null) return;

        JTextField nameField   = new JTextField(l.getName());
        JTextField seasonField = new JTextField(l.getSeason());

        Object[] fields = {
                "Name:",   nameField,
                "Season:", seasonField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Edit League - " + l.getName(), JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        if (!nameField.getText().trim().isEmpty())   l.setName(nameField.getText().trim());
        if (!seasonField.getText().trim().isEmpty()) l.setSeason(seasonField.getText().trim());

        print("League updated.");
        captureOutput(l::displayLeagueInfo);
        saveToFiles();
    }

    private void editCoach() {
        if (coachList.isEmpty()) { print("No coaches exist."); return; }
        Coach c = pickCoach("Select Coach to Edit");
        if (c == null) return;

        JTextField nameField        = new JTextField(c.getName());
        JTextField ageField         = new JTextField(String.valueOf(c.getAge()));
        JTextField nationalityField = new JTextField(c.getNationality());
        JTextField specialityField  = new JTextField(c.getSpeciality());

        Object[] fields = {
                "Name:",        nameField,
                "Age:",         ageField,
                "Nationality:", nationalityField,
                "Speciality:",  specialityField
        };

        int res = JOptionPane.showConfirmDialog(this, fields, "Edit Coach - " + c.getName(), JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            if (!nameField.getText().trim().isEmpty())        c.setName(nameField.getText().trim());
            if (!ageField.getText().trim().isEmpty())         c.setAge(Integer.parseInt(ageField.getText().trim()));
            if (!nationalityField.getText().trim().isEmpty()) c.setNationality(nationalityField.getText().trim());
            if (!specialityField.getText().trim().isEmpty())  c.setSpeciality(specialityField.getText().trim());

            print("Coach updated.");
            captureOutput(c::displayInfo);
            saveToFiles();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    // ================================================================ STATS

    private void comparePlayers() {
        if (playerList.size() < 2) { print("Need at least 2 players."); return; }
        Player p1 = pickPlayer("Select Player 1");
        if (p1 == null) return;
        Player p2 = pickPlayer("Select Player 2");
        if (p2 == null) return;
        captureOutput(() -> Stats.comparePlayer(p1, p2));
    }

    private void compareTeams() {
        if (teamList.size() < 2) { print("Need at least 2 teams."); return; }
        Team t1 = pickTeam("Select Team 1");
        if (t1 == null) return;
        Team t2 = pickTeam("Select Team 2");
        if (t2 == null) return;
        captureOutput(() -> Stats.compareTeams(t1, t2));
    }

    private void compareLeagues() {
        if (leagueList.size() < 2) { print("Need at least 2 leagues."); return; }
        League l1 = pickLeague("Select League 1");
        if (l1 == null) return;
        League l2 = pickLeague("Select League 2");
        if (l2 == null) return;
        captureOutput(() -> Stats.compareLeagues(l1, l2));
    }

    // ================================================================ PICKERS (JOptionPane dropdown helpers)

    private Player pickPlayer(String title) {
        return pickPlayerFromList(title, playerList);
    }

    private Player pickPlayerFromList(String title, List<Player> list) {
        if (list.isEmpty()) return null;
        String[] options = list.stream()
                .map(p -> "[" + p.getPlayerId() + "] " + p.getName())
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(
                this, title, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return null;
        int id = Integer.parseInt(choice.substring(1, choice.indexOf(']')));
        return playerService.findById(id);
    }

    private Team pickTeam(String title) {
        return pickTeamFromList(title, teamList);
    }

    private Team pickTeamFromList(String title, List<Team> list) {
        if (list.isEmpty()) return null;
        String[] options = list.stream()
                .map(t -> "[" + t.getTeamId() + "] " + t.getName())
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(
                this, title, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return null;
        int id = Integer.parseInt(choice.substring(1, choice.indexOf(']')));
        return teamService.findById(id);
    }

    private League pickLeague(String title) {
        if (leagueList.isEmpty()) return null;
        String[] options = leagueList.stream()
                .map(l -> "[" + l.getLeagueId() + "] " + l.getName())
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(
                this, title, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return null;
        int id = Integer.parseInt(choice.substring(1, choice.indexOf(']')));
        return leagueService.findById(id);
    }

    private Coach pickCoach(String title) {
        if (coachList.isEmpty()) return null;
        String[] options = coachList.stream()
                .map(c -> "[" + c.getCoachId() + "] " + c.getName())
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(
                this, title, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return null;
        int id = Integer.parseInt(choice.substring(1, choice.indexOf(']')));
        return coachService.findById(id);
    }
}