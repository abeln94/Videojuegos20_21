package com.unizar.game2;

import com.unizar.game.Window;
import com.unizar.game.commands.EngineException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game2 extends KeyAdapter {
    // ------------------------- objects -------------------------
    public final Window window;

    // ------------------------- data -------------------------

    private Data data;

    public Game2(String root) {
        this.data = new Data(root);
        window = new Window("engine2", 1, null);
        window.setKeyListener(this);
    }

    public void init() {
        data.loadFromFiles();
    }

    public void run(String rawText) {
        // skip empty lines
        if (rawText.isEmpty()) return;

        // write command
        window.addOutput("\n> " + rawText);

        try {
            final Command2 command = Analyzer.analyze(rawText, data.getElementWords(), data.getActionWords(), data.getModifierWords());

            // execute
            String result = Engine2.execute(data.getPlayer(), command);

            window.addOutput(result);

            // player end

        } catch (EngineException e) {
            if (e.userError != null) window.addOutput(e.userError);
            if (e.newUserInput != null) window.setCommand(e.newUserInput);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            // press enter to perform command
            case KeyEvent.VK_ENTER:
                String command = window.getCommand();
                window.clearCommand();
                run(command);
                break;
        }
    }
}
