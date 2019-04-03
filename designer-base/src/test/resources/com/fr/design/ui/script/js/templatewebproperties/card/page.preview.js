!(function () {
    /**
     * 分页预览设置
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
                    type: "bi.report.global_or_single_combo",
                    height: 24
                }, {
                    type: "bi.report.server.param_setting.report_show_location",
                    height: 24
                }, {
                    type: "bi.vertical_adapt",
                    height: 24,
                    items: [{
                        type: "bi.label",
                        text: BI.i18nText("Fine-Design_Report_PageSetup_Page"),
                        textAlign: "right",
                        width: 70
                    }, {
                        el: {
                            type: "bi.button_group",
                            height: 24,
                            chooseType: BI.Selection.Multi,
                            items: BI.createItems([{
                                text: BI.i18nText("Fine-Design_Report_Is_Paint_Page"),
                                value: 1
                            }, {
                                text: BI.i18nText("Fine-Design_Report_IS_Auto_Scale"),
                                value: 2
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
                                type: "bi.left",
                                lgap: 5
                            }]
                        },
                        lgap: 10
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
    BI.shortcut("bi.report.template.web_setting.page_preview", Analysis);
})();