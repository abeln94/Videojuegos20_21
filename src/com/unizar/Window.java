package com.unizar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
        frame.setLayout(null);
        frame.addComponentListener(new ComponentAdapter() {

            private int previousWidth = 0;
            private int previousHeight = 0;

            @Override
            public void componentResized(ComponentEvent e) {
                // manual position of elements

                int width = frame.getContentPane().getWidth(); // will change with remaining space
                int height = frame.getContentPane().getHeight(); // will change with remaining space

                if(previousWidth == width && previousHeight == height) return;
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
                paintImage();
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
        description.setPreferredSize(new Dimension(WINDOW_WIDTH - IMAGE_WIDTH, WINDOW_HEIGHT));
        frame.add(new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // show
        frame.pack();
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

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setOutput(String text) {
        commandOutput.setText(text);
    }

    public void addOutput(String text) {
        setOutput(commandOutput.getText() + (commandOutput.getText().isEmpty() ? "" : "\n") + text);
    }

    private BufferedImage img = null;

    public void setImage(String name) {
        try {
            img = ImageIO.read(getClass().getResource("/images/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        paintImage();
    }

    public void paintImage() {
        if (img == null) return;

        image.setIcon(new ImageIcon(
                img.getScaledInstance(image.getWidth(), image.getHeight(),
                        Image.SCALE_REPLICATE)
        ));
    }

}
