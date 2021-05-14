package com.unizar.game2;

import org.json.simple.JSONObject;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;

public class ScriptInterface {
    public static void main(String args[]) throws Exception {

        JSONObject object = new JSONObject();
        object.put("test", 1);
        System.out.println(object);

        ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
        Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("object", object);
        final Object result = js.eval("object.test=2");
        System.out.println(result.getClass() + ":" + result);
        System.out.println(object);
    }
}
