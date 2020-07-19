package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.location.PlayerRespawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@PlaceholderDefine(id = "BasicPlayer",
        keys = {"player_loc", "player_loc_eye", "player_loc_view", "player_name", "player",
                "player_display", "dplayer", "player_item_hand", "itemplayer", "player_inv", "invplayer",
                "health", "player_loc_death", "deathpoint", "player_id", "uuid", "player_level", "level",
                "player_held_slot", "slot"})
public class PlaceholderPlayer extends Placeholder {

    private static final Set<Material> NON_SOLID;

    static {
        NON_SOLID = new HashSet<>();
        for (Material mat : Material.values())
            if(!mat.isSolid()) NON_SOLID.add(mat);
    }

    @Override
    public String processPlaceholder(Player player, String key, String param) {
        if(player == null) return null;
        switch(key.toLowerCase()) {
            case "player_name":
            case "player":
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
                return LocationUtil.locationToString(player.getLocation());
            case "player_loc_death":
            case "deathpoint":
                Location loc = PlayerRespawner.getLastDeathPoint(player);
                if(loc == null) loc = player.getLocation();
                return LocationUtil.locationToString(loc);
            case "player_loc_eye":
                return LocationUtil.locationToString(player.getEyeLocation());
            case "player_loc_view":
                return LocationUtil.locationToString(getViewLocation(player, false));
            case "player_loc_view_solid":
                return LocationUtil.locationToString(getViewLocation(player, true));
            case "player_level":
            case "level":
                return Integer.toString(player.getLevel());
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
        Block b = p.getTargetBlock(solid ? NON_SOLID : null, 100);
        if(b == null) return p.getLocation();
        // Does it work ok on negative coordinates?
        return b.getLocation().add(0.5, 0.5, 0.5);
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
        if(vi == null) return "";
        return vi.toString();
    }

    private String getPlayerInventory(Player player, String value) {
        VirtualItem vi = null;
        if(Util.isInteger(value)) {
            int slotNum = Integer.parseInt(value);
            if(slotNum < 0 || slotNum >= player.getInventory().getSize()) return "";
            vi = VirtualItem.fromItemStack(player.getInventory().getItem(slotNum));
        } else {
            switch(value.toLowerCase()) {
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
        if(vi == null) return "";
        return vi.toString();
    }
}
