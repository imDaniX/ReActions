package me.fromgate.reactions.util.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class RaContext {
    public final static RaContext EMPTY_CONTEXT = new RaContext(":unknown", null, null, null);
    String activatorName;
    Player player;
    Map<String, String> variables;
    Map<String, DataValue> changeables;
    boolean async;

    public RaContext(String activator, Map<String, String> variables, Map<String, DataValue> changeables, Player player) {
        this(activator, variables, changeables, player, false);
    }

    public RaContext(String activator, Map<String, String> variables, Map<String, DataValue> changeables, Player player, boolean async) {
        this.variables = variables != null ? new HashMap<>(variables) : new HashMap<>();
        this.activatorName = activator;
        if (changeables == null || changeables.isEmpty()) {
            this.changeables = Collections.emptyMap();
        } else {
            this.changeables = changeables;
            changeables.keySet().forEach(key -> this.variables.put(key, changeables.get(key).asString()));
        }
        this.player = player;
        this.async = async;
    }

    public String getVariable(String key) {
        return variables.get(key.toLowerCase(Locale.ENGLISH));
    }

    public String getVariable(String key, String def) {
        return variables.getOrDefault(key.toLowerCase(Locale.ENGLISH), def);
    }

    public String setVariable(String key, String str) {
        return variables.put(key.toLowerCase(Locale.ENGLISH), str);
    }

    public String setVariable(String key, Object obj) {
        return variables.put(key.toLowerCase(Locale.ENGLISH), obj.toString());
    }

    public boolean setChangeable(String key, double value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        variables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, String value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        variables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, boolean value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        variables.put(key, dataValue.asString());
        return true;
    }

    public boolean setChangeable(String key, Location value) {
        key = key.toLowerCase(Locale.ENGLISH);
        DataValue dataValue = changeables.get(key);
        if (dataValue == null || !dataValue.set(value)) return false;
        variables.put(key, dataValue.asString());
        return true;
    }
}
