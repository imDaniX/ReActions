package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.TimeUtils;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Alias({"TIME_INGAME", "curtime", "TIME_SERVER", "servertime"})
public class PlaceholderTime implements Placeholder.Equal {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        Player player = context.getPlayer();
        switch (key) {
            case "time":
            case "time_server":
            case "servertime":
                return TimeUtils.fullTimeToString(System.currentTimeMillis(), param.isEmpty() ? "dd-MM-YYYY HH:mm:ss" : param);

            case "TIME_INGAME":
            case "curtime":
                return TimeUtils.formattedIngameTime((player == null ? Bukkit.getWorlds().get(0).getTime() : player.getWorld().getTime()), false);

            default:
                return null;
        }
    }

    @Override
    public String getId() {
        return "time";
    }
}
