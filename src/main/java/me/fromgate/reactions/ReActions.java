package me.fromgate.reactions;

import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;

public final class ReActions {
    private static ReActions.Provider provider;

    public static void register(ReActions.Provider provider) {
        ReActions.provider = provider;
    }

    public static ActivatorsManager getActivatorsManager() {
        return provider.getActivatorsManager();
    }

    public static PlaceholdersManager placeholdersManager() {
        return provider.getPlaceholdersManager();
    }

    public interface Provider {
        ActivatorsManager getActivatorsManager();
        PlaceholdersManager getPlaceholdersManager();
        // TODO: Variables, Placeholders, Selectors, Flags, Actions
    }
}
