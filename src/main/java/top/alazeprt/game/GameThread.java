package top.alazeprt.game;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import top.alazeprt.ABuildBattle;
import top.alazeprt.event.BuildEvent;
import top.alazeprt.event.ChatEvent;

import java.util.List;
import java.util.Random;

import static top.alazeprt.ABuildBattle.*;

public class GameThread {

    public static Thread thread;

    public static String theme;

    public static String builder = "";

    public static boolean running = false;

    public static void start(List<Player> playerList, long time) {
        running = true;
        thread = new Thread(() -> {
            Random random = new Random(new Random().nextInt());
            for(Player player : playerList) {
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " started to build...");
                String theme = contents.get(Math.abs(random.nextInt()) % contents.size());
                GameThread.theme = theme;
                builder = player.getName();
                Bukkit.getScheduler().runTask(ABuildBattle.getProvidingPlugin(ABuildBattle.class), () -> {
                    player.teleport(ABuildBattle.place);
                    player.setGameMode(GameMode.CREATIVE);
                    player.setAllowFlight(true);
                    player.sendTitle(ChatColor.GREEN + "Theme: " + theme,
                            ChatColor.YELLOW + "Build it in " + time + " seconds!");
                    player.sendMessage(ChatColor.GREEN + "Theme: " + theme + "\n" +
                            ChatColor.YELLOW + "Build it in " + time + " seconds!");
                    for(Player spectator : playerList) {
                        if(spectator == player) continue;
                        spectator.setGameMode(GameMode.SPECTATOR);
                        spectator.teleport(ABuildBattle.place);
                        spectator.sendTitle(ChatColor.RED + "Guess it!",
                                ChatColor.YELLOW + "You have " + time + " seconds!");
                    }
                });
                try {
                    Thread.sleep((time - 60) * 1000);
                } catch (InterruptedException e) {
                    return;
                }
                Bukkit.getScheduler().runTask(ABuildBattle.getProvidingPlugin(ABuildBattle.class), () -> Bukkit.broadcastMessage("Prompt: word count is " + theme.length()));
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    return;
                }
                player.sendTitle(ChatColor.AQUA + "Time is up!", "Correct theme: " + theme);
                Bukkit.broadcastMessage(ChatColor.AQUA + "Time is up! Correct theme: " + theme);
                Bukkit.getScheduler().runTask(ABuildBattle.getProvidingPlugin(ABuildBattle.class), () -> {
                    for(int x = Math.min(pos1.getBlockX(), pos2.getBlockX()); x <= Math.max(pos1.getBlockX(), pos2.getBlockX()); x++) {
                        for(int y = Math.min(pos1.getBlockY(), pos2.getBlockY()); y <= Math.max(pos1.getBlockY(), pos2.getBlockY()); y++) {
                            for(int z = Math.min(pos1.getBlockZ(), pos2.getBlockZ()); z <= Math.max(pos1.getBlockZ(), pos2.getBlockZ()); z++) {
                                Bukkit.getWorld("world").getBlockAt(x, y, z).setType(Material.AIR);
                            }
                        }
                    }
                    for(Entity entity : Bukkit.getWorld("world").getEntities()) {
                        if(entity instanceof Player) continue;
                        entity.remove();
                    }
                    BuildEvent.locations.clear();
                });
                ChatEvent.correctPlayers.clear();
            }
            Bukkit.broadcastMessage(ChatColor.RED + "Game is over!");
            Bukkit.getScheduler().runTask(ABuildBattle.getProvidingPlugin(ABuildBattle.class), () -> {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setAllowFlight(false);
                    player.teleport(player.getWorld().getSpawnLocation());
                    player.setHealth(20);
                    player.setFoodLevel(20);
                }
            });
            thread = null;
            theme = null;
            builder = null;
            running = false;
        });
        thread.start();
    }
}
