package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.data.RaContext;

@PlaceholderDefine(id = "activator_name", keys = {"activatorname"})
public class PlaceholderActivator extends Placeholder {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        return context.getActivatorName();
    }
}
