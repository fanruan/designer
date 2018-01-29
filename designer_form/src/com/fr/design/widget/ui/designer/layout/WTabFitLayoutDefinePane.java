package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.design.widget.ui.designer.component.TabFitLayoutBackgroundPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by ibm on 2017/8/5.
 */
public class WTabFitLayoutDefinePane extends AbstractDataModify<WTabFitLayout> {
    private PaddingBoundPane paddingBoundPane;
    private TabFitLayoutBackgroundPane borderStyle;
    private UISpinner componentInterval;
    private UITextField titleField;

    public WTabFitLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        paddingBoundPane = new PaddingBoundPane();
        borderStyle = new TabFitLayoutBackgroundPane();
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{borderStyle, null}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        jPanel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));
        advancePane.add(jPanel, BorderLayout.NORTH);
        advancePane.add(paddingBoundPane, BorderLayout.CENTER);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        initLayoutComponent();
    }

    public void initLayoutComponent(){
        componentInterval = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        titleField = new UITextField();
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Component_Interval")), componentInterval},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Title")), titleField}
        };
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        jPanel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));
        borderPane.add(jPanel, BorderLayout.CENTER);
        UIExpandablePane currentEditTab = new UIExpandablePane(Inter.getLocText("FR-Designer_Current_tab"), 280, 20, borderPane);
        this.add(currentEditTab, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WTabFitLayout ob) {
        borderStyle.populate(ob.getCurrentCard());
        paddingBoundPane.populate(ob);
        componentInterval.setValue(ob.getCompInterval());
        if(ob.getCurrentCard() == null){
            ob.setCurrentCard(getRelateSwitchButton(ob));
        }
        titleField.setText(ob.getCurrentCard().getText());
    }

    private CardSwitchButton getRelateSwitchButton(WTabFitLayout layout){
        int index = layout.getIndex();

        XWCardLayout cardLayout = (XWCardLayout)creator.getBackupParent();
        XWCardMainBorderLayout border = (XWCardMainBorderLayout)cardLayout.getBackupParent();
        WCardMainBorderLayout borderLayout = border.toData();
        WCardTitleLayout titleLayout = borderLayout.getTitlePart();
        if(titleLayout == null){
            return null;
        }

        WCardTagLayout tagLayout = titleLayout.getTagPart();
        return tagLayout == null ? null : tagLayout.getSwitchButton(index);
    }


    private void setLayoutGap(int gap, WTabFitLayout layout, XWTabFitLayout xwTabFitLayout) {
        if(xwTabFitLayout.canAddInterval(gap)){
            int  interval = layout.getCompInterval();
            if (gap != interval) {
                xwTabFitLayout.moveContainerMargin();
                xwTabFitLayout.moveCompInterval(xwTabFitLayout.getAcualInterval());
                layout.setCompInterval(gap);
                xwTabFitLayout.addCompInterval(xwTabFitLayout.getAcualInterval());
            }
        }
    }

    @Override
    public WTabFitLayout updateBean() {
        WTabFitLayout layout = (WTabFitLayout) creator.toData();
        borderStyle.update(layout.getCurrentCard());
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Layout-Padding"))) {
            paddingBoundPane.update(layout);
        }
        int gap = (int)componentInterval.getValue();
        setLayoutGap(gap, layout, (XWTabFitLayout)creator);
        layout.getCurrentCard().setText(titleField.getText());
        return layout;
    }
}
