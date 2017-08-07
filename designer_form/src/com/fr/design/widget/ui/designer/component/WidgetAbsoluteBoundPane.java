package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/3.
 */
public class WidgetAbsoluteBoundPane extends WidgetBoundPane {
    protected XWAbsoluteLayout parent;
    private UISpinner x;
    private UISpinner y;

    public WidgetAbsoluteBoundPane(XCreator source){
        super(source);
        XLayoutContainer xLayoutContainer = getParent(source);
        this.parent = (XWAbsoluteLayout) xLayoutContainer;

    }

    public void initBoundPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        x = new UISpinner(0, 1200, 1);
        y = new UISpinner(0, 1200, 1);

        width = new UISpinner(0, 1200, 1);
        height = new UISpinner(0, 1200, 1);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Widget_Position")), x, y},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer_X_Coordinate"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Y_Coordinate"), SwingConstants.CENTER)},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget_Size")), width, height},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer-Tree_Width"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer-Tree_Height"), SwingConstants.CENTER)},
        };
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 8, 5);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("Form-Component_Bounds"), 280, 20, panel);
        this.add(uiExpandablePane);
    }


    public void update() {
        super.update();
        Rectangle bounds = new Rectangle(creator.getBounds());
        bounds.x = (int) x.getValue();
        bounds.y = (int) y.getValue();
        if (parent == null) {
            return;
        }
        WAbsoluteLayout wabs = parent.toData();
        wabs.setBounds(creator.toData(), bounds);
        creator.setBounds(bounds);
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate() {
        super.populate();
        Rectangle bounds = new Rectangle(creator.getBounds());
        x.setValue(bounds.x);
        y.setValue(bounds.y);
    }
}
