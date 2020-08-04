package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.math.Rng;

import java.util.regex.Pattern;

@Alias({"rnd", "rng"})
public class PlaceholderRandom implements Placeholder.Prefixed {

    private static final Pattern WORD_LIST = Pattern.compile("[\\S,]*[\\S]");

    @Override
    public String processPlaceholder(RaContext context, String key, String param) {

        if (NumberUtils.INT_POSITIVE.matcher(param).matches())
            return Integer.toString(Rng.nextInt(Integer.parseInt(param)));

        if (NumberUtils.INT_MIN_MAX.matcher(param).matches())
            return Integer.toString(Rng.nextIntFromString(param));

        if (WORD_LIST.matcher(param).matches()) {
            String[] ln = param.split(",");
            if (ln.length == 0) return param;
            return ln[Rng.nextInt(ln.length)];
        }
        return param;
    }

    @Override
    public String getPrefix() {
        return "random";
    }
}
