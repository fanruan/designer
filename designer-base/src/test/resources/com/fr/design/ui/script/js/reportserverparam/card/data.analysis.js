!(function () {
    /**
     * 服务器 数据分析设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-data-analysis"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.button_group",
                    height: 24,
                    value: [1,2,3],
                    items: BI.createItems([{
                        text: BI.i18nText("Fine-Design_Report_Engine_Sort_Sort"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Engine_Selection_Filter"),
                        value: 2
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Engine_List_Filter"),
                        value: 2
                    }], {
                        type: "bi.multi_select_item",
                        logic: {
                            dynamic: true
                        },
                        iconWrapperWidth: 16,
                        hgap: 5
                    }),
                    chooseType: BI.Selection.Multi,
                    layouts: [{
                        type: "bi.vertical_adapt"
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
    BI.shortcut("bi.report.server.param_setting.data_analysis", Analysis);
})();