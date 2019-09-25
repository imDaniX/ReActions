package me.fromgate.reactions.placeholders;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

@Getter
public abstract class Placeholder {
	private String id = "UNKNOWN";

	private String[] keys = new String[] {};

	public Placeholder() {
		if (this.getClass().isAnnotationPresent(PlaceholderDefine.class)) {
			PlaceholderDefine pd = this.getClass().getAnnotation(PlaceholderDefine.class);
			this.id = pd.id();
			this.keys = pd.keys();
		}
	}

	public boolean checkKey(String key) {
		return Stream.of(this.keys).anyMatch(key::equalsIgnoreCase);
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