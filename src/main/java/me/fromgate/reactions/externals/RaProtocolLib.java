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

package me.fromgate.reactions.externals;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.experimental.UtilityClass;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.StoragesManager;
import me.fromgate.reactions.module.defaults.activators.MessageActivator.Source;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class RaProtocolLib {
    private final Pattern TEXT = Pattern.compile("^\\{\"text\":\".*\"\\}");
    private final Pattern TEXT_START = Pattern.compile("^\\{\"text\":\"");
    private final Pattern TEXT_END = Pattern.compile("\"\\}$");

    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void connectProtocolLib() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            connected = true;
        } else return;
        initPacketListener();
        ReActions.getPlugin().getLogger().info("ProtocolLib connected");

    }

    private String jsonToString(JSONObject source) {
        StringBuilder result = new StringBuilder();
        for (Object key : source.keySet()) {
            Object value = source.get(key);
            if (value instanceof String) {
                if ((key instanceof String) && (!((String) key).equalsIgnoreCase("text"))) continue;
                result.append(value);
            } else if (value instanceof JSONObject) {
                result.append(jsonToString((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.append(jsonToString((JSONArray) value));
            }
        }
        return result.toString();
    }

    private String jsonToString(JSONArray source) {
        StringBuilder result = new StringBuilder();
        for (Object value : source) {
            if (value instanceof String) {
                result.append(value);
            } else if (value instanceof JSONObject) {
                result.append(jsonToString((JSONObject) value));
            } else if (value instanceof JSONArray) {
                result.append(jsonToString((JSONArray) value));
            }
        }
        return result.toString();
    }

    private String jsonToString(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        if (jsonObject == null || json.isEmpty()) return json;
        JSONArray array = (JSONArray) jsonObject.get("extra");
        if (array == null || array.isEmpty()) return json;
        return jsonToString(array);
    }

    private String textToString(String message) {
        String text = message;
        if (TEXT.matcher(text).matches()) {
            text = TEXT_START.matcher(text).replaceAll("");
            text = TEXT_END.matcher(text).replaceAll("");
        }
        return ChatColor.stripColor(text);
    }


    private void initPacketListener() {
        if (!connected) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(ReActions.getPlugin(), PacketType.Play.Server.CHAT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        String message = "";
                        try {
                            String jsonMessage = event.getPacket().getChatComponents().getValues().get(0).getJson();
                            if (jsonMessage != null) message = jsonToString(jsonMessage);
                        } catch (Throwable ignore) {
                        }
                        if (message.isEmpty() && event.getPacket().getStrings().size() > 0) {
                            String jsonMessage = event.getPacket().getStrings().read(0);
                            if (jsonMessage != null) message = textToString(jsonMessage);
                        }
                        if (message.isEmpty()) return;
                        Map<String, DataValue> changeables = StoragesManager.triggerMessage(event.getPlayer(), Source.CHAT_OUTPUT, message);
                        if (changeables != null && changeables.get(Storage.CANCEL_EVENT).asBoolean())
                            event.setCancelled(true);

                    }
                });
    }


}
