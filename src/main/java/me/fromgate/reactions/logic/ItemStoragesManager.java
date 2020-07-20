package me.fromgate.reactions.logic;

import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.logic.activators.ItemHoldActivator;
import me.fromgate.reactions.logic.activators.ItemWearActivator;
import me.fromgate.reactions.logic.storages.ItemHoldStorage;
import me.fromgate.reactions.logic.storages.ItemWearStorage;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * To manage some item-related activators
 */
public class ItemStoragesManager {
    // TODO: Recode.

    private static void setFutureItemWearCheck(final UUID playerId, final String itemStr, boolean repeat) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;
        if (!player.isOnline()) return;
        String rg = "iw-" + itemStr;
        if (!StoragesManager.isTimeToRaiseEvent(player, rg, Cfg.itemWearRecheck, repeat)) return;
        ItemWearStorage iwe = new ItemWearStorage(player);
        if (!iwe.isItemWeared(itemStr)) return;
        ActivatorsManager.activate(iwe);
        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureItemWearCheck(playerId, itemStr, true), 20 * Cfg.itemWearRecheck);
    }

    public static void raiseItemWearActivator(Player player) {
        final UUID playerId = player.getUniqueId();
        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
            for (Activator iw : ActivatorsManager.getActivators(ActivatorType.ITEM_WEAR))
                setFutureItemWearCheck(playerId, ((ItemWearActivator) iw).getItemStr(), false);
        }, 1);
    }

    public static void raiseItemHoldActivator(Player player) {
        final UUID playerId = player.getUniqueId();
        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
            for (Activator ih : ActivatorsManager.getActivators(ActivatorType.ITEM_HOLD))
                setFutureItemHoldCheck(playerId, ((ItemHoldActivator) ih).getItemStr(), false);
        }, 1);
    }

    private static void setFutureItemHoldCheck(final UUID playerId, final String itemStr, boolean repeat) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null || !player.isOnline() || player.isDead()) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtil.isExist(item)) return;
        String rg = "ih-" + itemStr;
        if (!StoragesManager.isTimeToRaiseEvent(player, rg, Cfg.itemHoldRecheck, repeat)) return;
        if (!ItemUtil.compareItemStr(item, itemStr)) return;
        ItemHoldStorage ihe = new ItemHoldStorage(player, item, true);
        ActivatorsManager.activate(ihe);

        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureItemHoldCheck(playerId, itemStr, true), 20 * Cfg.itemHoldRecheck);
    }
}
