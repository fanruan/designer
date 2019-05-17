package com.fr.design.widget.ui.designer;

import com.fr.design.dialog.BasicPane;

/**
 * 有些控件在不同终端需要对相同的属性分别进行设置,基础设置面板是一样的但是映射到控件上的属性又是不一样的,为了重用面板,这边加上xmltag做区分
 */
public abstract class XmlRelationedBasicPane extends BasicPane{
    private String xmlTag;

    public XmlRelationedBasicPane(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public String getXmlTag() {
        return xmlTag;
    }
}