package com.fr.design.gui.toast;

/**
 * Created by richie on 15/11/24.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class Toast extends JFrame {

    private final float MAX_OPACITY = 0.8f;
    private final float OPACITY_INCREMENT = 0.05f;
    private final int FADE_REFRESH_RATE = 20;

    private static final int WINDOW_RADIUS = 0;
    private static final int CHARACTER_LENGTH_MULTIPLIER = 9;


    public Toast(Window owner, String toastText) {
        setTitle("Toast");
        setLayout(new GridBagLayout());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setFocusableWindowState(false);

        setOpacity(0.4f);


        // setup the toast lable
        JLabel b1 = new JLabel(toastText);
        b1.setForeground(Color.WHITE);
        b1.setOpaque(false);
        add(b1);
        int textWidth = toastText.length() * CHARACTER_LENGTH_MULTIPLIER;

        setSize(textWidth, 50);

        //int x = (int) (owner.getLocation().getX() + (owner.getWidth() / 2));
        int x = (int) (owner.getLocation().getX() + owner.getWidth() - textWidth);

        int y = (int) (owner.getLocation().getY() + (double)owner.getHeight() / 2);
        setLocation(new Point(x, y));


        // configure frame
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        getContentPane().setBackground(new Color(0, 0, 0, 170));

    }


    public void fadeIn() {
        setOpacity(0);
        setVisible(true);

        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 0;


            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += OPACITY_INCREMENT;
                setOpacity(Math.min(opacity, MAX_OPACITY));
                if (opacity >= MAX_OPACITY) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }


    public void fadeOut() {
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = MAX_OPACITY;


            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= OPACITY_INCREMENT;
                setOpacity(Math.max(opacity, 0));
                if (opacity <= 0) {
                    timer.stop();
                    setVisible(false);
                    dispose();
                }
            }
        });

        setOpacity(MAX_OPACITY);
        timer.start();
    }


    public static void makeToast(final Window owner, final String toastText, final int durationSec) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast toastFrame = new Toast(owner, toastText);
                    toastFrame.fadeIn();
                    Thread.sleep((long)durationSec * 1000);
                    toastFrame.fadeOut();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


    }


    public static void main(String args[]) {
        final JFrame frame = new JFrame("Cloud Tester");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton toastButton = new JButton("show toast");
        jPanel.add(toastButton);

        toastButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toast.makeToast(frame, "a asdadasdsadasni撒的撒打算!", 10);
            }
        });


        frame.add(jPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}