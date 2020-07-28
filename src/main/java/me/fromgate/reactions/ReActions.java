package me.fromgate.reactions;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;

@UtilityClass
public final class ReActions {
    private ReActions.Provider provider;

    public void register(ReActions.Provider provider) {
        ReActions.provider = provider;
    }

    public ActivatorsManager getActivators() {
        return provider.getActivators();
    }

    public PlaceholdersManager getPlaceholders() {
        return provider.getPlaceholders();
    }

    public VariablesManager getVariables() {
        return provider.getVariables();
    }

    public interface Provider {
        ActivatorsManager getActivators();
        PlaceholdersManager getPlaceholders();
        VariablesManager getVariables();
        // TODO: Selectors, Flags, Actions
    }
}
