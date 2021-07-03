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

package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.selectors.SelectorsManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ActionLog extends Action {

    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void saveToFile(RaContext context, String fileName, String message) {
        File path = new File("");
        String dir = path.getAbsolutePath();

        File file = new File(dir + "/" + fileName);
        context.setVariable("fullpath", file.getAbsolutePath());
        if (fileName.isEmpty()) return;

        Date date = new Date();
        String d = DATE_FORMAT.format(date);
        try {
            if (fileName.contains("/")) {
                String ph = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\") + 1);
                File fileDir = new File(ph);
                if (!fileDir.exists() && !fileDir.mkdirs()) return;
            }
            if (!file.exists()) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.close();
            }

            if (file.isFile()) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append("[").append(d).append("] ").append(message).append("\n");
                bw.close();
            }

        } catch (IOException e) {
            context.setVariable("logdebug", e.getLocalizedMessage());
        }
    }

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        if (params.containsAny("prefix", "color", "file")) {
            String plg_name = ReActions.getPlugin().getDescription().getName();
            boolean prefix = params.getBoolean("prefix", true);
            boolean color = params.getBoolean("color", false);
            String file = params.getString("file", "");
            String message = params.getString("text", removeParams(params.toString()));
            if (message.isEmpty()) return false;
            if (file.isEmpty()) {
                if (prefix) {
                    this.log(message, plg_name, color);
                } else this.log(message, "", color);
            } else {
                saveToFile(context, file, message);
            }
        } else Msg.logMessage(params.toString());

        return true;
    }

    @Override
    public @NotNull String getName() {
        return "LOG";
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    private String removeParams(String message) {
        StringBuilder sb = new StringBuilder("(?i)(");
        sb.append(String.join("|", SelectorsManager.getAllKeys()));
        sb.append("|hide|prefix|color|file):(\\{.*\\}|\\S+)\\s{0,1}");
        return message.replaceAll(sb.toString(), "");

    }

    private void log(String msg, String prefix, boolean color) {
        String px = "";
        if (!prefix.isEmpty()) px = "[" + prefix + "] ";
        if (color) LOGGER.info(ChatColor.translateAlternateColorCodes('&', px + msg));
        else LOGGER.info(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', px + msg)));
    }
}
