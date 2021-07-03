package me.fromgate.reactions.logic.activity.actions;

public class StoredAction {

    private final Action action;
    private final String params;
    private final boolean placeholders;

    public StoredAction(Action action, String params) {
        this.action = action;
        this.params = params;
        this.placeholders = params.contains("%");
    }

    public Action getAction() {
        return action;
    }

    public String getParameters() {
        return params;
    }

    public boolean hasPlaceholders() {
        return placeholders;
    }

    @Override
    public String toString() {
        return action.getName() + "=" + params;
    }
}