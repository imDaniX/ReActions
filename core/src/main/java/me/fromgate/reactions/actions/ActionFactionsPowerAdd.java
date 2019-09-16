package me.fromgate.reactions.actions;

import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.factions.RaFactions;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;

public class ActionFactionsPowerAdd extends Action {

	@Override
	public boolean execute(RaContext context, Param params) {
		if (!Externals.isConnectedFactions()) return false;
		RaFactions.addPower(context.getPlayer(), params.getParam("power", 0.0));
		return true;
	}
}
