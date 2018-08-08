package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.UIDialog;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.IframeEditor;

import com.fr.stable.ParameterProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class IframeEditorDefinePane extends AbstractDataModify<IframeEditor> {

    private static final int P_W = 610;
    private static final int P_H = 580;
    private UITextField srcTextField;
    private ReportletParameterViewPane parameterViewPane;
    private UICheckBox horizontalCheck;
    private UICheckBox verticalCheck;
    private UIButton parameterViewPaneButton;
    private List<ParameterProvider> list;


    public IframeEditorDefinePane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        JPanel attr = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
        attr.add(horizontalCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Preference-Horizontal_Scroll_Bar_Visible")));
        attr.add(verticalCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Preference-Vertical_Scroll_Bar_Visible")));
        contentPane.add(attr);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f};

        parameterViewPaneButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Edit"));
        parameterViewPaneButton.addActionListener(parameterListener);
        parameterViewPane = new ReportletParameterViewPane();
        horizontalCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        verticalCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        java.awt.Component[][] coms = {
                {horizontalCheck, null},
                {verticalCheck, null},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Form-Url")), srcTextField = new UITextField()},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameters")), parameterViewPaneButton}};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(coms, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);


        contentPane.add(panel);

        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 24, contentPane);
        this.add(uiExpandablePane, BorderLayout.NORTH);

    }

    ActionListener parameterListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<ParameterProvider> paraList = parameterViewPane.update();
            list = new ArrayList<ParameterProvider>();
            ParameterProvider pr = null;
            for (ParameterProvider parameterProvider : paraList) {
                try {
                    pr = (ParameterProvider) parameterProvider.clone();
                } catch (CloneNotSupportedException e1) {
                    FRContext.getLogger().error(e1.getMessage(), e1);
                }
                list.add(pr);
            }

            UIDialog dialog = parameterViewPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionListener() {
                @Override
                public void doOk() {
                }

                @Override
                public void doCancel() {
                    parameterViewPane.update(list);
                }
            });
            dialog.setSize(P_W, P_H);
            dialog.setVisible(true);
        }
    };

    @Override
    protected String title4PopupWindow() {
        return "iframe";
    }

    @Override
    public void populateBean(IframeEditor e) {
        srcTextField.setText(e.getSrc());
        parameterViewPane.populate(e.getParameters());
        this.horizontalCheck.setSelected(e.isOverflowx());
        this.verticalCheck.setSelected(e.isOverflowy());
    }

    @Override
    public IframeEditor updateBean() {
        IframeEditor ob = new IframeEditor();
        ob.setSrc(srcTextField.getText());
        List<ParameterProvider> parameterList = parameterViewPane.update();
        ob.setParameters(parameterList.toArray(new ParameterProvider[parameterList.size()]));
        ob.setOverflowx(this.horizontalCheck.isSelected());
        ob.setOverflowy(this.verticalCheck.isSelected());
        return ob;
    }
}
