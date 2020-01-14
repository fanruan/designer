var MAX_DESC_NUM = 5;  // 最多显示5条信息

function i18nText(key) {
    return Pool.data.i18nText(key);
}

function getDescArea(descList) {
    var descItems = [];
    for (var i in descList) {
        var num = parseInt(i) + 1;
        if (num > MAX_DESC_NUM) {
            break;
        }
        descItems.push({
            type: "bi.label",
            text: num + "）" + descList[i],
            whiteSpace: "pre-wrap",
            textAlign: "left"
        })
    }

    return BI.createWidget({
        type: "bi.vertical",
        cls: "desc",
        items: descItems
    });
}

function getTitleArea() {
    return BI.createWidget({
        type: "bi.vertical",
        items: [
            {
                type: "bi.label",
                text: i18nText("Fine-Design_Find_New_Version"),
                cls: "title font-bold",
                textAlign: "left"
            },
            {
                type: "bi.label",
                text: Pool.data.getVersion(),
                cls: "version font-bold",
                textAlign: "left"
            }
        ]
    });
}

function getButtonGroup() {
    return BI.createWidget({
        type: 'bi.left',
        cls: "buttonGroup",
        items: [
            {
                type: 'bi.button',
                text: i18nText("Fine-Design_Update_Now"),
                level: 'common',
                height: 24,
                handler: function () {
                    Pool.data.updateNow();
                }
            },
            {
                el: {
                    type: 'bi.button',
                    text: i18nText("Fine-Design_Remind_Me_Next_Time"),
                    level: 'ignore',
                    height: 24,
                    handler: function () {
                        Pool.data.remindNextTime();
                    }
                },
                lgap: 10
            },
            {
                el: {
                    type: 'bi.button',
                    text: i18nText("Fine-Design_Skip_This_Version"),
                    level: 'ignore',
                    height: 24,
                    handler: function () {
                        Pool.data.skipThisVersion();
                    }
                },
                lgap: 10
            }
        ]
    });
}

function getMoreInfo() {
    return BI.createWidget({
        type: "bi.text_button",
        text: i18nText("Fine-Design_See_More_New_Features"),
        cls: "moreInfo",
        textAlign: "left",
        handler: function () {
            // 为了使用系统的浏览器，只能从 Java 那边打开网页
            Pool.data.browseMoreInfoUrl();
        }
    });
}

function getCloseButton() {
    return BI.createWidget({
        type: "bi.button",
        text: String.fromCharCode(10005),
        cls: "close-btn",
        clear: true,
        handler: function () {
            Pool.data.closeWindow();
        }
    });
}

function getShowItems() {
    var title = getTitleArea();

    var closeButton = getCloseButton();

    var descList = Pool.data.getContent().split("\n");
    var descArea = getDescArea(descList);

    var moreInfo = getMoreInfo();

    var buttonGroup = getButtonGroup();

    return [title, closeButton, descArea, moreInfo, buttonGroup];
}

window.addEventListener("load", function (ev) {
    var showItems = getShowItems();

    var container = BI.createWidget({
        type:"bi.vertical",
        element: "body",
        cls: "container",
        items: showItems
    });

    container.element.css("background", "url(" + Pool.data.getBackgroundUrl() + ")");
});