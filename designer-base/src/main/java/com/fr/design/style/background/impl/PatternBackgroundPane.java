package com.fr.design.style.background.impl;

import com.fr.base.GraphHelper;
import com.fr.base.background.PatternBackground;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Background;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * Pattern background pane.
 */
public class PatternBackgroundPane extends BPane {

    private int patternIndex = 0; // pattern setIndex.
    protected ColorSelectBox foregroundColorPane;
    protected ColorSelectBox backgroundColorPane;
    private PatternButton[] patternButtonArray;

    public PatternBackgroundPane() {
        super(12);

        foregroundColorPane.addSelectChangeListener(colorChangeListener);
        backgroundColorPane.addSelectChangeListener(colorChangeListener);
    }

    public PatternBackgroundPane(int nColumn) {
        super(nColumn);

        if(foregroundColorPane != null) {
            foregroundColorPane.addSelectChangeListener(colorChangeListener);
        }
        if(backgroundColorPane != null) {
            backgroundColorPane.addSelectChangeListener(colorChangeListener);
        }
    }

    protected String titleOfTypePane() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Pattern");
    }
    protected LayoutManager layoutOfTypePane(int nColumn) {
        return FRGUIPaneFactory.createNColumnGridLayout(nColumn);
    }

    protected void setChildrenOfTypePane(JPanel typePane2) {
        ButtonGroup patternButtonGroup = new ButtonGroup();
        patternButtonArray = new PatternButton[PatternBackground.PATTERN_COUNT];
        for (int i = 0; i < PatternBackground.PATTERN_COUNT; i++) {
            patternButtonArray[i] = new PatternButton(i);
            patternButtonGroup.add(patternButtonArray[i]);
            typePane2.add(patternButtonArray[i]);
        }
    }

    protected void setChildrenOfContentPane(JPanel contentPane) {
        // colors
        JPanel colorPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Colors"));
        contentPane.add(colorPane);

        foregroundColorPane = new ColorSelectBox(80);
        backgroundColorPane = new ColorSelectBox(80);
        foregroundColorPane.setSelectObject(Color.lightGray);
        backgroundColorPane.setSelectObject(Color.black);

        colorPane.add(Box.createHorizontalStrut(2));
        colorPane.add(this.createLabelColorPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Foreground")
            + ":", foregroundColorPane));

        colorPane.add(Box.createHorizontalStrut(8));

        colorPane.add(this.createLabelColorPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background")
            + ":", backgroundColorPane));
    }

    private JPanel createLabelColorPane(String text,
        ColorSelectBox colorPane) {
        JPanel labelColorPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        labelColorPane.add(new UILabel(text));
        labelColorPane.add(colorPane);

        return labelColorPane;
    }

    public void populate(Background background) {
        if (background != null && background instanceof PatternBackground) {
            PatternBackground patternBackground = (PatternBackground) background;
            int patternIndex = patternBackground.getPatternIndex();

            if (patternIndex >= 0
                && patternIndex < this.patternButtonArray.length) {
                this.patternButtonArray[patternIndex].setSelected(true);
                this.patternIndex = patternIndex;
            } else {
                this.patternIndex = 0;
            }

            foregroundColorPane.setSelectObject(patternBackground.getForeground());
            backgroundColorPane.setSelectObject(patternBackground.getBackground());
        } else {
            patternIndex = 0;
            this.patternButtonArray[0].setSelected(true);

            foregroundColorPane.setSelectObject(Color.lightGray);
            backgroundColorPane.setSelectObject(Color.black);
        }
    }

    public Background update() throws Exception {
        return new PatternBackground(patternIndex, foregroundColorPane.getSelectObject(), backgroundColorPane.getSelectObject());
    }

    public void addChangeListener(ChangeListener changeListener) {
        foregroundColorPane.addSelectChangeListener(changeListener);
        backgroundColorPane.addSelectChangeListener(changeListener);

        for (int i = 0; i < this.patternButtonArray.length; i++) {
            this.patternButtonArray[i].addChangeListener(changeListener);
        }
    }
    // Foreground or Background changed.
    ChangeListener colorChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            for (int i = 0; i < patternButtonArray.length; i++) {
                patternButtonArray[i].setPatternForeground(foregroundColorPane.getSelectObject());
                patternButtonArray[i].setPatternBackground(backgroundColorPane.getSelectObject());
            }

            PatternBackgroundPane.this.repaint();// repaint
        }
    };

    /**
     * Pattern type button.
     */
    class PatternButton extends JToggleButton implements ActionListener {

        public PatternButton(int pIndex) {
            this.pIndex = pIndex;
            this.addActionListener(this);

            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setBorder(null);
            this.patternBackground = new PatternBackground(this.pIndex,
                Color.lightGray, Color.black);
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            Dimension d = getSize();
            this.patternBackground.paint(g2d, new Rectangle2D.Double(0, 0,
                d.width - 1d, d.height - 1d));

            if (this.pIndex == patternIndex) {// it's selected.
                g2d.setPaint(new Color(255, 51, 0));
            } else {
                g2d.setPaint(Color.gray);
            }
            GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1d,
                d.height - 1d));
        }

        public Dimension getPreferredSize() {
            return new Dimension(24, 24);
        }

        public void setPatternForeground(Color foreground) {
            this.patternBackground.setForeground(foreground);
        }

        public void setPatternBackground(Color background) {
            this.patternBackground.setBackground(background);
        }

        /**
         * set Pattern setIndex.
         */
        public void actionPerformed(ActionEvent evt) {
            PatternBackgroundPane.this.patternIndex = pIndex;

            fireChagneListener();
            PatternBackgroundPane.this.repaint();// repaint
        }

        public void addChangeListener(ChangeListener changeListener) {
            this.changeListener = changeListener;
        }

        private void fireChagneListener() {
            if (this.changeListener != null) {
                ChangeEvent evt = new ChangeEvent(this);
                this.changeListener.stateChanged(evt);
            }
        }
        private int pIndex = 0;
        private PatternBackground patternBackground;
    }
}
