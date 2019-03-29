!(function () {
    /**
     * 报表显示位置相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-report-show-location"
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.vertical_adapt",
                height: 24,
                items: [{
                    type: "bi.label",
                    text: BI.i18nText("Fine-Design_Report_Show_Location")
                }, {
                    type: "bi.button_group",
                    ref: function(_ref) {
                        self.group = _ref;
                    },
                    value: 2,
                    items: BI.createItems([{
                        text: BI.i18nText("Fine-Design_Report_Center_Display"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Left_Display"),
                        value: 2
                    }], {
                        type: "bi.single_select_radio_item",
                        logic: {
                            dynamic: true
                        }
                    }),
                    layouts: [{
                        type: "bi.left",
                        lgap: 10
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
    BI.shortcut("bi.report.server.param_setting.report_show_location", Analysis);
})();