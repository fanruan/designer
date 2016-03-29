package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.ui.CustomToolBarButton;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.report.web.button.*;
import com.fr.report.web.button.page.*;
import com.fr.report.web.button.write.*;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

public class ReportWebWidgetConstants {
    private ReportWebWidgetConstants() {
    }

    public static WidgetOption[] getPageToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT,
                SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getViewToolBarInstance() {
        return new WidgetOption[]{PAGESETUP, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, PRINTPREVIEW, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getPreviewToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT,
                SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getWriteToolBarInstance() {
        return new WidgetOption[]{SUBMIT, VERIFY, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, IMPORTEXCELDATA, SHOWCELLVALUE,
                APPENDCOLUMNROW, DELETECOLUMNROW, SETPRINTEROFFSET, WRITEOFFLINEHTML, CUSTOM_BUTTON, WRITESTASH, WRITESTASHCLEAR};
    }

    public static WidgetOption[] getFormToolBarInstance() {
        return new WidgetOption[]{EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    // 查询
    public static final WidgetOption SEARCH = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Query", "Form-Button"}),
            BaseUtils.readIcon("/com/fr/web/images/form/resources/preview_16.png"), StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class));

    // 提交按钮
    public static final WidgetOption SUBMIT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Submit"), BaseUtils.readIcon("/com/fr/web/images/save.png"),
            Submit.class);

    // flash打印按钮
    public static final WidgetOption FLASHPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/flashPrint.png"), FlashPrint.class);

    // appletprint
    public static final WidgetOption APPLETPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Applet_Print"),
            BaseUtils.readIcon("/com/fr/web/images/appletPrint.png"), AppletPrint.class);

    // PDF导出
    public static final WidgetOption PDF = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-PDF"), BaseUtils.readIcon("/com/fr/web/images/pdf.png"),
            PDF.class);

    // PDF导出，解决linux下无中文字体的导出字体混乱
    public static final WidgetOption PDF2 = WidgetOptionFactory.createByWidgetClass(
            Inter.getLocText(new String[]{"ReportServerP-PDF", "ReportServerP-PDF2-INFO"}, new String[]{"(", ")"}), BaseUtils.readIcon("/com/fr/web/images/pdf.png"), PDF2.class);

    // 客户端PDF打印
    public static final WidgetOption PDFPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Utils-Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/pdfPrint.png"), PDFPrint.class);

    // 服务器端打印
    public static final WidgetOption SERVERPRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Print[Server]"),
            BaseUtils.readIcon("/com/fr/web/images/serverPrint.png"), ServerPrint.class);
    // 邮件发送
    public static final WidgetOption EMAIL = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Email"), BaseUtils.readIcon("/com/fr/web/images/email.png"), Email.class);
    public static final WidgetOption PRINTPREVIEW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PrintP-Print_Preview"),
            BaseUtils.readIcon("/com/fr/web/images/preview.png"), PrintPreview.class);

    public static final WidgetOption EDIT = WidgetOptionFactory.createByWidgetClass("Edit", Edit.class);

    // 导出成Excel 分页导出
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-Page"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelP.class);
    // 导出成Excel 原样导出
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-Simple"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelO.class);
    // 导出成Excel 分页分Sheet导出
    public static final WidgetOption EXCELS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"Excel", "Export-Excel-PageToSheet"}, new String[]{"(", ")"}),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelS.class);

    // 导出成Word
    public static final WidgetOption WORD = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Word"), BaseUtils.readIcon("/com/fr/web/images/word.png"), Word.class);
    // 页面设置
    public static final WidgetOption PAGESETUP = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("PageSetup-Page_Setup"), BaseUtils.readIcon("/com/fr/web/images/pageSetup.png"), PageSetup.class);
    // 导出
    public static final WidgetOption EXPORT = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("FR-Engine_Export"), BaseUtils.readIcon("/com/fr/web/images/export.png"), Export.class);

    // 当前页/总页数
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(Inter.getLocText(new String[]{"HJS-Current_Page", "HF-Number_of_Page"}, new String[]{"/", ""}),
            BaseUtils.readIcon("/com/fr/web/images/pageNumber.png"), PageNavi.class);
    // 首页
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-First"), BaseUtils.readIcon("/com/fr/web/images/first.png"),
            First.class);
    // 末页
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Last"), BaseUtils.readIcon("/com/fr/web/images/last.png"),
            Last.class);
    // 前一页
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Previous"),
            BaseUtils.readIcon("/com/fr/web/images/previous.png"), Previous.class);
    // 后一页
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_ReportServerP-Next"), BaseUtils.readIcon("/com/fr/web/images/next.png"),
            Next.class);
    public static final WidgetOption SCALE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Enlarge_Or_Reduce"), BaseUtils.readIcon("/com/fr/web/images/scale.png"),
            Scale.class);

    public static final WidgetOption PRINT = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_Print"), BaseUtils.readIcon("/com/fr/web/images/print.png"), Print.class);
    public static final WidgetOption APPENDCOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Insert_Record"),
            BaseUtils.readIcon("/com/fr/web/images/appendRow.png"), AppendColumnRow.class);
    public static final WidgetOption DELETECOLUMNROW = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Delete_Record"),
            BaseUtils.readIcon("/com/fr/web/images/deleteRow.png"), DeleteColumnRow.class);
    public static final WidgetOption VERIFY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Verify-Data_Verify"), BaseUtils.readIcon("/com/fr/web/images/verify.gif"),
            Verify.class);
    public static final WidgetOption SUBMITFORCIBLY = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Submit_Forcibly"),
            BaseUtils.readIcon("/com/fr/web/images/save2.png"), SubmitForcibly.class);

    // show cell value
    public static final WidgetOption SHOWCELLVALUE = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Show_Cell_Value"),
            BaseUtils.readIcon("/com/fr/web/images/showValue.png"), ShowCellValue.class);

    // import excel data
    public static final WidgetOption IMPORTEXCELDATA = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("Utils-Import_Excel_Data"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImportExcelData.class);

    // 打印机偏移设置
    public static final WidgetOption SETPRINTEROFFSET = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Engine_SetPrinterOffset"), BaseUtils.readIcon("/com/fr/web/images/pianyi.png"), SetPrinterOffset.class);

    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText(new String[]{"Custom", "Form-Button"}), CustomToolBarButton.class);

    // 输出离线html报表
    public static final WidgetOption WRITEOFFLINEHTML = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("FR-Engine_Export-Offline-Html"), BaseUtils.readIcon("/com/fr/web/images/writeOffline.png"), WriteOfflineHTML.class);
    // 数据暂存
    public static final WidgetOption WRITESTASH = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("FR-Engine-Write_Stash"), BaseUtils.readIcon("/com/fr/web/images/edit/stash.png"), StashButton.class);
    // 数据清空
    public static final WidgetOption WRITESTASHCLEAR = WidgetOptionFactory
            .createByWidgetClass(Inter.getLocText("FR-Engine-Write_Clear"), BaseUtils.readIcon("/com/fr/web/images/edit/clearstash.png"), ClearStashedButton.class);

}