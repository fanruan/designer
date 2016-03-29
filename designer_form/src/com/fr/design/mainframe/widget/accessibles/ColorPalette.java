package com.fr.design.mainframe.widget.accessibles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import com.fr.design.gui.ibutton.UIButton;

import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import com.fr.general.Inter;
import com.fr.design.layout.FRGUIPaneFactory;

public class ColorPalette extends JPopupMenu {

    private Border BLACK_BORDER = BorderFactory.createLineBorder(new Color(127, 157, 185));
    private Object[] colors = new Object[]{new Object[]{Inter.getLocText("Black"), new Color(0, 0, 0)}, new Object[]{Inter.getLocText("Crimson"), new Color(128, 0, 0)}, new Object[]{Inter.getLocText("Red"), new Color(255, 0, 0)},
        new Object[]{Inter.getLocText("Pink"), new Color(255, 0, 255)}, new Object[]{Inter.getLocText("Rose_Red"), new Color(255, 153, 204)}, new Object[]{Inter.getLocText("Brown"), new Color(153, 51, 0)},
        new Object[]{Inter.getLocText("Orange"), new Color(255, 102, 0)}, new Object[]{Inter.getLocText("Light_Orange"), new Color(255, 153, 0)}, new Object[]{Inter.getLocText("Golden"), new Color(255, 204, 0)},
        new Object[]{Inter.getLocText("Brown_Orange"), new Color(255, 204, 153)}, new Object[]{"", new Color(51, 51, 0)}, new Object[]{"", new Color(128, 128, 0)}, new Object[]{"", new Color(153, 204, 0)},
        new Object[]{"", new Color(255, 255, 0)}, new Object[]{"", new Color(255, 255, 153)}, new Object[]{"", new Color(0, 51, 0)}, new Object[]{"", new Color(0, 128, 0)},
        new Object[]{"", new Color(51, 153, 102)}, new Object[]{"", new Color(172, 168, 153)}, new Object[]{"", new Color(204, 255, 204)}, new Object[]{"", new Color(0, 51, 102)},
        new Object[]{"", new Color(0, 128, 128)}, new Object[]{"", new Color(51, 204, 204)}, new Object[]{"", new Color(0, 255, 255)}, new Object[]{"", new Color(204, 255, 255)},
        new Object[]{"", new Color(0, 0, 128)}, new Object[]{"", new Color(0, 0, 255)}, new Object[]{"", new Color(51, 102, 255)}, new Object[]{"", new Color(0, 204, 255)},
        new Object[]{"", new Color(153, 204, 255)}, new Object[]{"", new Color(51, 51, 153)}, new Object[]{"", new Color(102, 102, 153)}, new Object[]{"", new Color(128, 0, 128)},
        new Object[]{"", new Color(153, 51, 102)}, new Object[]{"", new Color(204, 153, 255)}, new Object[]{"", new Color(51, 51, 51)}, new Object[]{"", new Color(128, 128, 128)},
        new Object[]{"", new Color(153, 153, 153)}, new Object[]{"", new Color(192, 192, 192)}, new Object[]{"", new Color(255, 255, 255)}};
    private UIButton btnDefault;
    private UIButton btnCustom;
    private UIButton btn;
    private JToggleButton[] btnColors;
    private ButtonGroup group;

    public ColorPalette() {
        setBorder(BorderFactory.createCompoundBorder(BLACK_BORDER, BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JToolBar top_bar = getTopBar();
        add(top_bar, BorderLayout.NORTH);
        JToolBar palette_bar = getPaletteBar();
        add(palette_bar, BorderLayout.CENTER);
        JToolBar bottom_bar = getBottomBar();
        add(bottom_bar, BorderLayout.SOUTH);
    }

    public void addDefaultAction(ActionListener l) {
        btnDefault.addActionListener(l);
    }

    public void addCustomAction(ActionListener l) {
        btnCustom.addActionListener(l);
    }

    public void addColorAction(ActionListener l) {
        for (JToggleButton button : btnColors) {
            button.addActionListener(l);
        }
    }

    private JToolBar getBottomBar() {
        JToolBar bottom_bar = new JToolBar();
        bottom_bar.setOpaque(false);
        bottom_bar.setLayout(new /**/GridLayout(1, 1));
        bottom_bar.setBorderPainted(false);
        bottom_bar.setFloatable(false);
        btnCustom = getBtn(Inter.getLocText("Custom") + "...");
        bottom_bar.add(btnCustom);
        return bottom_bar;
    }

    private UIButton getBtn(String buttonName) {
        if (btn == null) {
            btn = new UIButton();
            btn.setBorder(BLACK_BORDER);
            btn.setFocusPainted(false);
            btn.setText(buttonName);
        }
        return btn;
    }

    private JToolBar getPaletteBar() {
        JToolBar palette_bar = new JToolBar();
        palette_bar.setOpaque(false);
        GridLayout layout = new/**/ GridLayout(5, 8, 1, 1);
        palette_bar.setLayout(layout);
        palette_bar.setBorderPainted(false);
        palette_bar.setFloatable(false);
        group = new ButtonGroup();
        btnColors = new JToggleButton[colors.length];
        for (int i = 0; i < colors.length; i++) {
            btnColors[i] = new JToggleButton();
            Object[] color_object = (Object[]) colors[i];
            String name = (String) color_object[0];
            Color color = (Color) color_object[1];
            btnColors[i].setIcon(new ColorIcon(name, color));
            btnColors[i].setToolTipText(name);
            btnColors[i].setFocusPainted(false);
            group.add(btnColors[i]);
            palette_bar.add(btnColors[i]);
        }
        return palette_bar;
    }

    private JToolBar getTopBar() {
        JToolBar top_bar = new JToolBar();
        top_bar.setBorderPainted(false);
        top_bar.setOpaque(false);
        top_bar.setFloatable(false);
        top_bar.setLayout(new /**/GridLayout(1, 1));
        btnDefault = getBtn(Inter.getLocText("Form-Restore_Default_Value"));
        top_bar.add(btnDefault);
        return top_bar;
    }

    public void setChoosedColor(Color choosedColor) {
        group.clearSelection();
        if (choosedColor != null) {
            for (int i = 0; i < colors.length; i++) {
                Color c = (Color) ((Object[]) colors[i])[1];
                if (c.equals(choosedColor)) {
                    btnColors[i].setSelected(true);
                    return;
                }
            }
        }
    }
}