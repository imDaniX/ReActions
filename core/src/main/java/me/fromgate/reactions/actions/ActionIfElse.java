package me.fromgate.reactions.actions;

import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
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
	private static final ScriptEngineManager factory = new ScriptEngineManager();
	private static final ScriptEngine engine = factory.getEngineByName("JavaScript");

	@Override
	public boolean execute(Player p, Param params) {
		if (params.isParamsExists("if")&&params.hasAnyParam("then", "else")) {
			final ScriptContext context = new SimpleScriptContext();
			context.setBindings(new SimpleBindings(), ScriptContext.ENGINE_SCOPE);

			String condition = params.getParam("if", "");
			String then_ = params.getParam("then", "");
			String else_ = params.getParam("else", "");
			String suffix = params.getParam("suffix", "");

			try {
				Boolean result = (Boolean) engine.eval(condition, context);
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

	private static Boolean executeActivator(Player p, String condition, String paramStr) {
		Param param = Param.parseParams(paramStr);
		if (!param.hasAnyParam("run")) return false;
		param = Param.parseParams(param.getParam("run"));
		if (param.isEmpty() || !param.hasAnyParam("activator", "exec")) return false;
		param.set("player", p == null ? "null" : p.getName());
		Param tempVars = new Param();
		tempVars.set("condition", condition);
		EventManager.raiseExecEvent(p, param, tempVars);
		return true;
	}

	private Boolean executeActions(Player p, String paramStr) {
		List<ActVal> actions = new ArrayList<>();
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
			actions.add(new ActVal(Actions.getValidName(flag), param));
		}

		if (actions.isEmpty()) return false;
		Actions.executeActions(p, actions, true);
		actions.clear();
		return true;
	}

}
