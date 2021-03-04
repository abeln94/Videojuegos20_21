package com.unizar.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;

/**
 * Manages the window of the game.
 * A picture, an input text and two output texts
 */
public class Window {

    public interface InputListener {
        void onText(String text);
    }

    // ------------------------- elements -------------------------
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
                redrawImage();
                height -= imageHeight;

                // command output, remaining
                commandOutput.getParent().getParent().setBounds(0, imageHeight, width, height);
            }
        });

        // image
        image = new JLabel();
        image.setFocusable(false);
        frame.add(image);

        // command output
        commandOutput = new JTextArea(0, 0);
        commandOutput.setEditable(false);
        commandOutput.setLineWrap(true);
        commandOutput.setWrapStyleWord(true);
        commandOutput.setFocusable(false);
        frame.add(new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // command input
        commandInput = new JTextField();
        frame.add(commandInput);

        // description
        description = new JTextArea();
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFocusable(false);
        frame.add(new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // show
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setResizable(false); // noooooo
        frame.setVisible(true);
        commandInput.grabFocus();
    }

    // ------------------------- command -------------------------

    /**
     * Registers a listener for when commands are entered on the window
     *
     * @param inputListener will be called when the user enters a command
     */
    public void setCommandListener(InputListener inputListener) {
        commandInput.addActionListener(e -> {
            inputListener.onText(commandInput.getText());
            commandInput.setText("");
        });
    }

    /**
     * Registers a listener for any key pressed
     *
     * @param listener will be called when the user presses any key
     */
    public void setKeyListener(KeyAdapter listener) {
        commandInput.addKeyListener(listener);
    }

    // ------------------------- description -------------------------

    /**
     * Clears the description output text
     */
    public void clearDescription() {
        description.setText("");
    }

    /**
     * Adds a line to the description output text
     *
     * @param text line to add
     */
    public void addDescription(String text) {
        description.setText(description.getText() + (description.getText().isEmpty() ? "" : "\n") + text);
    }

    // ------------------------- output -------------------------

    /**
     * Clears the command output text
     */
    public void clearOutput() {
        commandOutput.setText("");
    }

    /**
     * Adds a line to the command output text
     *
     * @param text line to add
     */
    public void addOutput(String text) {
        commandOutput.setText(commandOutput.getText() + (commandOutput.getText().isEmpty() ? "" : "\n") + text);
    }

    // ------------------------- image -------------------------

    /**
     * Draws an image
     *
     * @param img image to draw
     */
    public void drawImage(Image img) {
        if (img == null) img = savedImg;
        else savedImg = img;

        if (img == null) return;
        image.setIcon(new ImageIcon(
                img.getScaledInstance(image.getWidth(), image.getHeight(),
                        Image.SCALE_SMOOTH)
        ));
    }

    private Image savedImg = null;

    /**
     * Redraws the previous image. Internal use
     */
    private void redrawImage() {
        drawImage(savedImg);
    }

}
