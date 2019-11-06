package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.general.cardtag.BannerTemplateStyle;
import com.fr.general.cardtag.BookMarkTemplateStyle;
import com.fr.general.cardtag.CardTemplateStyle;
import com.fr.general.cardtag.DefaultTemplateStyle;
import com.fr.general.cardtag.MenuTemplateStyle;
import com.fr.general.cardtag.PentagonTemplateStyle;
import com.fr.general.cardtag.TemplateStyle;
import com.fr.general.cardtag.TrapezoidTemplateStyle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by kerry on 2017/11/23.
 */
public class TemplateStylePane extends AbstractTemplateStylePane<TemplateStyle> {
    private DefaultListModel listModel;
    private JList styleList;
    private TemplateStylePreviewPane previewPane = new TemplateStylePreviewPane(new DefaultTemplateStyle(), new Rectangle(0, 50, 540, 400));

    public TemplateStylePane(){
        init();
    }

    public void init(){
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        listModel = new DefaultListModel();
        listModel.addElement(new DefaultTemplateStyle());
        listModel.addElement(new CardTemplateStyle());
        listModel.addElement(new BannerTemplateStyle());
        listModel.addElement(new BookMarkTemplateStyle());
        listModel.addElement(new MenuTemplateStyle());
        listModel.addElement(new PentagonTemplateStyle());
        listModel.addElement(new TrapezoidTemplateStyle());
        styleList = new JList(listModel);
        styleList.setCellRenderer(render);

        JPanel westPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        westPane.add(styleList, BorderLayout.CENTER);
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        westPane.setPreferredSize(new Dimension(100, 500));
        centerPane.setPreferredSize(new Dimension(300, 500));
        centerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"), null));
        centerPane.add(previewPane);
        styleList.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                previewPane.repaint((TemplateStyle) styleList.getSelectedValue());
            }
        });
        this.add(westPane, BorderLayout.WEST);
        this.add(centerPane, BorderLayout.CENTER);
    }
    public static ListCellRenderer render = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof TemplateStyle) {
                TemplateStyle l = (TemplateStyle) value;
                this.setText(l.toString());
            }
            return this;
        }
    };

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template");
    }

    public void populate(TemplateStyle templateStyle) {
        previewPane.repaint(templateStyle);
        for(int i = 0; i< listModel.getSize(); i++){
            if((listModel.getElementAt(i).toString()).equals(templateStyle.toString())){
                styleList.setSelectedIndex(i);
                return;
            }
        }
        styleList.setSelectedIndex(0);
    }

    public TemplateStyle update() {
        return (TemplateStyle) styleList.getSelectedValue();
    }
}
