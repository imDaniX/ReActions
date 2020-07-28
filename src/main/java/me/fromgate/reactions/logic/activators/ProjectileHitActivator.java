package me.fromgate.reactions.logic.activators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

// TODO

@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class ProjectileHitActivator extends Activator {
    EntityType projType;
    Material hitBlock;
    BlockFace hitFace;
    EntityType hitEntity;

    HitType hitType;

    private ProjectileHitActivator(ActivatorBase base, EntityType projType, Material hitBlock, BlockFace hitFace, EntityType hitEntity, HitType hitType) {
        super(base);
        this.projType = projType;
        this.hitBlock = hitBlock;
        this.hitFace = hitFace;
        this.hitEntity = hitEntity;
        this.hitType = hitType;
    }

    public static ProjectileHitActivator create(ActivatorBase base, Parameters param) {
        EntityType projType = Utils.getEnum(EntityType.class, param.getString("projectile", "ARROW"));
        Material hitBlock = Material.getMaterial(param.getString("block", ""));
        BlockFace hitFace = Utils.getEnum(BlockFace.class, param.getString("face", ""));
        EntityType hitEntity = Utils.getEnum(EntityType.class, param.getString("entity", ""));
        HitType hitType = HitType.getByName(param.getString("hit", "ANY"));
        return new ProjectileHitActivator(base, projType, hitBlock, hitFace, hitEntity, hitType);
    }

    public static ProjectileHitActivator load(ActivatorBase base, ConfigurationSection cfg) {
        EntityType projType = Utils.getEnum(EntityType.class, cfg.getString("projectile-type", "ARROW"));
        Material hitBlock = Material.getMaterial(cfg.getString("block-type", ""));
        BlockFace hitFace = Utils.getEnum(BlockFace.class, cfg.getString("block=face", ""));
        EntityType hitEntity = Utils.getEnum(EntityType.class, cfg.getString("entity-type", ""));
        HitType hitType = HitType.getByName(cfg.getString("hit", "ANY"));
        return new ProjectileHitActivator(base, projType, hitBlock, hitFace, hitEntity, hitType);
    }

    @Override
    public boolean activate(Storage storage) {
        return false;
    }

    @Override
    public void save(ConfigurationSection cfg) {

    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.EXEC;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private enum HitType {
        ENTITY, BLOCK, ANY;

        public static HitType getByName(String name) {
            if (name.equalsIgnoreCase("ENTITY")) return ENTITY;
            if (name.equalsIgnoreCase("BLOCK")) return BLOCK;
            return ANY;
        }
    }
}
