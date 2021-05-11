package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ActivatorType {
    @NotNull
    Class<? extends Activator> getType();

    @NotNull
    String getName();

    @NotNull
    Collection<String> getAliases();

    @NotNull
    Collection<Activator> getActivators();

    @Nullable
    Activator createActivator(@NotNull ActivatorLogic logic, @NotNull Parameters cfg);

    @Nullable
    Activator loadActivator(@NotNull ActivatorLogic logic, @NotNull ConfigurationSection cfg);

    // TODO Module name

    boolean isLocatable();

    void addActivator(@NotNull Activator activator);

    void removeActivator(@NotNull Activator activator);

    void clearActivators();

    @NotNull
    Collection<Activator> getActivatorsAt(@NotNull World world, int x, int y, int z);

    void activate(@NotNull Storage storage);
}
