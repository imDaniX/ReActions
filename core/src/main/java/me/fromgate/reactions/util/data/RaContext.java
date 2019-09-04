package me.fromgate.reactions.util.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RaContext {
	@Getter private final Player player;
	@Getter private final boolean async;
	@Getter private final Map<String, String> tempVars;
	private final Map<String, DataValue> changeables;

	public RaContext(Map<String, String> tempVars, Map<String, DataValue> changeables, Player player, boolean async) {
		this.tempVars = new HashMap<>(tempVars);
		this.changeables = changeables != null ? changeables : Collections.emptyMap();
		this.player = player;
		this.async = async;
		changeables.keySet().forEach(key -> tempVars.put(key, changeables.get(key).asString()));
	}

	public String setTempVariable(String key, String str) {
		return tempVars.put(key.toLowerCase(), str);
	}

	public String setTempVariable(String key, Object obj) {
		return tempVars.put(key.toLowerCase(), obj.toString());
	}

	public boolean setChangeable(String key, double value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVars.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, String value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVars.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, boolean value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVars.put(key, dataValue.asString());
		return true;
	}

	public boolean setChangeable(String key, Location value) {
		key = key.toLowerCase();
		DataValue dataValue = changeables.get(key);
		if(dataValue == null || !dataValue.set(value)) return false;
		tempVars.put(key, dataValue.asString());
		return true;
	}
}
