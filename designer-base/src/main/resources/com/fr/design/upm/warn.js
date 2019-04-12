window.onload = function () {
    let button = BI.createWidget({
        type : "bi.button",
        text : "点击我跳转到插件商店",
        level: 'common',
        height: 30,
        handler : function () {
            PluginBridgeTest.startDownload();
        }
    });
    BI.createWidget({
        type:"bi.absolute",
        element: "body",
        items: [{
            el: button,
            left: 100,
            top: 100
        }]
    });
};