!(function () {
    /**
     * 
     * 为模板单独设置的相关项
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-global-or-single-combo",
            value: 1
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.vertical_adapt",
                rgap: 10,
                height: 24,
                items: [{
                    type: "bi.label",
                    text: BI.i18nText("Fine-Design_Report_Blow_Set")
                }, {
                    type: "bi.text_value_combo",
                    width: 200,
                    ref: function(_ref) {
                        self.combo = _ref;
                    },
                    value: o.value,
                    items: [{
                        text: BI.i18nText("Fine-Design_Report_I_Want_To_Set_Single"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Form_Using_Server_Report_View_Settings"),
                        value: 2
                    }],
                    listerners: [{
                        eventName: "EVENT_CHANGE",
                        action: function() {
                            
                        }
                    }]
                }]
            };
        },

        getValue: function() {
            return self.combo.getValue();
        },

        setValue: function(v) {
            this.combo.setValue(v);
        }
    });
    BI.shortcut("bi.report.global_or_single_combo", Analysis);
})();