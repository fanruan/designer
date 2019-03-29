!(function () {
    /**
     * 填报当前编辑行背景设置相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-form-background-setting"
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.vertical_adapt",
                items: [{
                    type: "bi.multi_select_item",
                    logic: {
                        dynamic: true
                    },
                    iconWrapperWidth: 16,
                    text: BI.i18nText("Fine-Design_Report_Set_Face_Write_Current_Edit_Row_Background")
                }, {
                    el: {
                        type: "bi.color_chooser",
                        width: 24,
                        height: 24
                    },
                    lgap: 10
                }]
            };
        }
    });
    BI.shortcut("bi.report.form_background_setting", Analysis);
})();