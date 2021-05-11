package me.fromgate.reactions.logic.actions;

import lombok.AllArgsConstructor;
import lombok.Value;
import me.fromgate.reactions.module.defaults.actions.Actions;

@Value
@AllArgsConstructor
public class StoredAction {

    Actions action;
    String value;

    public StoredAction(String a, String v) {
        this.action = Actions.getByName(a);
        this.value = v;
    }

    public String getActionName() {
        return action == null ? "UNKNOWN" : action.name();
    }

    @Override
    public String toString() {
        return action.name() + "=" + value;
    }
}