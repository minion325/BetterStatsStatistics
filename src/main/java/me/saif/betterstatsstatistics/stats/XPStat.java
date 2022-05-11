package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.player.StatPlayer;
import me.saif.betterstats.statistics.Stat;
import me.saif.betterstatsstatistics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class XPStat extends Stat implements Listener {

    private BukkitTask task;
    private final Main plugin;

    public XPStat(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "XP";
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

    @Override
    public void onRegister() {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> {
                    StatPlayer sPlayer = BetterStats.getAPI().getPlayerStats(player);

                    if (((int) sPlayer.getStat(XPStat.this)) == player.getTotalExperience())
                        return;
                    sPlayer.setStat(XPStat.this, player.getTotalExperience());
                });
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        BetterStats.getAPI().getPlayerStats(event.getPlayer()).setStat(this, event.getPlayer().getTotalExperience());
    }

    @Override
    public void onUnregister() {
        this.task.cancel();
    }
}
