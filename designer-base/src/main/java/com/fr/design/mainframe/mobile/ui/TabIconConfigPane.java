package com.fr.design.mainframe.mobile.ui;

import com.fr.base.GraphHelper;
import com.fr.base.IconManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.web.CustomIconPane;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class TabIconConfigPane extends JPanel {
    private UIButton editIconButton;
    private String curIconName;
    private IconButton selectIconButton;
    private ArrayList<IconButton> iconButtons = new ArrayList<IconButton>();

    public TabIconConfigPane(int count) {
        initComp(count);
    }

    public void initComp(int count) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel panel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        editIconButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
        editIconButton.setPreferredSize(new Dimension(62, 20));
        panel.add(editIconButton);
        editIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final CustomIconPane cip = new CustomIconPane();
                BasicDialog editDialog = cip.showWindow(DesignerContext.getDesignerFrame());
                editDialog.addDialogActionListener(new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        curIconName = cip.update();
                        setShowIconImage();
                        TabIconConfigPane.this.repaint();
                    }
                });
                editDialog.setVisible(true);
            }
        });
        editIconButton.setEnabled(false);
        this.add(panel, BorderLayout.CENTER);

        JPanel northPane = new JPanel();
        northPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        for (int i = 0; i < count; i++) {
            IconButton iconButton = new IconButton("");
            northPane.add(iconButton);
            iconButtons.add(iconButton);
        }
        this.add(northPane, BorderLayout.NORTH);
    }

    public void setShowIconImage() {
        selectIconButton.setIconName(curIconName);
    }

    public void populate(ArrayList<String> iconArr) {
        for (int i = 0; i < iconButtons.size(); i++) {
            iconButtons.get(i).setIconName(iconArr.get(i));
        }
    }

    public ArrayList<String> update() {
        ArrayList<String> iconNames = new ArrayList<String>();
        for (int i = 0; i < iconButtons.size(); i++) {
            iconNames.add(iconButtons.get(i).getIconName());
        }
        return iconNames;
    }


    private class IconButton extends JToggleButton implements ActionListener {
        private String iconName;
        private Image iconImage = null;
        private static final int ICON_BUTTON_SIZE = 20;
        private static final int ICON_X = 2;
        private static final int ICON_Y = 2;

        public IconButton(String name) {
            this.iconName = name;
            this.addActionListener(this);
            this.setBackground(Color.WHITE);
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.iconImage = WidgetInfoConfig.getInstance().getIconManager().getIconImage(name);
        }

        @Override
        public void updateUI() {
            setUI(new BasicButtonUI() {
                public void paint(Graphics g, JComponent c) {
                    super.paint(g, c);
                }
            });
        }

        public String getIconName() {
            return iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
            this.iconImage = WidgetInfoConfig.getInstance().getIconManager().getIconImage(iconName);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // carl:这里缩放显示 16 × 16
            if (iconImage != null) {
                g2d.drawImage(iconImage, ICON_X, ICON_Y, IconManager.DEFAULT_ICONWIDTH, IconManager.DEFAULT_ICONHEIGHT, null);
            }
            if (this.iconName != null && ComparatorUtils.equals(this, selectIconButton)) {
                g2d.setPaint(Color.decode("#419BF9"));
            } else {
                g2d.setPaint(Color.decode("#D9DADD"));
            }
            GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, 20, 20), Constants.LINE_MEDIUM);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(ICON_BUTTON_SIZE, ICON_BUTTON_SIZE);
        }

        public void actionPerformed(ActionEvent evt) {
            selectIconButton = this;
            editIconButton.setEnabled(true);
            TabIconConfigPane.this.repaint();// repaint
        }

        @Override
        public void addChangeListener(ChangeListener changeListener) {
            this.changeListener = changeListener;
        }

        private void fireChagneListener() {
            if (this.changeListener != null) {
                ChangeEvent evt = new ChangeEvent(this);
                this.changeListener.stateChanged(evt);
            }
        }

    }
}
