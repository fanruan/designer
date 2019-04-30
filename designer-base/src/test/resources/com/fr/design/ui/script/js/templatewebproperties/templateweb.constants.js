!(function () {
    BI.constant("bi.constant.report.template.web_setting", [
        {
            text: BI.i18nText("Fine-Design_Report_Basic"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.BASE
        }, {
            text: BI.i18nText("Fine-Design_Report_Printer(Server)"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.PRINT
        }, {
            text: BI.i18nText("Fine-Design_Report_WEB_Pagination_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_WEB_Write_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.FORM_PAGE_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_Data_Analysis_Settings"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.DATA_ANALYSIS_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_Browser_Background"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.BROWSER
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_Css"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_CSS
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_JS
        }]
    );

    BI.constant("bi.constant.report.template.web_setting.tools", [
        {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_First"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.FIRST_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Previous"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.PRE_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Next"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.NEXT_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Last"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.LAST_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Print"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.PRINT
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Export"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.EXPORT
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Email"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.EMAIL
        }]
    );
})();