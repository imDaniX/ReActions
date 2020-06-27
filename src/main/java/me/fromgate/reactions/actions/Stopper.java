package me.fromgate.reactions.actions;

import java.util.List;

/**
 * Some actions can stop execution of activator
 * This interface should let devs to do it without "hacking" the plugin.
 */
public interface Stopper {
	/**
	 * This method will be called after action execution(if it was successful)
	 * @param stoppedActions List of actions that were stopped
	 */
	void stop(List<StoredAction> stoppedActions);
}
