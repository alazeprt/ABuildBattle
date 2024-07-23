package top.alazeprt.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.alazeprt.game.GameThread;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!commandSender.hasPermission("abuildbattle.admin")) {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to use this top.alazeprt.command");
            return true;
        }
        if(strings.length != 1) {
            commandSender.sendMessage("Usage: /buildbattlea <time_per_builder>");
            return true;
        }
        long time = Long.parseLong(strings[0]);
        List<Player> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> list.add(player));
        GameThread.start(list, time);
        return false;
    }
}
