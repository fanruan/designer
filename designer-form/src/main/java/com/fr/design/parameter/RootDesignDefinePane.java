package com.fr.design.parameter;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.PropertyGroupPane;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.PropertyTab;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.fun.ParameterExpandablePaneUIProvider;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.widget.accessibles.AccessibleBackgroundEditor;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.UIBoundSpinner;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Background;

import com.fr.report.stable.FormConstants;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Set;

/**
 * Created by ibm on 2017/8/2.
 */
public class RootDesignDefinePane extends AbstractDataModify<WParameterLayout> {
    private XWParameterLayout root;
    private UISpinner designerWidth;
    private UICheckBox displayReport;
    private UICheckBox useParamsTemplate;
    private AccessibleBackgroundEditor background;
    private UIButtonGroup hAlignmentPane;
    private UITextField labelNameTextField;
    /**
     * 插件带来的额外属性
     */
    private PropertyGroupPane extraPropertyGroupPane;

    public RootDesignDefinePane(XCreator xCreator) {
        super(xCreator);
        this.root = (XWParameterLayout) xCreator;
        initComponent();
    }


    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        designerWidth = new UIBoundSpinner(1, Integer.MAX_VALUE, 1);
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        JPanel layoutPane = createBoundsPane();
        UIExpandablePane layoutExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Size"), 280, 20, layoutPane);
        this.add(layoutExpandablePane, BorderLayout.CENTER);
        this.addExtraUIExpandablePaneFromPlugin();

    }

    private void addExtraUIExpandablePaneFromPlugin() {
        Set<ParameterExpandablePaneUIProvider> pluginCreators = ExtraDesignClassManager.getInstance().getArray(ParameterExpandablePaneUIProvider.XML_TAG);
        JPanel panel = FRGUIPaneFactory.createYBoxEmptyBorderPane();
        for (ParameterExpandablePaneUIProvider provider : pluginCreators) {
            UIExpandablePane uiExpandablePane = provider.createUIExpandablePane();
            PropertyTab propertyTab = provider.addToWhichPropertyTab();
            if (uiExpandablePane != null && propertyTab == PropertyTab.ATTR) {
                panel.add(uiExpandablePane);
            }
        }
        this.add(panel, BorderLayout.SOUTH);
    }

    public JPanel createBoundsPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Desin_Width")), designerWidth},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel.add(panel);
        return jPanel;
    }

    public JPanel createAdvancePane() {
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        labelNameTextField = new UITextField();
        displayReport = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Display_Nothing_Before_Query"));
        UIComponentUtils.setLineWrap(displayReport);
        useParamsTemplate = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Use_Params_Template"));
        background = new AccessibleBackgroundEditor();
        Icon[] hAlignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png"),};
        Integer[] hAlignment = new Integer[]{FormConstants.LEFTPOSITION, FormConstants.CENTERPOSITION, FormConstants.RIGHTPOSITION};
        hAlignmentPane = new UIButtonGroup<Integer>(hAlignmentIconArray, hAlignment);
        hAlignmentPane.setAllToolTips(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Left")
                , com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Center"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_StyleAlignment_Right")});
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Label_Name")), labelNameTextField},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Background")), background},
                new Component[]{displayReport, null},
                new Component[]{useParamsTemplate, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Display_Position")), hAlignmentPane}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L1, 0));
        CRPropertyDescriptor[] extraTableEditor = new CRPropertyDescriptor[0];
        extraTableEditor = root.getExtraTableEditor();
        extraPropertyGroupPane = new PropertyGroupPane(extraTableEditor, root);

        jPanel.add(panel, BorderLayout.NORTH);
        jPanel.add(extraPropertyGroupPane, BorderLayout.CENTER);
        return jPanel;
    }

    @Override
    public String title4PopupWindow() {
        return "parameter";
    }

    @Override
    public void populateBean(WParameterLayout ob) {
        labelNameTextField.setText(ob.getLabelName());
        background.setValue(ob.getBackground());
        displayReport.setSelected(ob.isDelayDisplayContent());
        useParamsTemplate.setSelected(ob.isUseParamsTemplate());
        designerWidth.setValue(ob.getDesignWidth());
        hAlignmentPane.setSelectedItem(ob.getPosition());

        if (extraPropertyGroupPane != null) {
            extraPropertyGroupPane.populate(ob);
        }
    }


    @Override
    public WParameterLayout updateBean() {
        WParameterLayout wParameterLayout = (WParameterLayout) creator.toData();
        wParameterLayout.setLabelName(labelNameTextField.getText());
        if (isCompsOutOfDesignerWidth((int) designerWidth.getValue())) {
            designerWidth.setValue(wParameterLayout.getDesignWidth());
        } else {
            wParameterLayout.setDesignWidth((int) designerWidth.getValue());
        }
        wParameterLayout.setDelayDisplayContent(displayReport.isSelected());
        wParameterLayout.setUseParamsTemplate(useParamsTemplate.isSelected());
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        jTemplate.needAddTemplateIdAttr(useParamsTemplate.isSelected());
        wParameterLayout.setBackground((Background) background.getValue());
        wParameterLayout.setPosition((int) hAlignmentPane.getSelectedItem());
        return wParameterLayout;
    }

    private boolean isCompsOutOfDesignerWidth(int designerWidth){
        for(int i=0; i<root.getComponentCount(); i++){
            Component comp = root.getComponent(i);
            if(comp.getX() + comp.getWidth() > designerWidth){
                return true;
            }
        }
        return false;
    }
    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}
