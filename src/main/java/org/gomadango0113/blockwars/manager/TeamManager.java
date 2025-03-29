package org.gomadango0113.blockwars.manager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TeamManager {

    private static final PluginManager plm = Bukkit.getPluginManager();
    private static final Scoreboard board = ScoreboardManager.getScoreboard();

    private static int team_size = 2;
    private static Map<GameTeam, Team> team_map = new HashMap<>();
    private static Team red_team = board.getTeam("red");
    private static Team blue_team = board.getTeam("blue");
    private static Team green_team = board.getTeam("green");
    private static Team yellow_team = board.getTeam("yellow");
    private static Team admin_team = board.getTeam("admin");

    private static List<GameTeam> dead_team_list = new ArrayList<>();

    static {
        createTeam();

        team_map.put(GameTeam.RED, red_team);
        team_map.put(GameTeam.BLUE, blue_team);
        team_map.put(GameTeam.GREEN, green_team);
        team_map.put(GameTeam.YELLOW, yellow_team);
        team_map.put(GameTeam.ADMIN, admin_team);
    }

    /**
     * チームを作成する。
     */
    public static void createTeam() {
        if (red_team == null || blue_team == null || green_team == null || yellow_team == null) {
            red_team = board.registerNewTeam("red");
            blue_team = board.registerNewTeam("blue");
            green_team = board.registerNewTeam("green");
            yellow_team = board.registerNewTeam("yellow");
            admin_team = board.registerNewTeam("admin");

            NameTagVisibility hide_nametag = NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            for (Team team : new Team[]{red_team, blue_team, green_team, yellow_team}) {
                team.setNameTagVisibility(hide_nametag);
            }

            red_team.setPrefix(ChatColor.RED + "");
            blue_team.setPrefix(ChatColor.BLUE + "");
            green_team.setPrefix(ChatColor.GREEN + "");
            yellow_team.setPrefix(ChatColor.YELLOW + "");
            admin_team.setPrefix(ChatColor.GOLD + "");

            Bukkit.getLogger().info("[TeamManager] チームを作成しました。");
        }
    }

    /**
     * プレイヤーを特定のチームに参加させる
     * @param team　参加させたいチーム
     */
    public static boolean joinTeam(GameTeam team, OfflinePlayer player) {
        if (getJoinTeam(player) == GameTeam.UNKNOWN) {
            Team board_team = team_map.get(team);
            board_team.addPlayer(player);

            return true;
        }

        return false;
    }

    /**
     * プレイヤーをランダムのチームに参加させる
     * @return 参加したチーム。（参加している場合は、そのチーム）
     */
    public static GameTeam joinTeam(Player player) {
        GameTeam join_team = getJoinTeam(player);
        if (join_team == GameTeam.UNKNOWN) {
            Map<GameTeam, Integer> team_size = new HashMap<>();
            for (GameTeam team : TeamManager.getActiveTeam()) {
                team_size.put(team, getTeamPlayers(team).size());

                for (GameTeam dead : TeamManager.getDeadTeamList()) {
                    team_size.remove(dead);
                }
            }

            Integer min_team_value = Collections.min(team_size.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
            for (Map.Entry<GameTeam, Integer> entry : team_size.entrySet()) {
                if (entry.getValue() == 0) {
                    GameTeam team = entry.getKey();
                    getTeam(team).addPlayer(player);
                    return team;
                }
            }

            for (Map.Entry<GameTeam, Integer> entry : team_size.entrySet()) {
                if (entry.getValue() != 0) {
                    if (min_team_value == entry.getValue().intValue()) {
                        GameTeam team = entry.getKey();
                        getTeam(entry.getKey()).addPlayer(player);
                        return team;
                    }
                    else {
                        List<GameTeam> game_team = new ArrayList<>(TeamManager.getActiveTeam());
                        Collections.shuffle(game_team);
                        GameTeam team = game_team.get(0);
                        getTeam(team).addPlayer(player);
                        return team;
                    }
                }
            }
        }

        return join_team;
    }

    /**
     * プレイヤーをチームから離脱させる
     */
    public static void leaveTeam(OfflinePlayer player) {
        GameTeam join_team = getJoinTeam(player);
        Team board_team = team_map.get(join_team);
        board_team.removePlayer(player);
    }

    /**
     * チームを空する。
     * @param team 空にしたいチーム。nullの場合は全チームをリセット
     */
    public static void teamEmpty(GameTeam team) {
        if (team == null) {
            for (Team board_team : team_map.values()) {
                for (OfflinePlayer player : board_team.getPlayers()) {
                    board_team.removePlayer(player);
                }
            }
        }
        else {
            Team board_team = getTeam(team);
            for (OfflinePlayer player : board_team.getPlayers()) {
                board_team.removePlayer(player);
            }
        }
    }

    /**
     * @return GameTeamから、スコアボードのチームを返す。
     */
    public static Team getTeam(GameTeam team) {
        return team_map.get(team);
    }

    /**
     * @return チームに所属しているプレイヤーセットを返す
     */
    public static Set<OfflinePlayer> getTeamPlayers(GameTeam team) {
        return team_map.get(team).getPlayers();
    }

    /**
     * @return プレイヤー所属しているチームを返す
     */
    public static GameTeam getJoinTeam(OfflinePlayer player) {
        return Arrays.stream(GameTeam.values())
                .filter(team -> team != GameTeam.UNKNOWN)
                .filter(team -> getTeamPlayers(team).contains(player))
                .findFirst()
                .orElse(GameTeam.UNKNOWN);
    }

    public static void teamRandom() {
        TeamManager.getGameTeam().forEach(TeamManager::teamEmpty);
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers())
                .stream()
                .filter(player-> getJoinTeam(player).equals(GameTeam.UNKNOWN))
                .collect(Collectors.toList());

        Collections.shuffle(players);

        for (int i = 0; i < players.size(); i++) {
            UUID uuid = players.get(i).getUniqueId();
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            switch (i % TeamManager.getTeamSize()) {
                case 0:
                    red_team.addPlayer(player);
                    break;
                case 1:
                    blue_team.addPlayer(player);
                    break;
                case 2:
                    green_team.addPlayer(player);
                    break;
                case 3:
                    yellow_team.addPlayer(player);
                    break;
            }
        }
    }

    /**
     * @return GameTeamからTeamと結びついたMapを返す
     */
    public static Map<GameTeam, Team> getTeamToBoardTeamMap() {
        return team_map;
    }

    /**
     * @return チーム数に対する、有効なチームリスト
     */
    public static List<GameTeam> getActiveTeam() {
        if (getTeamSize() == 2) {
            return Arrays.asList(GameTeam.RED, GameTeam.BLUE);
        }
        else if (getTeamSize() == 3) {
            return Arrays.asList(GameTeam.RED, GameTeam.BLUE, GameTeam.GREEN);
        }
        else if (getTeamSize() == 4) {
            return getGameTeam();
        }
        else {
            setTeamSize(2);
            return Arrays.asList(GameTeam.RED, GameTeam.BLUE);
        }
    }

    public static List<GameTeam> getGameTeam() {
        return Arrays.asList(GameTeam.RED, GameTeam.BLUE, GameTeam.GREEN, GameTeam.YELLOW);
    }

    public static int getTeamSize() {
        return team_size;
    }

    public static void setTeamSize(int size) {
        TeamManager.team_size = size;
    }

    public static List<GameTeam> getDeadTeamList() {
        return dead_team_list;
    }

    public enum GameTeam {
        RED("赤チーム", ChatColor.RED, Color.RED, DyeColor.RED),
        BLUE("青チーム", ChatColor.BLUE, Color.BLUE, DyeColor.BLUE),
        GREEN("緑チーム", ChatColor.GREEN, Color.GREEN, DyeColor.GREEN),
        YELLOW("黄色チーム", ChatColor.YELLOW, Color.YELLOW, DyeColor.YELLOW),
        ADMIN("運営チーム", ChatColor.GOLD, Color.ORANGE, DyeColor.ORANGE),
        UNKNOWN("不明", ChatColor.WHITE, Color.WHITE, DyeColor.WHITE);

        private final ChatColor chatcolor;
        private final Color color;
        private final String team_string;
        private final DyeColor dyeColor;

        GameTeam(String team_string, ChatColor chatcolor, Color color, DyeColor dyeColor) {
            this.chatcolor = chatcolor;
            this.color = color;
            this.team_string = team_string;
            this.dyeColor = dyeColor;
        }

        public ChatColor getChatColor() {
            return chatcolor;
        }

        public Color getColor() {
            return color;
        }

        public String getTeamString(boolean color) {
            if (color) {
                return getChatColor() + team_string;
            }
            else {
                return team_string;
            }
        }

        public String toLower() {
            return this.name().toLowerCase();
        }

        public DyeColor getDyeColor() {
            return dyeColor;
        }

        public static GameTeam dyeColorToTeam(DyeColor color) {
            for (GameTeam team : values()) {
                if (team.getDyeColor() == color) {
                    return team;
                }
            }
            return null;
        }

        public static GameTeam chatColorToTeam(ChatColor color) {
            for (GameTeam team : values()) {
                if (team.getChatColor() == color) {
                    return team;
                }
            }
            return null;
        }

        public static GameTeam colorToTeam(Color color) {
            for (GameTeam team : values()) {
                if (team.getColor() == color) {
                    return team;
                }
            }
            return null;
        }
    }
}

