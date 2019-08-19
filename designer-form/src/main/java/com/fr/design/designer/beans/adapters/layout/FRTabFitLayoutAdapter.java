/**
 *
 */
package com.fr.design.designer.beans.adapters.layout;


import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRTabFitLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.act.BorderPacker;

import java.awt.*;

/**
 * tab布局tabFit适配器
 *
 * @author focus
 * @date 2014-6-24
 */
public class FRTabFitLayoutAdapter extends FRFitLayoutAdapter {

    /**
     * 构造函数
     *
     * @param container XWTabFitLayout容器
     */
    public FRTabFitLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
     * 返回布局自身属性，方便一些特有设置在layout刷新时处理
     */
    @Override
    public GroupModel getLayoutProperties() {
        XWTabFitLayout xfl = (XWTabFitLayout) container;
        return new FRTabFitLayoutPropertiesGroupModel(xfl);
    }

    /**
     * 组件的ComponentAdapter在添加组件时，如果发现布局管理器不为空，会继而调用该布局管理器的
     * addComp方法来完成组件的具体添加。在该方法内，布局管理器可以提供额外的功能。
     *
     * @param creator 被添加的新组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否添加成功，成功返回true，否则false
     */
    @Override
    public boolean addBean(XCreator creator, int x, int y) {
        // 经过accept判断后，container会被改变，先备份
        XLayoutContainer backUpContainer = container;
        Rectangle rect = ComponentUtils.getRelativeBounds(container);
        int posX = x - rect.x;
        int posY = y - rect.y;
        if (!accept(creator, posX, posY)) {
            return false;
        }
        // posX，posY是新拖入组件相对于容器的位置，若在tab布局的边缘，则需要把新组件添加到l
        // 父层自适应布局中，这时候的添加位置就是tab布局所在的位置
        if (this.intersectsEdge(posX, posY, backUpContainer)) {
            if (!ComparatorUtils.equals(backUpContainer.getOuterLayout(), backUpContainer.getBackupParent())) {
                XWTabFitLayout tabLayout = (XWTabFitLayout) backUpContainer;
                y = adjustY(y, tabLayout);
            }
            addComp(creator, x, y);
            ((XWidgetCreator) creator).recalculateChildrenSize();
            return true;
        }
        // 如果不在边缘，容器为本自适应布局，增加组件的位置就是相对于容器的位置
        addComp(creator, posX, posY);
        ((XWidgetCreator) creator).recalculateChildrenSize();
        return true;
    }

    // tab布局的纵坐标受到tab高度以及参数面板高度的影响，判断的上边界取得是里面XWTabFitLayout的上边界，
    // 实际计算的时候的纵坐标用了外层的CardMainBorerLayout，需要将tab高度和参数面板高度减掉
    // 将y值变为相对坐标以实现获取到鼠标drop位置的控件
    // TODO 可以直接在这边将x，y都变成相对坐标，这样在后面判断拖进来的新控件放置方式的时候就不用再判断了
    private int adjustY(int y, XWTabFitLayout tabLayout) {
        XWCardLayout cardLayout = (XWCardLayout) tabLayout.getBackupParent();
        BorderPacker style = cardLayout.toData().getBorderStyle();
        y = y - this.getParaEditorYOffset();
        if (ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)) {
            y = y - WCardMainBorderLayout.TAB_HEIGHT;
        }
        return y;
    }

    protected Rectangle getLayoutBound(XWCardMainBorderLayout mainLayout) {
        return ComponentUtils.getRelativeBounds(mainLayout);
    }

}