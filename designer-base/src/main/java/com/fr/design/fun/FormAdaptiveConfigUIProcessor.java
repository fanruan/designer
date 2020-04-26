package com.fr.design.fun;

import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.ShortCut;
import com.fr.stable.fun.mark.Immutable;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


/**
 * Created by kerry on 2020-04-09
 * 临时接口，后续自适应内置后删除
 */
public interface FormAdaptiveConfigUIProcessor extends Immutable {

    String MARK_STRING = "FormAdaptiveConfigUIProcessor";
    int CURRENT_LEVEL = 1;

    /**
     * 获取表单自适应配置菜单
     * @return 表单自适应配置菜单
     */
    ShortCut getConfigShortCut(JTemplate jTemplate);

    /**
     * 绘制自适应下报表块在表单界面中显示图片
     * @param size 绘制尺寸
     * @param elementCasePane 报表块内容对象
     * @return 自适应下报表块在表单界面中显示的图片
     */
    BufferedImage paintFormElementCaseImage(Dimension size, JComponent elementCasePane);

}

