package me.fromgate.reactions;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class ReActions {
    @Setter
    private Platform platform;

    public ActivatorsManager getActivators() {
        return platform.getActivators();
    }

    public PlaceholdersManager getPlaceholders() {
        return platform.getPlaceholders();
    }

    public VariablesManager getVariables() {
        return platform.getVariables();
    }

    public Plugin getPlugin() {
        return platform.getPlugin();
    }

    public interface Platform {
        ActivatorsManager getActivators();
        PlaceholdersManager getPlaceholders();
        VariablesManager getVariables();
        Plugin getPlugin();

        // TODO: Selectors, Flags, Actions
    }
}
