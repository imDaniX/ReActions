package me.fromgate.reactions.logic.activity.flags;

import org.jetbrains.annotations.NotNull;

public class StoredFlag {

    private final Flag flag;
    private final String value;
    private final boolean inverted;
    private final boolean placeholders;

    public StoredFlag(@NotNull Flag flag, @NotNull String value, boolean inverted) {
        this.flag = flag;
        this.value = value;
        this.inverted = inverted;
        this.placeholders = value.contains("%");
    }

    @NotNull
    public Flag getFlag() {
        return flag;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public boolean isInverted() {
        return inverted;
    }

    public boolean hasPlaceholders() {
        return placeholders;
    }

    @Override
    public String toString() {
        return (inverted ? "!" : "") + flag.getName() + "=" + value;
    }
}
