package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.data.RaContext;

import java.util.Collection;

public interface Parser {
    boolean put(Placeholder ph);
    String parse(String text, RaContext context);
    boolean isEmpty();
    Collection<Placeholder> getPlaceholders();
}
