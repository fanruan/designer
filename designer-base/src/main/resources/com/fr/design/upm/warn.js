window.onload = function () {
    let button = BI.createWidget({
        type : "bi.button",
        text : PluginHelper.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Resource_Warn"),
        level: 'common',
        height: 30,
        handler : function () {
            PluginHelper.startDownload();
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