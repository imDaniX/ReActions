package me.fromgate.reactions.placeholders;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class Placeholder 
{
    private final String id = "UNKNOWN";
 
    private final String[] keys = new String[] {};

    public Placeholder() 
    {
        if (this.getClass().isAnnotationPresent(PlaceholderDefine.class)) 
        {
            PlaceholderDefine pd = this.getClass().getAnnotation(PlaceholderDefine.class);
    
            this.id = pd.id();
            this.keys = pd.keys();
        }
    }

    protected boolean equalsIgnoreCase(String key, String... values) 
    {
        return Stream.of(values)
                     .anyMatch((string) -> string.equalsIgnoreCase(key));
    }

    public boolean checkKey(String key) 
    {
        return Stream.of(this.keys)
                     .anyMatch((k) -> k.equalsIgnoreCase(key));
    }

    /**
     * Замена ключеового слова
     *
     * @param player - игрок, если он есть
     * @param key - Ключевое слово, без параметра и символа "%" в начале
     * @param param  - Параметр (без завершающего символа "%")
     * @return - возврат подстановки.
     */
    public abstract String processPlaceholder(Player player, String key, String param);
}
