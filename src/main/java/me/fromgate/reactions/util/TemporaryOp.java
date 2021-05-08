package me.fromgate.reactions.util;


import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class TemporaryOp {

    private final Set<UUID> tempOps = new HashSet<>();

    public void setOp(CommandSender sender) {
        if (sender instanceof Player && !sender.isOp()) {
            tempOps.add(((Player) sender).getUniqueId());
            sender.setOp(true);
        }
    }

    public void removeOp(CommandSender sender) {
        if (sender instanceof Player && tempOps.remove(((Player) sender).getUniqueId()))
            sender.setOp(false);
    }

    @SuppressWarnings("unchecked")
    public <T extends CommandSender> T asOp(T sender) {
        return (T) Proxy.newProxyInstance(
                sender.getClass().getClassLoader(),
                sender.getClass().getInterfaces(),
                (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "isOp": case "hasPermission": return true;
                            default: return method.invoke(sender, args);
                        }
                }
        );
    }
}
