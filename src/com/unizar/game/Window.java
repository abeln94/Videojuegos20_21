package com.unizar.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.io.InputStream;

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

    public Window(String title, int imageRatio, String fontName) {

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
                int top = 0;
                int left = 0;

                // update only if the size changed
                if (previousWidth == width && previousHeight == height) return;
                previousWidth = width;
                previousHeight = height;

                // image, top of screen, keep ratio
                int imageHeight = width / imageRatio;
                int imageWidth = width;
                int imageLeft = 0;
                if (imageHeight > height / 2) {
                    // reduce size so that it doesn't cover more than half the height
                    imageHeight = height / 2;
                    imageWidth = imageHeight * imageRatio;
                    imageLeft = (width - imageWidth) / 2;
                }
                image.setBounds(left + imageLeft, top, imageWidth, imageHeight);
                redrawImage();
                height -= imageHeight;
                top += imageHeight;

                // description, all right half of screen
                width /= 2;
                description.getParent().getParent().setBounds(left + width, top, width, height);

                // commandInput, bottom, keep original height
                int inputHeight = commandInput.getPreferredSize().height;
                height -= inputHeight;
                commandInput.setBounds(left, top + height, width, inputHeight);

                // command output, remaining
                commandOutput.getParent().getParent().setBounds(left, top, width, height);
            }
        });

        // load font
        Font font = null;
        try {
            InputStream is = this.getClass().getResourceAsStream(fontName);
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

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
        commandOutput.setFont(font);
        frame.add(new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // command input
        commandInput = new JTextField();
        commandInput.setFont(font);
        frame.add(commandInput);

        // description
        description = new JTextArea();
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFocusable(false);
        description.setFont(font);
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

    public void clearCommand() {
        commandInput.setText("");
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
        savedImg = img;
        if (img == null) {
            image.setIcon(null);
        } else {
            image.setIcon(new ImageIcon(
                    img.getScaledInstance(image.getWidth(), image.getHeight(),
                            Image.SCALE_SMOOTH)
            ));
        }
    }

    private Image savedImg = null;

    /**
     * Redraws the previous image. Internal use
     */
    private void redrawImage() {
        drawImage(savedImg);
    }

}
