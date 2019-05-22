window.onload = function () {
    let label = BI.createWidget({
        type: "bi.label",
        text: PluginHelper.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Resource_Warn"),
        height: 30
    });
    let callback = function(status, text) {
        download.setValue(text);
    };
    let buttonOK = BI.createWidget({
        type: "bi.button",
        text: PluginHelper.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Download"),
        level: 'common',
        height: 30,
        handler: function () {
            PluginHelper.startDownload(callback);
        }
    });
    let buttonClose = BI.createWidget({
        type: "bi.button",
        text: PluginHelper.i18nText("Fine-Design_Basic_Update_Plugin_Manager_Close"),
        level: 'warning',
        height: 30,
        handler: function () {
            PluginHelper.closeWindow();
        }
    });

    let download = BI.createWidget({
        type: "bi.label",
        height: 30
    });
    let left = 300;
    let top = 200;
    BI.createWidget({
        type: "bi.absolute",
        element: "body",
        items: [
            {
                el: label,
                left: left,
                top: top
            },
            {
                el : buttonOK,
                left : left,
                top : top + 40
            },
            {
                el : buttonClose,
                left : left + 100,
                top : top + 40
            },
            {
                el : download,
                left : left,
                top : top + 80
            }
            ]
    });
};