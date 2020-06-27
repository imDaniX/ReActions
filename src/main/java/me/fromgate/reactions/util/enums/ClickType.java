package me.fromgate.reactions.util.enums;

public enum ClickType {
	RIGHT,
	LEFT,
	ANY;

	public static ClickType getByName(String clickStr) {
		if (clickStr.equalsIgnoreCase("left")) return ClickType.LEFT;
		if (clickStr.equalsIgnoreCase("any")) return ClickType.ANY;
		return ClickType.RIGHT;
	}

	public boolean checkLeft(boolean left) {
		switch (this) {
			case ANY:
				return true;
			case LEFT:
				return left;
			case RIGHT:
				return !left;
		}
		return false;
	}

	public boolean checkRight(boolean right) {
		switch (this) {
			case ANY:
				return true;
			case LEFT:
				return !right;
			case RIGHT:
				return right;
		}
		return false;
	}
}
