package com.unizar.game2;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

public class ScriptEngine {
    static javax.script.ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");

    public static Object execute(String code, Map<String, Object> variables) throws ScriptException {
        Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
        variables.forEach(bindings::put);
        return js.eval(code);
    }
}
