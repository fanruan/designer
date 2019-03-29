!(function(){
    /**
     * 带确定取消的通用控件
     */
    var Bar = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-settings-bar-container",
            el: {

            }
        },

        render: function() {
            return {
                type: "bi.vtape",
                items: [this.options.el, {
                    type: "bi.right_vertical_adapt",
                    height: 24,
                    rgap: 10,
                    vgap: 10,
                    items: [{
                        type: "bi.button",
                        level: "ignore",
                        text: BI.i18nText("Fine-Design_Report_OK"),
                        handler: function() {

                        }
                    }, {
                        type: "bi.button",
                        level: "ignore",
                        text: BI.i18nText("Fine-Design_Basic_Engine_Cancel"),
                        handler: function() {
                            
                        }
                    }]
                }]
            }
        }
    })
    BI.shortcut("bi.settings.bar_container", Bar)
})();