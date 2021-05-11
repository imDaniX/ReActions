package me.fromgate.reactions.module;

import me.fromgate.reactions.logic.activators.ActivatorType;

import java.util.Collection;

public interface Module {
    default void register() {
        /*
        TODO
        ActivatorsManager activators = ReActions.getActivators();
        Logger logger = ReActions.getPlugin().getLogger();
        for (ActivatorType type : getActivatorTypes()) {
            List<String> names = activators.registerType(type);
            logger.info("Activator type '" + type.getName() + "' registered with aliases: " + String.join(names, ", "));
        }
         */
    }

    String getName();

    Collection<ActivatorType> getActivatorTypes();

    // TODO Actions, Flags, Placeholders
}
