package me.fromgate.reactions.util;


import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class TemporaryOp {

    private Set<UUID> tempOps = new HashSet<>();

    public void setTempOp(CommandSender sender) {
        if (sender instanceof Player && !sender.isOp()) {
            tempOps.add(((Player) sender).getUniqueId());
            sender.setOp(true);
        }
    }

    public void removeTempOp(CommandSender sender) {
        if (sender instanceof Player && tempOps.remove(((Player) sender).getUniqueId()))
            sender.setOp(false);
    }

}
