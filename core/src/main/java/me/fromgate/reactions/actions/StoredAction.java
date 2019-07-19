package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;

public class StoredAction {
	private final Actions action;
	private final String value;

	public StoredAction(String a, String v) {
		this.action = Actions.getByName(a);
		this.value = v;
	}

	public StoredAction(Actions a, String v) {
		this.action = a;
		this.value = v;
	}

	public Actions getAction() {
		return action;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Util.join(action.name(), "=", value);
	}
}