package me.fromgate.reactions.util.math;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface Rng {
    static <T> T randomElement(List<T> list) {
        return list.get(Rng.nextInt(list.size()));
    }

    static boolean percentChance(double chance) {
        return Rng.nextDouble(100) < chance;
    }

    static boolean chance(double v) {
        return nextDouble() < v;
    }

    static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    static int nextInt(int maxvalue) {
        return ThreadLocalRandom.current().nextInt(maxvalue);
    }

    static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    static double nextDouble(double max) {
        return ThreadLocalRandom.current().nextDouble(max);
    }

    static double nextDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Get random value by min and max values
     *
     * @param numsStr String with min-max values or just max value(e.g. "2-47", "76")
     * @return Random value
     */
    static int nextIntFromString(String numsStr) {
        if (numsStr.contains("-")) {
            int min = 0;
            int max = 0;
            String minStr;
            String maxStr;
            int index = numsStr.indexOf('-');
            minStr = numsStr.substring(0, index);
            maxStr = numsStr.substring(index + 1);
            if (NumberUtils.INT_POSITIVE.matcher(minStr).matches())
                min = Integer.parseInt(minStr);
            if (NumberUtils.INT_POSITIVE.matcher(maxStr).matches())
                max = Integer.parseInt(maxStr);
            if (max > min)
                return min + nextInt(1 + max - min);
            return min;
        } else {
            if (NumberUtils.INT_POSITIVE.matcher(numsStr).matches())
                return Integer.parseInt(numsStr);
            return 0;
        }
    }
}
