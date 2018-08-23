package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.form.ui.CustomToolBarButton;

import com.fr.report.web.button.AppletPrint;
import com.fr.report.web.button.Email;
import com.fr.report.web.button.ExcelO;
import com.fr.report.web.button.ExcelP;
import com.fr.report.web.button.ExcelS;
import com.fr.report.web.button.Export;
import com.fr.report.web.button.FlashPrint;
import com.fr.report.web.button.NewPrint;
import com.fr.report.web.button.PDF;
import com.fr.report.web.button.PDFPrint;
import com.fr.report.web.button.PageSetup;
import com.fr.report.web.button.Print;
import com.fr.report.web.button.PrintPreview;
import com.fr.report.web.button.Scale;
import com.fr.report.web.button.Word;
import com.fr.report.web.button.page.First;
import com.fr.report.web.button.page.Last;
import com.fr.report.web.button.page.Next;
import com.fr.report.web.button.page.PageNavi;
import com.fr.report.web.button.page.Previous;
import com.fr.report.web.button.page.SetPrinterOffset;
import com.fr.report.web.button.write.AppendColumnRow;
import com.fr.report.web.button.write.ClearStashedButton;
import com.fr.report.web.button.write.CustomizeImportExcelData;
import com.fr.report.web.button.write.DeleteColumnRow;
import com.fr.report.web.button.write.ExcelImport;
import com.fr.report.web.button.write.ImExcelAppend;
import com.fr.report.web.button.write.ImExcelClean;
import com.fr.report.web.button.write.ImExcelCover;
import com.fr.report.web.button.write.ImportExcelData;
import com.fr.report.web.button.write.ShowCellValue;
import com.fr.report.web.button.write.StashButton;
import com.fr.report.web.button.write.Submit;
import com.fr.report.web.button.write.SubmitForcibly;
import com.fr.report.web.button.write.Verify;
import com.fr.report.web.button.write.WriteOfflineHTML;

public class ReportWebWidgetConstants {
    private ReportWebWidgetConstants() {
    }

    public static WidgetOption[] getPageToolBarInstance() {
        return new WidgetOption[]{FIRST, PREVIOUS, PAGENAVI, NEXT, LAST, SCALE, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, NEW_PRINT, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT,
                SETPRINTEROFFSET, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getViewToolBarInstance() {
        return new WidgetOption[]{PAGESETUP, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, NEW_PRINT, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, SETPRINTEROFFSET, PRINTPREVIEW, CUSTOM_BUTTON};
    }

    public static WidgetOption[] getWriteToolBarInstance() {
        return new WidgetOption[]{SUBMIT, VERIFY, EMAIL, EXPORT, PDF, EXCELP, EXCELO, EXCELS, WORD, NEW_PRINT, PRINT, FLASHPRINT, APPLETPRINT, PDFPRINT, IMPORTEXCELDATA, SHOWCELLVALUE,
                APPENDCOLUMNROW, DELETECOLUMNROW, SETPRINTEROFFSET, WRITEOFFLINEHTML, CUSTOM_BUTTON, WRITESTASH, WRITESTASHCLEAR, IMPORTEXCELDATA_CUSTOMIZED,
                IMPORTEXCEL, IMPORTEXCEL_COVER, IMPORTEXCEL_CLEAN, IMPORTEXCEL_APPEND};
    }

    // 提交按钮
    public static final WidgetOption SUBMIT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Utils_Submit"), BaseUtils.readIcon("/com/fr/web/images/save.png"),
            Submit.class);

    // flash打印按钮
    public static final WidgetOption FLASHPRINT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Utils_Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/flashPrint.png"), FlashPrint.class);

    // appletprint
    public static final WidgetOption APPLETPRINT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Applet_Print"),
            BaseUtils.readIcon("/com/fr/web/images/appletPrint.png"), AppletPrint.class);

    // PDF导出
    public static final WidgetOption PDF = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_PDF"), BaseUtils.readIcon("/com/fr/web/images/pdf.png"),
            PDF.class);

    // 客户端PDF打印
    public static final WidgetOption PDFPRINT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Utils_Print[Client]"),
            BaseUtils.readIcon("/com/fr/web/images/pdfPrint.png"), PDFPrint.class);


    // 邮件发送
    public static final WidgetOption EMAIL = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Email"), BaseUtils.readIcon("/com/fr/web/images/email.png"), Email.class);
    public static final WidgetOption PRINTPREVIEW = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_PrintP_Print_Preview"),
            BaseUtils.readIcon("/com/fr/web/images/preview.png"), PrintPreview.class);


    // 导出成Excel 分页导出
    public static final WidgetOption EXCELP = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Excel_Page"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelP.class);
    // 导出成Excel 原样导出
    public static final WidgetOption EXCELO = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Excel_Simple"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelO.class);
    // 导出成Excel 分页分Sheet导出
    public static final WidgetOption EXCELS = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Excel_Page_To_Sheet"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelS.class);

    // 导出成Word
    public static final WidgetOption WORD = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Word"), BaseUtils.readIcon("/com/fr/web/images/word.png"), Word.class);
    // 页面设置
    public static final WidgetOption PAGESETUP = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Engine_Report_PageSetup_Page_Setup"), BaseUtils.readIcon("/com/fr/web/images/pageSetup.png"), PageSetup.class);
    // 导出
    public static final WidgetOption EXPORT = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Export"), BaseUtils.readIcon("/com/fr/web/images/export.png"), Export.class);

    // 当前页/总页数
    public static final WidgetOption PAGENAVI = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Page_Navi_Text"),
            BaseUtils.readIcon("/com/fr/web/images/pageNumber.png"), PageNavi.class);
    // 首页
    public static final WidgetOption FIRST = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_First"), BaseUtils.readIcon("/com/fr/web/images/first.png"),
            First.class);
    // 末页
    public static final WidgetOption LAST = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Last"), BaseUtils.readIcon("/com/fr/web/images/last.png"),
            Last.class);
    // 前一页
    public static final WidgetOption PREVIOUS = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Previous"),
            BaseUtils.readIcon("/com/fr/web/images/previous.png"), Previous.class);
    // 后一页
    public static final WidgetOption NEXT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ReportServerP_Next"), BaseUtils.readIcon("/com/fr/web/images/next.png"),
            Next.class);
    public static final WidgetOption SCALE = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Enlarge_Or_Reduce"), BaseUtils.readIcon("/com/fr/web/images/scale.png"),
            Scale.class);

    public static final WidgetOption NEW_PRINT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Print"), BaseUtils.readIcon("/com/fr/web/images/print.png"), NewPrint.class);
    public static final WidgetOption PRINT = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Print_Compatible"), BaseUtils.readIcon("/com/fr/web/images/print.png"), Print.class);
    public static final WidgetOption APPENDCOLUMNROW = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Insert_Record"),
            BaseUtils.readIcon("/com/fr/web/images/appendRow.png"), AppendColumnRow.class);
    public static final WidgetOption DELETECOLUMNROW = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Delete_Record"),
            BaseUtils.readIcon("/com/fr/web/images/deleteRow.png"), DeleteColumnRow.class);
    public static final WidgetOption VERIFY = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Verify_Data_Verify"), BaseUtils.readIcon("/com/fr/web/images/verify.gif"),
            Verify.class);
    public static final WidgetOption SUBMITFORCIBLY = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Submit_Forcibly"),
            BaseUtils.readIcon("/com/fr/web/images/save2.png"), SubmitForcibly.class);

    // show cell value
    public static final WidgetOption SHOWCELLVALUE = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Show_Cell_Value"),
            BaseUtils.readIcon("/com/fr/web/images/showValue.png"), ShowCellValue.class);

    // import excel data
    public static final WidgetOption IMPORTEXCELDATA = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Import_Excel_Data"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImportExcelData.class);
    //自定义导入excel
    public static final WidgetOption IMPORTEXCELDATA_CUSTOMIZED = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Utils_Import_Excel_Data_Customized"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), CustomizeImportExcelData.class);
    // 打印机偏移设置
    public static final WidgetOption SETPRINTEROFFSET = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_SetPrinterOffset"), BaseUtils.readIcon("/com/fr/web/images/pianyi.png"), SetPrinterOffset.class);

    public static final WidgetOption CUSTOM_BUTTON = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Custom_Form_Button"), CustomToolBarButton.class);

    // 输出离线html报表
    public static final WidgetOption WRITEOFFLINEHTML = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Export-Offline-Html"), BaseUtils.readIcon("/com/fr/web/images/writeOffline.png"), WriteOfflineHTML.class);
    // 数据暂存
    public static final WidgetOption WRITESTASH = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine-Write_Stash"), BaseUtils.readIcon("/com/fr/web/images/edit/stash.png"), StashButton.class);
    // 数据清空
    public static final WidgetOption WRITESTASHCLEAR = WidgetOptionFactory
            .createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine-Write_Clear"), BaseUtils.readIcon("/com/fr/web/images/edit/clearstash.png"), ClearStashedButton.class);
    //Excel导入
    public static final WidgetOption IMPORTEXCEL= WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Excel_Import_Repeat"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ExcelImport.class);
    //Excel导入_覆盖
    public static final WidgetOption IMPORTEXCEL_COVER= WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Excel_Import_Cover"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImExcelCover.class);
    //Excel导入_清空
    public static final WidgetOption IMPORTEXCEL_CLEAN= WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Excel_Import_Clean"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImExcelClean.class);
    //Excel导入_增量
    public static final WidgetOption IMPORTEXCEL_APPEND= WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Excel_Import_Append"),
            BaseUtils.readIcon("/com/fr/web/images/excel.png"), ImExcelAppend.class);
}
