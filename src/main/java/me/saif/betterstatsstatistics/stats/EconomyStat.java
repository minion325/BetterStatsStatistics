package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.statistics.ExternalStat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.Locale;

public class EconomyStat extends ExternalStat {

    private final Economy economy;

    public EconomyStat (Economy economy) {
        this.economy = economy;
    }

    @Override
    public double getValue(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Override
    public void setValue(OfflinePlayer player, double value) {
        economy.depositPlayer(player,value - economy.getBalance(player));
    }

    @Override
    public String getName() {
        return "Money";
    }

    @Override
    public String format(double value) {
        return DecimalFormat.getCurrencyInstance(Locale.US).format(value);
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
