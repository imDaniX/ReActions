package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.time.TimeUtil;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "Time", keys = {"TIME_INGAME", "curtime", "TIME_SERVER", "servertime"})
public class PlaceholderTime extends Placeholder {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        Player player = context.getPlayer();
        if (Util.containsValue(key, "TIME_INGAME", "curtime"))
            return TimeUtil.ingameTimeToString((player == null ? Bukkit.getWorlds().get(0).getTime() : player.getWorld().getTime()), false);
        if (Util.containsValue(key, "TIME_SERVER", "servertime"))
            return TimeUtil.fullTimeToString(System.currentTimeMillis(), param.isEmpty() ? "dd-MM-YYYY HH:mm:ss" : param);
        return null;
    }
}
