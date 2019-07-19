package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;

public class StoredAction {
	public String flag;
	public String value;

	public StoredAction(String f, String v) {
		this.flag = f;
		this.value = v;
	}

	public StoredAction(String f) {
		this.flag = f;
		this.value = "";
	}

	@Override
	public String toString() {
		return Util.join(Actions.getValidName(flag), "=", value);
	}
}