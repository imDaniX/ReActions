package me.fromgate.reactions.placeholders;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
            this.keys = new HashSet<>(Arrays.asList(pd.keys()));
            this.keys.add(this.id);
        } else {
            this.id = "UNKNOWN";
            this.keys = Collections.emptySet();
        }
    }

    public boolean checkKey(String key) {
        return keys.contains(key.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Замена ключеового слова
     *
     * @param player - игрок, если он есть
     * @param key    - Ключевое слово, без параметра и символа "%" в начале
     * @param param  - Параметр (без завершающего символа "%")
     * @return - возврат подстановки.
     */
    public abstract String processPlaceholder(Player player, String key, String param);
}