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
})();!(function () {
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
})();!(function () {
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
})();!(function () {
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
})();!(function () {
    /**
     * 离开提示/直接显示控件/自动暂存相关
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-leave-setting",
            value: [1]
        },

        render: function () {
            var self = this, o = this.options;
            return {
                type: "bi.button_group",
                value: o.value,
                items: BI.createItems([{
                    text: BI.i18nText("Fine-Design_Report_Unload_Check"),
                    value: 1
                }, {
                    text: BI.i18nText("Fine-Design_Basic_Engine_Event_Show_Widgets"),
                    value: 2
                }, {
                    text: BI.i18nText("Fine-Design_Report_Write_Auto_Stash"),
                    value: 3
                }], {
                    type: "bi.multi_select_item",
                    hgap: 5,
                    logic: {
                        dynamic: true
                    },
                    iconWrapperWidth: 16
                }),
                chooseType: BI.Selection.Multi,
                layouts: [{
                    type: "bi.vertical_adapt",
                    rgap: 5
                }],
                ref: function(_ref) {
                    self.group = _ref;
                }
            };
        },

        getValue: function() {
            return self.group.getValue();
        },

        setValue: function(v) {
            this.group.setValue(v);
        }
    });
    BI.shortcut("bi.report.leave_setting", Analysis);
})();!(function () {
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
})();!(function () {

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
})();!(function () {
    /**
     * sheet标签页显示位置
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
                    text: BI.i18nText("Fine-Design_Report_Sheet_Label_Page_Display_Position")
                }, {
                    type: "bi.button_group",
                    ref: function(_ref) {
                        self.group = _ref;
                    },
                    value: 2,
                    items: BI.createItems([{
                        text: BI.i18nText("Fine-Design_Form_Base_Top"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Bottom"),
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
    BI.shortcut("bi.report.sheet_label_location", Analysis);
})();!(function(){
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
})();!(function(){
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
})();!(function () {
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
})();!(function () {
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
})();window.BICst = window.BICst || {};
BICst.REPORT_SERVER_PARAM = {
    SPLIT_PAGE_PREVIEW_SETTING: 1,
    FORM_PAGE_SETTING: 2,
    DATA_ANALYSIS_SETTING: 3,
    IMPORT_CSS: 4,
    IMPORT_JS: 5,
    ERROR_TEMPLATE_DEFINE: 6,
    PRINT_SETTING: 7
};
BICst.REPORT_TEMPLATE_WEB_SETTING = {
    BASE: 1,
    PRINT: 2,
    SPLIT_PAGE_PREVIEW_SETTING: 3,
    FORM_PAGE_SETTING: 4,
    DATA_ANALYSIS_SETTING: 5,
    BROWSER: 6,
    IMPORT_CSS: 7,
    IMPORT_JS: 8
};

BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS = {
    FIRST_PAGE: 1,
    PRE_PAGE: 2,
    NEXT_PAGE: 3,
    LAST_PAGE: 4,
    PRINT: 5,
    EXPORT: 6,
    EMAIL: 7
};BI.addI18n({
    "Fine-Design_Report_WEB_Pagination_Setting": "分页预览设置",
    "Fine-Design_Report_WEB_Write_Setting": "填报页面设置",
    "Fine-Design_Report_Data_Analysis_Settings": "数据分析设置",
    "Fine-Design_Report_ReportServerP_Import_Css": "引用Css",
    "Fine-Design_Report_ReportServerP_Import_JavaScript": "引用JavaScript",
    "Fine-Design_Report_Error_Handler_Template": "出错模板定义",
    "Fine-Design_Report_Print_Setting": "打印设置",
    "Fine-Design_Report_Mobile_ToolBar": "工具栏",
    "Fine-Design_Report_Tool_Bar_High": "高",
    "Fine-Design_Report_Tool_Bar_Middle": "中",
    "Fine-Design_Report_Tool_Bar_Low": "低",
    "Fine-Design_Report_Mobile_ToolBar_Height": "工具栏高度",
    "Fine-Design_Report_Is_Paint_Page": "以图片方式显示",
    "Fine-Design_Report_IS_Auto_Scale": "iframe嵌入时自动缩放",
    "Fine-Design_Report_IS_TD_HEAVY_EXPORT": "重方式输出格子",
    "Fine-Design_Report_Use_ToolBar": "使用工具栏",
    "Fine-Design_Report_Show_Location": "报表显示位置",
    "Fine-Design_Report_Center_Display": "居中展示",
    "Fine-Design_Report_Left_Display": "左展示",
    "Fine-Design_Report_Editing_Listeners": "事件编辑",
    "Fine-Design_Report_Edit": "编辑",
    "Fine-Design_Report_Sheet_Label_Page_Display_Position": "sheet标签页显示位置：",
    "Fine-Design_Form_Base_Top": "上",
    "Fine-Design_Report_Bottom": "下",
    "Fine-Design_Report_Set_Face_Write_Current_Edit_Row_Background": "当前编辑行背景设置",
    "Fine-Design_Report_Unload_Check": "未提交离开提示",
    "Fine-Design_Basic_Engine_Event_Show_Widgets": "直接显示控件",
    "Fine-Design_Report_Write_Auto_Stash": "自动暂存",
    "Fine-Design_Report_Engine_Sort_Sort": "排序",
    "Fine-Design_Report_Engine_Selection_Filter": "条件筛选",
    "Fine-Design_Report_Engine_List_Filter": "列表筛选",
    "Fine-Design_Basic_Engine_Cancel": "取消",
    "Fine-Design_Report_OK": "确定",
    "Fine-Design_Report_Basic": "基本",
    "Fine-Design_Report_Printer(Server)": "打印机(服务器)",
    "Fine-Design_Report_Browser_Background": "浏览器背景",
    "Fine-Design_Report_I_Want_To_Set_Single": "为该模板单独设置",
    "Fine-Design_Form_Using_Server_Report_View_Settings": "采用服务器设置",
    "Fine-Design_Report_Blow_Set": "以下设置:",
    "Fine-Design_Report_PageSetup_Page": "页面",
    "Fine-Design_Report_ToolBar_Top": "顶部工具栏",
    "Fine-Design_Report_ToolBar_Bottom": "底部工具栏",
    "Fine-Design_Basic_Scale_Custom_Button": "自定义",
    "Fine-Design_Report_Restore_Default": "恢复默认",
    "Fine-Design_Report_Engine_ReportServerP_First": "首页",
    "Fine-Design_Report_Engine_ReportServerP_Previous": "上一页",
    "Fine-Design_Report_Engine_ReportServerP_Next": "下一页",
    "Fine-Design_Report_Engine_ReportServerP_Last": "末页",
    "Fine-Design_Report_Engine_Print": "打印",
    "Fine-Design_Report_Engine_Export": "导出",
    "Fine-Design_Report_Engine_Email": "邮件"
});!(function () {
    /**
     * 服务器 数据分析设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-data-analysis"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.button_group",
                    height: 24,
                    value: [1,2,3],
                    items: BI.createItems([{
                        text: BI.i18nText("Fine-Design_Report_Engine_Sort_Sort"),
                        value: 1
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Engine_Selection_Filter"),
                        value: 2
                    }, {
                        text: BI.i18nText("Fine-Design_Report_Engine_List_Filter"),
                        value: 2
                    }], {
                        type: "bi.multi_select_item",
                        logic: {
                            dynamic: true
                        },
                        iconWrapperWidth: 16,
                        hgap: 5
                    }),
                    chooseType: BI.Selection.Multi,
                    layouts: [{
                        type: "bi.vertical_adapt"
                    }]
                }, {
                    type: "bi.report.server.param_setting.use_tool_bar",
                    height: 24,
                    value: true
                }, {
                    type: "bi.report.server.param_setting.tool_bar_height_select",
                    height: 24
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.server.param_setting.data_analysis", Analysis);
})();!(function () {

    /**
     * 服务器 填报预览设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-form-page"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.grid",
                    height: 78,
                    columns: 2,
                    rows: 2,
                    items: [{
                        column: 0,
                        row: 0,
                        el: {
                            type: "bi.report.sheet_label_location",
                            height: 24
                        }
                    }, {
                        column: 1,
                        row: 0,
                        el: {
                            type: "bi.report.server.param_setting.report_show_location",
                            height: 24
                        }
                    }, {
                        column: 0,
                        row: 1,
                        el: {
                            type: "bi.report.form_background_setting",
                            height: 24
                        }
                    }, {
                        column: 1,
                        row: 1,
                        el: {
                            type: "bi.report.leave_setting",
                            height: 24
                        }
                    }]
                }, {
                    type: "bi.report.server.param_setting.use_tool_bar",
                    height: 24,
                    value: true
                }, {
                    type: "bi.report.server.param_setting.tool_bar_height_select",
                    height: 24
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.server.param_setting.form_page", Analysis);
})();!(function () {
    /**
     * 服务器 分页预览设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-page-preview"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.report.server.param_setting.report_show_location",
                    height: 24
                }, {
                    type: "bi.button_group",
                    height: 92,
                    chooseType: BI.Selection.Multi,
                    items: BI.createItems([{
                        el: {
                            text: BI.i18nText("Fine-Design_Report_Is_Paint_Page"),
                            value: 1
                        },
                        bgap: 10
                    }, {
                        el: {
                            text: BI.i18nText("Fine-Design_Report_IS_Auto_Scale"),
                            value: 2
                        },
                        bgap: 10
                    }, {
                        text: BI.i18nText("Fine-Design_Report_IS_TD_HEAVY_EXPORT"),
                        value: 3
                    }], {
                        type: "bi.multi_select_item",
                        hgap: 5,
                        logic: {
                            dynamic: true
                        },
                        iconWrapperWidth: 16
                    }),
                    layouts: [{
                        type: "bi.vertical"
                    }]
                }, {
                    type: "bi.report.server.param_setting.use_tool_bar",
                    height: 24
                }, {
                    type: "bi.report.server.param_setting.tool_bar_height_select",
                    height: 24
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.server.param_setting.page_preview", Analysis);
})();!(function () {
    BI.constant("bi.constant.report.server.param_setting", [
        {
            text: BI.i18nText("Fine-Design_Report_WEB_Pagination_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.SPLIT_PAGE_PREVIEW_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_WEB_Write_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.FORM_PAGE_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_Data_Analysis_Settings"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.DATA_ANALYSIS_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_Css"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.IMPORT_CSS
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.IMPORT_JS
        }, {
            text: BI.i18nText("Fine-Design_Report_Error_Handler_Template"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.ERROR_TEMPLATE_DEFINE
        }, {
            text: BI.i18nText("Fine-Design_Report_Print_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_SERVER_PARAM.PRINT_SETTING
        }]
    );
})();!(function () {

    /**
     * 报表服务器参数面板
     */
    var Tab = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-report-server-param-setting"
        },

        render: function () {
            return {
                type: "bi.setting.tab",
                value: BICst.REPORT_SERVER_PARAM.SPLIT_PAGE_PREVIEW_SETTING,
                tabItems: BI.Constants.getConstant("bi.constant.report.server.param_setting"),
                cardCreator: BI.bind(this._createCard, this)
            };
        },

        _createCard: function (v) {
            switch (v) {
                case BICst.REPORT_SERVER_PARAM.SPLIT_PAGE_PREVIEW_SETTING:
                    return {
                        type: "bi.report.server.param_setting.page_preview"
                    };
                case BICst.REPORT_SERVER_PARAM.FORM_PAGE_SETTING:
                    return {
                        type: "bi.report.server.param_setting.form_page"
                    };
                case BICst.REPORT_SERVER_PARAM.DATA_ANALYSIS_SETTING:
                    return {
                        type: "bi.report.server.param_setting.data_analysis"
                    }
                case BICst.REPORT_SERVER_PARAM.IMPORT_CSS:
                case BICst.REPORT_SERVER_PARAM.IMPORT_JS:
                case BICst.REPORT_SERVER_PARAM.ERROR_TEMPLATE_DEFINE:
                case BICst.REPORT_SERVER_PARAM.PRINT_SETTING:
                default:
                    return {
                        type: "bi.label",
                        text: "1"
                    };
            }
        }

    });

    BI.shortcut("bi.report.server.param_setting", Tab);
})();!(function () {
    /**
     * 填报页面设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-form-page"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.report.global_or_single_combo",
                    height: 24
                }, {
                    type: "bi.grid",
                    height: 78,
                    columns: 2,
                    rows: 2,
                    items: [{
                        column: 0,
                        row: 0,
                        el: {
                            type: "bi.report.sheet_label_location"
                        }
                    }, {
                        column: 1,
                        row: 0,
                        el: {
                            type: "bi.report.server.param_setting.report_show_location",
                            height: 24
                        }
                    }, {
                        column: 0,
                        row: 1,
                        el: {
                            type: "bi.report.form_background_setting"
                        }
                    }, {
                        column: 1,
                        row: 1,
                        el: {
                            type: "bi.report.leave_setting"
                        }
                    }]
                }, {
                    type: "bi.settings.tool_bar",
                    height: 180
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.template.web_setting.form_page", Analysis);
})();!(function () {
    /**
     * 分页预览设置
     */
    var Analysis = BI.inherit(BI.Widget, {
        props: {
            baseCls: "bi-report-server-param-setting-page-preview"
        },

        render: function () {
            return {
                type: "bi.vtape",
                vgap: 10,
                hgap: 10,
                items: [{
                    type: "bi.report.global_or_single_combo",
                    height: 24
                }, {
                    type: "bi.report.server.param_setting.report_show_location",
                    height: 24
                }, {
                    type: "bi.vertical_adapt",
                    height: 24,
                    items: [{
                        type: "bi.label",
                        text: BI.i18nText("Fine-Design_Report_PageSetup_Page"),
                        textAlign: "right",
                        width: 70
                    }, {
                        el: {
                            type: "bi.button_group",
                            height: 24,
                            chooseType: BI.Selection.Multi,
                            items: BI.createItems([{
                                text: BI.i18nText("Fine-Design_Report_Is_Paint_Page"),
                                value: 1
                            }, {
                                text: BI.i18nText("Fine-Design_Report_IS_Auto_Scale"),
                                value: 2
                            }, {
                                text: BI.i18nText("Fine-Design_Report_IS_TD_HEAVY_EXPORT"),
                                value: 3
                            }], {
                                type: "bi.multi_select_item",
                                hgap: 5,
                                logic: {
                                    dynamic: true
                                },
                                iconWrapperWidth: 16
                            }),
                            layouts: [{
                                type: "bi.left",
                                lgap: 5
                            }]
                        },
                        lgap: 10
                    }]
                }, {
                    type: "bi.settings.tool_bar",
                    height: 180
                }, {
                    type: "bi.label",
                    height: 24,
                    textAlign: "left",
                    text: BI.i18nText("Fine-Design_Report_Editing_Listeners") + ":",
                }, {
                    type: "bi.report.server.param_setting.edit_list"
                }]
            };
        }
    });
    BI.shortcut("bi.report.template.web_setting.page_preview", Analysis);
})();!(function () {
    BI.constant("bi.constant.report.template.web_setting", [
        {
            text: BI.i18nText("Fine-Design_Report_Basic"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.BASE
        }, {
            text: BI.i18nText("Fine-Design_Report_Printer(Server)"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.PRINT
        }, {
            text: BI.i18nText("Fine-Design_Report_WEB_Pagination_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_WEB_Write_Setting"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.FORM_PAGE_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_Data_Analysis_Settings"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.DATA_ANALYSIS_SETTING
        }, {
            text: BI.i18nText("Fine-Design_Report_Browser_Background"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.BROWSER
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_Css"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_CSS
        }, {
            text: BI.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript"),
            cls: "tab-item",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_JS
        }]
    );

    BI.constant("bi.constant.report.template.web_setting.tools", [
        {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_First"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.FIRST_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Previous"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.PRE_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Next"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.NEXT_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_ReportServerP_Last"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.LAST_PAGE
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Print"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.PRINT
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Export"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.EXPORT
        }, {
            text: BI.i18nText("Fine-Design_Report_Engine_Email"),
            cls: "close-font",
            value: BICst.REPORT_TEMPLATE_WEB_SETTING_TOOLS.EMAIL
        }]
    );
})();!(function () {

    /**
     * 模板Web属性面板
     */
    var Tab = BI.inherit(BI.Widget, {

        props: {
            baseCls: "bi-report-template-web-setting"
        },

        render: function () {
            return {
                type: "bi.setting.tab",
                value: BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING,
                tabItems: BI.Constants.getConstant("bi.constant.report.template.web_setting"),
                cardCreator: BI.bind(this._createCard, this)
            };
        },

        _createCard: function (v) {
            switch (v) {
                case BICst.REPORT_TEMPLATE_WEB_SETTING.SPLIT_PAGE_PREVIEW_SETTING:
                    return {
                        type: "bi.report.template.web_setting.page_preview"
                    }
                case BICst.REPORT_TEMPLATE_WEB_SETTING.FORM_PAGE_SETTING:
                    return {
                        type: "bi.report.template.web_setting.form_page"
                    }
                case BICst.REPORT_TEMPLATE_WEB_SETTING.DATA_ANALYSIS_SETTING:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.BASE:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.PRINT:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.BROWSER:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_CSS:
                case BICst.REPORT_TEMPLATE_WEB_SETTING.IMPORT_JS:
                default:
                    return {
                        type: "bi.label",
                        text: "1"
                    };
            }
        }

    });

    BI.shortcut("bi.report.template.web_setting", Tab);
})();!(function(){
    window.addEventListener("load", function (ev) {
        BI.createWidget({
            type:"bi.absolute",
            element: "body",
            items: [{
                el: {
                    type: "bi.settings.bar_container",
                    el: {
                        type: "bi.report.template.web_setting"
                    }
                },
                left: 0,
                top: 0,
                right: 0,
                bottom: 0
            }]
        });
    })
})();