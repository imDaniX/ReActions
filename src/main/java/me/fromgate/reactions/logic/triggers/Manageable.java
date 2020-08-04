package me.fromgate.reactions.logic.triggers;

/**
 * Some activators have it's own logic on load based on what does user want.
 * This interface should let devs to do it without "hacking" the plugin.
 */
public interface Manageable {
    void manage();
}
