package me.fromgate.reactions.logic.activators;

import lombok.AllArgsConstructor;
import me.fromgate.reactions.logic.actions.Actions;
import me.fromgate.reactions.logic.flags.Flags;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.configuration.ConfigurationSection;

@AllArgsConstructor
public abstract class Activator {
    protected final ActivatorLogic logic;

    /**
     * Execution of activator
     *
     * @param storage Storage with data for activator
     */
    public final void executeActivator(Storage storage) {
        if (!check(storage)) return;
        RaContext context = storage.generateContext(logic.getName());
        Actions.executeActions(context, logic, Flags.checkFlags(context, logic));
    }

    /**
     * Get activator logic
     *
     * @return Related activator logic
     */
    public final ActivatorLogic getLogic() {
        return logic;
    }

    /**
     * Save activator to config with actions, reactions and flags
     *
     * @param cfg Section of activator
     */
    public final void saveActivator(ConfigurationSection cfg) {
        logic.save(cfg);
        saveOptions(cfg);
    }

    /**
     * Check trigger options
     *
     * @param storage Storage with data for trigger
     * @return Are checks successfully past
     */
    public abstract boolean check(Storage storage);

    /**
     * Get type of activator
     *
     * @return Type of activator
     */
    public abstract ActivatorType getType();

    /**
     * Save activator options to the config
     *
     * @param cfg Section of activator
     */
    public void saveOptions(ConfigurationSection cfg) {
        // Sometimes we don't need those
    }

    /**
     * Check if trigger is valid
     *
     * @return Is trigger valid
     */
    public boolean isValid() {
        return true;
    }

    @Override
    public int hashCode() {
        return logic.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(logic.getGroup()).append(", ").append(logic.getName()).append(" [").append(getType()).append("]");
        sb.append(logic.toString());
        return sb.toString();
    }
}
