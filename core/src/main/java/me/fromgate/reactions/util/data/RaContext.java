package me.fromgate.reactions.util.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RaContext {
	private final Map<String, String> tempVars;
	private final Map<String, DataValue> changeables;
	@Getter @Setter private Player player;

	public RaContext(Map<String, String> tempVars, Map<String, DataValue> changeables, Player player) {
		this.tempVars = new HashMap<>(tempVars);
		this.changeables = changeables != null ? changeables : Collections.emptyMap();
		changeables.keySet().forEach(key -> tempVars.put(key, changeables.get(key).asString()));
		this.player = player;
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
