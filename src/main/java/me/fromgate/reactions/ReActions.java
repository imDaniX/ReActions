package me.fromgate.reactions;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;

@UtilityClass
public final class ReActions {
    private Platform platform;

    public void register(Platform platform) {
        ReActions.platform = platform;
    }

    public ActivatorsManager getActivators() {
        return platform.getActivators();
    }

    public PlaceholdersManager getPlaceholders() {
        return platform.getPlaceholders();
    }

    public VariablesManager getVariables() {
        return platform.getVariables();
    }

    public interface Platform {
        ActivatorsManager getActivators();
        PlaceholdersManager getPlaceholders();
        VariablesManager getVariables();
        // TODO: Selectors, Flags, Actions
    }
}
