!(function () {
    /**
     * 离开提示/直接显示控件/自动暂存相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-leave-setting",
            value: [1]
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.button_group",
                value: o.value,
                items: BI.createItems([{
                    text: BI.i18nText("Fine-Design_Report_Unload_Check"),
                    value: 1
                }, {
                    text: BI.i18nText("Fine-Design_Basic_Engine_Event_Show_Widgets"),
                    value: 2
                }, {
                    text: BI.i18nText("Fine-Design_Report_Write_Auto_Stash"),
                    value: 3
                }], {
                    type: "bi.multi_select_item",
                    hgap: 5,
                    logic: {
                        dynamic: true
                    },
                    iconWrapperWidth: 16
                }),
                chooseType: BI.Selection.Multi,
                layouts: [{
                    type: "bi.vertical_adapt",
                    rgap: 5
                }],
                ref: function(_ref) {
                    self.group = _ref;
                }
            };
        },

        getValue: function() {
            return self.group.getValue();
        },

        setValue: function(v) {
            this.group.setValue(v);
        }
    });
    BI.shortcut("bi.report.leave_setting", Analysis);
})();