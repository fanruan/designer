package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WGridLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.stable.Constants;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WLayoutSelectionPane extends BasicPane {

    private static Image hoverImage = BaseUtils.readImage("com/fr/design/images/form/hover.png");
    private WLayout wLayout = new WBorderLayout();
    private KindPane currentKindPane;

    public WLayoutSelectionPane() {
        this.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Please_Select_A_Kind_Of_Form_Container") + ":"));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        Component[][] coms = {
            {createTypeLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Border_Layout")), new
            	BorderLayoutPane()},
            {createTypeLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Grid_Layout")), new GridLayoutPane()},
            {createTypeLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Card_Layout")), new CardLayoutPane()},
            {createTypeLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Null_Layout")), new AbsoluteLayoutPane()}};
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        JPanel centerPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize, columnSize);
        JScrollPane scrollPane = new JScrollPane(centerPane);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public WLayout update() {
        return this.wLayout;
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Form_Widget_Container");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 400);
    }
    
    private class BorderLayoutPane extends BasicPane {

        public BorderLayoutPane() {
            this.setLayout(new /* */ GridLayout(1, 4, 5, 5));
            KindPane b1 = new KindPane("/com/fr/web/images/form/layout_border_nc.png", new WBorderLayout(0, 0, new String[]{WBorderLayout.NORTH, WBorderLayout.CENTER}));
            b1.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WLayout_Border_Layout_Container"));
            KindPane b2 = new KindPane("/com/fr/web/images/form/layout_border_ncw.png", new WBorderLayout(0, 0, new String[]{WBorderLayout.WEST, WBorderLayout.NORTH, WBorderLayout.CENTER}));
            b2.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WLayout_Border_Three_Container"));
            KindPane b4 = new KindPane("/com/fr/web/images/form/layout_border_all.png", new WBorderLayout(0, 0));
            b4.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WLayout_Border_ToolTips"));
            this.add(b1);
            this.add(b2);
            this.add(b4);
            this.add(new UILabel());
        }
        
        @Override
        protected String title4PopupWindow() {
        	return "BorderLayout";
        }
    }

    private class GridLayoutPane extends BasicPane {

        public GridLayoutPane() {
            this.setLayout(new/**/ GridLayout(1, 4, 5, 5));
            KindPane b1 = new KindPane("/com/fr/web/images/form/layout_grid_2x2.png", new WGridLayout(2, 2, 0, 0));
            b1.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_2x2"));
            KindPane b2 = new KindPane("/com/fr/web/images/form/layout_grid_2x3.png", new WGridLayout(2, 3, 0, 0));
            b2.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_2x3"));
            KindPane b3 = new KindPane("/com/fr/web/images/form/layout_grid_3x2.png", new WGridLayout(3, 2, 0, 0));
            b3.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_3x2"));
            KindPane b4 = new KindPane("/com/fr/web/images/form/layout_grid_3x3.png", new WGridLayout(3, 3, 0, 0));
            b4.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_3x3"));
            this.add(b1);
            this.add(b2);
            this.add(b3);
            this.add(b4);
        }
        
        @Override
        protected String title4PopupWindow() {
        	return "Fine-Design_Form_Grid_Layout";
        }
    }

    private class CardLayoutPane extends BasicPane {

        public CardLayoutPane() {
            this.setLayout(new /* */ GridLayout(1, 4, 5, 5));
            KindPane b1 = new KindPane("/com/fr/web/images/form/layout_absolute_big.png", new WCardLayout());
            b1.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WLayout_Card_ToolTip"));
            this.add(b1);
            this.add(new UILabel());
            this.add(new UILabel());
            this.add(new UILabel());
        }
        
        @Override
        protected String title4PopupWindow() {
        	return "CardLayout";
        }
    }

    private class AbsoluteLayoutPane extends BasicPane {

        public AbsoluteLayoutPane() {
            this.setLayout(new /* */ GridLayout(1, 4, 5, 5));
            KindPane b1 = new KindPane("/com/fr/web/images/form/layout_absolute_big.png", new WAbsoluteLayout());
            this.add(b1);
            this.add(new UILabel());
            this.add(new UILabel());
            this.add(new UILabel());
        }
        
        @Override
        protected String title4PopupWindow() {
        	return "AbsoluteLayout";
        }
    }

    private UILabel createTypeLabel(String text) {
        UILabel label = new UILabel(" " + text + ":");
        Font font = new Font("SimSun", Font.BOLD, 13);
        label.setFont(font);
        return label;
    }

    private class KindPane extends JPanel implements MouseListener {

        private boolean isMouseOver;
        private boolean isMousePressed;
        private WLayout layout;

        public KindPane(String iconPath, WLayout layout) {
            this.layout = layout;
            this.setLayout(new 
            		BorderLayout(20, 20));
            this.add(new UILabel(BaseUtils.readIcon(iconPath)));
            this.addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isMouseOver || isMousePressed) {
                Dimension d = this.getSize();
                GraphHelper.paintImage(g, d.width, d.height, hoverImage, Constants.IMAGE_CENTER, Constants.LEFT, Constants.CENTER, 105, 89);
            }
        }

        @Override
        public JToolTip createToolTip() {
            MultiLineToolTip tip = new MultiLineToolTip();
            tip.setComponent(this);
            tip.setOpaque(false);
            return tip;
        }

        @Override
        public Insets getInsets() {
            return new Insets(10, 10, 10, 10);
        }

        @Override
        public void mouseClicked(MouseEvent me) {
        	// 消除其他的选中状态
            if (currentKindPane != null) {
                currentKindPane.isMousePressed = false;
                currentKindPane.repaint();
            }
            currentKindPane = this;
            currentKindPane.isMousePressed = true;
            wLayout = this.layout;
            // richer:双击效果
            if (me.getClickCount() >= 2) {
                Component c = KindPane.this.getParent();
                while (c != null && !(c instanceof BasicDialog)) {
                    c = c.getParent();
                }
                if (c != null) {
                    ((BasicDialog) c).doOK();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            isMouseOver = true;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent me) {
            isMouseOver = false;
            repaint();
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel p = (JPanel) f.getContentPane();
        p.setLayout(FRGUIPaneFactory.createBorderLayout());
        WLayoutSelectionPane s = new WLayoutSelectionPane();
        p.add(s, BorderLayout.CENTER);
        f.setSize(300, 200);
        f.setVisible(true);
    }
}
