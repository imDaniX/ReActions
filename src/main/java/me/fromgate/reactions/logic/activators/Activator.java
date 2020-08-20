package me.fromgate.reactions.logic.activators;

import lombok.AllArgsConstructor;
import me.fromgate.reactions.logic.actions.Actions;
import me.fromgate.reactions.logic.flags.Flags;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.configuration.ConfigurationSection;

@AllArgsConstructor
public abstract class Activator {
    protected final ActivatorBase base;

    /**
     * Execution of activator
     *
     * @param storage Storage with data for activator
     */
    public final void executeActivator(Storage storage) {
        if (!proceed(storage)) return;
        RaContext context = storage.generateContext(base.getName());
        Actions.executeActions(context, base, Flags.checkFlags(context, base));
    }

    /**
     * Get activator base
     *
     * @return Related activator base
     */
    public final ActivatorBase getBase() {
        return base;
    }

    /**
     * Save activator to config with actions, reactions and flags
     *
     * @param cfg Section of activator
     */
    public final void saveActivator(ConfigurationSection cfg) {
        base.saveBase(cfg);
        saveOptions(cfg);
    }

    /**
     * Check trigger options
     *
     * @param storage Storage with data for trigger
     * @return Are checks successfully past
     */
    public abstract boolean proceed(Storage storage);

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
        return base.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(base.getGroup()).append(", ").append(base.getName()).append(" [").append(getType()).append("]");
        sb.append(base.toString());
        return sb.toString();
    }
}
