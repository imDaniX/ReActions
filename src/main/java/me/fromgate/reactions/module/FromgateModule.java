package me.fromgate.reactions.module;

import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.logic.activity.flags.Flag;
import me.fromgate.reactions.module.defaults.actions.ActionBack;
import me.fromgate.reactions.module.defaults.actions.ActionBlockFill;
import me.fromgate.reactions.module.defaults.actions.ActionBlockSet;
import me.fromgate.reactions.module.defaults.actions.ActionBroadcast;
import me.fromgate.reactions.module.defaults.actions.ActionCancelEvent;
import me.fromgate.reactions.module.defaults.actions.ActionChange;
import me.fromgate.reactions.module.defaults.actions.ActionChatMessage;
import me.fromgate.reactions.module.defaults.actions.ActionClearRadius;
import me.fromgate.reactions.module.defaults.actions.ActionClearRegion;
import me.fromgate.reactions.module.defaults.actions.ActionCommand;
import me.fromgate.reactions.module.defaults.actions.ActionDamage;
import me.fromgate.reactions.module.defaults.actions.ActionDelay;
import me.fromgate.reactions.module.defaults.actions.ActionDelayed;
import me.fromgate.reactions.module.defaults.actions.ActionExecStop;
import me.fromgate.reactions.module.defaults.actions.ActionExecUnstop;
import me.fromgate.reactions.module.defaults.actions.ActionExecute;
import me.fromgate.reactions.module.defaults.actions.ActionFile;
import me.fromgate.reactions.module.defaults.actions.ActionFly;
import me.fromgate.reactions.module.defaults.actions.ActionFlySpeed;
import me.fromgate.reactions.module.defaults.actions.ActionGlide;
import me.fromgate.reactions.module.defaults.actions.ActionGroupAdd;
import me.fromgate.reactions.module.defaults.actions.ActionGroupRemove;
import me.fromgate.reactions.module.defaults.actions.ActionHeal;
import me.fromgate.reactions.module.defaults.actions.ActionIfElse;
import me.fromgate.reactions.module.defaults.actions.ActionItems;
import me.fromgate.reactions.module.defaults.actions.ActionLog;
import me.fromgate.reactions.module.defaults.actions.ActionMenuItem;
import me.fromgate.reactions.module.defaults.actions.ActionMessage;
import me.fromgate.reactions.module.defaults.actions.ActionMobSpawn;
import me.fromgate.reactions.module.defaults.actions.ActionMoneyGive;
import me.fromgate.reactions.module.defaults.actions.ActionMoneyPay;
import me.fromgate.reactions.module.defaults.actions.ActionPlayerId;
import me.fromgate.reactions.module.defaults.actions.ActionPotion;
import me.fromgate.reactions.module.defaults.actions.ActionPotionRemove;
import me.fromgate.reactions.module.defaults.actions.ActionPowerSet;
import me.fromgate.reactions.module.defaults.actions.ActionRegex;
import me.fromgate.reactions.module.defaults.actions.ActionResponse;
import me.fromgate.reactions.module.defaults.actions.ActionShoot;
import me.fromgate.reactions.module.defaults.actions.ActionSignSet;
import me.fromgate.reactions.module.defaults.actions.ActionSound;
import me.fromgate.reactions.module.defaults.actions.ActionSql;
import me.fromgate.reactions.module.defaults.actions.ActionTimer;
import me.fromgate.reactions.module.defaults.actions.ActionTp;
import me.fromgate.reactions.module.defaults.actions.ActionVar;
import me.fromgate.reactions.module.defaults.actions.ActionVelocity;
import me.fromgate.reactions.module.defaults.actions.ActionVelocityJump;
import me.fromgate.reactions.module.defaults.actions.ActionWait;
import me.fromgate.reactions.module.defaults.actions.ActionWalkSpeed;
import me.fromgate.reactions.module.defaults.actions.ActionWeSuperPickaxe;
import me.fromgate.reactions.module.defaults.actions.ActionWeToolControl;
import me.fromgate.reactions.placeholders.Placeholder;
import me.fromgate.reactions.selectors.Selector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FromgateModule implements Module {
    @Override
    public @NotNull String getName() {
        return "reactions";
    }

    @Override
    public @NotNull String @NotNull [] getAuthors() {
        return new String[] {"fromgate", "MaxDikiy", "imDaniX"};
    }

    @Override
    public @NotNull Collection<ActivatorType> getActivatorTypes() {
        // TODO
        return Module.super.getActivatorTypes();
    }

    @Override
    public @NotNull Collection<Action> getActions() {
        // TODO Split actions one-by-one? Or implement MultiAction?..
        return List.of(
                new ActionTp(),
                new ActionVelocity(),
                new ActionVelocityJump(),
                new ActionSound(),
                new ActionPotion(),
                new ActionPotionRemove(),
                new ActionGroupAdd(),
                new ActionGroupRemove(),
                new ActionMessage(),
                new ActionResponse(),
                new ActionChatMessage(),
                new ActionBroadcast(),
                new ActionDamage(),
                new ActionItems(ActionItems.Type.GIVE_ITEM),
                new ActionItems(ActionItems.Type.REMOVE_ITEM_HAND),
                new ActionItems(ActionItems.Type.REMOVE_ITEM_OFFHAND),
                new ActionItems(ActionItems.Type.REMOVE_ITEM_INVENTORY),
                new ActionItems(ActionItems.Type.DROP_ITEM),
                new ActionItems(ActionItems.Type.WEAR_ITEM),
                new ActionItems(ActionItems.Type.UNWEAR_ITEM),
                new ActionItems(ActionItems.Type.SET_INVENTORY),
                new ActionItems(ActionItems.Type.GET_INVENTORY),
                new ActionCommand(ActionCommand.Type.NORMAL),
                new ActionCommand(ActionCommand.Type.OP),
                new ActionCommand(ActionCommand.Type.CONSOLE),
                new ActionCommand(ActionCommand.Type.CHAT),
                new ActionMoneyPay(),
                new ActionMoneyGive(),
                new ActionDelay(true),
                new ActionDelay(false),
                new ActionBack(),
                new ActionMobSpawn(),
                new ActionExecute(),
                new ActionExecStop(),
                new ActionExecUnstop(),
                new ActionClearRegion(),
                new ActionHeal(),
                new ActionBlockSet(),
                new ActionBlockFill(),
                new ActionSignSet(),
                new ActionPowerSet(),
                new ActionShoot(),
                new ActionVar(ActionVar.Type.SET, false),
                new ActionVar(ActionVar.Type.CLEAR, false),
                new ActionVar(ActionVar.Type.INCREASE, false),
                new ActionVar(ActionVar.Type.DECREASE, false),
                new ActionVar(ActionVar.Type.SET, true),
                new ActionVar(ActionVar.Type.CLEAR, true),
                new ActionVar(ActionVar.Type.INCREASE, true),
                new ActionVar(ActionVar.Type.DECREASE, true),
                new ActionVar(ActionVar.Type.TEMPORARY_SET, false),
                new ActionChange(),
                new ActionTimer(true),
                new ActionTimer(false),
                new ActionCancelEvent(),
                new ActionSql(ActionSql.Type.SELECT),
                new ActionSql(ActionSql.Type.UPDATE),
                new ActionSql(ActionSql.Type.INSERT),
                new ActionSql(ActionSql.Type.DELETE),
                new ActionSql(ActionSql.Type.SET),
                new ActionRegex(),
                new ActionDelayed(),
                new ActionMenuItem(),
                new ActionWait(),
                new ActionLog(),
                new ActionPlayerId(),
                new ActionFile(),
                new ActionFly(),
                new ActionGlide(),
                new ActionWalkSpeed(),
                new ActionFlySpeed(),
                new ActionIfElse(),
                new ActionWeToolControl(),
                new ActionWeSuperPickaxe(),
                new ActionClearRadius()
        );
    }

    @Override
    public @NotNull Collection<Flag> getFlags() {
        // TODO
        return Module.super.getFlags();
    }

    @Override
    public @NotNull Collection<Placeholder> getPlaceholders() {
        // TODO
        return Module.super.getPlaceholders();
    }

    @Override
    public @NotNull Collection<Selector> getSelectors() {
        // TODO
        return Module.super.getSelectors();
    }
}
