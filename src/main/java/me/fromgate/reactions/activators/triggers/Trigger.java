package me.fromgate.reactions.activators.triggers;

import lombok.AllArgsConstructor;
import me.fromgate.reactions.activators.actions.Actions;
import me.fromgate.reactions.activators.flags.Flags;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.configuration.ConfigurationSection;

@AllArgsConstructor
public abstract class Trigger {
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
     * Get activator base of trigger
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
        saveTrigger(cfg);
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
     * Save trigger to the config
     *
     * @param cfg Section of trigger
     */
    public void saveTrigger(ConfigurationSection cfg) {
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
