package com.unizar.game2;

import org.json.JSONObject;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * Allows to run javascript
 */
public class ScriptEngine {
    private static final javax.script.ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");

    private static final JSONObject storage = new JSONObject();

    public static Object execute(String code, Map<String, Object> variables) throws ScriptException {
        if (js == null) return new ScriptException("No javascript engine");

        Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
        variables.forEach(bindings::put);
        bindings.put("storage", storage);
        return js.eval(code);
    }
}
