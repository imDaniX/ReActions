package me.fromgate.reactions.util.parameter;

import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.collections.CaseInsensitiveMap;
import me.fromgate.reactions.util.math.NumberUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class Parameters implements Iterable<String> {
    private final String origin;
    private final Map<String, String> params;

    protected Parameters(String origin, Map<String, String> params) {
        this.origin = origin;
        this.params = params;
    }

    public static Parameters fromMap(Map<String, String> map) {
        StringBuilder bld = new StringBuilder();
        Map<String, String> params = new CaseInsensitiveMap<>(map);
        params.forEach((k, v) -> bld.append(k).append(":{").append(v).append("} "));
        String str = bld.toString();
        return new Parameters(str.isEmpty() ? str : str.substring(0, str.length() - 1), params);
    }

    public static Parameters fromString(String str) {
        return fromString(str, null);
    }

    public static Parameters fromString(String str, String defKey) {
        Map<String, String> params = new CaseInsensitiveMap<>();
        IterationState state = IterationState.SPACE;
        String param = "";
        StringBuilder bld = null;
        int brCount = 0;
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (state) {
                case SPACE:
                    if(c == ' ') {
                        continue;
                    }
                    bld = new StringBuilder().append(c);
                    state = IterationState.TEXT;
                    break;
                case TEXT:
                    if(c == ' ') {
                        if(!Utils.isStringEmpty(defKey)) {
                            String value = bld.toString();
                            params.put(defKey, value);
                        }
                        state = IterationState.SPACE;
                        continue;
                    }
                    if(c == ':') {
                        state = IterationState.DOTS;
                        param = bld.toString();
                        bld = new StringBuilder();
                        continue;
                    }
                    bld.append(c);
                    break;
                case DOTS:
                    if(c == ' ') {
                        state = IterationState.SPACE;
                        continue;
                    }
                    if(c == '{') {
                        state = IterationState.BR_PARAM;
                        continue;
                    }
                    bld.append(c);
                    state = IterationState.PARAM;
                    break;
                case PARAM:
                    if(c == ' ') {
                        state = IterationState.SPACE;
                        String value = bld.toString();
                        params.put(param, value);
                        continue;
                    }
                    bld.append(c);
                    break;
                case BR_PARAM:
                    if(c == '}') {
                        if(brCount == 0) {
                            state = IterationState.SPACE;
                            String value = bld.toString();
                            params.put(param, value);
                            continue;
                        } else brCount--;
                    } else if(c == '{')
                        brCount++;
                    bld.append(c);
                    break;
            }
        }

        if(state == IterationState.PARAM) {
            params.put(param, bld.toString());
        } else if(state == IterationState.TEXT && !Utils.isStringEmpty(defKey)) {
            params.put(defKey, bld.toString());
        }

        params.put("param-line", str);
        return new Parameters(str, params);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String def) {
        return params.getOrDefault(key, def);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double def) {
        return NumberUtils.getDouble(params.get(key), def);
    }

    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    public int getInteger(String key, int def) {
        return NumberUtils.getInteger(params.get(key), def);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        String value = params.get(key);
        if(Utils.isStringEmpty(value)) return def;
        if(key.equalsIgnoreCase("true"))
            return true;
        if(key.equalsIgnoreCase("false"))
            return false;
        return def;
    }

    public boolean contains(String key) {
        return params.containsKey(key);
    }

    public boolean containsEvery(String... keys) {
        for (String key : keys) {
            if (!params.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAny(Iterable<String> keys) {
        for (String key : keys) {
            if (params.containsKey(key)) return true;
        }
        return false;
    }

    public boolean containsAny(String... keys) {
        for (String key : keys) {
            if (params.containsKey(key)) return true;
        }
        return false;
    }

    public boolean matchesAny(Pattern... patterns) {
        for (Pattern pattern : patterns) {
            for (String param : params.keySet()) {
                if (pattern.matcher(param).matches()) return true;
            }
        }
        return false;
    }

    public boolean matchesAny(String... keys) {
        for (String key : keys) {
            for (String param : params.keySet()) {
                if (param.matches(key)) return true;
            }
        }
        return false;
    }

    public Set<String> keySet() {
        return this.params.keySet();
    }

    public Map<String, String> getMap() {
        return this.params;
    }

    public boolean isEmpty() {
        return this.params.isEmpty();
    }

    public String put(String key, String value) {
        return params.put(key, value);
    }

    public String remove(String key) {
        return this.params.remove(key);
    }

    public int size() {
        return this.params.size();
    }

    @Override
    public String toString() {
        return this.origin;
    }

    @Override
    public Iterator<String> iterator() {
        return params.keySet().iterator();
    }

    public void forEach(BiConsumer<String, String> consumer) {
        params.forEach(consumer);
    }

    public static Map<String, String> parametersMap(String param) {
        return fromString(param).getMap();
    }

    private enum IterationState {
        SPACE, TEXT, DOTS, PARAM, BR_PARAM
    }
}
