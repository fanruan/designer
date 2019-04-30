!(function () {

    /**
     * 服务器 填报预览设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-form-page"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.grid",
                    height: 78,
                    columns: 2,
                    rows: 2,
                    items: [{
                        column: 0,
                        row: 0,
                        el: {
                            type: "bi.report.sheet_label_location",
                            height: 24
                        }
                    }, {
                        column: 1,
                        row: 0,
                        el: {
                            type: "bi.report.server.param_setting.report_show_location",
                            height: 24
                        }
                    }, {
                        column: 0,
                        row: 1,
                        el: {
                            type: "bi.report.form_background_setting",
                            height: 24
                        }
                    }, {
                        column: 1,
                        row: 1,
                        el: {
                            type: "bi.report.leave_setting",
                            height: 24
                        }
                    }]
                }, {
                    type: "bi.report.server.param_setting.use_tool_bar",
                    height: 24,
                    value: true
                }, {
                    type: "bi.report.server.param_setting.tool_bar_height_select",
                    height: 24
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.server.param_setting.form_page", Analysis);
})();