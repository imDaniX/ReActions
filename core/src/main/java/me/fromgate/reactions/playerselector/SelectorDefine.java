package me.fromgate.reactions.playerselector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SelectorDefine {
	String key();
}
