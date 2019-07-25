package me.fromgate.reactions.activators;

import me.fromgate.reactions.storage.RAStorage;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

// TODO
public class ProjectileHitActivator extends Activator {
	private EntityType projType;
	private Material hitBlock;
	private BlockFace hitFace;
	private EntityType hitEntity;

	private HitType hitType;

	public ProjectileHitActivator(String name, String group) {
		super(name, group);
	}

	public ProjectileHitActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAStorage storage) {
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {

	}

	@Override
	public void load(ConfigurationSection cfg) {

	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PROJECTILE_HIT;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private enum HitType {
		ENTITY,BLOCK,ANY
	}
}
