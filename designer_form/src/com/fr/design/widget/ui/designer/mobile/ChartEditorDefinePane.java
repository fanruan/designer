package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.ChartMobileFitAttrState;
import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2018/1/18.
 */
public class ChartEditorDefinePane extends MobileWidgetDefinePane{
    private static final Item[] ITEMS = {
            new Item(ChartMobileFitAttrState.AUTO.description(), ChartMobileFitAttrState.AUTO),
            new Item(ChartMobileFitAttrState.AREA.description(), ChartMobileFitAttrState.AREA),
            new Item(ChartMobileFitAttrState.GEOMETRIC.description(), ChartMobileFitAttrState.GEOMETRIC)
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer; // 当前设计器
//    private UIComboBox hComboBox; // 横屏下拉框
    private UIComboBox vComboBox;// 竖屏下拉框
//    private UICheckBox heightRestrictCheckBox; // 手机显示限制高度复选框
    private UILabel maxHeightLabel;
    private UISpinner maxHeightSpinner; // 最大高度Spinner
    private AttributeChangeListener changeListener;

    public ChartEditorDefinePane (XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    protected void initContentPane() {}

    @Override
    protected JPanel createContentPane() {
        return null;
    }

    @Override
    public String getIconPath() {
        return "";
    }

    @Override
    public String title4PopupWindow() {
        return "ChartEditor";
    }


    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
//        this.hComboBox = new UIComboBox(ITEMS);
        this.vComboBox = new UIComboBox(ITEMS);
//        this.heightRestrictCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Mobile-Height-Limit"));
        this.maxHeightLabel = new UILabel(Inter.getLocText("FR-Designer_Mobile-Height-Percent"), SwingConstants.LEFT);
        this.maxHeightSpinner = new UISpinner(0, 1, 0.01, 0.75);
        maxHeightSpinner.setVisible(false);
        maxHeightLabel.setVisible(false);

        Component[][] components = new Component[][]{
                new Component[] {new UILabel("放大逻辑", SwingConstants.LEFT), new UILabel("等比例自适应")},
                new Component[] {new UILabel("缩小逻辑", SwingConstants.LEFT), vComboBox},
                new Component[] {new UILabel("提示：系统根据模版自动匹配缩小逻辑"), null},
                new Component[] {maxHeightLabel, maxHeightSpinner}
        };
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        UIExpandablePane folderPane = new UIExpandablePane("图表自适应", 280, 20, panelWrapper);
        this.add(folderPane, BorderLayout.NORTH);
        this.bingListeners2Widgets();
        this.setGlobalNames();
        this.repaint();
    }

    private void bingListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
    }

    /**
     * 后台初始化所有事件.
     */
    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
//        this.designer = designer;
//        this.addAttributeChangeListener(changeListener);
//        ChartEditor elementCaseEditor = (ChartEditor)xCreator.toData();
//        this.hComboBox.setSelectedItem(new Item (elementCaseEditor.getHorziontalAttr().description(), elementCaseEditor.getHorziontalAttr()));
//        this.vComboBox.setSelectedItem(new Item (elementCaseEditor.getVerticalAttr().description(), elementCaseEditor.getVerticalAttr()));
//        this.heightRestrictCheckBox.setSelected(elementCaseEditor.isHeightRestrict());
//        this.maxHeightLabel.setVisible();
//        this.maxHeightSpinner.setVisible(elementCaseEditor.isHeightRestrict());
//        this.maxHeightSpinner.setValue(elementCaseEditor.getHeightPercent());
    }

    @Override
    public void update() {
//        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
//        String globalName = this.getGlobalName();
//        switch (globalName) {
//            case "hComboBox":
////                ((ChartEditor)xCreator.toData()).setHorziontalAttr(((MobileFitAttrState)((Item)hComboBox.getSelectedItem()).getValue()));
//                break;
//            case "vComboBox":
////                ((ChartEditor)xCreator.toData()).setVerticalAttr(((MobileFitAttrState)((Item)vComboBox.getSelectedItem()).getValue()));
//                break;
//            case "heightRestrictCheckBox":
//                boolean isHeightRestrict = heightRestrictCheckBox.isSelected();
////                ((ChartEditor)xCreator.toData()).setHeightRestrict(isHeightRestrict);
//                maxHeightSpinner.setVisible(isHeightRestrict);
//                maxHeightLabel.setVisible(isHeightRestrict);
//                break;
//            case "maxHeightSpinner":
////                ((ChartEditor)xCreator.toData()).setHeightPercent(maxHeightSpinner.getValue());
//                break;
//        }
    }

    private void setGlobalNames() {
//        this.hComboBox.setGlobalName("hComboBox");
//        this.vComboBox.setGlobalName("vComboBox");
//        this.heightRestrictCheckBox.setGlobalName("heightRestrictCheckBox");
//        this.maxHeightSpinner.setGlobalName("maxHeightSpinner");
    }

}
