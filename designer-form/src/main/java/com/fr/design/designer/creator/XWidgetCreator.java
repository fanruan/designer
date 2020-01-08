/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Transparency;
import java.beans.IntrospectionException;

/**
 * @author richer
 * @since 6.5.3 com.fr.rpt.web.ui.Widget的设计组件
 */
public abstract class XWidgetCreator extends XCreator {

    protected static final float FULL_OPACITY = 1.0f;
    protected static final float HALF_OPACITY = 0.4f;

    public XWidgetCreator(Widget widget, Dimension initSize) {
        super(widget, initSize);
        setOpaque(false);
    }

    /**
     * 待说明
     *
     * @return 待说明
     * @throws IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form_Form_Widget_Name")),
                new CRPropertyDescriptor("enabled", this.data.getClass()).setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Enabled"))
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                    @Override
                    public void propertyChange() {
                        setEnabled(toData().isEnabled());
                    }
                }),
                new CRPropertyDescriptor("visible", this.data.getClass()).setI18NName(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible")).setPropertyChangeListener(new PropertyChangeAdapter() {

                    @Override
                    public void propertyChange() {
                        makeVisible(toData().isVisible());
                    }
                }),
                new CRPropertyDescriptor("labelName", this.data.getClass(), "getLabelName", "setLabelName")
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Label_Name"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
        };

    }

    /**
     * 待说明
     *
     * @return 待说明
     */
    public Widget toData() {
        return this.data;
    }

    /**
     * 根据Widget的属性值初始化XCreator的属性值
     */
    @Override
    protected void initXCreatorProperties() {
        this.setEnabled(toData().isEnabled());
    }

    /**
     * 待说明
     */
    public void recalculateChildrenSize() {
    }


    protected void makeVisible(boolean visible) {
    }

    public class LimpidButton extends JButton {
        private String name;
        private String imagePath;
        private float opacity = 0.4f;

        public LimpidButton(String name, String imagePath, float opacity) {
            this.name = name;
            this.imagePath = imagePath;
            this.opacity = opacity;
            this.draw();
        }

        public void draw() {
            try {
                ImageIcon imageIcon = (ImageIcon) BaseUtils.readIcon(imagePath);
                Image img = imageIcon.getImage();
                MediaTracker mt = new MediaTracker(this);
                int w = 21;
                int h = 21;
                mt.addImage(img, 0);
                mt.waitForAll();

                GraphicsConfiguration gc = new JFrame().getGraphicsConfiguration(); // 本地图形设备
                Image image = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);//建立透明画布
                Graphics2D g = (Graphics2D) image.getGraphics(); //在画布上创建画笔

                Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f); //指定透明度为半透明90%
                g.setComposite(alpha);
                g.drawImage(img, 0, 0, this); //注意是,将image画到g画笔所在的画布上
                g.setColor(Color.black);//设置颜色为黑色
                g.drawString(name, 25, 20);//写字
                g.dispose(); //释放内存

                Composite alpha2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
                Image image1 = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
                g = (Graphics2D) image1.getGraphics();
                g.setComposite(alpha2);
                g.drawImage(img, 2, 2, this); //改变图像起始位置,产生动态效果
                g.setColor(Color.black);
                g.drawString(name, 25, 20);
                g.dispose();

                this.setIgnoreRepaint(true);
                this.setFocusable(false);//设置没有焦点
                this.setBorder(null);//设置不画按钮边框
                this.setContentAreaFilled(false);//设置不画按钮背景
                this.setIcon(new ImageIcon(image1)); //把刚才生成的半透明image变成ImageIcon,贴到按钮上去
                this.setRolloverIcon(new ImageIcon(image1));
                this.setPressedIcon(new ImageIcon(image));//按下去的图标
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }

        /**
         * 待说明
         *
         * @param visible 待说明
         */
        public void makeVisible(boolean visible) {
            this.opacity = visible ? FULL_OPACITY : HALF_OPACITY;
            this.draw();
        }
    }

    /**
     * 渲染Painter
     */
    public void paint(Graphics g) {
        //不可见时，按钮.4f透明
        AlphaComposite composite = this.data.isVisible() ? (AlphaComposite) ((Graphics2D) g).getComposite() : AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HALF_OPACITY);
        ((Graphics2D) g).setComposite(composite);
        super.paint(g);
    }

    /**
     * 重命名
     *
     * @param designer 表单设计器
     * @param creator  当前组件
     */
    public void ChangeCreatorName(FormDesigner designer, XCreator creator) {
        String oldName = creator.toData().getWidgetName();
        String value = FineJOptionPane.showInputDialog(designer, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Change_Widget_Name_Discription"), oldName);
        if (value != null) {
            designer.renameCreator(creator, value);
        }
    }

}
