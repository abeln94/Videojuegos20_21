package com.unizar.game2;

import com.unizar.game.commands.EngineException;
import org.json.simple.JSONObject;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Engine2 {
    public static class Action {
        String name;
        Map<String, List<String>> parameters;
        String code;
    }


    public static String execute(JSONObject npc, Command2 command, List<Action> actions, Set<JSONObject> elements) throws EngineException {

        Action action = actions.stream().filter(command::matches).findFirst().orElseThrow(() -> new EngineException("No se como '" + command.toCommandString()));

        Map<String, Object> execution = new HashMap<>();

        action.parameters.forEach((parameter, filters) -> {
            Command2.Filterable2 filterable = command.getParameter(parameter);
            filters.forEach(filterable::filter);
            execution.put(parameter, filterable.get());
        });

        try {
            ScriptEngine.execute(action.code, execution);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new EngineException("Can't execute code");
        }
    }
}
