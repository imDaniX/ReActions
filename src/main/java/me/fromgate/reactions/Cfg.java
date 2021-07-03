package me.fromgate.reactions;

import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.Shoot;
import org.bukkit.configuration.file.FileConfiguration;

// TODO: Fully rework. Please.
public class Cfg {

    public static boolean debugMode = false;
    public static boolean saveEmptySections = false;
    public static String actionMsg = "tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
    public static String language = "english";
    public static boolean languageSave = false;
    public static boolean checkUpdates = false;
    public static boolean centerTpCoords = true;
    public static int worldguardRecheck = 2;
    public static int itemHoldRecheck = 2;
    public static int itemWearRecheck = 2;
    public static boolean horizontalPushback = false;
    public static int chatLength = 55;
    public static boolean playerSelfVarFile = false;
    public static boolean playerAsynchSaveSelfVarFile = false;
    public static boolean playerMoveTaskUse = false;
    public static int playerMoveTaskTick = 5;
    public static boolean godActivatorEnable = false; // experimental, disabled by default
    public static int godActivatorCheckTicks = 10;
    public static boolean altOperator = false; // experimental, disabled by default
    private static final FileConfiguration config;

    static {
        config = ReActions.getPlugin().getConfig();
    }

    public static void save() {
        config.set("general.language", language);
        config.set("general.check-updates", checkUpdates);
        config.set("general.debug", debugMode);
        config.set("general.player-self-variable-file", playerSelfVarFile);
        config.set("general.player-asynch-save-self-variable-file", playerAsynchSaveSelfVarFile);
        config.set("general.player-move-event.use-task", playerMoveTaskUse);
        config.set("general.player-move-event.task-tick", playerMoveTaskTick);
        config.set("general.placeholder-limit", ReActions.getPlaceholders().getCountLimit());
        config.set("general.waiter-hours-limit", WaitingManager.getTimeLimit());
        config.set("reactions.activators.god.enable", godActivatorEnable);
        config.set("reactions.activators.god.recheck-ticks", godActivatorCheckTicks);
        config.set("reactions.save-empty-actions-and-flags-sections", saveEmptySections);
        config.set("reactions.show-messages-for-actions", actionMsg);
        config.set("reactions.center-player-teleport", centerTpCoords);
        config.set("reactions.region-recheck-delay", worldguardRecheck);
        config.set("reactions.item-hold-recheck-delay", itemHoldRecheck);
        config.set("reactions.item-wear-recheck-delay", itemWearRecheck);
        config.set("reactions.horizontal-pushback-action", horizontalPushback);
        config.set("reactions.default-chat-line-length", chatLength);
        config.set("actions.shoot.break-block", Shoot.actionShootBreak);
        config.set("actions.shoot.penetrable", Shoot.actionShootThrough);
        config.set("actions.cmd_op.proxy-operator", false);

        ReActions.getPlugin().saveConfig();
    }

    public static void load() {
        language = config.getString("general.language", "english");
        checkUpdates = config.getBoolean("general.check-updates", true);
        languageSave = config.getBoolean("general.language-save", false);
        debugMode = config.getBoolean("general.debug", false);
        playerSelfVarFile = config.getBoolean("general.player-self-variable-file", false);
        playerAsynchSaveSelfVarFile = config.getBoolean("general.player-asynch-save-self-variable-file", false);
        playerMoveTaskUse = config.getBoolean("general.player-move-event.use-task", false);
        playerMoveTaskTick = config.getInt("general.player-move-event.task-tick", 5);
        ReActions.getPlaceholders().setCountLimit(config.getInt("general.placeholder-limit", 127));
        WaitingManager.setTimeLimit(config.getInt("general.waiter-hours-limit", 720));
        godActivatorEnable = config.getBoolean("reactions.activators.god.enable", true);
        godActivatorCheckTicks = config.getInt("reactions.activators.god.recheck-ticks", 10);
        chatLength = config.getInt("reactions.default-chat-line-length", 55);
        saveEmptySections = config.getBoolean("reactions.save-empty-actions-and-flags-sections", false);
        centerTpCoords = config.getBoolean("reactions.center-player-teleport", true);
        actionMsg = config.getString("reactions.show-messages-for-actions", "tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
        worldguardRecheck = config.getInt("reactions.region-recheck-delay", 2);
        itemHoldRecheck = config.getInt("reactions.item-hold-recheck-delay", 2);
        itemWearRecheck = config.getInt("reactions.item-wear-recheck-delay", 2);
        horizontalPushback = config.getBoolean("reactions.horizontal-pushback-action", false);
        Shoot.actionShootBreak = config.getString("actions.shoot.break-block", Shoot.actionShootBreak);
        Shoot.actionShootThrough = config.getString("actions.shoot.penetrable", Shoot.actionShootThrough);
        altOperator = config.getBoolean("actions.cmd_op.proxy-operator", false);
    }
}
