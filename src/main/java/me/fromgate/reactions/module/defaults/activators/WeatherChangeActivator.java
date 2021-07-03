package me.fromgate.reactions.module.defaults.activators;

import me.fromgate.reactions.logic.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.WeatherChangeStorage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class WeatherChangeActivator extends Activator {
    private final String world;
    private final WeatherState state;

    private WeatherChangeActivator(ActivatorLogic base, String world, WeatherState state) {
        super(base);
        this.world = world;
        this.state = state;
    }

    @Override
    public boolean checkStorage(Storage strg) {
        WeatherChangeStorage storage = (WeatherChangeStorage) strg;
        if (world != null && !storage.getWorld().equalsIgnoreCase(world)) return false;
        if (state == WeatherState.ANY) return true;
        return storage.isRaining() == (state == WeatherState.RAINING);
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("world", world);
        cfg.set("weather", state.name());
    }

    public static WeatherChangeActivator create(ActivatorLogic base, Parameters params) {
        String world = params.getString("world");
        WeatherState state = WeatherState.getByName(params.getString("weather", "any"));
        return new WeatherChangeActivator(base, world, state);
    }

    public static WeatherChangeActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        String world = cfg.getString("world");
        WeatherState state = WeatherState.getByName(cfg.getString("weather", "any"));
        return new WeatherChangeActivator(base, world, state);
    }

    private enum WeatherState {
        RAINING, CLEAR, ANY;

        public static WeatherState getByName(String name) {
            return switch (name.toUpperCase(Locale.ENGLISH)) {
                case "raining", "rain" -> RAINING;
                case "clear", "sun" -> CLEAR;
                default -> ANY;
            };
        }
    }
}
