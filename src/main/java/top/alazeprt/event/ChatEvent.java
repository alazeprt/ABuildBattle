package top.alazeprt.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import top.alazeprt.game.GameThread;

import java.util.ArrayList;
import java.util.List;

public class ChatEvent implements Listener {

    public static List<String> correctPlayers = new ArrayList<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.getPlayer().getName().equals(GameThread.builder)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot send message when you're building!");
            return;
        }
        if(GameThread.running) {
            if(correctPlayers.contains(event.getPlayer().getName())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot send message after you guessed it out!");
            } else if(event.getMessage().equals(GameThread.theme)) {
                event.setCancelled(true);
                Bukkit.broadcastMessage(ChatColor.GREEN + event.getPlayer().getName() + " guessed the correct theme!");
                event.getPlayer().sendTitle(ChatColor.AQUA + "Correct!", ChatColor.YELLOW + "You guessed it out!");
                event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
                correctPlayers.add(event.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(event.getPlayer().getName().equals(GameThread.builder)) return;
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }
}
