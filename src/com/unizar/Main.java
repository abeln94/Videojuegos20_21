package com.unizar;

public class Main {

    public static void main(String[] args) {
        Window window = new Window();

        window.setCommandListener(e -> {
            window.addOutput("> " + e);
            String text;
            switch (e) {
                case "blue" -> text = "A nice blue room";
                case "green" -> text = "A relaxing room";
                case "red" -> text = "A dangerous room";
                default -> {
                    window.addOutput("Unknown room\n");
                    return;
                }
            }
            window.addOutput("You enter the room\n");
            window.setDescription(text);
            window.setImage(e);
        });

        window.setDescription("red/green/blue");
    }
}
