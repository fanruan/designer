!(function () {

    var Tab = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-setting-tab",
            value: "",
            tabItems: [],
            cardCreator: BI.emptyFn
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.vtape",
                items: [{
                    type: "bi.vertical_adapt",
                    cls: "tab-group",
                    items: [{
                        type: "bi.button_group",
                        layouts: [{
                            type: "bi.left"
                        }],
                        value: o.value,
                        items: BI.map(o.tabItems, function(idx, item){
                            return {
                                el: BI.extend({
                                    type: "bi.text_button",
                                    hgap: 10,
                                    height: 24
                                }, item)
                            };
                        }),
                        listeners: [{
                            eventName: BI.ButtonGroup.EVENT_CHANGE,
                            action: function (v) {
                                self.tableTab.setSelect(v);
                            }
                        }],
                        ref: function (_ref) {
                            // self.buttons = _ref;
                        }
                    }],
                    height: 24
                }, {
                    type: "bi.tab",
                    cls: "bi-card",
                    showIndex: o.value,
                    cardCreator: this.options.cardCreator,
                    ref: function (ref) {
                        self.tableTab = ref;
                    }
                }]
            };
        }
    });

    BI.shortcut("bi.setting.tab", Tab);
})();