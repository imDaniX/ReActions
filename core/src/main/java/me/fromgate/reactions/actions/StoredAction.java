package me.fromgate.reactions.actions;

import lombok.Getter;
import me.fromgate.reactions.util.Util;

public class StoredAction {
	@Getter private final Actions action;
	@Getter private final String value;

	public StoredAction(String a, String v) {
		this.action = Actions.getByName(a);
		this.value = v;
	}

	public StoredAction(Actions a, String v) {
		this.action = a;
		this.value = v;
	}

	@Override
	public String toString() {
		return Util.join(action.name(), "=", value);
	}
}