package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.MathEvaluator;

@PlaceholderDefine(id = "Calculate", keys = {"CALC", "calculate", "expression"})
public class PlaceholderCalc extends Placeholder {
    @Override
    public String processPlaceholder(RaContext context, String key, String param) {
        if(!param.contains("%")) try {
            double result = MathEvaluator.eval(param);
            return (result == (int) result) ?
                    Integer.toString((int) result) :
                    Double.toString(result);
        } catch (NumberFormatException | ArithmeticException ignore) {}
        return null;
    }
}
