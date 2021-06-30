package me.fromgate.reactions.logic.activity.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.jetbrains.annotations.NotNull;

public abstract class Action {
    public boolean execute(@NotNull RaContext context, @NotNull String paramsStr) {
        return execute(
                context,
                isParameterized() ?
                        Parameters.fromString(paramsStr) :
                        Parameters.noParse(paramsStr)
        );
    }

    @NotNull
    public abstract String getName();

    public abstract boolean requiresPlayer();

    // TODO
    protected boolean isAsync() {
        return true;
    }

    protected boolean isParameterized() {
        return true;
    }

    protected abstract boolean execute(@NotNull RaContext context, @NotNull Parameters params);
}
