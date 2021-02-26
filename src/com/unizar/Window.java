package com.unizar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window {

    public static final int IMAGE_WIDTH = 320;
    public static final int IMAGE_HEIGHT = 152;
    public static final int WINDOW_HEIGHT = 480;
    public static final int WINDOW_WIDTH = 640;

    private final JTextArea commandOutput;

    interface InputListener {
        void onText(String text);
    }

    private final JTextField commandInput;
    private final JTextArea description;
    private final JLabel image;

    public Window() {

        // frame
        JFrame frame = new JFrame("The hobbit");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // image
        image = new JLabel();
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        image.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        frame.add(image, gbc);

        // command output
        commandOutput = new JTextArea();
        commandOutput.setEditable(false);
        gbc.gridy = 1;
        gbc.weighty = 1;
        frame.add(commandOutput, gbc);

        // command input
        commandInput = new JTextField();
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        frame.add(commandInput, gbc);

        // description
        description = new JTextArea();
        description.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        description.setPreferredSize(new Dimension(WINDOW_WIDTH - IMAGE_WIDTH, WINDOW_HEIGHT));
        frame.add(description, gbc);

        // show
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setCommandListener(InputListener inputListener) {
        commandInput.addActionListener(e -> {
            inputListener.onText(commandInput.getText());
            commandInput.setText("");
        });
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setOutput(String text) {
        commandOutput.setText(text);
    }

    public void addOutput(String text) {
        setOutput(commandOutput.getText() + (commandOutput.getText().isEmpty() ? "" : "\n") + text);
    }

    public void setImage(String name) {
        try {
            image.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/" + name + ".png")).getScaledInstance(image.getWidth(), image.getHeight(),
                    Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
