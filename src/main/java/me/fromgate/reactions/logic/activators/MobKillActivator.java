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

package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.MobKillStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.Locale;

public class MobKillActivator extends Activator {
    // TODO: EntityType
    private final String mobType;
    private final String mobName;

    private MobKillActivator(ActivatorLogic base, String type, String name) {
        super(base);
        this.mobType = type;
        this.mobName = name;
    }

    public static MobKillActivator create(ActivatorLogic base, Parameters param) {
        String type = param.toString();
        String name = "";
        if (param.contains("type")) {
            type = param.getString("type");
            name = param.getString("name");
        } else if (param.toString().contains("$")) {
            name = type.substring(0, type.indexOf("$"));
            type = type.substring(name.length() + 1);
        }
        return new MobKillActivator(base, type, name);
    }

    public static MobKillActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        String type = cfg.getString("mob-type", "");
        String name = cfg.getString("mob-name", "");
        return new MobKillActivator(base, type, name);
    }

    @Override
    public boolean check(Storage event) {
        MobKillStorage me = (MobKillStorage) event;
        if (mobType.isEmpty()) return false;
        if (me.getEntity() == null) return false;
        return isActivatorMob(me.getEntity());
    }

    private boolean isActivatorMob(LivingEntity mob) {
        if (!mobName.isEmpty()) {
            if (!ChatColor.translateAlternateColorCodes('&', mobName.replace("_", " ")).equals(getMobName(mob)))
                return false;
        } else if (!getMobName(mob).isEmpty()) return false;
        return mob.getType().name().equalsIgnoreCase(this.mobType);
    }

    private String getMobName(LivingEntity mob) {
        if (mob.getCustomName() == null) return "";
        return mob.getCustomName();
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("mob-type", mobType);
        cfg.set("mob-name", mobName);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.MOB_KILL;
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(mobType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("type:").append(mobType.isEmpty() ? "-" : mobType.toUpperCase(Locale.ENGLISH));
        sb.append(" name:").append(mobName.isEmpty() ? "-" : mobName);
        sb.append(")");
        return sb.toString();
    }
}
