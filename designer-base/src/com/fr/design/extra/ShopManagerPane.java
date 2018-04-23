package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

import java.awt.BorderLayout;
import java.awt.Component;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 * 应用中心的构建采用JavaScript代码来动态实现,但是不总是依赖于服务器端的HTML
 * 采用JDK提供的JavaScript引擎,实际是用JavaScript语法实现Java端的功能,并通过JavaScript引擎动态调用
 * JavaScript放在安装目录下的scripts/store目录下,检测到新版本的时候,可以通过更新这个目录下的文件实现热更新
 * 不直接嵌入WebView组件的原因是什么呢?
 * 因为如果直接嵌入WebView,和设计器的交互就需要预先设定好,这样灵活性会差很多,而如果使用JavaScript引擎,
 * 就可以直接在JavaScript中和WebView组件做交互,而同时JavaScript中可以调用任何的设计器API.
 */
public class ShopManagerPane extends BasicPane {

    public ShopManagerPane(Component webPane) {
        setLayout(new BorderLayout());
        add(webPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Plugin_Manager");
    }
}