package com.fr.design.hyperlink;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.js.JavaScript;
import com.fr.js.LinkAnimateType;

import javax.swing.JPanel;
import java.util.HashMap;

/**
 * Created by mengao on 2017/10/12.
 */
public abstract class AbstractHyperLinkPane<T> extends FurtherBasicBeanPane<T> {
    private HashMap hyperLinkEditorMap;
    private boolean needRenamePane = false;
    protected ReportletParameterViewPane parameterViewPane;
    private UIButtonGroup<LinkAnimateType> animateTypeUIButtonGroup;


    public AbstractHyperLinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        this.hyperLinkEditorMap = hyperLinkEditorMap;
        this.needRenamePane = needRenamePane;
    }

    public AbstractHyperLinkPane() {
    }

    public ReportletParameterViewPane getParameterViewPane() {
        return parameterViewPane;
    }

    public void setParameterViewPane(ReportletParameterViewPane parameterViewPane) {
        this.parameterViewPane = parameterViewPane;
    }

    public boolean accept(Object ob) {
        return ob instanceof JavaScript;
    }

    public void reset() {
    }

    protected boolean needAnimatePane() {
        return false;
    }

    protected JPanel createAnimateTypeUIButtonGroup() {
        animateTypeUIButtonGroup = new UIButtonGroup<LinkAnimateType>(
                new String[]{LinkAnimateType.RELOAD.toLocaleString(), LinkAnimateType.INCREMENT.toLocaleString()},
                new LinkAnimateType[]{LinkAnimateType.RELOAD, LinkAnimateType.INCREMENT});

        animateTypeUIButtonGroup.setSelectedIndex(0);
        JPanel jp = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();

        jp.add(new UILabel(Toolkit.i18nText("Fine-Design_Chart_Link_Animate_Type")));
        jp.add(animateTypeUIButtonGroup);

        return jp;
    }

    protected void populateAnimateType(LinkAnimateType animateType) {
        if (animateTypeUIButtonGroup != null && animateType != LinkAnimateType.NONE) {
            animateTypeUIButtonGroup.setSelectedItem(animateType);
        }
    }

    protected LinkAnimateType updateAnimateType() {
        if (animateTypeUIButtonGroup != null) {
            return animateTypeUIButtonGroup.getSelectedItem();
        }
        return LinkAnimateType.NONE;
    }

    protected int getChartParaType() {
        return hyperLinkEditorMap != null ? ParameterTableModel.CHART_NORMAL_USE : ParameterTableModel.NO_CHART_USE;
    }

    protected ValueEditorPane getValueEditorPane() {
        return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType(), hyperLinkEditorMap);
    }

    protected boolean needRenamePane() {
        return needRenamePane;
    }

}
