package com.unizar.game.elements;

import com.unizar.game.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A generic NPC
 */
abstract public class NPC extends Holdable {


    public NPC(String name, Class<? extends Element> room) {
        super(name, room);
    }

    public Class<Room> getRoom() {
        Class<? extends Element> holder = super.getHolder();
        if (Room.class.isAssignableFrom(holder)) {
            return (Class<Room>) holder;
        } else {
            return null;
        }
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        if (command == Command.Wait && npc == this) return () -> "Esperas";

        return super.doCommand(command, npc);
    }

    // ------------------------- commands -------------------------

    public static class AvailableCommand {
        public Command command;
        public Element element;
        Supplier<String> action;

        public AvailableCommand(Command command, Element element, Supplier<String> action) {
            this.command = command;
            this.element = element;
            this.action = action;
        }
    }

    public List<AvailableCommand> getAllAvailableCommands() {
        List<AvailableCommand> available = new ArrayList<>();

        List<Element> elements = game.data.getInteractables(this);
        for (Element element : elements) {
            for (Command command : Command.values()) {
                Supplier<String> action = element.doCommand(command, this);
                if (action != null) available.add(new AvailableCommand(command, element, action));
            }
        }
        return available;
    }

    public void actAsPlayer() {
        List<AvailableCommand> commands = getAllAvailableCommands();

        System.out.println(name + " -> " + commands.get(new Random().nextInt(commands.size())).action.get());
    }

}
