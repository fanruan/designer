package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;


/**
 * Created by ibm on 2017/8/3.
 */
public abstract class WTitleLayoutDefinePane<T extends AbstractBorderStyleWidget>  extends AbstractDataModify<T> {
    private AccessibleWLayoutBorderStyleEditor borderStyleEditor;

    public WTitleLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        borderStyleEditor = new AccessibleWLayoutBorderStyleEditor();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Style")), borderStyleEditor}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        advancePane.add(panel, BorderLayout.NORTH);
        JPanel centerPane = createCenterPane();
        if(centerPane!=null){
            advancePane.add(centerPane, BorderLayout.CENTER);
        }
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);

        this.add(advanceExpandablePane);

    }

    protected JPanel createCenterPane(){
        return null;
    }


    @Override
    public String title4PopupWindow() {
        return "titleLayout";
    }

    @Override
    public void populateBean(T ob) {
        populateSubBean(ob);
        borderStyleEditor.setValue(ob.getBorderStyle());
    }


    @Override
    public T updateBean() {
        T e = updateSubBean();
        if(!ComparatorUtils.equals(borderStyleEditor.getValue(), e.getBorderStyle())){
            e.setBorderStyle((LayoutBorderStyle) borderStyleEditor.getValue());
        }
        return e;
    }

    protected abstract T updateSubBean();

    protected abstract void populateSubBean(T ob);

}
