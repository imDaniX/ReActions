package me.fromgate.reactions.logic.activity.actions;

public class StoredAction {

    private final Action action;
    private final String value;
    private final boolean placeholders;

    public StoredAction(Action action, String value) {
        this.action = action;
        this.value = value;
        this.placeholders = value.contains("%");
    }

    public Action getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }

    public boolean hasPlaceholders() {
        return placeholders;
    }

    @Override
    public String toString() {
        return action.getName() + "=" + value;
    }
}