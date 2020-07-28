package me.fromgate.reactions.util.math;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Rng {
    public <T> T randomElement(List<T> list) {
        return list.get(Rng.nextInt(list.size()));
    }

    public boolean percentChance(double chance) {
        return Rng.nextDouble(100) < chance;
    }

    public boolean chance(double v) {
        return nextDouble() < v;
    }

    public boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public int nextInt(int maxvalue) {
        return ThreadLocalRandom.current().nextInt(maxvalue);
    }

    public int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public double nextDouble(double max) {
        return ThreadLocalRandom.current().nextDouble(max);
    }

    public double nextDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Get random value by min and max values
     *
     * @param numsStr String with min-max values or just max value(e.g. "2-47", "76")
     * @return Random value
     */
    public int nextIntFromString(String numsStr) {
        int index = numsStr.indexOf('-');
        if (index > -1) {
            int min = 0;
            int max = 0;
            String minStr = numsStr.substring(0, index);
            String maxStr = numsStr.substring(index + 1);
            if (NumberUtils.INT_POSITIVE.matcher(minStr).matches())
                min = Integer.parseInt(minStr);
            if (NumberUtils.INT_POSITIVE.matcher(maxStr).matches())
                max = Integer.parseInt(maxStr);
            if (max > min)
                return nextInt(min, max);
            return min;
        } else {
            if (NumberUtils.INT_POSITIVE.matcher(numsStr).matches())
                return Integer.parseInt(numsStr);
            return 0;
        }
    }
}
