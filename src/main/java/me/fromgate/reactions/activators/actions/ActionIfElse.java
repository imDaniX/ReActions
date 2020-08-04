package me.fromgate.reactions.activators.actions;

import me.fromgate.reactions.activators.StoragesManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-17.
 */
public class ActionIfElse extends Action {
    // TODO: Maybe use some custom evaluator instead of freaking JS engine?
    private static final ScriptEngineManager factory = new ScriptEngineManager();
    private static final ScriptEngine engine = factory.getEngineByName("JavaScript");

    private static boolean executeActivator(Player p, String condition, String paramStr) {
        Parameters param = Parameters.fromString(paramStr);
        if (!param.contains("run")) return false;
        param = Parameters.fromString(param.getString("run"));
        if (param.isEmpty() || !param.containsAny("activator", "exec")) return false;
        param.put("player", p == null ? "null" : p.getName());
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("condition", condition);
        StoragesManager.raiseExecActivator(p, param, tempVars);
        return true;
    }

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        if (params.contains("if") && params.containsAny("then", "else")) {
			/*
			TODO: Meh, not really good - does not support multiply checks
			String condition = params.getParam("if", "");
			Param ifParams = new Param(condition);
			// "=", ">", ">=", "<", "<=", "boolean", "equals", "ignorecase", "regex"
			ConditionType type = ConditionType.getByName(ifParams.getParam("type", "equals"));
			String val1 = ifParams.getParam("val1", ifParams.getParam("regex", "unknown));
			String val2 = ifParams.getParam("val2", "unknown");

			boolean result = false;
			if(type.isNumeric()) {
				double val1Num = 0;
				double val2Num = 0;
				if(Util.FLOAT.matcher(val1).matches() && Util.FLOAT.matcher(val2).matches()) {
					val1Num = Double.valueOf(val1);
					val2Num = Double.valueOf(val2);
				}
				switch(type) {
					EQUAL:
						result = val1Num == val2Num;
						break;
					MORE:
						result = val1Num > val2Num;
						break;
					MORE_OR_EQUAL:
						result = val1Num >= val2Num;
						break;
					LESS:
						result = val1Num < val2Num;
						break;
					LESS_OR_EQUAL:
						result = val1Num <= val2Num;
						break;
				}
			} else {
				switch(type) {
					BOOLEAN:
						boolean checkTo = val2.equalsIgnoreCase("true") || val2.equalsIgnoreCase("on");
						result = checkTo && (val1.equalsIgnoreCase("true") || val1.equalsIgnoreCase("on"));
						break;
					S_EQUALS:
						result = val1.equals(val2);
						break;
					IGNORE_CASE:
						result = val1.equalsIgnoreCase(val2);
						break;
					REGEX:
						result = val2.matches(val1);
						break;
				}
			}

			String strResult = (result) ? params.getParam("then", "") : params.getParam("else", "");
			if (!executeActivator(p, condition, strResult) && !executeActions(p, strResult))
				context.setTempVariable("ifelseresult" + params.getParam("suffix", ""), strResult);

			return true;
			*/
            final ScriptContext scriptContext = new SimpleScriptContext();
            scriptContext.setBindings(new SimpleBindings(), ScriptContext.ENGINE_SCOPE);

            String condition = params.getString("if", "");
            String then_ = params.getString("then", "");
            String else_ = params.getString("else", "");
            String suffix = params.getString("suffix", "");

            try {
                boolean result = (boolean) engine.eval(condition, scriptContext);
                if (!executeActivator(player, condition, (result) ? then_ : else_)
                        && !executeActions(context, (result) ? then_ : else_))
                    context.setVariable("ifelseresult" + suffix, (result) ? then_ : else_);
            } catch (ScriptException e) {
                context.setVariable("ifelsedebug", e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean executeActions(RaContext context, String paramStr) {
        List<StoredAction> actions = new ArrayList<>();
        Parameters params = Parameters.fromString(paramStr);
        if (!params.contains("run")) return false;
        params = Parameters.fromString(params.getString("run"));
        if (params.isEmpty() || !params.contains("actions")) return false;
        params = Parameters.fromString(params.getString("actions"));

        if (!params.contains("action1")) return false;
        for (String actionKey : params.keySet()) {
            if (!((actionKey.toLowerCase(Locale.ENGLISH)).startsWith("action"))) continue;
            if (params.isEmpty() || !params.toString().contains("=")) continue;
            String action = params.getString(actionKey);

            String flag = action.substring(0, action.indexOf("="));
            String param = action.substring(action.indexOf("=") + 1);
            actions.add(new StoredAction(Actions.getValidName(flag), param));
        }

        if (actions.isEmpty()) return false;
        Actions.executeActions(context, actions, true);
        actions.clear();
        return true;
    }

	/*
	private enum ConditionType {
		EQUAL("="), MORE(">"), MORE_OR_EQUAL(">="), LESS("<"), LESS_OR_EQUAL("<="),
		BOOLEAN(false, "check"), S_EQUALS(false, "equals"), IGNORE_CASE(false, "ignorecase"), REGEX(false, "regular");

		@Getter private final boolean numeric;
		private final String alias;
		private static final Map<String, ConditionType> BY_NAME;
		static {
			Map<String, ConditionType> byName = new HashMap<>();
			for(ConditionType cnd : ConditionType.values()) {
				byName.put(cnd.name(), cnd);
				byName.put(cnd.alias.toUpperCase(Locale.ENGLISH), cnd);
			}
			BY_NAME = Collections.unmodifiableMap(byName);
		}
		ConditionType(boolean num, String alias) {
			this.alias = alias;
			this.numeric = num;
		}
		ConditionType(String alias) {
			this.alias = alias;
			this.numeric = true;
		}

		public static ConditionType getByName(String name) {
			return BY_NAME.get(name);
		}
	}
	*/
}
