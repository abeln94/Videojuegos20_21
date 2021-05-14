package com.unizar.game2;

import com.unizar.Utils;
import com.unizar.game.commands.EngineException;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Command2 {
    private String action = null;
    private Set<Filterable2> parameters = new HashSet<>();

    public void setAction(String action) throws EngineException {
        if (this.action != null)
            throw new EngineException("Solo puedes especificar una acción");

        this.action = action;
    }

    public void initParameter(String parameter) throws EngineException {
        if (parameters.stream().anyMatch(m -> m.parameter.equals(parameter)))
            throw new EngineException("Ya habías especificado el parámetro '" + parameter + "' antes");

        parameters.add(new Filterable2(parameter));
    }

    public Filterable2 getParameter(String parameter) {
        return parameters.stream()
                .filter(m -> m.parameter.equals(parameter))
                .findFirst()
                .orElseGet(() -> {
                    final Filterable2 f = new Filterable2(parameter);
                    parameters.add(f);
                    return f;
                });
    }

    public boolean matches(Engine2.Action action) {
        return action.name.equals(this.action)
                && this.parameters.stream().map(p -> p.parameter).collect(Collectors.toSet()).equals(action.parameters.keySet());
    }

    public String toCommandString() {
        return action + Utils.joinList("", " ", " ", parameters.stream().map(p -> p.parameter).collect(Collectors.toList()));
    }

    public static class Filterable2 {
        private String parameter;
        private Set<String> elementWords = new HashSet<>();
        private boolean all = false;
        private boolean any = false;

        public Filterable2(String parameter) {
            this.parameter = parameter;
        }

        public void addWord(String word) {
            elementWords.add(word);
        }

        public void markAsAll() {
            all = true;
        }

        public void markAsAny() {
            any = true;
        }

        public String filter(String filter) {

        }

        public JSONObject get() {
            return null;
        }
    }
}
