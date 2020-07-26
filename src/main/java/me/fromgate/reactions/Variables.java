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

package me.fromgate.reactions;

import me.fromgate.reactions.logic.StoragesManager;
import me.fromgate.reactions.util.CaseInsensitiveMap;
import me.fromgate.reactions.util.FileUtils;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Variables {
    // TODO: Something like classes and objects that just contains variables - actually just global variables

    private static Map<String, String> vars = new CaseInsensitiveMap<>();

    private static String varId(Player player, String var) {
        return (player == null ? "general." + var : player.getName() + "." + var);
    }

    private static String varId(String player, String var) {
        return (player.isEmpty() ? "general." + var : player + "." + var);
    }

    public static void setVar(String player, String var, String value) {
        String prevVal = Variables.getVariable(player, var, "");
        vars.put(varId(player, var), value);
        if (!Cfg.playerSelfVarFile) save();
        else save(player);
        StoragesManager.raiseVariableActivator(var, player, value, prevVal);
    }

    public static boolean clearVar(String player, String var) {
        String prevVal = Variables.getVariable(player, var, "");
        String id = varId(player, var);
        if (!vars.containsKey(id)) return false;
        vars.remove(id);
        if (!Cfg.playerSelfVarFile) save();
        else save(player);
        StoragesManager.raiseVariableActivator(var, player, "", prevVal);
        return true;
    }

    public static String getVariable(String player, String var) {
        return vars.get(varId(player, var));
    }

    public static String getVariable(Player player, String var) {
        return vars.get(varId(player, var));
    }

        public static String getVariable(String player, String var, String defvar) {
        return vars.getOrDefault(varId(player, var), defvar);
    }

    public static String getVariable(Player player, String var, String defvar) {
        return vars.getOrDefault(varId(player, var), defvar);
    }

    public static boolean compareVariable(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        String value = getVariable(playerName, var, "");
        if (NumberUtils.isNumber(cmpvalue, value)) return (Double.parseDouble(cmpvalue) == Double.parseDouble(value));
        return value.equalsIgnoreCase(cmpvalue);
    }

    public static boolean cmpGreaterVar(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        if (!NumberUtils.isNumber(vars.get(id), cmpvalue)) return false;
        return Double.parseDouble(vars.get(id)) > Double.parseDouble(cmpvalue);
    }

    public static boolean cmpLowerVar(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        if (!NumberUtils.isNumber(vars.get(id), cmpvalue)) return false;
        return Double.parseDouble(vars.get(id)) < Double.parseDouble(cmpvalue);
    }

    public static boolean existVar(String playerName, String var) {
        return (vars.containsKey(varId(playerName, var)));
    }

    public static boolean incVar(String player, String var, double addValue) {
        String id = varId(player, var);
        if (!vars.containsKey(id)) setVar(player, var, "0");
        String valueStr = vars.get(id);
        if (!NumberUtils.isNumber(valueStr)) return false;
        setVar(player, var, String.valueOf(Double.parseDouble(valueStr) + addValue));
        return true;
    }


    public static boolean decVar(String player, String var, double decValue) {
        return incVar(player, var, decValue * (-1));
    }

    public static void save() {
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables.yml");
        for (String key : vars.keySet())
            cfg.set(key, vars.get(key));
        FileUtils.saveCfg(cfg, f, "Failed to save variables configuration file");
    }

    public static void save(String player) {
        if (Cfg.playerAsynchSaveSelfVarFile) saveAsync(player);
        else savePlayer(player);
    }

    public static void savePlayer(String player) {
        YamlConfiguration cfg = new YamlConfiguration();
        String varDir = ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables";
        File dir = new File(varDir);
        if (!dir.exists() && !dir.mkdirs()) return;
        saveGeneral();
        if (player == null || player.isEmpty()) return;
        UUID id = Utils.getUUID(player);
        if (id == null) return;
        File f = new File(varDir + File.separator + id.toString() + ".yml");
        for (String key : vars.keySet()) {
            if (key.contains(player)) cfg.set(key, vars.get(key));
        }
        if (FileUtils.saveCfg(cfg, f, "Failed to save variable configuration file"))
            removePlayerVars(player);
    }

    public static void saveAsync(String player) {
        JavaPlugin pluginInstance = ReActionsPlugin.getInstance();
        pluginInstance.getServer().getScheduler().runTaskAsynchronously(pluginInstance, () -> savePlayer(player));
    }

    private static void saveGeneral() {
        YamlConfiguration cfg = new YamlConfiguration();
        String varDir = ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables";
        File f = new File(varDir + File.separator + "general.yml");
        for (String key : vars.keySet())
            if (key.contains("general")) cfg.set(key, vars.get(key));
        FileUtils.saveCfg(cfg, f, "Failed to save variable configuration file");
    }

    public static void load() {
        vars.clear();
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File f = new File(ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables.yml");
            if (!f.exists()) return;
            cfg.load(f);
            for (String key : cfg.getKeys(true)) {
                if (!key.contains(".")) continue;
                vars.put(key, cfg.getString(key));
            }
            if (!Cfg.playerSelfVarFile) {
                loadVars();
                File dir = new File(ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables");
                if (!dir.exists() || !dir.isDirectory()) return;
                String[] files = dir.list();
                for (String file : files) {
                    File fl = new File(dir, file);
                    fl.delete();
                }
                dir.delete();
            }
        } catch (Exception ignored) {
        }
    }

    public static void loadVars() {
        if (Cfg.playerSelfVarFile) load();
        try {
            int deleted = 0;
            YamlConfiguration cfg = new YamlConfiguration();
            File dir = new File(ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables");
            if (!dir.exists()) return;
            for (File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    if (f.length() == 0) {
                        f.delete();
                        deleted++;
                        continue;
                    }
                    String fstr = f.getName();
                    if (fstr.endsWith(".yml")) {
                        cfg.load(f);
                        for (String key : cfg.getKeys(true)) {
                            if (key.contains(".")) vars.put(key, cfg.getString(key));
                        }
                    }
                }
            }
            Msg.logMessage("Deleted " + deleted + " variable files.");
        } catch (Exception ignored) {
        }
    }

    private static void removePlayerVars(String player) {
        Map<String, String> varsTmp = new CaseInsensitiveMap<>();
        YamlConfiguration cfg = new YamlConfiguration();
        String fileName = ReActionsPlugin.getInstance().getDataFolder() + File.separator + "variables.yml";
        File f = new File(fileName);
        if (!f.exists()) return;
        if (!FileUtils.loadCfg(cfg, f, "Failed to load variable file")) return;
        for (String key : cfg.getKeys(true)) {
            if (!key.contains(".")) continue;
            if (key.contains(player) || key.contains("general")) continue;
            varsTmp.put(key, cfg.getString(key));
        }

        YamlConfiguration cfg2 = new YamlConfiguration();
        for (String key : varsTmp.keySet())
            cfg2.set(key, varsTmp.get(key));
        if (!FileUtils.saveCfg(cfg2, f, "Failed to save variable file")) return;
        varsTmp.clear();
    }

    public static void printList(CommandSender sender, int pageNum, String mask) {
        int linesPerPage = (sender instanceof Player) ? 15 : 10000;
        List<String> varList = new ArrayList<>();
        for (String key : vars.keySet()) {
            if (mask.isEmpty() || key.contains(mask)) {
                varList.add(key + " : " + vars.get(key));
            }
        }
        Msg.printPage(sender, varList, Msg.MSG_VARLIST, pageNum, linesPerPage);
    }

    public static boolean matchVar(String playerName, String var, String value) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        String varValue = vars.get(id);
        return varValue.matches(value);
    }
}
