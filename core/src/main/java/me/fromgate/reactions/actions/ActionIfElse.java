package me.fromgate.reactions.actions;

import me.fromgate.reactions.storages.StoragesManager;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaxDikiy on 2017-05-17.
 */
public class ActionIfElse extends Action {
	// TODO: Maybe use some custom evaluator instead of freaking JS engine?
	private static final ScriptEngineManager factory = new ScriptEngineManager();
	private static final ScriptEngine engine = factory.getEngineByName("JavaScript");

	@Override
	public boolean execute(Player p, Param params) {
		if (params.isParamsExists("if") && params.hasAnyParam("then", "else")) {
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
				Variables.setTempVar("ifelseresult" + params.getParam("suffix", ""), strResult);

			return true;
			*/
			final ScriptContext context = new SimpleScriptContext();
			context.setBindings(new SimpleBindings(), ScriptContext.ENGINE_SCOPE);

			String condition = params.getParam("if", "");
			String then_ = params.getParam("then", "");
			String else_ = params.getParam("else", "");
			String suffix = params.getParam("suffix", "");

			try {
				boolean result = (boolean) engine.eval(condition, context);
				if (!executeActivator(p, condition, (result) ? then_ : else_)
						&& !executeActions(p, (result) ? then_ : else_))
					Variables.setTempVar("ifelseresult" + suffix, (result) ? then_ : else_);
			} catch (ScriptException e) {
				Variables.setTempVar("ifelsedebug", e.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}

	private static boolean executeActivator(Player p, String condition, String paramStr) {
		Param param = Param.parseParams(paramStr);
		if (!param.hasAnyParam("run")) return false;
		param = Param.parseParams(param.getParam("run"));
		if (param.isEmpty() || !param.hasAnyParam("activator", "exec")) return false;
		param.set("player", p == null ? "null" : p.getName());
		Param tempVars = new Param();
		tempVars.set("condition", condition);
		StoragesManager.raiseExecActivator(p, param, tempVars);
		return true;
	}

	private boolean executeActions(Player p, String paramStr) {
		List<StoredAction> actions = new ArrayList<>();
		Param params = Param.parseParams(paramStr);
		if (!params.hasAnyParam("run")) return false;
		params = Param.parseParams(params.getParam("run"));
		if (params.isEmpty() || !params.hasAnyParam("actions")) return false;
		params = Param.parseParams(params.getParam("actions"));

		if (!params.isParamsExists("action1")) return false;
		for (String actionKey : params.keySet()) {
			if (!((actionKey.toLowerCase()).startsWith("action"))) continue;
			if (params.isEmpty() || !params.toString().contains("=")) continue;
			String action = params.getParam(actionKey);

			String flag = action.substring(0, action.indexOf("="));
			String param = action.substring(action.indexOf("=") + 1);
			actions.add(new StoredAction(Actions.getValidName(flag), param));
		}

		if (actions.isEmpty()) return false;
		Actions.executeActions(p, actions, true);
		actions.clear();
		return true;
	}

	/*
	private enum ConditionType {
		EQUAL("="), MORE(">"), MORE_OR_EQUAL(">="), LESS("<"), LESS_OR_EQUAL("<="),
		BOOLEAN(false, "check"), S_EQUALS(false, "equals"), IGNORE_CASE(false, "ignorecase"), REGEX(false, "regular");

		@Getter private final boolean numeric;
		private final String alias;
		private final static Map<String, ConditionType> BY_NAME;
		static {
			Map<String, ConditionType> byName = new HashMap<>();
			for(ConditionType cnd : ConditionType.values()) {
				byName.put(cnd.name(), cnd);
				byName.put(cnd.alias.toUpperCase(), cnd);
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
