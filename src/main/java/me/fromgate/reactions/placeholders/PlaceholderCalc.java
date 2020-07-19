package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.math.MathEvaluator;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "Calculate", keys = {"CALC", "calculate", "expression"})
public class PlaceholderCalc extends Placeholder {
    @Override
    public String processPlaceholder(Player player, String key, String param) {
        try {
            double result = MathEvaluator.eval(param);
            return (result == (int) result) ? Integer.toString((int) result) : Double.toString(result);
        } catch (NumberFormatException | ArithmeticException ignore) {
        }
        return null;
    }
}
