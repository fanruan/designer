package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.mobile.ui.TemplateStyleDefinePaneFactory;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.MobileTemplateStyleType;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileTemplateStylePane extends AbstractTemplateStylePane<MobileTemplateStyle> {
    private static final List<MobileTemplateStyleType> STYLE_LIST = new ArrayList<MobileTemplateStyleType>();
    static {
        STYLE_LIST.add(MobileTemplateStyleType.DEFAULT_STYLE);
        STYLE_LIST.add(MobileTemplateStyleType.UP_MENU_STYLE);
        STYLE_LIST.add(MobileTemplateStyleType.DOWN_MENU_STYLE);
        STYLE_LIST.add(MobileTemplateStyleType.SLIDER_STYLE);
    }

    private DefaultListModel listModel;
    private JList styleList;
    private Map<String, BasicBeanPane<MobileTemplateStyle>> map = new HashMap<>();
    private JPanel right;
    private CardLayout card;
    public MobileTemplateStylePane(WCardTagLayout tagLayout){
        init(tagLayout);
    }

    public void init(WCardTagLayout tagLayout){
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        listModel = new DefaultListModel();
        card = new CardLayout();
        right = FRGUIPaneFactory.createCardLayout_S_Pane();
        right.setLayout(card);
        for(MobileTemplateStyleType style : STYLE_LIST){
            listModel.addElement(style.getDisplayName());
            BasicBeanPane<MobileTemplateStyle> styleBasicBeanPane = TemplateStyleDefinePaneFactory.createDefinePane(style.getStyle(), tagLayout);
            map.put(style.getDisplayName(), styleBasicBeanPane);
            right.add(style.getDisplayName(), styleBasicBeanPane);
        }
        styleList = new JList(listModel);
        styleList.setCellRenderer(render);

        JPanel westPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        westPane.add(styleList, BorderLayout.CENTER);
        westPane.setPreferredSize(new Dimension(100, 500));


        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel attrConfPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setPreferredSize(new Dimension(500, 500));
        attrConfPane.add(right, BorderLayout.CENTER);
        centerPane.add(attrConfPane, BorderLayout.CENTER);
        styleList.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                String selectedValue = (String)styleList.getSelectedValue();
                card.show(right, selectedValue);
            }
        });
        this.add(westPane, BorderLayout.WEST);
        this.add(centerPane, BorderLayout.CENTER);
    }
    public static ListCellRenderer render = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof MobileTemplateStyle) {
                MobileTemplateStyle l = (MobileTemplateStyle) value;
                this.setText(l.toString());
            }
            return this;
        }
    };

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template");
    }

    public void populate(MobileTemplateStyle templateStyle) {
        for(int i = 0; i< listModel.getSize(); i++){
            String style = templateStyle.getStyle();
            MobileTemplateStyleType templateStyleType = MobileTemplateStyleType.parse(style);
            if((listModel.getElementAt(i)).equals(templateStyleType.getDisplayName())){
                styleList.setSelectedIndex(i);
                map.get(templateStyle.toString()).populateBean(templateStyle);
                card.show(right, templateStyle.toString());
                return;
            }
        }
        styleList.setSelectedIndex(0);
    }

    public MobileTemplateStyle update() {
        return map.get(styleList.getSelectedValue()).updateBean();
    }
}
