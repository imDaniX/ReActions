package me.fromgate.reactions.logic.triggers;

import lombok.EqualsAndHashCode;
import lombok.Value;
import me.fromgate.reactions.util.RaGenerator;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

@Value
public class TriggerDefine {
    @EqualsAndHashCode.Include
    String type;
    String[] aliases;
    RaGenerator<Parameters> creator;
    RaGenerator<ConfigurationSection> loader;
    boolean needBlock;
    boolean locatable;

    @Override
    public String toString() {
        return aliases.length < 1? type : type + "(" + String.join(", ", aliases) + ")";
    }
}
