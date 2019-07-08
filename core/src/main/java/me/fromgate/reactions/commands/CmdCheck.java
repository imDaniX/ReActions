package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CmdDefine(command = "react", description = Msg.CMD_CHECK, permission = "reactions.config",
        subCommands = {"check"}, allowConsole = false, shortDescription = "&3/react check [radius]")
public class CmdCheck extends Cmd {

    @Override
    public boolean execute(Player player, String[] args) {
        int radius = args.length > 1 && Util.isIntegerGZ(args[1]) ? Integer.parseInt(args[1]) : 8;
        printActivatorsAround(player, radius);
        return true;
    }

    public void printActivatorsAround(Player player, int radius) {
        int playerX = player.getLocation().getBlockX();
        int playerY = player.getLocation().getBlockY();
        int playerZ = player.getLocation().getBlockZ();
        Set<String> set = new HashSet<>();
        for (int x = playerX - radius; x <= playerX + radius; x++) {
            for (int y = playerY - radius; y <= playerY + radius; y++) {
                for (int z = playerZ - radius; z <= playerZ + radius; z++) {
                    Set<Activator> found = Activators.getActivatorInLocation(player.getWorld(), x, y, z);
                    if (found.isEmpty()) continue;
                    for (Activator aFound : found)
                        set.add(aFound.toString());
                }
            }
        }
        List<String> plst = new ArrayList<>(set);
        Msg.printPage(player, plst, Msg.MSG_CHECK, 1, 100, true);
    }
}
