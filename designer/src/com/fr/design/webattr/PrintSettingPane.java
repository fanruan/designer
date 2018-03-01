package com.fr.design.webattr;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.web.attr.ReportWebAttr;

/**
 * Created by plough on 2018/3/1.
 */
public class PrintSettingPane extends BasicPane {

    public PrintSettingPane() {
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("ReportServerP-Import_JavaScript");
    }

    public void populate(ReportWebAttr reportWebAttr) {

    }

    public void update(ReportWebAttr reportWebAttr) {

    }
}
