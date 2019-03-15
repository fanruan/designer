package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.iofile.attr.FormBodyPaddingAttrMark;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentAdvancePane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentLayoutIntervalPane;
import com.fr.form.ui.RichStyleWidgetProvider;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/2/1.
 */
public class BodyFitMobileDefinePane extends BodyMobileDefinePane {
    private MobileComponentAdvancePane advancePane;
    private MobileComponentLayoutIntervalPane intervalPane;

    public BodyFitMobileDefinePane(XCreator xCreator) {
        super(xCreator);
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        setDesigner(WidgetPropertyPane.getInstance().getEditingFormDesigner());
        this.add(createNorthPane(), BorderLayout.NORTH);
        this.add(getMobileWidgetListPane(), BorderLayout.CENTER);
        this.repaint();
    }

    private JPanel createNorthPane() {
        JPanel holder = FRGUIPaneFactory.createBorderLayout_S_Pane();

        advancePane = new MobileComponentAdvancePane(FormBodyPaddingAttrMark.XML_TAG);
        intervalPane = new MobileComponentLayoutIntervalPane(FormBodyPaddingAttrMark.XML_TAG);

        holder.add(getMobilePropertyPane(), BorderLayout.NORTH);
        //高级
        holder.add(advancePane, BorderLayout.CENTER);
        //布局
        holder.add(intervalPane, BorderLayout.SOUTH);
        return holder;
    }

    @Override
    public void populate(FormDesigner designer) {
        super.populate(designer);
        advancePane.populate((RichStyleWidgetProvider) getBodyCreator().toData());
        intervalPane.populate((RichStyleWidgetProvider) getBodyCreator().toData());
    }

    @Override
    public void update() {
        super.update();
        advancePane.update((RichStyleWidgetProvider) getBodyCreator().toData());
        intervalPane.update((RichStyleWidgetProvider) getBodyCreator().toData());
    }
}
