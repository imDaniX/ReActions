package me.fromgate.reactions.util.enums;

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
        return switch (this) {
            case ANY -> true;
            case MAIN -> isMain;
            case OFF -> !isMain;
        };
    }

    public boolean checkOff(boolean isOff) {
        return switch (this) {
            case ANY -> true;
            case MAIN -> !isOff;
            case OFF -> isOff;
        };
    }
}
