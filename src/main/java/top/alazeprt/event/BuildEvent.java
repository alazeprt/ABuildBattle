package top.alazeprt.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

import static top.alazeprt.ABuildBattle.pos1;
import static top.alazeprt.ABuildBattle.pos2;
import static top.alazeprt.game.GameThread.builder;

public class BuildEvent implements Listener {

    public static List<Location> locations = new ArrayList<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!event.getPlayer().getName().equals(builder)) {
            event.setCancelled(true);
            return;
        }
        Location location = event.getBlock().getLocation();
        if(pos1.getBlockX() > location.getBlockX() || location.getBlockX() > pos2.getBlockX()
        || pos1.getBlockZ() > location.getBlockZ() || location.getBlockZ() > pos2.getBlockZ()
        || pos1.getBlockY() > location.getBlockY() || location.getBlockY() > pos2.getBlockY()) {
            event.setCancelled(true);
            return;
        }
        locations.add(event.getBlock().getLocation());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!event.getPlayer().getName().equals(builder)) {
            event.setCancelled(true);
            return;
        }
        Location location = event.getBlock().getLocation();
        if(!locations.contains(location)) {
            event.setCancelled(true);
            return;
        }
        locations.remove(location);
    }
}
