package com.eipl.amcs.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionEval {

	public static Double evaluate(String expression) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");
			Object result = null;
			result = engine.eval(expression);
			return NumberUtil.round(Double.valueOf(result.toString()), 2);
		} catch (ScriptException e) {
			e.printStackTrace();
			return null;
		}
	}
 
}