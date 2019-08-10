package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.DamageByMobStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
// TODO: Assemble to one activator
public class DamageByMobActivator extends Activator {

	private final String damagerName;
	private final String damagerType;
	private final String entityType;
	private final String damageCause;
	
	public DamageByMobActivator(ActivatorBase base, String damagerName, String damagerType, String entityType, String damageCause) {
		super(base);
		this.damagerName = damagerName;
		this.damagerType = damagerType;
		this.entityType = entityType;
		this.damageCause = damageCause;
	}

	@Override
	public boolean activate(RAStorage event) {
		DamageByMobStorage pde = (DamageByMobStorage) event;
		if (damagerType.isEmpty()) return false;
		Entity damager = pde.getDamager();
		if (damager != null && !isActivatorDamager(damager)) return false;
		if (!damageCauseCheck(pde.getCause())) return false;
		Variables.setTempVar("damagerlocation", (damager != null) ? Locator.locationToString(damager.getLocation()) : "");
		Variables.setTempVar("damagertype", (damager != null) ? damager.getType().name() : "");
		Variables.setTempVar("entitytype", damager.getType().name());
		Player player = damager instanceof Player ? (Player) damager : null;
		String damagerName = (player == null) ? ((damager != null) ? damager.getCustomName() : "") : player.getName();
		Variables.setTempVar("damagername", damagerName != null && !damagerName.isEmpty() ? damagerName : ((damager != null) ? damager.getType().name() : ""));
		Variables.setTempVar("damage", Double.toString(pde.getDamage()));
		Variables.setTempVar("cause", pde.getCause().name());
		boolean result = Actions.executeActivator(pde.getPlayer(), this);
		String dmgStr = Variables.getTempVar("damage");
		if (Util.FLOAT.matcher(dmgStr).matches()) pde.setDamage(Double.parseDouble(dmgStr));
		return result;
	}

	private boolean isActivatorDamager(Entity damager) {
		if (!damagerName.isEmpty() && damagerName.equals(getMobName(damager))) return false;
		if (damagerType.equalsIgnoreCase("ANY")) return true;
		return damager.getType().name().equalsIgnoreCase(this.damagerType);
	}

	private boolean isActivatorEntity(Entity entity) {
		if (entityType.equalsIgnoreCase("ANY")) return true;
		return entity.getType().name().equalsIgnoreCase(this.entityType);
	}


	private String getMobName(Entity mob) {
		return mob.getCustomName() == null ? "" : mob.getCustomName();
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

	private static String getDamagerTypeByName(String damagerTypeStr) {
		if (damagerTypeStr != null) {
			for (EntityType damager : EntityType.values()) {
				if (damagerTypeStr.equalsIgnoreCase(damager.name())) {
					return damager.name();
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
		cfg.set("damager-type", this.damagerType);
		cfg.set("damager-name", this.damagerName);
		cfg.set("entity-type", this.entityType);
		cfg.set("cause", this.damageCause);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.damagerType = cfg.getString("damager-type", "");
		this.damagerName = cfg.getString("damager-name", "");
		this.entityType = cfg.getString("entity-type", "");
		this.damageCause = cfg.getString("cause", "");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DAMAGE_BY_MOB;
	}

	@Override
	public boolean isValid() {
		return true;//!Util.emptyString(damagerType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("type:").append(damagerType.isEmpty() ? "-" : damagerType.toUpperCase());
		sb.append("; name:").append(damagerName.isEmpty() ? "-" : damagerName);
		sb.append("; etype:").append(entityType.isEmpty() ? "-" : entityType.toUpperCase());
		sb.append("; cause:").append(damageCause);
		sb.append(")");
		return sb.toString();
	}

}
