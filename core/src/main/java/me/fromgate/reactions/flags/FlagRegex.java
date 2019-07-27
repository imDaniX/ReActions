package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;

public class FlagRegex implements Flag {
	@Override
	public boolean checkFlag(Player player, String param) {
		Param params = new Param(param, "unknown");
		String regexValue = params.getParam("regex", "");
		if (regexValue.isEmpty()) return false;
		if (!params.isParamsExists("regex")) return false;
		for (String valueKey : params.keySet()) {
			if (!((valueKey.toLowerCase()).startsWith("value"))) continue;
			if (params.getParam(valueKey).matches(regexValue)) return true;
		}
		return false;
	}
}
