package me.fromgate.reactions.logic.activators;

/**
 * Some activators have its own logic on load based on what does user want.
 * This interface should let devs to do it without "hacking" the plugin.
 */
// TODO
public interface Manageable {
    void manage();
}
