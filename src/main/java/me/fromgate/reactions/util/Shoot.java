/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */

package me.fromgate.reactions.util;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.module.defaults.StoragesManager;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.math.Rng;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class Shoot {

    public String actionShootBreak = "GLASS,THIN_GLASS,STAINED_GLASS,STAINED_GLASS_PANE,GLOWSTONE,REDSTONE_LAMP_OFF,REDSTONE_LAMP_ON";
    public String actionShootThrough = "FENCE,FENCE_GATE,IRON_BARDING,IRON_FENCE,NETHER_FENCE";

    public void shoot(LivingEntity shooter, Parameters params) {
        boolean onehit = params.getBoolean("singlehit", true);
        int distance = params.getInteger("distance", 100);
        float knockbackTarget = params.getInteger("knockbackTarget", 0);
        for (LivingEntity le : getEntityBeam(shooter, getBeam(shooter, distance), onehit)) {
            double damage = Rng.nextIntRanged(params.getString("damage", "1"));
            boolean shoot = true;
            if (damage > 0) {
                shoot = damageEntity(shooter, le, damage, knockbackTarget);
            }
            if (shoot && params.contains("run")) {
                executeActivator(shooter instanceof Player ? (Player) shooter : null, le, params.getString("run"));
            }
        }
    }

    private String getMobName(LivingEntity mob) {
        if (mob.getCustomName() == null) return mob.getType().name();
        return mob.getCustomName();
    }

    private void executeActivator(Player shooter, LivingEntity target, String paramStr) {
        Parameters param = Parameters.fromString(paramStr);
        if (param.isEmpty() || !param.containsAny("activator", "exec")) return;
        Player player = target instanceof Player ? (Player) target : null;
        if (player == null && param.getBoolean("playeronly", true)) return;
        param.put("player", player == null ? "null" : player.getName());
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("targettype", target.getType().name());
        tempVars.put("targetname", (player == null) ? getMobName(target) : player.getName());
        tempVars.put("targetloc", LocationUtils.locationToString(target.getLocation()));
        if (shooter != null) {
            tempVars.put("shooter", shooter.getName());
            tempVars.put("shooterloc", LocationUtils.locationToString(shooter.getLocation()));
        }
        StoragesManager.triggerExec(shooter, param, tempVars);
    }

    private List<Block> getBeam(LivingEntity p, int distance) {
        List<Block> beam = new ArrayList<>();
        BlockIterator bi = new BlockIterator(p, distance);
        while (bi.hasNext()) {
            Block b = bi.next();
            if (isEmpty(b, p)) beam.add(b);
            else break;
        }
        return beam;
    }

    private Set<LivingEntity> getEntityBeam(LivingEntity shooter, List<Block> beam, boolean hitSingle) {
        Set<LivingEntity> list = new HashSet<>();
        for (Block b : beam)
            for (Entity e : b.getChunk().getEntities()) {
                if (!(e instanceof LivingEntity le)) continue;
                if (le.equals(shooter)) continue;
                if (isEntityAffectByBeamBlock(b, le)) {
                    list.add(le);
                    if (hitSingle) return list;
                }
            }
        return list;
    }

    private boolean isEmpty(Block b, LivingEntity shooter) {
        if (!b.getType().isSolid()) return true;
        if (ItemUtils.isItemInList(b.getType(), 0, actionShootThrough)) return true;
        if ((shooter instanceof Player) && (isShotAndBreak(b, (Player) shooter))) {
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
            b.breakNaturally();
            return true;
        }
        return false;
    }


    public boolean breakBlock(Block b, Player p) {
        BlockBreakEvent event = new BlockBreakEvent(b, p);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    private boolean isShotAndBreak(Block b, Player p) {
        if (ItemUtils.isItemInList(b.getType(), 0, actionShootBreak)) return breakBlock(b, p);
        return false;
    }

    private boolean isEntityAffectByBeamBlock(Block b, LivingEntity le) {
        if (le.getLocation().getBlock().equals(b)) return true;
        return le.getEyeLocation().getBlock().equals(b);
    }

    public boolean damageEntity(LivingEntity damager, LivingEntity entity, double damage, float knockbackTarget) {
        Vector eVec = entity.getLocation().toVector().clone();
        Vector dVec = damager.getLocation().toVector().clone();
        Vector eDirection = eVec.subtract(dVec).normalize();
        eDirection.add(new Vector(0.0D, 0.1D, 0.0D)).multiply(knockbackTarget);
        entity.setVelocity(eDirection);

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, entity, DamageCause.ENTITY_ATTACK, damage);
        Bukkit.getPluginManager().callEvent(event);
        if (!(event.isCancelled()))
            entity.damage(damage);
        return !event.isCancelled();
    }

}
