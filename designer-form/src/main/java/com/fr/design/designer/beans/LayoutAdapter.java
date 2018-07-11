package com.fr.design.designer.beans;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;

/**
 * 该接口是LayoutManager的BeanInfo类。标准Java平台没有提供布局管理器的BeanInfo类，
 * 对于界面设计工具来说还需一些特殊的行为。
 *
 * @since 6.5.3
 */
public interface LayoutAdapter {

    /**
     * 在添加组件状态时，当鼠标移动到某个容器上方时，如果该容器有布局管理器，则会调用该布局
     * 管理适配器的accept来决定当前位置是否可以放置，并提供特殊的标识，比如红色区域标识。比
     * 如在BorderLayout中，如果某个方位已经放置了组件，则此时应该返回false标识该区域不可以
     * 放置。
     *
     * @param creator 组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否可以放置
     */
    boolean accept(XCreator creator, int x, int y);

    /**
     * 有的控件在拖拽调整大小后需要根据自身内容重新计算下当前的尺寸是否合适，如果不合适，就需要重新fix一下
     *
     * @param creator 组件
     */
    void fix(XCreator creator);

    /**
     * 组件的ComponentAdapter在添加组件时，如果发现布局管理器不为空，会继而调用该布局管理器的
     * addComp方法来完成组件的具体添加。在该方法内，布局管理器可以提供额外的功能。
     *
     * @param creator 被添加的新组件
     * @param x       添加的位置x，该位置是相对于container的
     * @param y       添加的位置y，该位置是相对于container的
     * @return 是否添加成功，成功返回true，否则false
     */
    boolean addBean(XCreator creator, int x, int y);

    /**
     * 返回该布局管理适配器的Painter，为容器提供放置位置的标识。
     */
    HoverPainter getPainter();

    /**
     * 显示parent的字组件child，解决CardLayout中显示某个非显示组件的特殊情况
     *
     * @param child 组件
     */
    void showComponent(XCreator child);

    void addNextComponent(XCreator dragged);

    /**
     * 组件叠放顺序前插入
     *
     * @param target 目标组件
     * @param added  插入组件
     */
    void addBefore(XCreator target, XCreator added);

    /**
     * 组件叠放顺序后插入
     *
     * @param target 目标组件
     * @param added  放置组件
     */
    void addAfter(XCreator target, XCreator added);

    /**
     * 能否放置更多组件
     *
     * @return 能则返回true
     */
    boolean canAcceptMoreComponent();

    ConstraintsGroupModel getLayoutConstraints(XCreator creator);

    GroupModel getLayoutProperties();

    /**
     * 删除组件
     *
     * @param creator    组件
     * @param initWidth  组件之前宽度
     * @param initHeight 组件之前高度
     */
    void removeBean(XCreator creator, int initWidth, int initHeight);
}