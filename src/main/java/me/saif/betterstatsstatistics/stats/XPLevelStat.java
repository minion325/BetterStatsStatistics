package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class XPLevelStat extends Stat implements Listener {
    @Override
    public String getName() {
        return "XP Level";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        return "Level " + ((int) value);
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
    private void onJoin(PlayerJoinEvent event) {
        BetterStats.getAPI().getPlayerStats(event.getPlayer()).setStat(this, event.getPlayer().getLevel());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onXPChange(PlayerLevelChangeEvent event) {
        BetterStats.getAPI().getPlayerStats(event.getPlayer()).setStat(this, event.getNewLevel());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        BetterStats.getAPI().getPlayerStats(event.getPlayer()).setStat(this, event.getPlayer().getLevel());
    }

    @Override
    public void onRegister() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            BetterStats.getAPI().getPlayerStats(onlinePlayer).setStat(this, onlinePlayer.getLevel());
        }
    }
}

