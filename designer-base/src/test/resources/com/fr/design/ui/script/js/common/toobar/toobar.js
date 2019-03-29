!(function(){
    var ToolBar = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-settings-tool-bar",
            value: [1]
        },

        render: function() {
            var self = this, o = this.options;
            return {
                type: "bi.vertical",
                items: [{
                    type: "bi.left_right_vertical_adapt",
                    height: 24,
                    bgap: 15,
                    rhgap: 10,
                    items: {
                        left: [{
                            type: "bi.label",
                            text: BI.i18nText("Fine-Design_Report_Mobile_ToolBar"),
                            width: 70,
                            textAlign: "left"
                        }, {
                            type: "bi.button_group",
                            value: o.value,
                            chooseType: BI.Selection.Multi,
                            items: BI.createItems([{
                                text: BI.i18nText("Fine-Design_Report_ToolBar_Top"),
                                value: 1,
                                listeners: [{
                                    eventName: "EVENT_CHANGE",
                                    action: function() {
                                        if(this.isSelected()) {
                                            self._populateDefault();
                                        } else {
                                            self.topRegion.populate();
                                        }
                                    }
                                }]
                            }, {
                                text: BI.i18nText("Fine-Design_Report_ToolBar_Bottom"),
                                value: 2
                            }], {
                                type: "bi.multi_select_item",
                                hgap: 5,
                                logic: {
                                    dynamic: true
                                },
                                iconWrapperWidth: 16
                            }),
                            ref: function(_ref) {
                                self.group = _ref;
                            },
                            layouts: [{
                                type: "bi.left"
                            }],
                            listeners: [{
                                eventName: "EVENT_CHANGE",
                                action: function() {
                                    
                                }
                            }]
                        }],
                        right: [{
                            el: {
                                type: "bi.text_button",
                                cls: "bi-border",
                                text: BI.i18nText("Fine-Design_Basic_Scale_Custom_Button"),
                                handler: function() {

                                },
                                hgap: 5
                            }
                        }, {
                            el: {
                                type: "bi.text_button",
                                cls: "bi-border",
                                text: BI.i18nText("Fine-Design_Report_Restore_Default"),
                                handler: function() {
                                
                                },
                                hgap: 5
                            }
                        }]
                    }
                }, {
                    el: {
                        type: "bi.settings.tool_bar.region",
                        height: 56,
                        title: BI.i18nText("Fine-Design_Report_ToolBar_Top"),
                        ref: function(_ref) {
                            self.topRegion = _ref;
                        },
                        items: BI.contains(o.value, 1) ? this._createDefaultItems() : []
                    },
                    bgap: 20
                }, {
                    type: "bi.settings.tool_bar.region",
                    height: 56,
                    title: BI.i18nText("Fine-Design_Report_ToolBar_Bottom"),
                    ref: function(_ref) {
                        self.bottomRegion = _ref;
                    }
                }]
            }
        },

        _createDefaultItems: function() {
            return BI.createItems(BI.Constants.getConstant("bi.constant.report.template.web_setting.tools"), {
                type: "bi.icon_text_item",
                height: 24,
                extraCls: "bi-background bi-list-item-select bi-border-radius",
                logic: {
                    dynamic: true
                },
                textHgap: 10,
                value: BI.UUID()
            })
        },

        _populateDefault: function() {
            this.topRegion.populate(this._createDefaultItems());
        }
    })
    BI.shortcut("bi.settings.tool_bar", ToolBar)
})();