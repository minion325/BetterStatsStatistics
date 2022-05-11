package me.saif.betterstatsstatistics.stats;

import me.saif.betterstats.player.StatPlayer;
import me.saif.betterstats.statistics.DependantStat;

import java.text.DecimalFormat;

public class KDStat extends DependantStat {

    private final DeathsStat deaths;
    private final KillsStat kills;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public KDStat(KillsStat kills, DeathsStat deaths) {
        this.kills = kills;
        this.deaths = deaths;
    }

    @Override
    public String getName() {
        return "K/D Ratio";
    }

    @Override
    public double getDefaultValue() {
        return 0;
    }

    @Override
    public String format(double value) {
        return decimalFormat.format(value);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public double getValue(StatPlayer statPlayer) {
        double deathsNum = statPlayer.getStat(deaths);
        double killsNum = statPlayer.getStat(kills);

        if (deathsNum ==  0)
            return killsNum;
        return killsNum/deathsNum;
    }
}
