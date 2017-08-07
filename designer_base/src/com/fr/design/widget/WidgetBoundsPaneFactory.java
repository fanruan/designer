package com.fr.design.widget;

import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2017/8/7.
 */
public class WidgetBoundsPaneFactory {

    public static UIExpandablePane createBoundsPane(UISpinner width, UISpinner height) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget_Size")), width, height},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer-Tree_Width"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer-Tree_Height"), SwingConstants.CENTER)},
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 8, 5);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        return new UIExpandablePane(Inter.getLocText("FR-Designer_Coords_And_Size"), 280, 24, panel);
    }

    public static UIExpandablePane createAbsoluteBoundsPane(UISpinner x, UISpinner y, UISpinner width, UISpinner height) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;

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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        return new UIExpandablePane(Inter.getLocText("FR-Designer_Coords_And_Size"), 230, 24, panel);
    }
}
