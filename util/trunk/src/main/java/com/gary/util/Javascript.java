package com.gary.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Javascript {
	public static Object execute(String javascript, String method, Object...params) throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("javascript");
		se.eval(javascript);
        if (se instanceof Invocable) {  
            Invocable invoke = (Invocable) se;  
            return invoke.invokeFunction(method,params);  
        } 
        return null;
	}
}
