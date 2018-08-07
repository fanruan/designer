package com.fr.design.mainframe;

import com.fr.base.GraphHelper;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.general.IOUtils;

import com.fr.stable.Constants;

import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;


/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-24
 * Time: 上午9:09
 */
public class CoverPane extends JPanel {

    private UIButton editButton;
    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    private static final int BORDER_WIDTH = 2;
    private static final Color COVER_COLOR = new Color(216, 242, 253);
    private static final int EDIT_BTN_WIDTH = 75;
    private static final int EDIT_BTN_HEIGHT = 20;

    public CoverPane() {
        setLayout(getCoverLayout());
        setBackground(null);
        setOpaque(false);

        editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"), IOUtils.readIcon(IconPathConstants.EDIT_ICON_PATH)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 24);
            }
        };
        editButton.setBorderPainted(false);
        editButton.setExtraPainted(false);
        editButton.setForeground(Color.WHITE);
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
        int x = 0;
        int y = 0;
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(composite);
        g2d.setColor(COVER_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        g2d.setColor(XCreatorConstants.EDIT_COLOR);
        boolean editHover = formDesigner.getCursor().getType() != Cursor.DEFAULT_CURSOR;
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, editHover ? 0.9f : 0.7f);
        g2d.setComposite(alphaComposite);
        g2d.fillRoundRect((x + w / 2 - EDIT_BTN_WIDTH / 2), (y + h / 2 - EDIT_BTN_HEIGHT / 2), EDIT_BTN_WIDTH, EDIT_BTN_HEIGHT, 4, 4);
        g2d.setComposite(oldComposite);
        g.setColor(XCreatorConstants.FORM_BORDER_COLOR);
        GraphHelper.draw(g, getPaintBorderBounds(), Constants.LINE_MEDIUM);


        super.paint(g);
    }

    protected Rectangle getPaintBorderBounds(){
        return new Rectangle(BORDER_WIDTH, BORDER_WIDTH, getWidth() - BORDER_WIDTH * 2 , getHeight() - BORDER_WIDTH * 2);
    }
}
