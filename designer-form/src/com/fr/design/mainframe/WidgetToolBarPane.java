package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.core.FormWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/*
 * author: august
 * */
public class WidgetToolBarPane extends BasicPane implements DesignToolbarProvider {

    private static final int WIDTH = 56;
    private static final int BUTTON_GAP = 4;
    private static final int BUTTON_HEIGHT = 20;
    private static final int WIDTH_GAP = 13;

    // componentsList用于布局的
    private ArrayList<JComponent> componentsList4Form = new ArrayList<JComponent>();
    private FormDesigner designer;

    private JPanel formWidgetPanel;
    private FormWidgetPopWindow chartWindow;

    private static WidgetToolBarPane singleton = new WidgetToolBarPane();

    public static WidgetToolBarPane getInstance() {
        return singleton;
    }

    public static WidgetToolBarPane getInstance(FormDesigner designer) {
        singleton.setTarget(designer);
        singleton.checkEnable();
        return singleton;
    }

    public void refreshToolbar() {
        reset();
    }

    public static void refresh() {
        singleton.reset();
    }

    private WidgetToolBarPane() {
        super();
        this.setLayout(new BorderLayout());
    }

    private void reset() {
        if (formWidgetPanel != null) {
            formWidgetPanel.removeAll();
        }
        if (designer == null || !this.isShowing()) {
            return;
        }

        if (!designer.getDesignerMode().isFormParameterEditor()) {
            initFormComponent();
            formWidgetPanel.doLayout();
        }
        this.doLayout();
        this.repaint();
    }

    private void setTarget(FormDesigner designer) {
        if (designer == null) {
            return;
        }
        FormDesigner oldDesigner = this.designer;
        this.designer = designer;
        if (isChangeToOrInitFormEditor(oldDesigner)) {
            initFormComponent();
        }
    }

    private boolean isChangeToOrInitFormEditor(FormDesigner oldDesigner) {
        return !designer.getDesignerMode().isFormParameterEditor()
                && (oldDesigner == null || oldDesigner.getDesignerMode().isFormParameterEditor());
    }

    private void initFormComponent() {
        if (formWidgetPanel == null) {
            initFormButtons();
            formWidgetPanel = new JPanel();
            formWidgetPanel.setLayout(new WidgetToolBarPaneLayout());
        }

        if (formWidgetPanel.getComponentCount() == 0) {
            for (JComponent comp : componentsList4Form) {
                formWidgetPanel.add(comp);
            }
        }

        this.removeAll();
        this.add(formWidgetPanel, BorderLayout.CENTER);
    }

    private void checkEnable() {
        for (JComponent comp : componentsList4Form) {
            comp.setEnabled(!BaseUtils.isAuthorityEditing());
        }
    }

    public static FormDesigner getTarget() {
        return singleton.designer;
    }

    private void initFormButtons() {
        if (componentsList4Form.isEmpty()) {
            // 表单布局
            this.componentsList4Form.add(new TitleLabel(Inter.getLocText("Form-Layout")));
            WidgetOption[] containerWidgetArray = FormWidgetOption.getFormContainerInstance();
            for (WidgetOption no : containerWidgetArray) {
                this.componentsList4Form.add(new ToolBarButton(no));
            }

            final WidgetOption[] wo = DesignModuleFactory.getExtraWidgetOptions();

            // 这个条件说明是否加载了图表模块
            if (wo != null && wo.length > 0) {
                this.componentsList4Form.add(new TitleLabel(Inter.getLocText("Chart")));
                UIButton chartButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/chart.png"));
                chartButton.setToolTipText(Inter.getLocText("Click-Me"));
                chartButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (BaseUtils.isAuthorityEditing()) {
                            return;
                        }
                        if (chartWindow == null) {
                            chartWindow = new FormWidgetPopWindow();
                        }
                        chartWindow.showToolTip(e.getXOnScreen() + BUTTON_HEIGHT, e.getYOnScreen() - BUTTON_HEIGHT, wo);
                    }
                });

                this.componentsList4Form.add(chartButton);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, ImageObserver.HEIGHT);
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    public class TitleLabel extends UILabel {

        public TitleLabel(String text) {
            super(text);
            this.setBackground(Color.gray);
            this.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public void paintComponent(Graphics g) {
            Dimension d = this.getPreferredSize();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(215, 215, 215));
            g2d.fillRect(0, 0, d.width, d.height);
            super.paintComponent(g);

            g2d.setColor(Color.black);
            Stroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 3.5f, new float[]{3, 1}, 0);
            g2d.setStroke(bs);
            g2d.drawLine(0, d.height, d.width, d.height);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(WidgetToolBarPane.WIDTH, BUTTON_HEIGHT);
        }

    }

    private class WidgetToolBarPaneLayout implements LayoutManager {
        private int max_Y = 0;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(WIDTH, max_Y);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(WIDTH, max_Y);
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();
            int width = parent.getWidth() - insets.left - insets.right - BUTTON_GAP * 2;
            int x = insets.left;
            int y = insets.top;
            boolean isFirstPlace = true;
            for (int i = 0; i < parent.getComponentCount(); i++) {
                Component lable = parent.getComponent(i);
                if (lable instanceof TitleLabel) {
                    if (isFirstPlace) {
                        lable.setBounds(x, y, width + BUTTON_GAP * 2, BUTTON_HEIGHT);
                        y = y + BUTTON_HEIGHT + BUTTON_GAP;
                    } else {
                        y = y + BUTTON_HEIGHT + BUTTON_GAP;
                        lable.setBounds(x, y, width + BUTTON_GAP * 2, BUTTON_HEIGHT);
                        isFirstPlace = true;
                        y = y + BUTTON_HEIGHT + BUTTON_GAP;
                    }
                } else if (lable instanceof ToolBarButton) {
                    if (isFirstPlace) {
                        lable.setBounds(x, y, width / 2, BUTTON_HEIGHT);
                        isFirstPlace = false;
                    } else {
                        lable.setBounds(x + width / 2, y, width / 2, BUTTON_HEIGHT);
                        y = y + BUTTON_HEIGHT + BUTTON_GAP;
                        isFirstPlace = true;
                    }
                } else {
                    lable.setBounds(x + WIDTH_GAP, y, width / 2, BUTTON_HEIGHT);
                    isFirstPlace = true;
                    y = y + BUTTON_HEIGHT + BUTTON_GAP;
                }
            }
            max_Y = y;
            if (!isFirstPlace) {
                max_Y += BUTTON_HEIGHT + BUTTON_GAP;
            }
        }
    }

}