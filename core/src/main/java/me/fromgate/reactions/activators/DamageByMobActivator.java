package me.fromgate.reactions.activators;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.DamageByMobStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
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
	public boolean activate(Storage event) {
		DamageByMobStorage pde = (DamageByMobStorage) event;
		if (damagerType.isEmpty()) return false;
		Entity damager = pde.getDamager();
		if (damager != null && !isActivatorDamager(damager)) return false;
		if (!damageCauseCheck(pde.getCause())) return false;
		Variables.setTempVar("damagerlocation", (damager != null) ? LocationUtil.locationToString(damager.getLocation()) : "");
		Variables.setTempVar("damagertype", (damager != null) ? damager.getType().name() : "");
		Variables.setTempVar("entitytype", damager.getType().name());
		Player player = damager instanceof Player ? (Player) damager : null;
		String damagerName = (player == null) ? ((damager != null) ? damager.getCustomName() : "") : player.getName();
		Variables.setTempVar("damagername", damagerName != null && !damagerName.isEmpty() ? damagerName : ((damager != null) ? damager.getType().name() : ""));
		Variables.setTempVar("damage", Double.toString(pde.getDamage()));
		Variables.setTempVar("cause", pde.getCause().name());
		boolean result = Actions.executeActivator(pde.getPlayer(), getBase());
		String dmgStr = Variables.getTempVar("damage");
		if (Util.FLOAT_POSITIVE.matcher(dmgStr).matches()) pde.setDamage(Double.parseDouble(dmgStr));
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

	private static String getEntityTypeByName(String sType) {
		if (sType != null) {
			sType = sType.toUpperCase();
			for (EntityType type : EntityType.values()) {
				if (sType.equals(type.name()))
					return type.name();
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
	public ActivatorType getType() {
		return ActivatorType.DAMAGE_BY_MOB;
	}

	@Override
	public boolean isValid() {
		return true;//!Util.emptyString(damagerType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("type:").append(damagerType.isEmpty() ? "-" : damagerType.toUpperCase());
		sb.append("; name:").append(damagerName.isEmpty() ? "-" : damagerName);
		sb.append("; etype:").append(entityType.isEmpty() ? "-" : entityType.toUpperCase());
		sb.append("; cause:").append(damageCause);
		sb.append(")");
		return sb.toString();
	}
	
	public static DamageByMobActivator create(ActivatorBase base, Param param) {
		String damagerType = param.toString();
		String damagerName;
		if (damagerType.contains("$")) {
			damagerName = getEntityTypeByName(damagerType.substring(0, damagerType.indexOf("$")));
			damagerType = damagerType.substring(damagerName.length() + 1);
		} else {
			damagerType = getEntityTypeByName(param.getParam("type", "ANY"));
			damagerName = param.getParam("name");
		}
		damagerName = ChatColor.translateAlternateColorCodes('&', damagerName.replace("\\_", " "));
		String entityType = getEntityTypeByName(param.getParam("etype", "ANY"));
		String damageCause = getCauseByName(param.getParam("cause", "ANY"));
		return new DamageByMobActivator(base, damagerType, damagerName, entityType, damageCause);
	}
	
	public static DamageByMobActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String damagerType = cfg.getString("damager-type", "");
		String damagerName = cfg.getString("damager-name", "");
		String entityType = cfg.getString("entity-type", "");
		String cause = cfg.getString("cause", "");
		return new DamageByMobActivator(base, damagerType, damagerName, entityType, cause);
	}

}
