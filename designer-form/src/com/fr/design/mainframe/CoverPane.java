package com.fr.design.mainframe;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-24
 * Time: 上午9:09
 */
public class CoverPane extends JPanel {

    private UIButton editButton;
    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 60 / 100.0F);

    public CoverPane() {
        setLayout(getCoverLayout());
        setBackground(null);
        setOpaque(false);

        editButton = new UIButton(Inter.getLocText("Edit"), IOUtils.readIcon(IconPathConstants.TD_EDIT_ICON_PATH)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 24);
            }
        };
        editButton.setBorderPainted(false);
        editButton.setExtraPainted(false);
        editButton.setBackground(new Color(176, 196, 222));
        add(editButton);
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public void setComposite(AlphaComposite composite) {
        this.composite = composite;
    }

    public UIButton getEditButton() {
        return editButton;
    }

    public void setEditButton(UIButton editButton) {
        this.editButton = editButton;
    }

    protected LayoutManager getCoverLayout() {
        return new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return parent.getPreferredSize();
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return null;
            }

            @Override
            public void layoutContainer(Container parent) {
                int width = parent.getParent().getWidth();
                int height = parent.getParent().getHeight();
                int preferWidth = editButton.getPreferredSize().width;
                int preferHeight = editButton.getPreferredSize().height;
                editButton.setBounds((width - preferWidth) / 2, (height - preferHeight) / 2, preferWidth, preferHeight);
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }
        };
    }


    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(composite);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(oldComposite);
        super.paint(g);
    }
}