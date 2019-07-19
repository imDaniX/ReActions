package me.fromgate.reactions.flags;

public class StoredFlag {
	public String flag;
	public String value;
	public boolean not;

	public StoredFlag(String f, String v, boolean not) {
		this.flag = f;
		this.value = v;
		this.not = not;
	}

	@Override
	public String toString() {
		String str = Flags.getValidName(flag) + "=" + value;
		if (this.not) str = "!" + str;
		return str;
	}
}
