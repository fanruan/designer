!(function () {
    /**
     * 工具栏高度相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-tool-bar-height-select",
            height: 24,
            value: 2
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.vertical_adapt",
                height: 24,
                items: [{
                    el: {
                        type: "bi.label",
                        text: BI.i18nText("Fine-Design_Report_Mobile_ToolBar_Height")
                    },
                    rgap: 10
                }, {
                    type: "bi.button_group",
                    value: o.value,
                    ref: function(_ref) {
                        self.group = _ref;
                    },
                    items: BI.createItems([{
                        text: BI.i18nText("Fine-Design_Report_Tool_Bar_High"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Tool_Bar_Middle"),
                        value: 2
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Tool_Bar_Low"),
                        value: 3
                    }], {
                        type: "bi.single_select_radio_item",
                        logic: {
                            dynamic: true
                        }
                    }),
                    layouts: [{
                        type: "bi.vertical_adapt"
                    }],
                    listeners: [{
                        eventName: "EVENT_CHANGE",
                        action: function() {

                        }
                    }]
                }]
            
            };
        },

        getValue: function() {
            return self.group.getValue();
        },

        setValue: function(v) {
            this.group.setValue(v);
        }
    });
    BI.shortcut("bi.report.server.param_setting.tool_bar_height_select", Analysis);
})();