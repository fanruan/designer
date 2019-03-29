!(function () {
    /**
     * 事件设置
     */
    var List = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-edit-list"
        },

        render: function () {
            var self = this;
            return {
                type: "bi.vtape",
                vgap: 5,
                items: [{
                    type: "bi.vertical_adapt",
                    height: 24,
                    items: [{
                        type: "bi.icon_button",
                        cls: "text-add-tip-font",
                        width: 24,
                        height: 24,
                        handler: function() {
                            self.group.addItems([self._createItem()])
                        }
                    }, {
                        type: "bi.icon_button",
                        cls: "close-font",
                        width: 24,
                        height: 24,
                        handler: function() {
                            self.group.removeItemAt();
                        }
                    }, {
                        type: "bi.icon_button",
                        cls: "close-font",
                        width: 24,
                        height: 24,
                        handler: function() {
                            self.removeItemAt(self._getIndexOfItemValue(self.group.getValue()))
                        }
                    }]
                }, {
                    type: "bi.button_group",
                    cls: "bi-border bi-card",
                    ref: function(_ref) {
                        self.group = _ref;
                    },
                    items: []
                }]
            };
        },

        _getIndexOfItemValue: function(values) {
            values = BI.isArray(values) ? values : [values];
            var indexes = [];
            BI.each(this.group.getAllButtons(), function(idx, button){
                if(BI.contains(values, button.getValue())) {
                    indexes.push(idx);
                }
            });
            return indexes;
        },

        _createItem: function() {
            return {
                type: "bi.text_button",
                textAlign: "left",
                hgap: 10,
                text: "选项" + this.group.getAllButtons().length,
                cls: "bi-list-item-select",
                value: BI.UUID()
            };
        },

        populate: function(items) {
            this.group.populate(items);
        },

        addItems: function(items) {

        },

        removeItemAt: function(indexes) {
            this.group.removeItemAt(indexes);
        }
    });
    BI.shortcut("bi.report.server.param_setting.edit_list", List);
})();