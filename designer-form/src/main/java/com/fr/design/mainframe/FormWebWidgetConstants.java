package com.fr.design.mainframe;

import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.web.button.ExcelO;
import com.fr.form.web.button.ExcelP;
import com.fr.form.web.button.Export;
import com.fr.form.web.button.page.*;
import com.fr.general.IOUtils;


/**
 * Created by harry on 2017-3-3.
 */
public class FormWebWidgetConstants {

    private FormWebWidgetConstants() {
    }

    public static WidgetOption[] getFormElementCaseToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, EXPORT, EXCELP, EXCELO, CUSTOM_BUTTON};
    }

    // 当前页/总页数
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Page_Navi_Text"),
            IOUtils.readIcon("/com/fr/web/images/pageNumber.png"), PageNavi.class);
    // 首页
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_First"), IOUtils.readIcon("/com/fr/web/images/first.png"),
            First.class);
    // 末页
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Last"), IOUtils.readIcon("/com/fr/web/images/last.png"),
            Last.class);
    // 前一页
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Previous"),
            IOUtils.readIcon("/com/fr/web/images/previous.png"), Previous.class);
    // 后一页
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Next"), IOUtils.readIcon("/com/fr/web/images/next.png"),
            Next.class);

    // 导出成Excel 分页导出
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Excel_Page"),
            IOUtils.readIcon("/com/fr/web/images/excel.png"), ExcelP.class);
    // 导出成Excel 原样导出
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Excel_Simple"),
            IOUtils.readIcon("/com/fr/web/images/excel.png"), ExcelO.class);
    // 导出
    public static final WidgetOption EXPORT = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Export"), IOUtils.readIcon("/com/fr/web/images/export.png"), Export.class);

    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Toolbar_Custom_Button"), CustomToolBarButton.class);

}
