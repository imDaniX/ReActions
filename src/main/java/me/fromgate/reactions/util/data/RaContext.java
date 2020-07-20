package me.fromgate.reactions.util.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RaContext {
    public final static RaContext EMPTY_CONTEXT = new RaContext("unknown", null, null, null);
    @Getter
    private final String activatorName;
    @Getter
    private final boolean async;
    @Getter
    private final Player player;
    @Getter
    private final Map<String, String> tempVariables;
    @Getter
    private final Map<String, DataValue> changeables;

    public RaContext(String activator, Map<String, String> tempVariables, Map<String, DataValue> changeables, Player player) {
        this(activator, tempVariables, changeables, player, false);
    }

    public RaContext(String activator, Map<String, String> tempVariables, Map<String, DataValue> changeables, Player player, boolean async) {
        this.tempVariables = tempVariables != null ? new HashMap<>(tempVariables) : new HashMap<>();
        this.activatorName = activator;
        if (changeables == null || changeables.isEmpty()) {
            this.changeables = Collections.emptyMap();
        } else {
            this.changeables = changeables;
            changeables.keySet().forEach(key -> this.tempVariables.put(key, changeables.get(key).asString()));
        }
        this.player = player;
        this.async = async;
    }

    public String getTempVariable(String key) {
        return tempVariables.get(key.toLowerCase(Locale.ENGLISH));
    }

    public String getTempVariable(String key, String def) {
        return tempVariables.getOrDefault(key.toLowerCase(Locale.ENGLISH), def);
    }

    public String setTempVariable(String key, String str) {
        return tempVariables.put(key.toLowerCase(Locale.ENGLISH), str);
    }

    public String setTempVariable(String key, Object obj) {
        return tempVariables.put(key.toLowerCase(Locale.ENGLISH), obj.toString());
    }

    public boolean setChangeable(String key, double value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        tempVariables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, String value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        tempVariables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, boolean value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        tempVariables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, Location value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        tempVariables.put(key, dataValue.asString());
        return true;
    }
}
