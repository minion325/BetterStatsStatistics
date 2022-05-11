package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillsStat extends Stat implements Listener {

    @Override
    public String getName() {
        return "Players Killed";
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

    @EventHandler
    private void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null)
            BetterStats.getAPI().getPlayerStats(event.getEntity().getKiller()).addToStat(this, 1);
    }
}
