package me.fromgate.reactions.flags;

import lombok.Getter;

public class StoredFlag {
	@Getter private final Flags flag;
	@Getter private final String value;
	@Getter private final boolean inverted;

	public StoredFlag(String f, String v, boolean not) {
		this.flag = Flags.getByName(f);
		this.value = v;
		this.inverted = not;
	}

	public StoredFlag(Flags f, String v, boolean not) {
		this.flag = f;
		this.value = v;
		this.inverted = not;
	}

	public String getFlagName() {
		return flag == null ? "UNKNOWN" : flag.name();
	}

	@Override
	public String toString() {
		String str = this.getFlagName() + "=" + value;
		if (this.inverted) str = "!" + str;
		return str;
	}
}
