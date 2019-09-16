package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;

public class FlagCheckOnline implements Flag {
	@Override
	public boolean checkFlag(RaContext context, String param) {
		return Util.getPlayerExact(param) != null;
	}
}
