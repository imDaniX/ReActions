package me.fromgate.reactions.util.parameter;

import me.fromgate.reactions.util.math.NumberUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameters {
    private final static Pattern PARAM_PATTERN = Pattern.compile("\\S+:\\{[^\\{\\}]*\\}|\\S+:\\S+|\\S+");
    private final static Pattern PARAM_BRACKET = Pattern.compile("\\{.*\\}");
    private final static Pattern PARAM_BRACKET_SE = Pattern.compile("^\\{.*\\}$");
    private final static Pattern BOOLEAN = Pattern.compile("(?i)true|on|yes");
    private String paramStr;
    private Map<String, String> params;

    public Parameters(String param) {
        this.paramStr = param;
        this.params = parseParams(param, "param");
        this.params.put("param-line", this.paramStr);
    }

    public Parameters(String param, String defaultKey) {
        this.paramStr = param;
        this.params = parseParams(param, defaultKey);
        this.params.put("param-line", this.paramStr); // очередная залипуха
    }

    public Parameters(Map<String, String> params) {
        this.params = new HashMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(key).append(":");
            String value = params.get(key);
            if (value.contains(" ") && !PARAM_BRACKET_SE.matcher(value).matches())
                sb.append("{").append(value).append("}");
            else sb.append(value);
        }
        this.paramStr = sb.toString();
    }

    public Parameters() {
        this.params = new HashMap<>();
        this.paramStr = "";
    }

    /**
     * Преобразует строку вида <параметр>/<параметр>/<параметр> в объект Param
     *
     * @param oldFormat — строка старого формата
     * @param divider   — разделитель (любой, а не только "/")
     * @param keys      — перечень ключей для параметров
     * @return — возвращает
     * <p>
     * Пример:
     * fromOldFormat ("123/test/953","/","num1","word1","num2");
     * - создаст объект Param со следующими параметрами и значениями:
     * - param-line: 123/test/953
     * - num1: 123
     * - word1: test
     * - num2: 953
     */
    public static Parameters fromOldFormat(String oldFormat, String divider, String... keys) {
        Parameters param = new Parameters(oldFormat);
        param.paramStr = oldFormat;
        if (param.hasAnyParam(keys)) return param;
        param = new Parameters();
        param.paramStr = oldFormat;
        param.set("param-line", oldFormat); // и снова залипуха
        String[] ln = oldFormat.split(Pattern.quote(divider), keys.length);
        if (ln.length == 0) return param;
        for (int i = 0; i < Math.min(ln.length, keys.length); i++)
            param.set(keys[i], ln[i]);
        return param;
    }

    public static Parameters parseParams(String paramStr) {
        return new Parameters(paramStr);
    }

    public static Map<String, String> parseParams(String param, String defaultKey) {
        Map<String, String> params = new HashMap<>();
        Matcher matcher = PARAM_PATTERN.matcher(hideBkts(param));
        while (matcher.find()) {
            String paramPart = matcher.group().trim().replace("#BKT1#", "{").replace("#BKT2#", "}");
            String key = paramPart;
            String value = "";
            if (matcher.group().contains(":")) {
                key = paramPart.substring(0, paramPart.indexOf(":"));
                value = paramPart.substring(paramPart.indexOf(":") + 1);
            }
            if (value.isEmpty()) {
                value = key;
                key = defaultKey;
            }
            if (PARAM_BRACKET.matcher(value).matches()) value = value.substring(1, value.length() - 1);
            params.put(key, value);
        }
        return params;
    }

    private static String hideBkts(String s) {
        int count = 0;
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String a = String.valueOf(c);
            if (c == '{') {
                count++;
                if (count != 1) a = "#BKT1#";
            } else if (c == '}') {
                if (count != 1) a = "#BKT2#";
                count--;
            }
            r.append(a);
        }
        return r.toString();
    }

    @SuppressWarnings("unused")
    public static boolean hasAnyParam(Map<String, String> params, String... param) {
        for (String key : params.keySet())
            for (String prm : param) {
                if (key.equalsIgnoreCase(prm)) return true;
            }
        return false;
    }

    public String getParam(String key, String defParam) {
        if (!params.containsKey(key)) return defParam;
        return params.get(key);
    }

    public int getParam(String key, int defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!NumberUtils.INT.matcher(str).matches()) return defParam;
        return Integer.parseInt(str);
    }

    public float getParam(String key, float defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!NumberUtils.FLOAT.matcher(str).matches()) return defParam;
        return Float.parseFloat(str);
    }

    public double getParam(String key, double defParam) {
        if (!params.containsKey(key)) return defParam;
        String str = params.get(key);
        if (!NumberUtils.FLOAT.matcher(str).matches()) return defParam;
        return Double.parseDouble(str);
    }

    public boolean getParam(String key, boolean defValue) {
        if (!params.containsKey(key)) return defValue;
        String str = params.get(key);
        return BOOLEAN.matcher(str).matches();
    }

    public boolean isParamsExists(String... keys) {
        for (String key : keys) {
            if (!params.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyParam(Collection<String> keys) {
        for (String key : keys) {
            if (params.containsKey(key)) return true;
        }
        return false;
    }

    public boolean hasAnyParam(String... keys) {
        for (String key : keys) {
            if (params.containsKey(key)) return true;
        }
        return false;
    }

    public boolean matchAnyParam(Pattern... patterns) {
        for (Pattern pattern : patterns) {
            for (String param : params.keySet()) {
                if (pattern.matcher(param).matches()) return true;
            }
        }
        return false;
    }

    public boolean matchAnyParam(String... keys) {
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

    public String getParam(String key) {
        return this.params.getOrDefault(key, "");
    }

    public Map<String, String> getMap() {
        return this.params;
    }

    public boolean isEmpty() {
        return this.params.isEmpty();
    }

    public void set(String key, String value) {
        params.put(key, value);
    }

    public void remove(String key) {
        this.params.remove(key);
    }

    public int size() {
        return this.params.size();
    }

    @Override
    public String toString() {
        return this.paramStr;
    }
}
