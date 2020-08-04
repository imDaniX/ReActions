package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.BlockBreakStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakTrigger extends Trigger implements Locatable {

    private final Material blockType;
    // TODO: VirtualLocation
    private final String blockLocation;

    private BlockBreakTrigger(ActivatorBase base, Material block, String location) {
        super(base);
        this.blockType = block;
        this.blockLocation = location;
    }

    public static BlockBreakTrigger create(ActivatorBase base, Parameters param) {
        Material block = ItemUtils.getMaterial(param.getString("block"));
        String loc = param.getString("loc");
        return new BlockBreakTrigger(base, block, loc);
    }

    public static BlockBreakTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        Material block = ItemUtils.getMaterial(cfg.getString("block"));
        String loc = cfg.getString("loc");
        return new BlockBreakTrigger(base, block, loc);
    }

    @Override
    public boolean proceed(Storage event) {
        BlockBreakStorage bbe = (BlockBreakStorage) event;
        Block brokenBlock = bbe.getBlock();
        if (brokenBlock == null) return false;
        return isActivatorBlock(brokenBlock);
    }

    private boolean isActivatorBlock(Block block) {
        if (this.blockType != null && blockType != block.getType()) return false;
        if (Utils.isStringEmpty(blockLocation)) return true;
        return this.isLocatedAt(block.getLocation());
    }

    @Override
    public boolean isLocatedAt(Location l) {
        if (Utils.isStringEmpty(blockLocation)) return false;
        Location loc = LocationUtils.parseLocation(this.blockLocation, null);
        if (loc == null) return false;
        return l.getWorld().equals(loc.getWorld()) &&
                l.getBlockX() == loc.getBlockX() &&
                l.getBlockY() == loc.getBlockY() &&
                l.getBlockZ() == loc.getBlockZ();
    }

    @Override
    public boolean isLocatedAt(World world, int x, int y, int z) {
        return isLocatedAt(new Location(world, x, y, z));
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        if (blockType != null) cfg.set("block", this.blockType.name());
        cfg.set("location", Utils.isStringEmpty(blockLocation) ? null : this.blockLocation);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.BLOCK_BREAK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("block:").append(blockType == null ? "-" : blockType);
        sb.append("; loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
        sb.append(")");
        return sb.toString();
    }
}
