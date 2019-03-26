!(function () {
    /**
     * 使用工具栏相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-use-tool-bar"
        },

        render: function () {
            var self = this;
            return {
                type: "bi.vertical_adapt",
                    height: 24,
                    items: [{
                        el: {
                            type: "bi.multi_select_item",
                            ref: function(_ref) {
                                self.multiSelect = _ref;
                            },
                            hgap: 5,
                            selected: true,
                            text: BI.i18nText("Fine-Design_Report_Use_ToolBar"),
                            logic: {
                                dynamic: true
                            },
                            iconWrapperWidth: 16
                        }
                    }, {
                        type: "bi.text_button",
                        cls: "bi-card bi-border",
                        text: BI.i18nText("Fine-Design_Report_Edit"),
                        hgap: 10
                    }]
            };
        },

        getValue: function() {
            return self.multiSelect.getSelected();
        },

        setValue: function(v) {
            this.multiSelect.setSelected(v);
        }
    });
    BI.shortcut("bi.report.server.param_setting.use_tool_bar", Analysis);
})();