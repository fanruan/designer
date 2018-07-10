package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;

/**
 * Created by kerry on 2017/8/30.
 */
public class DefaultWidgetDefinePane extends AbstractDataModify<Widget> {

    public DefaultWidgetDefinePane(XCreator xCreator){
        super(xCreator);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    @Override
    public String title4PopupWindow() {
        return "default";
    }

    @Override
    public void populateBean(Widget w) {
    }

    @Override
    public Widget updateBean() {
        return creator.toData();
    }
}
