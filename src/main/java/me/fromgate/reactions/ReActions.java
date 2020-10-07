package me.fromgate.reactions;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.placeholders.PlaceholdersManager;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

@UtilityClass
public class ReActions {
    private Platform platform;

    public void setPlatform(Platform platform) {
        Objects.requireNonNull(platform, "Platform cannot be null.");
        Objects.requireNonNull(platform.getActivators(), "ActivatorsManager cannot be null.");
        Objects.requireNonNull(platform.getPlaceholders(), "PlaceholdersManager cannot be null.");
        Objects.requireNonNull(platform.getVariables(), "VariablesManager cannot be null.");
        Objects.requireNonNull(platform.getPlugin(), "Plugin cannot be null.");
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
