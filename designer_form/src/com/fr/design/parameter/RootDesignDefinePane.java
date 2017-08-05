package com.fr.design.parameter;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.items.FRLayoutTypeItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.designer.properties.items.ItemProvider;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/2.
 */
public class RootDesignDefinePane extends AbstractDataModify<WParameterLayout> {
    private XWParameterLayout root;
    private UISpinner designerWidth;
    private UICheckBox displayReport;

    public RootDesignDefinePane(XCreator xCreator) {
        super(xCreator);
        this.root = (XWParameterLayout) xCreator;
        initComponent();
    }


    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        designerWidth = new UISpinner(1, 1000, 1);
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        JPanel layoutPane = createBoundsPane();
//        layoutPane.setLayout(FRGUIPaneFactory.createBorderLayout());
//        layoutPane.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel("设计宽度"), designerWidth}, FlowLayout.LEFT, 4));
        UIExpandablePane layoutExpandablePane = new UIExpandablePane(Inter.getLocText("Size"), 280, 20, layoutPane);
        this.add(layoutExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createBoundsPane(){
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Form-Desin_Width")), designerWidth},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));
        return panel;
    }

    public JPanel createAdvancePane(){
        displayReport = new UICheckBox(Inter.getLocText("FR-Designer_DisplayNothingBeforeQuery"));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background")), new UITextField()},
                new Component[]{displayReport, null },
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WidgetDisplyPosition")),  new UITextField()}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));
        return panel;
    }

    @Override
    public String title4PopupWindow() {
        return "wfitlayout";
    }

    @Override
    public void populateBean(WParameterLayout ob) {

    }


    @Override
    public WParameterLayout updateBean() {
        WParameterLayout wParameterLayout = (WParameterLayout) creator.toData();
        wParameterLayout.setDesignWidth((int) designerWidth.getValue());
        wParameterLayout.setDelayDisplayContent(displayReport.isSelected());
        return wParameterLayout;
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}