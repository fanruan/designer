!(function(){
    var Region = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-settings-tool-bar-region",
            title: ""
        },

        render: function() {
            var self = this, o = this.options;
            return [{
                type: "bi.button_group",
                cls: "bi-border",
                items: o.items,
                chooseType: BI.Selection.Multi,
                layouts: [{
                    type: "bi.vertical_adapt",
                    hgap: 10
                }],
                ref: function(_ref) {
                    self.group = _ref;
                }
            }, {
                type: "bi.absolute",
                items: [{
                    el: {
                        type: "bi.label",
                        cls: "bi-background",
                        text: o.title
                    },
                    top: -10,
                    left: 10
                }]
            }]
        },

        populate: function(items) {
            this.group.populate(items);
        }
    })
    BI.shortcut("bi.settings.tool_bar.region", Region);
})();