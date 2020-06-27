package me.fromgate.reactions.util.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RaContext {
	public final static RaContext EMPTY_CONTEXT = new RaContext(null, null, null);
	@Getter private final boolean async;
	@Getter private final Player player;
	@Getter private final Map<String, String> tempVariables;
	@Getter private final Map<String, DataValue> changeables;

	public RaContext(Map<String, String> tempVariables, Map<String, DataValue> changeables, Player player) {
		this.tempVariables = tempVariables != null ? new HashMap<>(tempVariables) : new HashMap<>();
		if(changeables == null || changeables.isEmpty()) {
			this.changeables = Collections.emptyMap();
		} else {
			this.changeables = changeables;
			changeables.keySet().forEach(key -> this.tempVariables.put(key, changeables.get(key).asString()));
		}
		this.player = player;
		this.async = false;
	}

	public RaContext(Map<String, String> tempVariables, Map<String, DataValue> changeables, Player player, boolean async) {
		this.tempVariables = tempVariables != null ? new HashMap<>(tempVariables) : new HashMap<>();
		if(changeables == null || changeables.isEmpty()) {
			this.changeables = Collections.emptyMap();
		} else {
			this.changeables = changeables;
			changeables.keySet().forEach(key -> this.tempVariables.put(key, changeables.get(key).asString()));
		}
		this.player = player;
		this.async = async;
	}

	public String getTempVariable(String key) {
		return tempVariables.get(key);
	}

	public String getTempVariable(String key, String def) {
		return tempVariables.getOrDefault(key, def);
	}

	public String setTempVariable(String key, String str) {
		return tempVariables.put(key.toLowerCase(), str);
	}

	public String setTempVariable(String key, Object obj) {
		return tempVariables.put(key.toLowerCase(), obj.toString());
	}

	public boolean setChangeable(String key, double value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVariables.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, String value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVariables.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, boolean value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVariables.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, Location value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVariables.put(key, dataValue.asString());
		return true;
	}
}
