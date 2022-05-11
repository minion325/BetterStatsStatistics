package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class GapplesEatenStat extends Stat implements Listener {
    @Override
    public String getName() {
        return "Golden Apples Eaten";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        return ((int) value) + " Golden apples";
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onEat(PlayerItemConsumeEvent event) {
        if (event.isCancelled())
            return;
        if (event.getItem().getType() == Material.GOLDEN_APPLE || event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            BetterStats.getAPI().getPlayerStats(event.getPlayer()).addToStat(this, 1);
        }
    }
}
