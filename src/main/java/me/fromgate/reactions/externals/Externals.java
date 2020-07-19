package me.fromgate.reactions.externals;

import me.fromgate.reactions.externals.placeholderapi.RaPlaceholderAPI;
import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import org.bukkit.Bukkit;

// TODO: Externals system looks terrible, should be more complex
// Maybe external jar modules, that will be registered from folder?
public class Externals {

    private static boolean factions = false;

    //разные переменные
    private static boolean townyConnected = false;

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                RaPlaceholderAPI.init();
            } catch (Throwable ignore) {
            }
        }


        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            try {
                RaProtocolLib.connectProtocolLib();
            } catch (Throwable ignore) {
            }
        }

        // TODO: Actions with WorldEdit API - placing blocks, working with schematics
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            try {
                RaWorldEdit.init();
            } catch (Throwable ignore) {
            }
        }

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                RaWorldGuard.init();
            } catch (Throwable ignore) {
            }
        }

        // TODO: Essentials support (homes, warps, native god check)
    }

    public static boolean isConnectedFactions() {
        return factions;
    }

    public static boolean isTownyConnected() {
        return townyConnected;
    }

}
