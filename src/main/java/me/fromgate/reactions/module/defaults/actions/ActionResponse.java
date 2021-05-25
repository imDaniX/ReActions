package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionResponse extends OldAction {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        if (player == null)
            Bukkit.getConsoleSender().sendMessage(params.toString());
        else if (player.isOnline()) player.sendMessage(params.toString());
        return true;
    }
}
