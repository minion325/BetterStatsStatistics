package me.saif.betterstatsstatistics.commands;

import me.saif.betterstats.BetterStats;
import me.saif.betterstats.player.StatPlayer;
import me.saif.betterstats.statistics.Stat;
import me.saif.betterstats.utils.Callback;
import me.saif.betterstats.utils.Verify;
import me.saif.betterstatsstatistics.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.command.ArgumentStack;

import java.util.List;

public class StatCommand {

    private final Main plugin;
    private int pageLength;
    private final String header, footer, entry;

    public StatCommand(Main plugin) {
        this.plugin = plugin;
        this.pageLength = plugin.getConfig().getInt("stats-command.page-size");
        this.header = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats-command.header", "&7------=====&a%name%&7=====------"));
        this.footer = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats-command.footer", "&7------=====&a%page%&7=====------"));
        this.entry = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stats-command.entry", "&a%stat% &f- %value%"));
        if (pageLength <= 0)
            pageLength = 999;

    }

    @Command("stats")
    @AutoComplete("@players")
    public void statCommand(Player sender, ArgumentStack args) {
        int page = 1;
        List<Stat> allVisible = BetterStats.getAPI().getVisibleStats();

        if (args.isEmpty()) {
            StatPlayer statPlayer = BetterStats.getAPI().getPlayerStats(sender);
            sendStats(sender, statPlayer, 0, pageLength, allVisible);
            return;
        }

        String firstArg = args.get(0);

        if (Verify.isAnInteger(firstArg)) {
            /*
             If the first arg is an integer, then we know that the player
             is trying to see a specific page of his own profile and not
             anything else, so we won't be checking the other arguments
             */
            page = Integer.parseInt(firstArg); // Setting as the specific page

            if (page < 1) { // anti-dumbness
                page = 1;
            } else if (page > ((allVisible.size() - 1) / pageLength) + 1)
                page = (allVisible.size() - 1) / pageLength + 1;

            StatPlayer statPlayer = BetterStats.getAPI().getPlayerStats(sender);

            sendStats(sender, statPlayer, page - 1, pageLength, allVisible);
        } else {

            /*
             As you said, the command sender can specify the target's status page,
             so we gotta check for a possible target's status page argument too
             */
            if (args.size() > 1) {
                String possiblePage = args.get(1);
                if (Verify.isAnInteger(possiblePage))
                    page = Integer.parseInt(possiblePage); // Setting as the specific page

                if (page < 1) { // anti-dumbness
                    page = 1;
                } else if (page > ((allVisible.size() - 1) / pageLength) + 1)
                    page = (allVisible.size() - 1) / pageLength + 1;
            }

            Player player = Bukkit.getPlayer(firstArg);

            if (player != null && player.getName().equalsIgnoreCase(firstArg)) {
                StatPlayer statPlayer = BetterStats.getAPI().getPlayerStats(player);
                sendStats(sender, statPlayer, page - 1, pageLength, allVisible);
                return;
            }

            Callback<StatPlayer> statPlayerCallback = BetterStats.getAPI().getPlayerStats(firstArg);
            final int lambdapage = page - 1;
            statPlayerCallback.addResultListener(() -> {
                StatPlayer statPlayer = statPlayerCallback.getResult();
                if (statPlayer == null) {
                    sender.sendMessage("Could not find the stats for that player");
                    return;
                }
                sendStats(sender, statPlayer, lambdapage, pageLength, allVisible);
            });
        }
    }

    private void sendStats(Player sender, StatPlayer statPlayer, int page, int pageLength, List<Stat> allVisible) {
        sender.sendMessage(StringUtils.replace(header, "%name%", statPlayer.getPlayer().getName()));
        for (int i = page * pageLength; i < pageLength * (page + 1) && i < allVisible.size(); i++) {
            String toSend = StringUtils.replace(entry, "%stat%", allVisible.get(i).getName());
            toSend = StringUtils.replace(toSend, "%value%", statPlayer.getFormattedStat(allVisible.get(i)));
            sender.sendMessage(toSend);
        }
        sender.sendMessage(StringUtils.replace(footer, "%page%", (page + 1) + "/" + (((allVisible.size() - 1) / pageLength) + 1)));
    }
}
