package me.fromgate.reactions.logic.flags;

import lombok.AllArgsConstructor;
import lombok.Value;
import me.fromgate.reactions.module.defaults.flags.Flags;

@Value
@AllArgsConstructor
public class StoredFlag {

    Flags flag;
    String value;
    boolean inverted;

    public StoredFlag(String f, String v, boolean not) {
        this.flag = Flags.getByName(f);
        this.value = v;
        this.inverted = not;
    }

    public String getFlagName() {
        return flag == null ? "UNKNOWN" : flag.name();
    }

    @Override
    public String toString() {
        String str = this.getFlagName() + "=" + value;
        if (this.inverted) str = "!" + str;
        return str;
    }
}
