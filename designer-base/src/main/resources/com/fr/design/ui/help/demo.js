window.addEventListener("load", function (ev) {
    window.BI.i18nText = function(key) {return window.Pool.i18n.i18nText(key);}
    var combo1 = BI.createWidget({
        type: "bi.vertical",
        items: [
            {
                type: "bi.text_value_combo",
                text: "选项1",
                width: 300,
                items: [
                    {
                        el: {
                            type: "bi.single_select_radio_item",
                            width: 290,
                            text: "选项1",
                            value: 1
                        },
                        text: "选项1",
                        value: 1,
                        lgap: 10
                    },
                    {
                        el: {
                            type: "bi.single_select_radio_item",
                            width: 290,
                            text: "选项2",
                            value: 2
                        },
                        lgap: 10,
                        text: "选项2",
                        value: 2
                    },
                    {
                        el: {
                            type: "bi.single_select_radio_item",
                            width: 290,
                            text: "选项3",
                            value: 3
                        },
                        lgap: 10,
                        text: "选项3",
                        value: 3
                    }
                ]
            }
        ]
    });

    var date = BI.createWidget({
        type: "bi.left",
        items: [{
            el: {
                type: "bi.date_time_combo",
                value: {
                    year: 2018,
                    month: 9,
                    day: 28,
                    hour: 13,
                    minute: 31,
                    second: 1
                }
            }
        }]
    });

    var comboTree = BI.createWidget({
        type: "bi.vertical",
        items: [
            {
                type: "bi.tree_value_chooser_combo",
                width: 300,
                itemsCreator: function(op, callback) {
                    callback([
                        {
                            id: 1,
                            text: "第1项",
                            value: "1"
                        },
                        {
                            id: 2,
                            text: "第2项",
                            value: "2"
                        },
                        {
                            id: 3,
                            text: "第3项",
                            value: "3",
                            open: true
                        },
                        {
                            id: 11,
                            pId: 1,
                            text: "子项1",
                            value: "11"
                        },
                        {
                            id: 12,
                            pId: 1,
                            text: "子项2",
                            value: "12"
                        },
                        {
                            id: 13,
                            pId: 1,
                            text: "子项3",
                            value: "13"
                        },
                        {
                            id: 31,
                            pId: 3,
                            text: "子项1",
                            value: "31"
                        },
                        {
                            id: 32,
                            pId: 3,
                            text: "子项2",
                            value: "32"
                        },
                        {
                            id: 33,
                            pId: 3,
                            text: "子项3",
                            value: "33"
                        }
                    ]);
                }
            }
        ]
    });

    var color = BI.createWidget({
        type: "bi.left",
        items: [{
            type: "bi.simple_color_chooser",
            width: 24,
            height: 24
        }, {
            el: {
                type: "bi.color_chooser",
                width: 230,
                height: 24
            },
            lgap: 10
        }]
    });

    var Slider = BI.inherit(BI.Widget, {
        props: {
            width: 300,
            height: 50,
            min: 0,
            max: 100
        },

        mounted: function() {
            var o = this.options;
            this.singleSliderInterval.setMinAndMax({
                min: o.min,
                max: o.max
            });

            this.singleSliderInterval.setValue({
                min: 10,
                max: 80
            });
            this.singleSliderInterval.populate();
        },

        render: function() {
            var self = this,
                o = this.options;
            return {
                type: "bi.vertical",
                element: this,
                items: [
                    {
                        type: "bi.interval_slider",
                        digit: 0,
                        width: o.width,
                        height: o.height,
                        ref: function(_ref) {
                            self.singleSliderInterval = _ref;
                        }
                    }
                ]
            };
        }
    });
    BI.shortcut("demo.slider_interval", Slider);
    var slider = BI.createWidget({
        type: "demo.slider_interval"
    });

    BI.createWidget({
        type:"bi.absolute",
        element: "body",
        items: [{
            el: combo1,
            left: 100,
            top: 100
        }, {
            el : date,
            left: 100,
            top : 150
        }, {
            el : comboTree,
            left : 100,
            top : 200
        }, {
            el : color,
            left : 100,
            top : 250
        }, {
            el : slider,
            left : 400,
            top : 100
        }]
    });
});