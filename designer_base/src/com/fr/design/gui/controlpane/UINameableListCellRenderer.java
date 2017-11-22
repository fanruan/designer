package com.fr.design.gui.controlpane;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
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
    private static final Color BORDER_COLOR = new Color(201, 198, 184);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private static final int BUTTON_WIDTH = 25;
    private UILabel editButton;  // "编辑按钮"，实际上是一个 UILabel，由列表项（UIListControlPane）统一处理点击事件
    private UILabel label;
    private UIListControlPane listControlPane;
    private Color initialLabelForeground;

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
        editButton.setIcon(listControlPane.isNewStyle() ? UIConstants.LIST_EDIT_ICON : UIConstants.CPT_ICON);
        editButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIConstants.LIST_ITEM_SPLIT_LINE));
        editButton.setHorizontalAlignment(SwingConstants.CENTER);
        label = new UILabel();
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        initialLabelForeground = label.getForeground();
        this.setLayout(new BorderLayout());
        this.add(editButton, BorderLayout.WEST);
        this.add(label, BorderLayout.CENTER);
    }

    private Border getNoFocusBorder() {
        return BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LIST_ITEM_SPLIT_LINE);
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
            label.setForeground(Color.WHITE);
            if (listControlPane.isNewStyle()) {
                editButton.setIcon(UIConstants.LIST_EDIT_WHITE_ICON);
            }
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            label.setForeground(initialLabelForeground);
            if (listControlPane.isNewStyle()) {
                editButton.setIcon(UIConstants.LIST_EDIT_ICON);
            }
        }

        setText((value == null) ? "" : value.toString());

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        if (value instanceof ListModelElement) {
            Nameable wrappee = ((ListModelElement) value).wrapper;
            this.setText(((ListModelElement) value).wrapper.getName());

            for (NameableCreator creator : listControlPane.creators()) {
                if (creator.menuIcon() != null && creator.acceptObject2Populate(wrappee) != null) {
                    this.setToolTipText(creator.createTooltip());
                    break;
                }
            }
        }

        return this;
    }
}