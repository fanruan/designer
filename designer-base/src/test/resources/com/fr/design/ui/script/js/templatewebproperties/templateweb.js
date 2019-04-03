!(function () {

    /**
     * 模板Web属性面板
     */
    var Tab = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-report-template-web-setting"
        },

        render: function () {
            return {
                type: "bi.setting.tab",
                value: BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING,
                tabItems: BI.Constants.getConstant("bi.constant.report.template.web_setting"),
                cardCreator: BI.bind(this._createCard, this)
            };
        },

        _createCard: function (v) {
            switch (v) {
                case BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING:
                    return {
                        type: "bi.report.template.web_setting.page_preview"
                    }
                case BICst.REPORT_TEMPLATE_WEB_SETTING.FORM_PAGE_SETTING:
                    return {
                        type: "bi.report.template.web_setting.form_page"
                    }
                case BICst.REPORT_TEMPLATE_WEB_SETTING.DATA_ANALYSIS_SETTING:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.BASE:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.PRINT:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.BROWSER:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_CSS:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_JS:
                default:
                    return {
                        type: "bi.label",
                        text: "1"
                    };
            }
        }

    });

    BI.shortcut("bi.report.template.web_setting", Tab);
})();