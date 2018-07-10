package com.fr.design.mainframe;

import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.web.button.ExcelO;
import com.fr.form.web.button.ExcelP;
import com.fr.form.web.button.Export;
import com.fr.form.web.button.page.*;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

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
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"HJS-Current_Page", "HF-Number_of_Page"}, new String[]{"/", ""}),
            IOUtils.readIcon("/com/fr/web/images/pageNumber.png"), PageNavi.class);
    // 首页
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-First"), IOUtils.readIcon("/com/fr/web/images/first.png"),
            First.class);
    // 末页
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Last"), IOUtils.readIcon("/com/fr/web/images/last.png"),
            Last.class);
    // 前一页
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Previous"),
            IOUtils.readIcon("/com/fr/web/images/previous.png"), Previous.class);
    // 后一页
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Next"), IOUtils.readIcon("/com/fr/web/images/next.png"),
            Next.class);

    // 导出成Excel 分页导出
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "FR-Designer_Export_Excel_Page"}, new String[]{"(", ")"}),
            IOUtils.readIcon("/com/fr/web/images/excel.png"), ExcelP.class);
    // 导出成Excel 原样导出
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "FR-Designer_Export_Excel_Simple"}, new String[]{"(", ")"}),
            IOUtils.readIcon("/com/fr/web/images/excel.png"), ExcelO.class);
    // 导出
    public static final WidgetOption EXPORT = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("FR-Engine_Export"), IOUtils.readIcon("/com/fr/web/images/export.png"), Export.class);

    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText(new String[]{"Custom", "Form-Button"}), CustomToolBarButton.class);

}
