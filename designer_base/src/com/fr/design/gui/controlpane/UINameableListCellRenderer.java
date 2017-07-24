package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.Nameable;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Nameable的ListCellRenerer
 * Created by plough on 2017/7/21.
 */

public class UINameableListCellRenderer extends
        JPanel implements ListCellRenderer {
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private static final int BUTTON_WIDTH = 20;
    private UILabel editButton;  // "编辑按钮"，实际上是一个 UILabel，由列表项（UIListControlPane）统一处理点击事件
    private UILabel label;
    private UIListControlPane listControlPane;

    public UINameableListCellRenderer(UIListControlPane listControlPane) {
        super();
        this.listControlPane = listControlPane;
        initComponents();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
    }

    private void initComponents() {
        editButton = new UILabel() {
            public Dimension getPreferredSize() {
                return new Dimension(BUTTON_WIDTH, BUTTON_WIDTH);
            }
        };
//        editButton.set4LargeToolbarButton();
        editButton.setIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
//        editButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                popupEditPane();
//            }
//        });
        label = new UILabel();
//        label.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(editButton, BorderLayout.WEST);
        this.add(label, BorderLayout.CENTER);
    }

    private void popupEditPane() {
        GUICoreUtils.showPopupMenu(listControlPane.popupEditPane, editButton,
                - listControlPane.popupEditPane.getPreferredSize().width, 0);
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) return border;
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null &&
                    (noFocusBorder == null ||
                            noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }
            return noFocusBorder;
        }
    }

    private void setText(String t) {
        label.setText(t);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

//        if (value instanceof Icon) {
//            setIcon((Icon)value);
//            setText("");
//        }
//        else {
//            setIcon(null);
            setText((value == null) ? "" : value.toString());
//        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        setBorder(border);

        if (value instanceof ListModelElement) {
            Nameable wrappee = ((ListModelElement) value).wrapper;
            this.setText(((ListModelElement) value).wrapper.getName());

            boolean iconSet = false;
            for (NameableCreator creator : listControlPane.creators()) {
                if (creator.menuIcon() != null && creator.acceptObject2Populate(wrappee) != null) {
//                    this.setIcon(creator.menuIcon());
                    this.setToolTipText(creator.createTooltip());
                    iconSet = true;
                    break;
                }
            }
//            if (!iconSet) {
//                this.setIcon(BaseUtils.readIcon("/com/fr/base/images/oem/cpt.png"));
//            }
        }

        return this;
    }
}