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

package me.fromgate.reactions.activators.triggers;

import lombok.Getter;
import me.fromgate.reactions.util.RaGenerator;
import me.fromgate.reactions.util.message.BukkitMessenger;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// TODO: Will be irrelevant because of modules(externals) system
public enum ActivatorType {
    /*
     TODO: More activators
     GlideActivator, MoneyTransactionActivator, PotionSplashActivator, ProjectileHitActivator
    */
    EXEC("exe", ExecTrigger::create, ExecTrigger::load),
    BUTTON("b", ButtonTrigger::create, ButtonTrigger::load, true, true),
    PLATE("plt", PlateTrigger::create, PlateTrigger::load, true, true),
    TELEPORT("tp", TeleportTrigger::create, TeleportTrigger::load, true),
    COMMAND("cmd", CommandTrigger::create, CommandTrigger::load),
    MESSAGE("msg", MessageTrigger::create, MessageTrigger::load),
    PVP_KILL("pvpkill", PvpKillTrigger::create, PvpKillTrigger::load),
    DEATH("player_death", DeathTrigger::create, DeathTrigger::load),
    RESPAWN("player_respawn", RespawnTrigger::create, RespawnTrigger::load),
    LEVER("lvr", LeverTrigger::create, LeverTrigger::load, true, true),
    DOOR("door", DoorTrigger::create, DoorTrigger::load, true, true),
    JOIN("join", JoinTrigger::create, JoinTrigger::load),
    QUIT("quit", QuitTrigger::create, QuitTrigger::load),
    MOB_CLICK("mobclick", MobClickTrigger::create, MobClickTrigger::load, true),
    MOB_KILL("mobkill", MobKillTrigger::create, MobKillTrigger::load),
    MOB_DAMAGE("mobdamage", MobDamageTrigger::create, MobDamageTrigger::load),
    ITEM_CLICK("itemclick", ItemClickTrigger::create, ItemClickTrigger::load),
    ITEM_CONSUME("consume", ItemConsumeTrigger::create, ItemConsumeTrigger::load),
    ITEM_HOLD("itemhold", ItemHoldTrigger::create, ItemHoldTrigger::load),
    ITEM_HELD("itemheld", ItemHeldTrigger::create, ItemHeldTrigger::load),
    ITEM_WEAR("itemwear", ItemWearTrigger::create, ItemWearTrigger::load),
    SIGN("sign", SignTrigger::create, SignTrigger::load, true, true),
    BLOCK_CLICK("blockclick", BlockClickTrigger::create, BlockClickTrigger::load, true, true),
    INVENTORY_CLICK("inventoryclick", InventoryClickTrigger::create, InventoryClickTrigger::load),
    DROP("drop", DropTrigger::create, DropTrigger::load),
    PICKUP_ITEM("pickupitem", PickupItemTrigger::create, PickupItemTrigger::load),
    FLIGHT("flight", FlightTrigger::create, FlightTrigger::load),
    ENTITY_CLICK("entityclick", EntityClickTrigger::create, EntityClickTrigger::load),
    BLOCK_BREAK("blockbreak", BlockBreakTrigger::create, BlockBreakTrigger::load, true, true),
    SNEAK("sneak", SneakTrigger::create, SneakTrigger::load),
    DAMAGE("damage", DamageTrigger::create, DamageTrigger::load),
    DAMAGE_BY_MOB("damagebymob", DamageByMobTrigger::create, DamageByMobTrigger::load),
    DAMAGE_BY_BLOCK("damagebyblock", DamageByBlockTrigger::create, DamageByBlockTrigger::load, true, true),
    VARIABLE("var", VariableTrigger::create, VariableTrigger::load),
    GAMEMODE("gamemode", GamemodeTrigger::create, GamemodeTrigger::load),
    GOD("god", GodTrigger::create, GodTrigger::load),
    CUBOID("cube", CuboidTrigger::create, CuboidTrigger::load, true),
    //PROJECTILE_HIT("projhit", ProjectileHitActivator::create, ProjectileHitActivator::load),
    /* WorldGuard */
    REGION("rg", RegionTrigger::create, RegionTrigger::load, true),
    REGION_ENTER("rgenter", RegionEnterTrigger::create, RegionEnterTrigger::load, true),
    REGION_LEAVE("rgleave", RegionLeaveTrigger::create, RegionLeaveTrigger::load, true),
    /* WorldEdit */
    WE_SELECTION_REGION("weselectionregion", WeSelectionTrigger::create, WeSelectionTrigger::load),
    WE_CHANGE("wechange", WeChangeTrigger::create, WeChangeTrigger::load);

    private static final Map<String, ActivatorType> BY_NAME;

    static {
        Map<String, ActivatorType> byName = new HashMap<>();
        for (ActivatorType act : ActivatorType.values()) {
            byName.put(act.name(), act);
            byName.put(act.alias.toUpperCase(Locale.ENGLISH), act);
        }
        BY_NAME = Collections.unmodifiableMap(byName);
    }

    private final String alias;
    private final RaGenerator<Parameters> creator;
    private final RaGenerator<ConfigurationSection> loader;
    @Getter
    private final boolean needBlock;
    @Getter
    private final boolean locatable;

    ActivatorType(String alias, RaGenerator<Parameters> creator, RaGenerator<ConfigurationSection> loader, boolean locatable, boolean needBlock) {
        this.alias = alias.toUpperCase(Locale.ENGLISH);
        this.creator = creator;
        this.loader = loader;
        this.needBlock = needBlock;
        this.locatable = locatable;
    }

    ActivatorType(String alias, RaGenerator<Parameters> creator, RaGenerator<ConfigurationSection> loader, boolean locatable) {
        this(alias, creator, loader, locatable, false);
    }

    ActivatorType(String alias, RaGenerator<Parameters> creator, RaGenerator<ConfigurationSection> loader) {
        this(alias, creator, loader, false);
    }

    @SuppressWarnings("unused")
    public static boolean isValid(String str) {
        return getByName(str) != null;
    }

    public static ActivatorType getByName(String name) {
        return BY_NAME.get(name.toUpperCase(Locale.ENGLISH));
    }

    public static void listActivators(CommandSender sender, int pageNum) {
        List<String> activatorList = new ArrayList<>();
        for (ActivatorType activatorType : ActivatorType.values()) {
            String name = activatorType.name();
            String alias = activatorType.alias.equalsIgnoreCase(name) ? " " : " (" + activatorType.alias + ") ";
            Msg activatorDesc = Msg.getByName("ACTIVATOR_" + name);
            if (activatorDesc == null) {
                Msg.LNG_MISSED_ACTIVATOR_DESC.log(name);
            } else {
                activatorList.add("&6" + name + "&e" + alias + "&3: &a" + activatorDesc.getText("NOCOLOR"));
            }
        }
        BukkitMessenger.printPage(sender, activatorList, Msg.MSG_ACTIVATORLISTTITLE, pageNum);
    }

    public Trigger create(String name, String group, Parameters param) {
        ActivatorBase base = new ActivatorBase(name, group);
        return creator.generate(base, param);
    }

    public Trigger load(String name, String group, ConfigurationSection cfg) {
        ActivatorBase base = new ActivatorBase(name, group, cfg);
        return loader.generate(base, cfg);
    }

}

/*
public abstract class ActivatorType {
	@Getter private final String name;

	private static Map<String, ActivatorType> byName;

	public ActivatorType(String name) {
		this.name  name;
	}

	public static register(ActivatorType type, String alias) {
		byName.put(type.getame.toUpperCase(Locale.ENGLISH), this);
		byName.put(alias.toUpperCase(Locale.ENGLISH), this);
	}

	public abstract Activator create(ActivatorBase base, Param params);

	public abstract Activator load(ActivatorBase base, ConfigurationSection cfg);
}
*/