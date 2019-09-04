package me.fromgate.reactions.util;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorBase;

/**
 * Used to generate activators
 * @param <T> Data class for activators creation
 */
@FunctionalInterface
public interface RaFunction<T> {
	/**
	 * Generate activator from base and data container
	 * @param base Base of activator
	 * @param data Data container
	 * @return Generated activator, or null if failed
	 */
	Activator generate(ActivatorBase base, T data);
}
