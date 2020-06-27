package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.util.math.MathEvaluator;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PlaceholderDefine(id = "Calculate", keys = {"CALC", "calculate", "expression"})
public class PlaceholderCalc extends Placeholder {
	@Override
	public String processPlaceholder(Player player, String key, String param) {
		String expression = replaceVariablesInExpression(param);
		try {
			double result = MathEvaluator.eval(expression);
			return (result == (int) result) ? Integer.toString((int) result) : Double.toString(result);
		} catch (NumberFormatException | ArithmeticException ignore) {}
		return null;
	}

	private String replaceVariablesInExpression(String expression) {
		String result = expression;
		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
			String var = matcher.group();
			result = result.replace(var, Variables.getVar("", var, var));
		}
		return result;
	}
}
