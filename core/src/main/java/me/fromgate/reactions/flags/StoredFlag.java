package me.fromgate.reactions.flags;

public class StoredFlag {
	private final Flags flag;
	private final String value;
	private final boolean not;

	public StoredFlag(String f, String v, boolean not) {
		this.flag = Flags.getByName(f);
		this.value = v;
		this.not = not;
	}

	public StoredFlag(Flags f, String v, boolean not) {
		this.flag = f;
		this.value = v;
		this.not = not;
	}

	public Flags getFlag() {
		return flag;
	}

	public String getValue() {
		return value;
	}

	public boolean isInverted() {
		return not;
	}

	@Override
	public String toString() {
		String str = flag.name() + "=" + value;
		if (this.not) str = "!" + str;
		return str;
	}
}
