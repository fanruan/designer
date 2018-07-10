package com.fr.design.gui.frpane;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.stable.ArrayUtils;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Locale;

/**
 * Created by zack on 2015/3/3.
 */
public class UIBasicOptionPaneUI extends BasicOptionPaneUI {
    private static final int NUM_8 = 8;
    private static final int NUM_4 = 4;

    /**
     * 创建组件UI
     * @param x 组件
     * @return 组件
     */
    public static ComponentUI createUI(JComponent x) {
        return new UIBasicOptionPaneUI();
    }
    @Override
    protected Object[] getButtons() {
        if (optionPane != null) {
            int minimumWidth =
                    DefaultLookup.getInt(optionPane, this,
                            "OptionPane.buttonMinimumWidth", -1);
            Object[] suppliedOptions = optionPane.getOptions();
            if (suppliedOptions == null) {
                Object[] defaultOptions;
                int type = optionPane.getOptionType();
                Locale l = optionPane.getLocale();
                if (type == JOptionPane.YES_NO_OPTION) {
                    defaultOptions = new ButtonFactory[2];
                    defaultOptions[0] = new ButtonFactory(
                            UIManager.getString("OptionPane.yesButtonText", l),
                            getMnemonic("OptionPane.yesButtonMnemonic", l),
                            (Icon)DefaultLookup.get(optionPane, this,
                                    "OptionPane.yesIcon"), minimumWidth);
                    defaultOptions[1] = new ButtonFactory(UIManager.getString("OptionPane.noButtonText", l),
                            getMnemonic("OptionPane.noButtonMnemonic", l),
                            (Icon)DefaultLookup.get(optionPane, this,
                                    "OptionPane.noIcon"), minimumWidth);
                } else if (type == JOptionPane.YES_NO_CANCEL_OPTION) {
                    defaultOptions = getYNCTypeOptions(minimumWidth, l);
                } else if (type == JOptionPane.OK_CANCEL_OPTION) {
                    defaultOptions = new ButtonFactory[2];
                    defaultOptions[0] = new ButtonFactory(UIManager.getString("OptionPane.okButtonText",l),
                            getMnemonic("OptionPane.okButtonMnemonic", l),
                            (Icon)DefaultLookup.get(optionPane, this,
                                    "OptionPane.okIcon"), minimumWidth);
                    defaultOptions[1] = new ButtonFactory(
                            UIManager.getString("OptionPane.cancelButtonText",l),
                            getMnemonic("OptionPane.cancelButtonMnemonic", l),
                            (Icon)DefaultLookup.get(optionPane, this,
                                    "OptionPane.cancelIcon"), minimumWidth);
                } else {
                    defaultOptions = new ButtonFactory[1];
                    defaultOptions[0] = new ButtonFactory(UIManager.getString("OptionPane.okButtonText",l),
                            getMnemonic("OptionPane.okButtonMnemonic", l),
                            (Icon)DefaultLookup.get(optionPane, this,
                                    "OptionPane.okIcon"), minimumWidth);
                }
                return defaultOptions;
            }
            Object[] suppliedFlatOptions =new ButtonFactory[suppliedOptions.length];
            for (int i = 0; i < suppliedOptions.length; i++) {
                suppliedFlatOptions[i]=new ButtonFactory(suppliedOptions[i].toString(), 0, null, minimumWidth);
            }
            return suppliedFlatOptions;
        }
        return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
    private int getMnemonic(String key, Locale l) {
        String value = (String)UIManager.get(key, l);

        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException nfe) { }
        return 0;
    }
    private  Object[] getYNCTypeOptions(int minimumWidth, Locale l){
        Object[] defaultOptions = new ButtonFactory[3];
        defaultOptions[0] = new ButtonFactory(
                UIManager.getString("OptionPane.yesButtonText", l),
                getMnemonic("OptionPane.yesButtonMnemonic", l),
                (Icon)DefaultLookup.get(optionPane, this,
                        "OptionPane.yesIcon"), minimumWidth);
        defaultOptions[1] = new ButtonFactory(
                UIManager.getString("OptionPane.noButtonText",l),
                getMnemonic("OptionPane.noButtonMnemonic", l),
                (Icon)DefaultLookup.get(optionPane, this,
                        "OptionPane.noIcon"), minimumWidth);
        defaultOptions[2] = new ButtonFactory(
                UIManager.getString("OptionPane.cancelButtonText",l),
                getMnemonic("OptionPane.cancelButtonMnemonic", l),
                (Icon)DefaultLookup.get(optionPane, this,
                        "OptionPane.cancelIcon"), minimumWidth);
        return defaultOptions;
    }
    protected void addButtonComponents(Container container, Object[] buttons,
                                       int initialIndex) {
        if (ArrayUtils.isNotEmpty(buttons)) {
            boolean            sizeButtonsToSame = getSizeButtonsToSameWidth();
            boolean            createdAll = true;
            int                numButtons = buttons.length;
            JButton[]          createdButtons = null;
            int                maxWidth = 0;

            if (sizeButtonsToSame) {
                createdButtons = new JButton[numButtons];
            }

            for(int counter = 0; counter < numButtons; counter++) {
                Object       button = buttons[counter];
                Component    newComponent;

                if (button instanceof Component) {
                    createdAll = false;
                    newComponent = (Component)button;
                    container.add(newComponent);
                    hasCustomComponents = true;

                } else {
                    JButton      aButton;

                    if (button instanceof ButtonFactory) {
                        aButton = ((ButtonFactory)button).createButton();
                    }
                    else if (button instanceof Icon)
                        aButton = new JButton((Icon)button);
                    else
                        aButton = new JButton(button.toString());

                    aButton.setName("OptionPane.button");
                    aButton.setMultiClickThreshhold(DefaultLookup.getInt(
                            optionPane, this, "OptionPane.buttonClickThreshhold",
                            0));
                    configureButton(aButton);

                    container.add(aButton);

                    ActionListener buttonListener = createButtonActionListener(counter);
                    if (buttonListener != null) {
                        aButton.addActionListener(buttonListener);
                    }
                    newComponent = aButton;
                }
                if (sizeButtonsToSame && createdAll) {
                    if ((newComponent instanceof JButton)) {
                        createdButtons[counter] = (JButton)newComponent;
                        maxWidth = Math.max(maxWidth,
                                newComponent.getMinimumSize().width);
                    }
                }
                if (counter == initialIndex) {
                    initialFocusComponent = newComponent;
                    if (initialFocusComponent instanceof JButton) {
                        JButton defaultB = (JButton)initialFocusComponent;
                        defaultB.addHierarchyListener(new HierarchyListener() {
                            public void hierarchyChanged(HierarchyEvent e) {
                                if ((e.getChangeFlags() &
                                        HierarchyEvent.PARENT_CHANGED) != 0) {
                                    JButton defaultButton = (JButton) e.getComponent();
                                    JRootPane root =
                                            SwingUtilities.getRootPane(defaultButton);
                                    if (root != null) {
                                        root.setDefaultButton(defaultButton);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            ((ButtonAreaLayout)container.getLayout()).
                    setSyncAllWidths((sizeButtonsToSame && createdAll));
	    /* Set the padding, windows seems to use 8 if <= 2 components,
	       otherwise 4 is used. It may actually just be the size of the
	       buttons is always the same, not sure. */
            if (DefaultLookup.getBoolean(optionPane, this,
                    "OptionPane.setButtonMargin", true)) {
                if (sizeButtonsToSame && createdAll) {
                    JButton  aButton;
                    int  padSize;
                    padSize = (numButtons <= 2 ? NUM_8 : NUM_4);
                    for(int counter = 0; counter < numButtons; counter++) {
                        aButton = createdButtons[counter];
                        aButton.setMargin(new Insets(2, padSize, 2, padSize));
                    }
                }
            }
        }
    }
    private void configureButton(JButton button) {
        Font buttonFont = (Font)DefaultLookup.get(optionPane, this,
                "OptionPane.buttonFont");
        if (buttonFont != null) {
            button.setFont(buttonFont);
        }
    }
    private static class ButtonFactory {
        private String text;
        private int mnemonic;
        private Icon icon;
        private int minimumWidth = -1;

        ButtonFactory(String text, int mnemonic, Icon icon, int minimumWidth) {
            this.text = text;
            this.mnemonic = mnemonic;
            this.icon = icon;
            this.minimumWidth = minimumWidth;
        }

        JButton createButton() {
            JButton button = null;

            if (minimumWidth > 0) {
                button = new ConstrainedButton(text, minimumWidth);
            } else {
                button = new UIButton(text);
            }
            if (icon != null) {
                button.setIcon(icon);
            }
            if (mnemonic != 0) {
                button.setMnemonic(mnemonic);
            }
            return button;
        }

        private static class ConstrainedButton extends JButton {
            int minimumWidth;

            ConstrainedButton(String text, int minimumWidth) {
                super(text);
                this.minimumWidth = minimumWidth;
            }

            public Dimension getMinimumSize() {
                Dimension min = super.getMinimumSize();
                min.width = Math.max(min.width, minimumWidth);
                return min;
            }

            public Dimension getPreferredSize() {
                Dimension pref = super.getPreferredSize();
                pref.width = Math.max(pref.width, minimumWidth);
                return pref;
            }
        }
    }
}