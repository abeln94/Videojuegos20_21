package com.unizar.game;

import com.unizar.AlphaIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages the window of the game.
 * A picture, an input text and two output texts
 */
public class Window {

    // ------------------------- elements -------------------------
    private final JFrame frame;
    private final JTextArea commandOutput;
    private final JTextField commandInput;
    private final JTextArea description;
    private final JLabel image;

    public Window(String title, int imageRatio, String fontFile) {

        // frame
        frame = new JFrame(title);
        frame.setIconImage(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT));
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
        if (fontFile != null) {
            try {
                InputStream is = new FileInputStream(fontFile);
                font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
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
        if (font != null) commandOutput.setFont(font);
        frame.add(new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // command input
        commandInput = new JTextField();
        if (font != null) commandInput.setFont(font);
        frame.add(commandInput);

        // description
        description = new JTextArea();
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFocusable(false);
        if (font != null) description.setFont(font);
        frame.add(new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // changeFont listener
        for (Component component : new Component[]{frame, commandOutput, description}) {
            component.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.isControlDown()) {
                        // change font size
                        int amount = e.getWheelRotation();
                        if (amount == 0) return;
                        for (Component textview : new Component[]{commandInput, commandOutput, description}) {
                            Font font1 = textview.getFont();
                            final float newSize = font1.getSize2D() - amount;
                            if (newSize > 8 && newSize < 72) {
                                font1 = font1.deriveFont(newSize);
                                textview.setFont(font1);
                            }
                        }
                    } else {
                        // scroll parent
                        Container parent = component.getParent();
                        if (parent != null) parent.dispatchEvent(e);
                    }
                }
            });
        }

        // show
        frame.setSize(640, 480);
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false); // noooooo
        frame.setVisible(true);
        commandInput.requestFocusInWindow();
    }

    // ------------------------- command -------------------------

    /**
     * Registers a listener for any key pressed
     *
     * @param listener will be called when the user presses any key
     */
    public void setKeyListener(KeyAdapter listener) {
        commandInput.addKeyListener(listener);
    }

    /**
     * @return the entered command
     */
    public String getCommand() {
        return commandInput.getText();
    }

    /**
     * Clears the command input textbox
     */
    public void clearCommand() {
        commandInput.setText("");
    }

    /**
     * Sets the command input text
     */
    public void setCommand(String command) {
        if (command == null) return;

        commandInput.setText(command);
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
    public void setDescription(String text) {
        description.setText(text);
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
        commandOutput.setCaretPosition(commandOutput.getText().length());
    }

    // ------------------------- image -------------------------

    /**
     * Draws an image
     *
     * @param path image to draw
     */
    public void drawImage(String path) {
        savedImg = path;

        if (path == null) {
            image.setIcon(null);
            return;
        }

        try {

            // read image
            Image img = ImageIO.read(new File(path));

            // show image
            final float alpha = image.getIcon() != null ? ((AlphaIcon) image.getIcon()).getAlpha() : 1f;
            image.setIcon(new AlphaIcon(new ImageIcon(
                    img.getScaledInstance(image.getWidth(), image.getHeight(),
                            Image.SCALE_SMOOTH)), alpha
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the transparency of the image
     *
     * @param transparency between 0 (invisible) to 1 (opaque)
     */
    public void setImageTransparency(float transparency) {
        final Icon icon = image.getIcon();
        if (icon != null) {
            ((AlphaIcon) icon).setAlpha(transparency);
            image.updateUI();
        }
    }

    private String savedImg = null;

    /**
     * Redraws the previous image. Internal use
     */
    private void redrawImage() {
        drawImage(savedImg);
    }

    // ------------------------- behaviour -------------------------

    private int caretSavedPos = -1;

    /**
     * Disables user inputs
     */
    public void disableInput() {
        commandInput.setEditable(false);
        caretSavedPos = commandInput.getCaretPosition();
        commandInput.getCaret().setVisible(false);
    }

    /**
     * Enables user inputs
     */
    public void enableInput() {
        if (caretSavedPos < 0 || caretSavedPos > commandInput.getText().length())
            caretSavedPos = commandInput.getText().length();
        commandInput.setCaretPosition(caretSavedPos);
        commandInput.setEditable(true);
        final Caret caret = commandInput.getCaret();
        if (caret != null) caret.setVisible(true);
    }

    /**
     * Closes the window
     */
    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
