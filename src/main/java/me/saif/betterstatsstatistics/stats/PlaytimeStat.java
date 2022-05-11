package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import me.saif.betterstatsstatistics.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlaytimeStat extends Stat {

    private BukkitTask bukkitTask;
    private final Main plugin;
    private long lastUpdate;

    public PlaytimeStat(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Time Played";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        int seconds = (int) (value / 1000) % 60;
        int minutes = (int) ((value / (1000 * 60)) % 60);
        int hours = (int) ((value / (1000 * 60 * 60)) % 24);
        int days = (int) ((value / (1000 * 60 * 60 * 24)));
        String time = "";

        if (days != 0)
            time = days + " day" + (days == 1 ? "" : "s");
        if (hours != 0)
            time = time + (time.equals("") ? "" : " ") + hours + " hour" + (hours == 1 ? "" : "s");
        if (minutes != 0)
            time = time + (time.equals("") ? "" : " ") + minutes + " minute" + (minutes == 1 ? "" : "s");
        if (seconds != 0)
            time = time + (time.equals("") ? "" : " ") + seconds + " second" + (seconds == 1 ? "" : "s");
        return time;
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
        lastUpdate = System.currentTimeMillis();
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                long toadd = System.currentTimeMillis() - lastUpdate;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    BetterStats.getAPI().getPlayerStats(onlinePlayer).addToStat(PlaytimeStat.this, toadd);
                }
                lastUpdate = System.currentTimeMillis();
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    @Override
    public void onUnregister() {
        this.bukkitTask.cancel();
    }
}
