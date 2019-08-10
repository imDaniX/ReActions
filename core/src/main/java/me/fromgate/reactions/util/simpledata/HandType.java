package me.fromgate.reactions.util.simpledata;

public enum HandType {
	MAIN,
	OFF,
	ANY;

	public static HandType getByName(String clickStr) {
		if (clickStr.equalsIgnoreCase("off")) return HandType.OFF;
		if (clickStr.equalsIgnoreCase("any")) return HandType.ANY;
		return HandType.MAIN;
	}

	public boolean checkMain(boolean isMain) {
		switch (this) {
			case ANY:
				return true;
			case MAIN:
				return isMain;
			case OFF:
				return !isMain;
		}
		return false;
	}

	public boolean checkOff(boolean isOff) {
		switch (this) {
			case ANY:
				return true;
			case MAIN:
				return !isOff;
			case OFF:
				return isOff;
		}
		return false;
	}
}
