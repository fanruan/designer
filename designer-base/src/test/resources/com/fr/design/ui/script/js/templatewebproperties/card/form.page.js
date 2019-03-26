!(function () {
    /**
     * 填报页面设置
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
                    type: "bi.report.global_or_single_combo",
                    height: 24
                }, {
                    type: "bi.grid",
                    height: 78,
                    columns: 2,
                    rows: 2,
                    items: [{
                        column: 0,
                        row: 0,
                        el: {
                            type: "bi.report.sheet_label_location"
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
                            type: "bi.report.form_background_setting"
                        }
                    }, {
                        column: 1,
                        row: 1,
                        el: {
                            type: "bi.report.leave_setting"
                        }
                    }]
                }, {
                    type: "bi.settings.tool_bar",
                    height: 180
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
    BI.shortcut("bi.report.template.web_setting.form_page", Analysis);
})();