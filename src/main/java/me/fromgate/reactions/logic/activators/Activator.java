package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.Actions;
import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.logic.Flags;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.configuration.ConfigurationSection;

public abstract class Activator {
    private final ActivatorBase base;

    public Activator(ActivatorBase base) {
        this.base = base;
    }

    /**
     * Execution of activator
     *
     * @param storage Storage with data for activator
     */
    public final void executeActivator(Storage storage) {
        if (!activate(storage)) return;
        RaContext context = storage.generateContext(base.getName());
        Actions.executeActions(context, getBase(), Flags.checkFlags(context, getBase()));
    }

    /**
     * Get base of activator
     *
     * @return Related ActivatorBase
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
        save(cfg);
    }

    /**
     * Execution of activator
     *
     * @param storage Storage with data for activator
     * @return Is activation success
     */
    public abstract boolean activate(Storage storage);

    /**
     * Get type of activator
     *
     * @return Type of activator
     */
    public abstract ActivatorType getType();

    /**
     * Save activator to config
     *
     * @param cfg Section of activator
     */
    public void save(ConfigurationSection cfg) {
    }

    /**
     * Check if activator is valid
     *
     * @return Is activator valid
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
