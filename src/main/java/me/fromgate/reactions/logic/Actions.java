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

package me.fromgate.reactions.logic;

import lombok.Getter;
import me.fromgate.reactions.logic.actions.Action;
import me.fromgate.reactions.logic.actions.ActionBack;
import me.fromgate.reactions.logic.actions.ActionBlockFill;
import me.fromgate.reactions.logic.actions.ActionBlockSet;
import me.fromgate.reactions.logic.actions.ActionBroadcast;
import me.fromgate.reactions.logic.actions.ActionCancelEvent;
import me.fromgate.reactions.logic.actions.ActionChange;
import me.fromgate.reactions.logic.actions.ActionChatMessage;
import me.fromgate.reactions.logic.actions.ActionClearRadius;
import me.fromgate.reactions.logic.actions.ActionClearRegion;
import me.fromgate.reactions.logic.actions.ActionCommand;
import me.fromgate.reactions.logic.actions.ActionDamage;
import me.fromgate.reactions.logic.actions.ActionDelay;
import me.fromgate.reactions.logic.actions.ActionDelayed;
import me.fromgate.reactions.logic.actions.ActionExecStop;
import me.fromgate.reactions.logic.actions.ActionExecUnstop;
import me.fromgate.reactions.logic.actions.ActionExecute;
import me.fromgate.reactions.logic.actions.ActionFile;
import me.fromgate.reactions.logic.actions.ActionFly;
import me.fromgate.reactions.logic.actions.ActionFlySpeed;
import me.fromgate.reactions.logic.actions.ActionGlide;
import me.fromgate.reactions.logic.actions.ActionGroupAdd;
import me.fromgate.reactions.logic.actions.ActionGroupRemove;
import me.fromgate.reactions.logic.actions.ActionHeal;
import me.fromgate.reactions.logic.actions.ActionIfElse;
import me.fromgate.reactions.logic.actions.ActionItems;
import me.fromgate.reactions.logic.actions.ActionItems.ItemActionType;
import me.fromgate.reactions.logic.actions.ActionLog;
import me.fromgate.reactions.logic.actions.ActionMenuItem;
import me.fromgate.reactions.logic.actions.ActionMessage;
import me.fromgate.reactions.logic.actions.ActionMobSpawn;
import me.fromgate.reactions.logic.actions.ActionMoneyGive;
import me.fromgate.reactions.logic.actions.ActionMoneyPay;
import me.fromgate.reactions.logic.actions.ActionPlayerId;
import me.fromgate.reactions.logic.actions.ActionPotion;
import me.fromgate.reactions.logic.actions.ActionPotionRemove;
import me.fromgate.reactions.logic.actions.ActionPowerSet;
import me.fromgate.reactions.logic.actions.ActionRegex;
import me.fromgate.reactions.logic.actions.ActionResponse;
import me.fromgate.reactions.logic.actions.ActionShoot;
import me.fromgate.reactions.logic.actions.ActionSignSet;
import me.fromgate.reactions.logic.actions.ActionSound;
import me.fromgate.reactions.logic.actions.ActionSql;
import me.fromgate.reactions.logic.actions.ActionTimer;
import me.fromgate.reactions.logic.actions.ActionTp;
import me.fromgate.reactions.logic.actions.ActionVar;
import me.fromgate.reactions.logic.actions.ActionVelocity;
import me.fromgate.reactions.logic.actions.ActionVelocityJump;
import me.fromgate.reactions.logic.actions.ActionWait;
import me.fromgate.reactions.logic.actions.ActionWalkSpeed;
import me.fromgate.reactions.logic.actions.ActionWeSuperPickaxe;
import me.fromgate.reactions.logic.actions.ActionWeToolControl;
import me.fromgate.reactions.logic.actions.StoredAction;
import me.fromgate.reactions.logic.activators.ActivatorBase;
import me.fromgate.reactions.placeholders.PlaceholdersManager;
import me.fromgate.reactions.time.TimeUtil;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.BukkitMessenger;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Will be irrelevant because of modules(externals) system
public enum Actions {
    /*
     TODO: More actions
     ActionSetLevel, ActionModifyString, ActionKill, ActionJavaScript(execute js file)
     ActionDynamic(for actions from placeholders), ActionCompassTarget,
     ActionReturn(stop execution of activator by some condition),
     ActionStatistic(editing player's stats)
    */
    TP("tp", new ActionTp(), true),
    VELOCITY("velocity", new ActionVelocity(), true),
    VELOCITY_JUMP("jump", new ActionVelocityJump(), true),
    SOUND("sound", new ActionSound()),
    POTION("potion", new ActionPotion(), true),
    POTION_REMOVE("rmvpot", new ActionPotionRemove(), true),
    GROUP_ADD("grpadd", new ActionGroupAdd(), true),
    GROUP_REMOVE("grprmv", new ActionGroupRemove(), true),
    MESSAGE("msg", new ActionMessage()),
    RESPONSE("rspns", new ActionResponse()),
    CHAT_MESSAGE("chat", new ActionChatMessage(), true),
    BROADCAST("msgall", new ActionBroadcast()),
    DAMAGE("dmg", new ActionDamage()),
    // TODO: Don't like it
    ITEM_GIVE("itemgive", new ActionItems(ItemActionType.GIVE_ITEM), true),
    ITEM_REMOVE("itemrmv", new ActionItems(ItemActionType.REMOVE_ITEM_HAND), true),
    ITEM_REMOVE_OFFHAND("itemrmvoffhand", new ActionItems(ItemActionType.REMOVE_ITEM_OFFHAND), true),
    ITEM_REMOVE_INVENTORY("invitemrmv", new ActionItems(ItemActionType.REMOVE_ITEM_INVENTORY), true),
    ITEM_DROP("itemdrop", new ActionItems(ItemActionType.DROP_ITEM), true),
    ITEM_WEAR("itemwear", new ActionItems(ItemActionType.WEAR_ITEM), true),
    ITEM_UNWEAR("itemundress", new ActionItems(ItemActionType.UNWEAR_ITEM), true),
    ITEM_SLOT("itemslot", new ActionItems(ItemActionType.SET_INVENTORY), true),
    ITEM_SLOT_VIEW("itemslotview", new ActionItems(ItemActionType.GET_INVENTORY), true),
    // *******************
    CMD("cmdplr", new ActionCommand(ActionCommand.NORMAL), true),
    CMD_OP("cmdop", new ActionCommand(ActionCommand.OP), true),
    CMD_CONSOLE("cmdsrv", new ActionCommand(ActionCommand.CONSOLE)),
    CMD_CHAT("cmdchat", new ActionCommand(ActionCommand.CHAT), true),
    MONEY_PAY("moneypay", new ActionMoneyPay()),
    MONEY_GIVE("moneygive", new ActionMoneyGive()),
    DELAY("delay", new ActionDelay(true)),
    DELAY_PLAYER("pdelay", new ActionDelay(false), true),
    BACK("back", new ActionBack(), true),
    MOB_SPAWN("mob", new ActionMobSpawn()),
    EXECUTE("run", new ActionExecute()),  /// ???? не уверен
    EXECUTE_STOP("stop", new ActionExecStop()),  /// ???? не уверен
    EXECUTE_UNSTOP("unstop", new ActionExecUnstop()),  /// ???? не уверен
    REGION_CLEAR("rgclear", new ActionClearRegion()),
    HEAL("hp", new ActionHeal()),
    BLOCK_SET("blockset", new ActionBlockSet()),
    BLOCK_FILL("blockfill", new ActionBlockFill()),
    SIGN_SET_LINE("sign", new ActionSignSet()),
    POWER_SET("power", new ActionPowerSet()),
    SHOOT("shoot", new ActionShoot(), true),
    VAR_SET("varset", new ActionVar(0, false)),
    VAR_CLEAR("varclr", new ActionVar(1, false)),
    VAR_INC("varinc", new ActionVar(2, false)),
    VAR_DEC("vardec", new ActionVar(3, false)),
    VAR_PLAYER_SET("varpset", new ActionVar(0, true), true),
    VAR_PLAYER_CLEAR("varpclr", new ActionVar(1, true), true),
    VAR_PLAYER_INC("varpinc", new ActionVar(2, true), true),
    VAR_PLAYER_DEC("varpdec", new ActionVar(3, true), true),
    VAR_TEMP_SET("tempset", new ActionVar(4, false)),
    CHANGE("chng", new ActionChange()),
    TIMER_STOP("timerstop", new ActionTimer(true)),
    TIMER_RESUME("timerresume", new ActionTimer(false)),
    CANCEL_EVENT("cancel", new ActionCancelEvent()),
    SQL_SELECT("sqlselect", new ActionSql(0)),
    SQL_UPDATE("sqlupdate", new ActionSql(2)),
    SQL_INSERT("sqlinsert", new ActionSql(1)),
    SQL_DELETE("sqldelete", new ActionSql(3)),
    SQL_SET("sqlset", new ActionSql(4)),
    REGEX("regex", new ActionRegex()),
    ACTION_DELAYED("actdelay", new ActionDelayed()),
    MENU_ITEM("itemmenu", new ActionMenuItem(), true),
    WAIT("wait", new ActionWait()),
    LOG("log", new ActionLog()),
    PLAYER_ID("playerid", new ActionPlayerId()),
    FILE("file", new ActionFile()),
    FLY("fly", new ActionFly()),
    GLIDE("glide", new ActionGlide()),
    WALK_SPEED("walkspeed", new ActionWalkSpeed()),
    FLY_SPEED("flyspeed", new ActionFlySpeed()),
    IF_ELSE("ifelse", new ActionIfElse()),
    WE_TOOLCONTROL("wetoolcontrol", new ActionWeToolControl(), true),
    WE_SUPERPICKAXE("wesuperpickaxe", new ActionWeSuperPickaxe(), true),
    RADIUS_CLEAR("clearradius", new ActionClearRadius(), true);

    private final static Map<String, Actions> BY_NAME;

    static {
        Map<String, Actions> byName = new HashMap<>();
        for (Actions act : Actions.values()) {
            byName.put(act.name(), act);
            byName.put(act.alias.toUpperCase(), act);
        }
        BY_NAME = Collections.unmodifiableMap(byName);
    }

    @Getter
    private final String alias;
    private final boolean requirePlayer;
    @Getter
    private final Action action;

    Actions(String alias, Action action, boolean requirePlayer) {
        this.alias = alias.toUpperCase();
        this.requirePlayer = requirePlayer;
        this.action = action;
        this.action.init(this);
    }

    Actions(String alias, Action action) {
        this(alias, action, false);
    }

    public static Actions getByName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    public static String getValidName(String name) {
        Actions act = getByName(name);
        if (act != null) return act.name();
        return name;
    }

    public static void executeActions(RaContext context, ActivatorBase act, boolean isAction) {
        List<StoredAction> actions = isAction ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return;
        executeActions(context, actions, isAction);
    }

    public static void executeActions(RaContext context, List<StoredAction> actions, boolean isAction) {
        for (int i = 0; i < actions.size(); i++) {
            StoredAction av = actions.get(i);
            if (av.getAction() == null) continue;
            Actions at = av.getAction();
            // TODO: Should be inside ActionWait
            if (at == Actions.WAIT) {
                if (i == actions.size() - 1) return;
                ActionWait aw = (ActionWait) at.action;
                Parameters param = new Parameters(PlaceholdersManager.replacePlaceholderButRaw(context, av.getValue()), "time");
                String timeStr = param.getParam("time", "0");
                long time = TimeUtil.parseTime(timeStr);
                if (time == 0) continue;
                List<StoredAction> futureList = new ArrayList<>(actions.subList(i + 1, actions.size()));
                aw.executeDelayed(context.getPlayer(), futureList, isAction, time);
                return;
            }
            at.performAction(context, isAction, new Parameters(PlaceholdersManager.replacePlaceholderButRaw(context, av.getValue())));
        }
    }

    public static boolean isValid(String name) {
        return getByName(name) != null;
    }

    public static void listActions(CommandSender sender, int pageNum) {
        List<String> actionList = new ArrayList<>();
        for (Actions actionType : Actions.values()) {
            String name = actionType.name();
            String alias = actionType.alias.equalsIgnoreCase(name) ? " " : " (" + actionType.alias + ") ";
            Msg msg = Msg.getByName("action_" + name);
            if (msg == null) {
                Msg.LNG_FAIL_ACTION_DESC.log(name);
            } else {
                actionList.add("&6" + name + "&e" + alias + "&3: &a" + msg.getText("NOCOLOR"));
            }
        }
        BukkitMessenger.printPage(sender, actionList, Msg.MSG_ACTIONLISTTITLE, pageNum);
    }

    public void performAction(RaContext context, boolean action, Parameters actionParam) {
        if (context.getPlayer() == null && this.requirePlayer) return;
        this.action.executeAction(context, action, actionParam);
    }
}
