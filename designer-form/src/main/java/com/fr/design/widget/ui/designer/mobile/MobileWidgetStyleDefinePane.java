package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.accessibles.AccessibleMobileStyleEditor;
import com.fr.design.mainframe.mobile.ui.MobileStylePane;
import com.fr.form.ui.mobile.MobileStyle;

import javax.swing.*;
import java.awt.*;

public class MobileWidgetStyleDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private AccessibleMobileStyleEditor mobileStyleEditor;
    private AttributeChangeListener changeListener;

    public MobileWidgetStyleDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template"));
        mobileStyleEditor = new AccessibleMobileStyleEditor(new MobileStylePane(this.xCreator.toData()));
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{label, mobileStyleEditor}},
                TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_LARGE
        );
        JPanel holder = FRGUIPaneFactory.createBorderLayout_S_Pane();
        holder.add(jPanel, BorderLayout.NORTH);

        this.add(holder, BorderLayout.NORTH);
    }

    @Override
    public void populate(FormDesigner designer) {
        mobileStyleEditor.setValue(xCreator.toData().getMobileStyle());
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
    }

    @Override
    public void update() {
        xCreator.toData().setMobileStyle((MobileStyle) mobileStyleEditor.getValue());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 30);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(400, 30);
    }

    private void bindListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
    }

    private void reInitAllListeners() {
        initListener(this);
    }
}
