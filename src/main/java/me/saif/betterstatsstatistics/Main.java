package me.saif.betterstatsstatistics;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.statistics.Stat;
import me.saif.betterstatsstatistics.commands.StatCommand;
import me.saif.betterstatsstatistics.stats.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.core.BukkitHandler;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.process.ValueResolver;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        CommandHandler commandHandler = new BukkitHandler(this)
                .registerValueResolver(ArgumentStack.class, ValueResolver.ValueResolverContext::arguments);
        commandHandler.getAutoCompleter().registerSuggestion("players", (args, sender, command) -> {
            String last = args.get(args.size() - 1).toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream().map((Function<Player, String>) HumanEntity::getName).filter(s -> s.toLowerCase(Locale.ROOT).startsWith(last)).collect(Collectors.toList());
        });
        commandHandler.register(new StatCommand(this));

        registerStats();
        hookVault();
    }

    private void registerStats() {
        BetterStats.getAPI().registerStats(this, getStats());

        if (BetterStats.getAPI().getStat(KillsStat.class) != null && BetterStats.getAPI().getStat(DeathsStat.class) != null) {
            KDStat kdStat = new KDStat(BetterStats.getAPI().getStat(KillsStat.class), BetterStats.getAPI().getStat(DeathsStat.class));
            if (getConfig().getBoolean("stats." + kdStat.getInternalName()))
                BetterStats.getAPI().registerStats(this, kdStat);
        }
    }

    private Stat[] getStats() {
        Stat[] possible = new Stat[]{new BlocksBrokenStat(),
                new PlaytimeStat(this), new BlocksPlacedStat(),
                new DeathsStat(), new KillsStat(), new XPStat(this), new XPLevelStat(), new GapplesEatenStat()};
        Set<Stat> toReturn = new HashSet<>();

        for (Stat stat : possible) {
            if (getConfig().getBoolean("stats." + stat.getInternalName()))
                toReturn.add(stat);
        }

        return toReturn.toArray(new Stat[]{});
    }

    private void hookVault() {
        if (!this.getConfig().getBoolean("stats.money"))
            return;
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return;

        //stat thing here EconomyStatistic

        Economy econ = rsp.getProvider();
        EconomyStat economyStat = new EconomyStat(econ);
        BetterStats.getAPI().registerStats(this, economyStat);
    }


}
