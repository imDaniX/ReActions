package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.DamageByBlockStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
// TODO: Assemble to one activator
public class DamageByBlockActivator extends Activator implements Locatable {
	private final String blockStr;
	private final String blockLocation;
	private final String damageCause;
	
	public DamageByBlockActivator(ActivatorBase base, String block, String location, String cause) {
		super(base);
		this.blockStr = block;
		this.blockLocation = location;
		this.damageCause = cause;
	}

	@Override
	public boolean activate(Storage event) {
		DamageByBlockStorage db = (DamageByBlockStorage) event;
		Block damagerBlock = db.getBlockDamager();
		if (damagerBlock == null) return false;
		if (!isActivatorBlock(damagerBlock)) return false;
		if (!damageCauseCheck(db.getCause())) return false;
		Variables.setTempVar("blocklocation", LocationUtil.locationToString(db.getBlockLocation()));
		Variables.setTempVar("blocktype", damagerBlock.getType().name());
		Variables.setTempVar("block", ItemUtil.itemFromBlock(damagerBlock).toString());
		Variables.setTempVar("damage", Double.toString(db.getDamage()));
		Variables.setTempVar("cause", db.getCause().name());
		return Actions.executeActivator(db.getPlayer(), getBase());
	}

	private boolean checkLocations(Block block) {
		if (this.blockLocation.isEmpty()) return true;
		return this.isLocatedAt(block.getLocation());
	}

	private boolean isActivatorBlock(Block block) {
		if (!this.blockStr.isEmpty() && !ItemUtil.compareItemStr(block, this.blockStr)) return false;
		return checkLocations(block);
	}

	@Override
	public boolean isLocatedAt(Location l) {
		if (this.blockLocation.isEmpty()) return false;
		Location loc = LocationUtil.parseLocation(this.blockLocation, null);
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

	private static String getCauseByName(String damageCauseStr) {
		if (damageCauseStr != null) {
			for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
				if (damageCauseStr.equalsIgnoreCase(damageCause.name())) {
					return damageCause.name();
				}
			}
		}
		return "ANY";
	}

	private boolean damageCauseCheck(EntityDamageEvent.DamageCause dc) {
		if (damageCause.equals("ANY")) return true;
		return dc.name().equals(damageCause);
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("block", this.blockStr);
		cfg.set("location", this.blockLocation.isEmpty() ? null : this.blockLocation);
		cfg.set("cause", this.damageCause);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DAMAGE_BY_BLOCK;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("block:").append(blockStr.isEmpty() ? "-" : blockStr);
		sb.append("; loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
		sb.append("; cause:").append(damageCause);
		sb.append(")");
		return sb.toString();
	}

	public static DamageByBlockActivator create(ActivatorBase base, Param param) {
		String block = param.getParam("block", "");
		String location = param.getParam("loc", "");
		String cause = param.getParam("cause", "ANY");
		return new DamageByBlockActivator(base, block, location, cause);
	}

	public static DamageByBlockActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String block = cfg.getString("block", "");
		String location = cfg.getString("loc", "");
		String cause = cfg.getString("cause", "ANY");
		return new DamageByBlockActivator(base, block, location, cause);
		
	}
}
