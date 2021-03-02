package com.unizar.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Window {

    public interface InputListener {
        void onText(String text);
    }

    // elements
    private final JTextArea commandOutput;
    private final JTextField commandInput;
    private final JTextArea description;
    private final JLabel image;

    public Window(String title) {

        // frame
        JFrame frame = new JFrame(title);
        frame.setLayout(null);
        frame.addComponentListener(new ComponentAdapter() {

            private int previousWidth = 0;
            private int previousHeight = 0;

            @Override
            public void componentResized(ComponentEvent e) {
                // manual position of elements

                int width = frame.getContentPane().getWidth(); // will change with remaining space
                int height = frame.getContentPane().getHeight(); // will change with remaining space

                if (previousWidth == width && previousHeight == height) return;
                previousWidth = width;
                previousHeight = height;

                // description, all right half of screen
                width /= 2;
                description.getParent().getParent().setBounds(width, 0, width, height);

                // commandInput, bottom, keep original height
                int inputHeight = commandInput.getPreferredSize().height;
                height -= inputHeight;
                commandInput.setBounds(0, height, width, inputHeight);

                // image, keep ratio
                int imageHeight = width * 152 / 320;
                image.setBounds(0, 0, width, imageHeight);
                drawImage(null);
                height -= imageHeight;

                // command output, remaining
                commandOutput.getParent().getParent().setBounds(0, imageHeight, width, height);
            }
        });

        // image
        image = new JLabel();
        frame.add(image);

        // command output
        commandOutput = new JTextArea(0, 0);
        commandOutput.setEditable(false);
        commandOutput.setLineWrap(true);
        frame.add(new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // command input
        commandInput = new JTextField();
        frame.add(commandInput);

        // description
        description = new JTextArea();
        description.setEditable(false);
        commandOutput.setLineWrap(true);
        frame.add(new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // show
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false); // noooooo
        frame.setVisible(true);
        commandInput.grabFocus();
    }

    public void setCommandListener(InputListener inputListener) {
        commandInput.addActionListener(e -> {
            inputListener.onText(commandInput.getText());
            commandInput.setText("");
        });
    }

    public void addDescription(String text) {
        description.setText(description.getText() + (description.getText().isEmpty() ? "" : "\n") + text);
    }

    public void clearDescription() {
        description.setText("");
    }

    public void clearOutput() {
        commandOutput.setText("");
    }

    public void addOutput(String text) {
        commandOutput.setText(commandOutput.getText() + (commandOutput.getText().isEmpty() ? "" : "\n") + text);
    }

    private Image savedImg = null;

    public void drawImage(Image img) {
        if (img == null) img = savedImg;
        else savedImg = img;

        if (img == null) return;
        image.setIcon(new ImageIcon(
                img.getScaledInstance(image.getWidth(), image.getHeight(),
                        Image.SCALE_REPLICATE)
        ));
    }

}
