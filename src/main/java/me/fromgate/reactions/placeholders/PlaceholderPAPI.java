package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.externals.placeholderapi.RaPlaceholderAPI;
import me.fromgate.reactions.util.data.RaContext;

public class PlaceholderPAPI implements Placeholder {
    @Override
    public String processPlaceholder(RaContext context, String ph, String text) {
        String backup = "%" + text + "%";
        String result = RaPlaceholderAPI.processPlaceholder(context.getPlayer(), backup);
        return backup.equals(result) ? null : result;
    }
}
