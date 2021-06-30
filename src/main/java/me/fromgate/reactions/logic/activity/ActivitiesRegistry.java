package me.fromgate.reactions.logic.activity;

import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.logic.activity.flags.Flag;
import me.fromgate.reactions.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivitiesRegistry {

    private final Map<String, Action> actionByName;
    private final Map<String, Flag> flagByName;
    private final List<Action> actions;
    private final List<Flag> flags;

    public ActivitiesRegistry() {
        actionByName = new HashMap<>();
        flagByName = new HashMap<>();
        actions = new ArrayList<>();
        flags = new ArrayList<>();
    }

    public void registerAction(@NotNull Action action) {
        if (actionByName.containsKey(action.getName().toUpperCase(Locale.ENGLISH))) {
            throw new IllegalStateException("Action '" + action.getName().toUpperCase(Locale.ENGLISH) + "' is already registered!");
        }
        actions.add(action);
        for (String alias : Utils.getAliases(action)) {
            actionByName.putIfAbsent(alias.toUpperCase(Locale.ENGLISH), action);
        }
    }

    public void registerFlag(@NotNull Flag flag) {
        if (flagByName.containsKey(flag.getName().toUpperCase(Locale.ENGLISH))) {
            throw new IllegalStateException("Flag '" + flag.getName().toUpperCase(Locale.ENGLISH) + "' is already registered!");
        }
        flags.add(flag);
        for (String alias : Utils.getAliases(flag)) {
            flagByName.putIfAbsent(alias.toUpperCase(Locale.ENGLISH), flag);
        }
    }

    @Nullable
    public Action getAction(@NotNull String name) {
        return actionByName.get(name.toUpperCase(Locale.ENGLISH));
    }

    @Nullable
    public Flag getFlag(@NotNull String name) {
        return flagByName.get(name.toUpperCase(Locale.ENGLISH));
    }
}
