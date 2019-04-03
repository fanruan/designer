!(function () {
    /**
     * 服务器 分页预览设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-page-preview"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.report.server.param_setting.report_show_location",
                    height: 24
                }, {
                    type: "bi.button_group",
                    height: 92,
                    chooseType: BI.Selection.Multi,
                    items: BI.createItems([{
                        el: {
                            text: BI.i18nText("Fine-Design_Report_Is_Paint_Page"),
                            value: 1
                        },
                        bgap: 10
                    }, {
                        el: {
                            text: BI.i18nText("Fine-Design_Report_IS_Auto_Scale"),
                            value: 2
                        },
                        bgap: 10
                    }, {
                        text: BI.i18nText("Fine-Design_Report_IS_TD_HEAVY_EXPORT"),
                        value: 3
                    }], {
                        type: "bi.multi_select_item",
                        hgap: 5,
                        logic: {
                            dynamic: true
                        },
                        iconWrapperWidth: 16
                    }),
                    layouts: [{
                        type: "bi.vertical"
                    }]
                }, {
                    type: "bi.report.server.param_setting.use_tool_bar",
                    height: 24
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
    BI.shortcut("bi.report.server.param_setting.page_preview", Analysis);
})();