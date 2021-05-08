package me.fromgate.reactions.util;

import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.ActivatorLogic;

/**
 * Used to generate triggers
 *
 * @param <T> Data class for activators creation
 */
@FunctionalInterface
public interface RaGenerator<T> {
    /**
     * Generate activator from logic and data container
     *
     * @param base Base of activator
     * @param data Data container
     * @return Generated activator, or null if failed
     */
    Activator generate(ActivatorLogic base, T data);
}
