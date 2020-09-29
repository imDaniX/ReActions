package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.MathEvaluator;

@Alias({"calculate", "expression", "eval"})
public class PlaceholderCalc implements Placeholder.Prefixed {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        if(!param.contains("%")) try {
            double result = MathEvaluator.eval(param);
            return (result == (long) result) ?
                   Long.toString((long) result) :
                   Double.toString(result);
        } catch (NumberFormatException | ArithmeticException ignore) {
            // TODO: Error
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return "calc";
    }
}
