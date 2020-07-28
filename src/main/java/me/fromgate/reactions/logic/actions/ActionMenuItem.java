package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

public class ActionMenuItem extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        return InventoryMenu.createAndOpenInventory(context.getPlayer(), params, context.getVariables());
    }

}
