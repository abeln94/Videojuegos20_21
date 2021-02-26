package com.unizar;

public class Main {

    public static void main(String[] args) {
        Window window = new Window();

        window.setCommandListener(e -> {
            window.addOutput(e);
            String text;
            String image = e;
            switch (e) {
                case "blue" -> text = "A nice blue room";
                case "green" -> text = "A relaxing room";
                case "red" -> text = "A dangerous room";
                default -> {
                    text = "Unknown room";
                    image = null;
                }
            }
            window.setDescription(text);
            if(image != null) window.setImage(image);
        });

        window.setDescription("red/green/blue");
    }
}
