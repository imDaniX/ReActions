package me.fromgate.reactions.placeholders;

import lombok.Getter;
import me.fromgate.reactions.util.data.RaContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter
public abstract class Placeholder {
    private final String id;
    private final Set<String> keys;

    public Placeholder() {
        if (this.getClass().isAnnotationPresent(PlaceholderDefine.class)) {
            PlaceholderDefine pd = this.getClass().getAnnotation(PlaceholderDefine.class);
            this.id = pd.id();
            this.keys = new HashSet<>();
            for(String key : pd.keys())
                keys.add(key.toLowerCase(Locale.ENGLISH));
            this.keys.add(this.id.toLowerCase(Locale.ENGLISH));
        } else {
            this.id = "UNKNOWN";
            this.keys = Collections.emptySet();
        }
    }

    public boolean checkKey(String key) {
        return keys.contains(key);
    }

    /**
     * Замена ключеового слова
     *
     * @param context - игрок, если он есть
     * @param key    - Ключевое слово, без параметра и символа "%" в начале
     * @param param  - Параметр (без завершающего символа "%")
     * @return - возврат подстановки.
     */
    public abstract String processPlaceholder(RaContext context, String key, String param);
}