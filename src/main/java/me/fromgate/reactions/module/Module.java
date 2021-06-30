package me.fromgate.reactions.module;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.logic.activity.flags.Flag;
import me.fromgate.reactions.placeholders.Placeholder;
import me.fromgate.reactions.playerselector.Selector;

import java.util.Collection;
import java.util.Collections;

public interface Module {
    default void register(ReActions.Platform rea) {

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

    String[] getAuthors();

    String getVersion();

    default Collection<ActivatorType> getActivatorTypes() {
        return Collections.emptyList();
    }

    default Collection<Action> getActions() {
        return Collections.emptyList();
    }

    default Collection<Flag> getFlags() {
        return Collections.emptyList();
    }

    default Collection<Placeholder> getPlaceholders() {
        return Collections.emptyList();
    }

    default Collection<Selector> getSelectors() {
        return Collections.emptyList();
    }
}
