package me.fromgate.reactions.logic.activity.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Some actions can stop execution of activator
 * This interface should let devs to do it without "hacking" the plugin.
 */
// TODO
public interface Stopper {
    /**
     * This method will be called after action execution (if it was successful)
     * @param actions List of actions that were stopped
     */
    void stop(@NotNull RaContext context, @NotNull Parameters params, @NotNull List<StoredAction> actions);
}
