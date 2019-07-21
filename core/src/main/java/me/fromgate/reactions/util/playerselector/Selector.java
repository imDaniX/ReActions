package me.fromgate.reactions.util.playerselector;

import org.bukkit.entity.Player;

import java.util.Set;

public interface Selector {
	default String getKey() {
		if (!this.getClass().isAnnotationPresent(SelectorDefine.class)) return null;
		SelectorDefine sd = this.getClass().getAnnotation(SelectorDefine.class);
		return sd.key();
	}

	Set<Player> selectPlayers(String param);

}
