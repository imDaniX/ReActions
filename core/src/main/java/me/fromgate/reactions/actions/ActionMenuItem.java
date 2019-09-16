package me.fromgate.reactions.actions;

import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;

public class ActionMenuItem extends Action {

	@Override
	public boolean execute(RaContext context, Param params) {
		return InventoryMenu.createAndOpenInventory(context.getPlayer(), params, context.getTempVariables());
	}

}
