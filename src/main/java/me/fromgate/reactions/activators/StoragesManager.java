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

package me.fromgate.reactions.activators;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.ReActionsPlugin;
import me.fromgate.reactions.activators.storages.BlockBreakStorage;
import me.fromgate.reactions.activators.storages.BlockClickStorage;
import me.fromgate.reactions.activators.storages.ButtonStorage;
import me.fromgate.reactions.activators.storages.CommandStorage;
import me.fromgate.reactions.activators.storages.CuboidStorage;
import me.fromgate.reactions.activators.storages.DamageByBlockStorage;
import me.fromgate.reactions.activators.storages.DamageByMobStorage;
import me.fromgate.reactions.activators.storages.DamageStorage;
import me.fromgate.reactions.activators.storages.DeathStorage;
import me.fromgate.reactions.activators.storages.DoorStorage;
import me.fromgate.reactions.activators.storages.DropStorage;
import me.fromgate.reactions.activators.storages.EntityClickStorage;
import me.fromgate.reactions.activators.storages.ExecStorage;
import me.fromgate.reactions.activators.storages.FlightStorage;
import me.fromgate.reactions.activators.storages.GameModeStorage;
import me.fromgate.reactions.activators.storages.GodStorage;
import me.fromgate.reactions.activators.storages.InventoryClickStorage;
import me.fromgate.reactions.activators.storages.ItemClickStorage;
import me.fromgate.reactions.activators.storages.ItemConsumeStorage;
import me.fromgate.reactions.activators.storages.ItemHeldStorage;
import me.fromgate.reactions.activators.storages.JoinStorage;
import me.fromgate.reactions.activators.storages.LeverStorage;
import me.fromgate.reactions.activators.storages.MessageStorage;
import me.fromgate.reactions.activators.storages.MobClickStorage;
import me.fromgate.reactions.activators.storages.MobDamageStorage;
import me.fromgate.reactions.activators.storages.MobKillStorage;
import me.fromgate.reactions.activators.storages.PickupItemStorage;
import me.fromgate.reactions.activators.storages.PlateStorage;
import me.fromgate.reactions.activators.storages.ProjectileHitStorage;
import me.fromgate.reactions.activators.storages.PvpKillStorage;
import me.fromgate.reactions.activators.storages.QuitStorage;
import me.fromgate.reactions.activators.storages.RegionEnterStorage;
import me.fromgate.reactions.activators.storages.RegionLeaveStorage;
import me.fromgate.reactions.activators.storages.RegionStorage;
import me.fromgate.reactions.activators.storages.SignStorage;
import me.fromgate.reactions.activators.storages.SneakStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.activators.storages.TeleportStorage;
import me.fromgate.reactions.activators.storages.VariableStorage;
import me.fromgate.reactions.activators.triggers.ActivatorType;
import me.fromgate.reactions.activators.triggers.MessageTrigger;
import me.fromgate.reactions.activators.triggers.SignTrigger;
import me.fromgate.reactions.activators.triggers.Trigger;
import me.fromgate.reactions.commands.custom.FakeCommander;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.playerselector.SelectorsManager;
import me.fromgate.reactions.util.BlockUtils;
import me.fromgate.reactions.util.TimeUtils;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.enums.DeathCause;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.mob.EntityUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class StoragesManager {

    public Map<String, DataValue> raiseTeleportActivator(Player player, TeleportCause cause, Location to) {
        TeleportStorage storage = new TeleportStorage(player, cause, to);
        ActivatorsManager.getInstance().activate(storage);
        return storage.getChangeables();
    }

    public boolean raisePrecommandActivator(Player player, CommandSender sender, String fullCommand) {
        CommandStorage storage = new CommandStorage(player, sender, fullCommand);
        ActivatorsManager.getInstance().activate(storage);
        return storage.getChangeables().get(Storage.CANCEL_EVENT).asBoolean() | FakeCommander.raiseRaCommand(storage);
    }

    public boolean raiseMobClickActivator(Player player, LivingEntity mob) {
        if (mob == null) return false;
        MobClickStorage e = new MobClickStorage(player, mob);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public void raiseMobKillActivator(Player player, LivingEntity mob) {
        if (mob == null) return;
        MobKillStorage e = new MobKillStorage(player, mob);
        ActivatorsManager.getInstance().activate(e);
    }

    public void raiseJoinActivator(Player player, boolean joinfirst) {
        JoinStorage e = new JoinStorage(player, joinfirst);
        ActivatorsManager.getInstance().activate(e);
    }

    public boolean raiseDoorActivator(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if (!BlockUtils.isOpenable(event.getClickedBlock()) || event.getHand() != EquipmentSlot.HAND) return false;
        DoorStorage e = new DoorStorage(event.getPlayer(), BlockUtils.getBottomDoor(event.getClickedBlock()));
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseItemConsumeActivator(PlayerItemConsumeEvent event) {
        ItemConsumeStorage ce = new ItemConsumeStorage(event.getPlayer(), event.getItem(), event.getPlayer().getInventory().getItemInMainHand().isSimilar(event.getItem()));
        ActivatorsManager.getInstance().activate(ce);
        return ce.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseItemClickActivator(PlayerInteractEntityEvent event) {
        ItemClickStorage ice;
        boolean mainHand = event.getHand() == EquipmentSlot.HAND;
        ItemStack item = mainHand ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return false;
        ice = new ItemClickStorage(event.getPlayer(), item, mainHand);
        ActivatorsManager.getInstance().activate(ice);
        return ice.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseItemClickActivator(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        ItemClickStorage ice;
        boolean mainHand = event.getHand() == EquipmentSlot.HAND;
        ItemStack item = mainHand ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
        if (!ItemUtils.isExist(item)) return false;
        ice = new ItemClickStorage(event.getPlayer(), item, mainHand);
        ActivatorsManager.getInstance().activate(ice);
        return ice.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }


    public boolean raiseLeverActivator(PlayerInteractEvent event) {
        if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
            return false;
        if (event.getHand() != EquipmentSlot.HAND) return false;
        if (event.getClickedBlock().getType() != Material.LEVER) return false;
        LeverStorage e = new LeverStorage(event.getPlayer(), event.getClickedBlock());
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    // PVP Kill Event
    public void raisePvpKillActivator(PlayerDeathEvent event) {
        Player deadplayer = event.getEntity();
        Player killer = EntityUtils.getKiller(deadplayer.getLastDamageCause());
        if (killer == null) return;
        PvpKillStorage pe = new PvpKillStorage(killer, deadplayer);
        ActivatorsManager.getInstance().activate(pe);
    }

    // PVP Death Event
    public void raisePvpDeathActivator(PlayerDeathEvent event) {
        Player deadplayer = event.getEntity();
        LivingEntity killer = EntityUtils.getAnyKiller(deadplayer.getLastDamageCause());
        DeathCause ds = (killer == null) ? DeathCause.OTHER : (killer instanceof Player) ? DeathCause.PVP : DeathCause.PVE;
        DeathStorage pe = new DeathStorage(killer, deadplayer, ds);
        ActivatorsManager.getInstance().activate(pe);
    }

    // Button Event
    public boolean raiseButtonActivator(PlayerInteractEvent event) {
        if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
            return false;
        if (!Tag.BUTTONS.isTagged(event.getClickedBlock().getType())) return false;
        if (event.getHand() != EquipmentSlot.HAND) return false;
        Switch button = (Switch) event.getClickedBlock().getBlockData();
        if (button.isPowered()) return false;
        ButtonStorage be = new ButtonStorage(event.getPlayer(), event.getClickedBlock().getLocation());
        ActivatorsManager.getInstance().activate(be);
        return be.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseSignActivator(Player player, String[] lines, Location loc, boolean leftClick) {
        for (Trigger act : ActivatorsManager.getInstance().getActivators(ActivatorType.SIGN)) {
            SignTrigger sign = (SignTrigger) act;
            if (sign.checkMask(lines)) {
                SignStorage se = new SignStorage(player, lines, loc, leftClick);
                ActivatorsManager.getInstance().activate(se);
                return se.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
            }
        }
        return false;
    }

    // TODO: I think all of it should be inside ActionExecute class

    public boolean raiseExecActivator(CommandSender sender, String param) {
        if (param.isEmpty()) return false;
        return raiseExecActivator(sender, Parameters.fromString(param, "player"));
    }

    public boolean raiseExecActivator(CommandSender sender, Parameters param) {
        return raiseExecActivator(sender, param, null);
    }

    public boolean raiseExecActivator(CommandSender sender, Parameters param, Map<String, String> tempVars) {
        if (param.isEmpty()) return false;
        final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
        final String id = param.getString("activator", param.getString("exec"));
        if (id.isEmpty()) return false;
        Trigger act = ActivatorsManager.getInstance().getActivator(id);
        if (act == null) {
            Msg.logOnce("wrongact_" + id, "Failed to run exec activator " + id + ". Activator not found.");
            return false;
        }
        if (act.getType() != ActivatorType.EXEC) {
            Msg.logOnce("wrongactype_" + id, "Failed to run exec activator " + id + ". Wrong activator type.");
            return false;
        }

        int repeat = Math.min(param.getInteger("repeat", 1), 1);

        long delay = TimeUtils.timeToTicks(TimeUtils.parseTime(param.getString("delay", "1t")));

        final Set<Player> target = new HashSet<>();

        if (param.contains("player")) {
            target.addAll(SelectorsManager.getPlayerList(Parameters.fromString(param.getString("player"), "player")));
        }
        target.addAll(SelectorsManager.getPlayerList(param));   // Оставляем для совместимости со старым вариантом

        if (target.isEmpty() && !param.containsAny(SelectorsManager.getAllKeys())) target.add(senderPlayer);

        for (int i = 0; i < repeat; i++) {
            Bukkit.getScheduler().runTaskLater(ReActionsPlugin.getInstance(), () -> {
                for (Player player : target) {
                    if (ActivatorsManager.getInstance().isStopped(player, id, true)) continue;
                    ExecStorage ce = new ExecStorage(player, tempVars);
                    ActivatorsManager.getInstance().activate(ce, id);
                }
            }, delay * repeat);
        }
        return true;
    }

    public boolean raiseExecActivator(CommandSender sender, String id, Map<String, String> tempVars) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        Trigger act = ActivatorsManager.getInstance().getActivator(id);
        if (act == null) {
            Msg.logOnce("wrongact_" + id, "Failed to run exec activator " + id + ". Activator not found.");
            return false;
        }
        if (act.getType() != ActivatorType.EXEC) {
            Msg.logOnce("wrongactype_" + id, "Failed to run exec activator " + id + ". Wrong activator type.");
            return false;
        }
        if (ActivatorsManager.getInstance().isStopped(player, id, true)) return true;
        ExecStorage ce = new ExecStorage(player, tempVars);
        ActivatorsManager.getInstance().activate(ce, id);
        return true;
    }

    public boolean raisePlateActivator(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return false;
        if (!(event.getClickedBlock().getType().name().endsWith("_PRESSURE_PLATE"))) return false;
        PlateStorage pe = new PlateStorage(event.getPlayer(), event.getClickedBlock().getLocation());
        ActivatorsManager.getInstance().activate(pe);
        return pe.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public void raiseCuboidActivator(final Player player) {
        ActivatorsManager.getInstance().activate(new CuboidStorage(player));
    }

    public void raiseAllRegionActivators(final Player player, final Location to, final Location from) {
        if (!RaWorldGuard.isConnected()) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(ReActionsPlugin.getInstance(), () -> {

            final Set<String> regionsTo = RaWorldGuard.getRegions(to);
            final Set<String> regionsFrom = RaWorldGuard.getRegions(from);

            Bukkit.getScheduler().runTask(ReActionsPlugin.getInstance(), () -> {
                raiseRegionActivator(player, regionsTo);
                raiseRgEnterActivator(player, regionsTo, regionsFrom);
                raiseRgLeaveActivator(player, regionsTo, regionsFrom);
            });
        }, 1);
    }

    private void raiseRgEnterActivator(Player player, Set<String> regionTo, Set<String> regionFrom) {
        if (regionTo.isEmpty()) return;
        for (String rg : regionTo)
            if (!regionFrom.contains(rg)) {
                RegionEnterStorage wge = new RegionEnterStorage(player, rg);
                ActivatorsManager.getInstance().activate(wge);
            }
    }

    private void raiseRgLeaveActivator(Player player, Set<String> regionTo, Set<String> regionFrom) {
        if (regionFrom.isEmpty()) return;
        for (String rg : regionFrom)
            if (!regionTo.contains(rg)) {
                RegionLeaveStorage wge = new RegionLeaveStorage(player, rg);
                ActivatorsManager.getInstance().activate(wge);
            }
    }

    private void raiseRegionActivator(Player player, Set<String> to) {
        if (to.isEmpty()) return;
        for (String region : to) {
            setFutureRegionCheck(player.getName(), region, false);
        }
    }

    private void setFutureRegionCheck(final String playerName, final String region, boolean repeat) {
        Player player = Utils.getPlayerExact(playerName);
        if (player == null) return;
        if (!player.isOnline()) return;
        if (player.isDead()) return;
        if (!RaWorldGuard.isPlayerInRegion(player, region)) return;
        String rg = "rg-" + region;
        if (!isTimeToRaiseEvent(player, rg, Cfg.worldguardRecheck, repeat)) return;

        RegionStorage wge = new RegionStorage(player, region);
        ActivatorsManager.getInstance().activate(wge);

        Bukkit.getScheduler().runTaskLater(ReActionsPlugin.getInstance(), () -> setFutureRegionCheck(playerName, region, true), 20 * Cfg.worldguardRecheck);
    }

    public boolean isTimeToRaiseEvent(Player p, String id, int seconds, boolean repeat) {
        long curTime = System.currentTimeMillis();
        long prevTime = p.hasMetadata("reactions-rchk-" + id) ? p.getMetadata("reactions-rchk-" + id).get(0).asLong() : 0;
        boolean needUpdate = repeat || ((curTime - prevTime) >= (1000 * seconds));
        if (needUpdate) p.setMetadata("reactions-rchk-" + id, new FixedMetadataValue(ReActionsPlugin.getInstance(), curTime));
        return needUpdate;
    }

    // TODO: Redesign
    public Map<String, DataValue> raiseMessageActivator(CommandSender sender, MessageTrigger.Source source, String message) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        for (Trigger act : ActivatorsManager.getInstance().getActivators(ActivatorType.MESSAGE)) {
            MessageTrigger a = (MessageTrigger) act;
            if (a.filterMessage(source, message)) {
                MessageStorage me = new MessageStorage(player, a, message);
                ActivatorsManager.getInstance().activate(me);
                return me.getChangeables();
            }
        }
        return null;
    }

    public void raiseVariableActivator(String var, String playerName, String newValue, String prevValue) {
        if (newValue.equalsIgnoreCase(prevValue)) return;
        Player player = Utils.getPlayerExact(playerName);
        if (!playerName.isEmpty() && player == null) return;
        VariableStorage ve = new VariableStorage(player, var, newValue, prevValue);
        ActivatorsManager.getInstance().activate(ve);
    }

    public Map<String, DataValue> raiseMobDamageActivator(Player damager, LivingEntity entity, double damage, EntityDamageEvent.DamageCause cause) {
        MobDamageStorage mde = new MobDamageStorage(entity, damager, damage, cause);
        ActivatorsManager.getInstance().activate(mde);
        return mde.getChangeables();
    }

    public String raiseQuitActivator(PlayerQuitEvent event) {
        QuitStorage qu = new QuitStorage(event.getPlayer(), event.getQuitMessage());
        ActivatorsManager.getInstance().activate(qu);
        return qu.getChangeables().get(QuitStorage.QUIT_MESSAGE).asString();
    }

    public boolean raiseBlockClickActivator(PlayerInteractEvent event) {
        boolean leftClick;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) leftClick = false;
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) leftClick = true;
        else return false;
        if (event.getHand() != EquipmentSlot.HAND) return false;
        BlockClickStorage e = new BlockClickStorage(event.getPlayer(), event.getClickedBlock(), leftClick);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public Map<String, DataValue> raiseInventoryClickActivator(InventoryClickEvent event) {
        InventoryClickStorage e = new InventoryClickStorage((Player) event.getWhoClicked(), event.getAction(),
                event.getClick(), event.getInventory(), event.getSlotType(),
                event.getCurrentItem(), event.getHotbarButton(),
                event.getView(), event.getSlot());
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables();
    }

    public Map<String, DataValue> raiseDropActivator(Player player, Item item, int pickupDelay) {
        DropStorage e = new DropStorage(player, item, pickupDelay);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables();
    }

    public boolean raiseFlightActivator(Player player, boolean flying) {
        FlightStorage e = new FlightStorage(player, flying);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseEntityClickActivator(Player player, Entity rightClicked) {
        EntityClickStorage e = new EntityClickStorage(player, rightClicked);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public Map<String, DataValue> raiseBlockBreakActivator(Player player, Block block, boolean dropItems) {
        BlockBreakStorage e = new BlockBreakStorage(player, block, dropItems);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables();
    }

    public void raiseSneakActivator(PlayerToggleSneakEvent event) {
        SneakStorage e = new SneakStorage(event.getPlayer(), event.isSneaking());
        ActivatorsManager.getInstance().activate(e);
    }

    public Map<String, DataValue> raiseDamageByMobActivator(EntityDamageByEntityEvent event) {
        DamageByMobStorage dm = new DamageByMobStorage((Player) event.getEntity(), event.getDamager(), event.getDamage(), event.getCause());
        ActivatorsManager.getInstance().activate(dm);
        return dm.getChangeables();
    }

    public Map<String, DataValue> raiseDamageByBlockActivator(EntityDamageByBlockEvent event, Block blockDamager) {
        double damage = event.getDamage();
        DamageByBlockStorage db = new DamageByBlockStorage((Player) event.getEntity(), blockDamager, damage, event.getCause());
        ActivatorsManager.getInstance().activate(db);
        return db.getChangeables();
    }

    public Map<String, DataValue> raiseDamageActivator(EntityDamageEvent event, String source) {
        double damage = event.getDamage();
        DamageStorage de = new DamageStorage((Player) event.getEntity(), damage, event.getCause(), source);
        ActivatorsManager.getInstance().activate(de);
        return de.getChangeables();
    }

    // TODO
    public void raiseProjectileHitActivator(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        ProjectileHitStorage ph = new ProjectileHitStorage((Player) event.getEntity().getShooter(),
                event.getEntityType(),
                event.getHitBlock(), event.getHitBlockFace(),
                event.getHitEntity());
        ActivatorsManager.getInstance().activate(ph);
    }

    public Map<String, DataValue> raisePickupItemActivator(Player player, Item item, int pickupDelay) {
        PickupItemStorage e = new PickupItemStorage(player, item, pickupDelay);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables();
    }

    public boolean raiseGamemodeActivator(Player player, GameMode gameMode) {
        GameModeStorage e = new GameModeStorage(player, gameMode);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseGodActivator(Player player, boolean god) {
        GodStorage e = new GodStorage(player, god);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }

    public boolean raiseItemHeldActivator(Player player, int newSlot, int previousSlot) {
        ItemHeldStorage e = new ItemHeldStorage(player, newSlot, previousSlot);
        ActivatorsManager.getInstance().activate(e);
        return e.getChangeables().get(Storage.CANCEL_EVENT).asBoolean();
    }
}
