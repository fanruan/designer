window.addEventListener("load", function (ev) {
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

    BI.createWidget({
        type:"bi.absolute",
        element: "body",
        items: [{
            el: combo1,
            left: 100,
            top: 100
        }]
    });
});