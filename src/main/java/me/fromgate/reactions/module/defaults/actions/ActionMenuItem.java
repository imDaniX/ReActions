package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

public class ActionMenuItem extends OldAction {

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        return InventoryMenu.createAndOpenInventory(context.getPlayer(), params, context.getVariables());
    }

}
