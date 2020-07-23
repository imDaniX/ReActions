package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.TimeUtils;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "Time", keys = {"TIME_INGAME", "curtime", "TIME_SERVER", "servertime"})
public class PlaceholderTime extends Placeholder {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        Player player = context.getPlayer();
        if (Utils.containsValue(key, "TIME_INGAME", "curtime"))
            return TimeUtils.formattedIngameTime((player == null ? Bukkit.getWorlds().get(0).getTime() : player.getWorld().getTime()), false);
        if (Utils.containsValue(key, "TIME_SERVER", "servertime"))
            return TimeUtils.fullTimeToString(System.currentTimeMillis(), param.isEmpty() ? "dd-MM-YYYY HH:mm:ss" : param);
        return null;
    }
}
