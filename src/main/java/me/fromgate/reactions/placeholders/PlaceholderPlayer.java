package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.location.PlayerRespawner;
import me.fromgate.reactions.util.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

// TODO: Split to different classes
@Alias({"player_loc", "player_loc_eye", "player_loc_view", "player_name",
                "player_display", "dplayer", "player_item_hand", "itemplayer", "player_inv", "invplayer",
                "health", "player_loc_death", "deathpoint", "player_id", "player_uuid", "uuid", "player_level", "level",
                "player_held_slot", "slot"})
public class PlaceholderPlayer implements Placeholder.Prefixed {

    private static final Set<Material> NON_SOLID;
    static {
        Set<Material> nonSolid = new HashSet<>();
        for (Material mat : Material.values())
            if (!mat.isSolid()) nonSolid.add(mat);
        NON_SOLID = Collections.unmodifiableSet(EnumSet.copyOf(nonSolid));
    }

    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        Player player = context.getPlayer();
        if (player == null) return null;
        switch (key) {
            case "player":
            case "player_name":
                return player.getName();
            case "health":
                return Double.toString(player.getHealth());
            case "player_inv":
            case "invplayer":
                return getPlayerInventory(player, param);
            case "player_item_hand":
            case "itemplayer":
                return getPlayerItemInHand(player, false);
            case "player_item_offhand":
            case "offitemplayer":
                return getPlayerItemInHand(player, true);
            case "player_display":
            case "dplayer":
                return player.getDisplayName();
            case "player_loc":
                return LocationUtils.locationToString(player.getLocation());
            case "player_loc_death":
            case "deathpoint":
                Location loc = PlayerRespawner.getLastDeathPoint(player);
                if (loc == null) loc = player.getLocation();
                return LocationUtils.locationToString(loc);
            case "player_loc_eye":
                return LocationUtils.locationToString(player.getEyeLocation());
            case "player_loc_view":
                return LocationUtils.locationToString(getViewLocation(player, false));
            case "player_loc_view_solid":
                return LocationUtils.locationToString(getViewLocation(player, true));
            case "player_level":
            case "level":
                return Integer.toString(player.getLevel());
            case "player_uuid":
            case "player_id":
            case "uuid":
                return player.getUniqueId().toString();
            case "player_ip":
            case "ip_address":
                return player.getAddress().getAddress().getHostAddress();
            case "player_held_slot":
            case "slot":
                return Integer.toString(player.getInventory().getHeldItemSlot());
        }
        return null;
    }

    /**
     * Get location that player is looking on
     *
     * @param p     Player to use
     * @param solid Search for only solid blocks or not
     * @return Location of block
     */
    private Location getViewLocation(Player p, boolean solid) {
        return p.getTargetBlock(solid ? NON_SOLID : null, 100).getLocation().add(0.5, 0.5, 0.5);
    }

    /**
     * Get item in hand
     *
     * @param player  Player to use
     * @param offhand Check offhand or not
     * @return Item string
     */
    private String getPlayerItemInHand(Player player, boolean offhand) {
        VirtualItem vi = VirtualItem.fromItemStack(offhand ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand());
        if (vi == null) return "";
        return vi.toString();
    }

    private String getPlayerInventory(Player player, String value) {
        VirtualItem vi = null;
        if (NumberUtils.isInteger(value)) {
            int slotNum = Integer.parseInt(value);
            if (slotNum < 0 || slotNum >= player.getInventory().getSize()) return "";
            vi = VirtualItem.fromItemStack(player.getInventory().getItem(slotNum));
        } else {
            switch (value.toLowerCase(Locale.ENGLISH)) {
                case "mainhand":
                case "hand":
                    return getPlayerItemInHand(player, false);
                case "offhand":
                    return getPlayerItemInHand(player, true);
                case "head":
                case "helm":
                case "helmet":
                    vi = VirtualItem.fromItemStack(player.getInventory().getHelmet());
                    break;
                case "chestplate":
                case "chest":
                    vi = VirtualItem.fromItemStack(player.getInventory().getChestplate());
                    break;
                case "leggings":
                case "legs":
                    vi = VirtualItem.fromItemStack(player.getInventory().getLeggings());
                    break;
                case "boots":
                case "boot":
                    vi = VirtualItem.fromItemStack(player.getInventory().getBoots());
                    break;
            }
        }
        if (vi == null) return "";
        return vi.toString();
    }

    @Override
    public String getPrefix() {
        return "player";
    }
}
