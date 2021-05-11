package me.fromgate.reactions.util;

import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.ActivatorLogic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to generate triggers
 *
 * @param <D> Data class for activators creation
 */
@FunctionalInterface
public interface RaGenerator<D> {
    /**
     * Generate activator from logic and data container
     *
     * @param logic Logic of activator
     * @param data Data container
     * @return Generated activator, or null if failed
     */
    @Nullable
    Activator generate(@NotNull ActivatorLogic logic, @NotNull D data);
}
