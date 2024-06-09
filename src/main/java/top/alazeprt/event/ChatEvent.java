package top.alazeprt.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import top.alazeprt.game.GameThread;

import java.util.ArrayList;
import java.util.List;

public class ChatEvent implements Listener {

    public static List<String> correctPlayers = new ArrayList<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.getPlayer().getName().equals(GameThread.builder)) {
            event.setCancelled(true);
            return;
        }
        if(GameThread.running) {
            if(correctPlayers.contains(event.getPlayer().getName())) {
                event.setCancelled(true);
            } else if(event.getMessage().equals(GameThread.theme)) {
                event.setCancelled(true);
                Bukkit.broadcastMessage(ChatColor.GREEN + event.getPlayer().getName() + " guessed the correct theme!");
                correctPlayers.add(event.getPlayer().getName());
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Wrong theme!");
            }
        }
    }
}
