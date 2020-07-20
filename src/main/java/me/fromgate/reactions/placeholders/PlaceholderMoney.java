package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.util.data.RaContext;

import java.util.Map;

@PlaceholderDefine(id = "Money", keys = {"MONEY"})
public class PlaceholderMoney extends Placeholder {

    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        Map<String, String> params = RaEconomics.getBalances(context.getPlayer());
        return params.getOrDefault(key, null);
    }

}
