package me.fromgate.reactions.placeholders;

import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class Placeholder {

	@Getter private final String id;
	@Getter private final String[] keys;

	public Placeholder() {
		if (this.getClass().isAnnotationPresent(PlaceholderDefine.class)) {
			PlaceholderDefine pd = this.getClass().getAnnotation(PlaceholderDefine.class);
			this.id = pd.id();
			this.keys = pd.keys();
		} else {
			id = "UNKNOWN";
			keys = new String[]{};
		}
	}

	public boolean checkKey(String key) {
		for (String k : this.getKeys())
			if (k.equalsIgnoreCase(key)) return true;
		return false;
	}

	/**
	 * Замена ключеового слова
	 *
	 * @param player - игрок, если он есть
	 * @param key	- Ключевое слово, без параметра и символа "%" в начале
	 * @param param  - Параметр (без завершающего символа "%")
	 * @return - возврат подстановки.
	 */
	public abstract String processPlaceholder(Player player, String key, String param);
}
