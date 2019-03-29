!(function () {

    /**
     * 报表服务器参数面板
     */
    var Tab = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-report-server-param-setting"
        },

        render: function () {
            return {
                type: "bi.setting.tab",
                value: BICst.REPORT_SERVER_PARAM.SPLIT_PAGE_PREVIEW_SETTING,
                tabItems: BI.Constants.getConstant("bi.constant.report.server.param_setting"),
                cardCreator: BI.bind(this._createCard, this)
            };
        },

        _createCard: function (v) {
            switch (v) {
                case BICst.REPORT_SERVER_PARAM.SPLIT_PAGE_PREVIEW_SETTING:
                    return {
                        type: "bi.report.server.param_setting.page_preview"
                    };
                case BICst.REPORT_SERVER_PARAM.FORM_PAGE_SETTING:
                    return {
                        type: "bi.report.server.param_setting.form_page"
                    };
                case BICst.REPORT_SERVER_PARAM.DATA_ANALYSIS_SETTING:
                    return {
                        type: "bi.report.server.param_setting.data_analysis"
                    }
                case BICst.REPORT_SERVER_PARAM.IMPORT_CSS:
                case BICst.REPORT_SERVER_PARAM.IMPORT_JS:
                case BICst.REPORT_SERVER_PARAM.ERROR_TEMPLATE_DEFINE:
                case BICst.REPORT_SERVER_PARAM.PRINT_SETTING:
                default:
                    return {
                        type: "bi.label",
                        text: "1"
                    };
            }
        }

    });

    BI.shortcut("bi.report.server.param_setting", Tab);
})();