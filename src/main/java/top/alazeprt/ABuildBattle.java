package top.alazeprt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import top.alazeprt.command.AdminCommand;
import top.alazeprt.event.BuildEvent;
import top.alazeprt.event.ChatEvent;
import top.alazeprt.game.GameThread;

import java.io.File;
import java.util.List;

public class ABuildBattle extends JavaPlugin {

    public static Location pos1;

    public static Location pos2;

    public static Location place;

    public static List<String> contents;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()) saveResource("config.yml", false);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        pos1 = new Location(Bukkit.getWorld(configuration.getString("place.world")),
                configuration.getInt("place.teleport.x") - configuration.getInt("place.size"),
                configuration.getInt("place.bottom_y"),
                configuration.getInt("place.teleport.z") - configuration.getInt("place.size"));
        pos2 = new Location(Bukkit.getWorld(configuration.getString("place.world")),
                configuration.getInt("place.teleport.x") + configuration.getInt("place.size"),
                configuration.getInt("place.bottom_y") + configuration.getInt("place.height"),
                configuration.getInt("place.teleport.z") + configuration.getInt("place.size"));
        place = new Location(Bukkit.getWorld(configuration.getString("place.world")),
                configuration.getInt("place.teleport.x"),
                configuration.getInt("place.teleport.y"),
                configuration.getInt("place.teleport.z"));
        contents = configuration.getStringList("contents");
        getCommand("buildbattlea").setExecutor(new AdminCommand());
        getServer().getPluginManager().registerEvents(new BuildEvent(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
    }

    @Override
    public void onDisable() {
        if(GameThread.thread != null && !GameThread.thread.isInterrupted()) {
            GameThread.thread.interrupt();
        }
    }
}
