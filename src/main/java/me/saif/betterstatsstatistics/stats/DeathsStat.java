package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathsStat extends Stat implements Listener {

    @Override
    public String getName() {
        return "Deaths";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        return String.valueOf(((int) value));
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
    private void onDie(PlayerDeathEvent event) {
        BetterStats.getAPI().getPlayerStats(event.getEntity()).addToStat(this, 1);
    }
}
