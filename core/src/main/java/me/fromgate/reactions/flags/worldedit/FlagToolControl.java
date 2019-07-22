package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.flags.Flag;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 11/10/2017.
 */
public class FlagToolControl implements Flag {
	@Override
	public boolean checkFlag(Player player, String param) {
		return Boolean.parseBoolean(param) == RaWorldEdit.isToolControl(player);
	}
}
