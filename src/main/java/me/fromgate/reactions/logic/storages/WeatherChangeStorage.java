package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;

import java.util.Map;

@Getter
public class WeatherChangeStorage extends Storage {
    private final String world;
    private final boolean raining;

    public WeatherChangeStorage(String world, boolean raining) {
        super(null, ActivatorType.WEATHER_CHANGE);
        this.world = world;
        this.raining = raining;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(Storage.CANCEL_EVENT, new BooleanValue(false));
    }

    @Override
    protected Map<String, String> prepareVariables() {
        return new MapBuilder<String, String>()
                .put("world", world)
                .put("raining", Boolean.toString(raining))
                .build();
    }
}
