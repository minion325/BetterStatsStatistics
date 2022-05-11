package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlocksBrokenStat extends Stat implements Listener {

    @Override
    public String getName() {
        return "Blocks Broken";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        return ((int) value) + " Blocks";
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
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        BetterStats.getAPI().getPlayerStats(event.getPlayer()).addToStat(this, 1);
    }

}
