package me.fromgate.reactions;

import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;

public final class ReActions {
    private static ReActions.Provider provider;

    public static void register(ReActions.Provider provider) {
        ReActions.provider = provider;
    }

    public static ActivatorsManager getActivators() {
        return provider.getActivators();
    }

    public static PlaceholdersManager getPlaceholders() {
        return provider.getPlaceholders();
    }

    public static VariablesManager getVariables() {
        return provider.getVariables();
    }

    public interface Provider {
        ActivatorsManager getActivators();
        PlaceholdersManager getPlaceholders();
        VariablesManager getVariables();
        // TODO: Selectors, Flags, Actions
    }
}
