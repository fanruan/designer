package com.fr.design.mainframe.widget;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.FRFont;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class MobileTabFontConfPane extends JPanel {
    private static final Icon[] ITALIC_ICONS = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic_white.png")};
    private static final Icon[] BOLD_ICONS = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold_white.png")};

    private EventListenerList fontChangeListener = new EventListenerList();
    private UIComboBox fontFamily;
    private UIComboBox fontSize;
    private UIToggleButton bold;
    private UIColorButton color;
    private UIToggleButton italic;


    public MobileTabFontConfPane() {
        super();
        init();
    }

    private void init() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fontFamily = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        Vector<Integer> integerList = new Vector<Integer>();
        for (int i = 1; i < 100; i++) {
            integerList.add(i);
        }
        fontFamily.setPreferredSize(new Dimension(152, 20));
        fontSize = new UIComboBox(integerList);
        color = new UIColorButton();
        bold = new UIToggleButton(BOLD_ICONS, true);
        italic = new UIToggleButton(ITALIC_ICONS, true);
        fontFamily.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireFontStateChanged();
            }
        });
        fontSize.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireFontStateChanged();
            }
        });
        bold.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireFontStateChanged();
            }
        });
        italic.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireFontStateChanged();
            }
        });
        color.addColorChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireFontStateChanged();
            }
        });
        this.add(fontFamily);
        this.add(fontSize);
        this.add(color);
        this.add(bold);
        this.add(italic);

    }

    public FRFont update() {
        String family = (String) fontFamily.getSelectedItem();
        int size = (int) fontSize.getSelectedItem();
        int style = Font.PLAIN;
        style += this.bold.isSelected() ? Font.BOLD : Font.PLAIN;
        style += this.italic.isSelected() ? Font.ITALIC : Font.PLAIN;
        FRFont frFont = FRFont.getInstance(family, style, size, color.getColor());
        return frFont;
    }

    public void populate(FRFont frFont) {
        fontFamily.setSelectedItem(frFont.getFamily());
        fontSize.setSelectedItem(frFont.getSize());
        color.setColor(frFont.getForeground());
        bold.setSelected(frFont.isBold());
        italic.setSelected(frFont.isItalic());

    }


    /**
     * 添加监听
     *
     * @param changeListener 监听列表
     */
    public void addFontChangeListener(ChangeListener changeListener) {
        fontChangeListener.add(ChangeListener.class, changeListener);
    }

    /**
     * 移除监听
     * Removes an old ColorChangeListener.
     *
     * @param changeListener 监听列表
     */
    public void removeFontChangeListener(ChangeListener changeListener) {
        fontChangeListener.remove(ChangeListener.class, changeListener);
    }

    /**
     * 颜色状态改变
     */
    public void fireFontStateChanged() {
        Object[] listeners = fontChangeListener.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
}
