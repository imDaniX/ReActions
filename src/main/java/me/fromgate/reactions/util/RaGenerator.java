package me.fromgate.reactions.util;

import me.fromgate.reactions.activators.triggers.ActivatorBase;
import me.fromgate.reactions.activators.triggers.Trigger;

/**
 * Used to generate triggers
 *
 * @param <T> Data class for activators creation
 */
@FunctionalInterface
public interface RaGenerator<T> {
    /**
     * Generate activator from base and data container
     *
     * @param base Base of activator
     * @param data Data container
     * @return Generated activator, or null if failed
     */
    Trigger generate(ActivatorBase base, T data);
}
