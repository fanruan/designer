package com.fr.design.mainframe;

import com.fr.form.stable.fun.AbstractECThumbnailProcessor;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * 报表块缩略图处理
 * 从xml中读取缩略图
 * Created by zhouping on 2017/2/22.
 */
public class ElementCaseThumbnail extends AbstractECThumbnailProcessor {
    public ElementCaseThumbnail(){
    }

    @Override
    public Image readThumbnail(XMLableReader reader) {
        return GeneralXMLTools.readImage(reader);
    }
}
