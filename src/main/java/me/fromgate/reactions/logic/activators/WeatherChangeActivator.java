package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.logic.storages.WeatherChangeStorage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class WeatherChangeActivator extends Activator {
    private final String world;
    private final WeatherState state;

    private WeatherChangeActivator(ActivatorBase base, String world, WeatherState state) {
        super(base);
        this.world = world;
        this.state = state;
    }

    @Override
    public boolean proceed(Storage strg) {
        WeatherChangeStorage storage = (WeatherChangeStorage) strg;
        if (world != null && !storage.getWorld().equalsIgnoreCase(world)) return false;
        if (state == WeatherState.ANY) return true;
        return storage.isRaining() == (state == WeatherState.RAINING);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WEATHER_CHANGE;
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("world", world);
        cfg.set("weather", state.name());
    }

    public static WeatherChangeActivator create(ActivatorBase base, Parameters params) {
        String world = params.getString("world");
        WeatherState state = WeatherState.getByName(params.getString("weather", "any"));
        return new WeatherChangeActivator(base, world, state);
    }

    public static WeatherChangeActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String world = cfg.getString("world");
        WeatherState state = WeatherState.getByName(cfg.getString("weather", "any"));
        return new WeatherChangeActivator(base, world, state);
    }

    private enum WeatherState {
        RAINING, CLEAR, ANY;

        public static WeatherState getByName(String name) {
            switch (name.toUpperCase(Locale.ENGLISH)) {
                case "raining": case "rain": return RAINING;
                case "clear": case "sun": return CLEAR;
                default: return ANY;
            }
        }
    }
}
