package com.fr.design.javascript;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.JavaScriptActionProvider;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.event.Listener;
import com.fr.js.JavaScript;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListenerEditPane extends BasicBeanPane<Listener> {
    private UITextField nameText;
    private UIComboBox styleBox;
    private CardLayout card;
    private List<FurtherBasicBeanPane<? extends JavaScript>> cards;
    private JPanel hyperlinkPane;

    private static final String JS = Toolkit.i18nText("Fine-Design_Report_JavaScript");
    private static final String DBCOMMIT = Toolkit.i18nText("Fine-Design_Basic_JavaScript_Commit_To_Database");
    private static final String CUSTOMACTION = Toolkit.i18nText("Fine-Design_Report_Submit_Type_Custom");
    private static final String EMAIL = Toolkit.i18nText("Fine-Design_Report_Email_Sent_Email");

    private Listener listener;

    public ListenerEditPane() {
        this.initComponents(new String[0]);
    }

    public ListenerEditPane(String[] defaultArgs) {
        this.initComponents(defaultArgs);
    }

    /**
     * 初始化各个组件
     *
     * @param defaultArgs 初始化参数
     */
    public void initComponents(String[] defaultArgs) {
        cards = new ArrayList<>();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel namePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        nameText = new UITextField(8);
        nameText.setEditable(false);
        namePane.add(nameText, BorderLayout.WEST);
        final String[] style = {JS, DBCOMMIT, CUSTOMACTION, EMAIL};
        styleBox = new UIComboBox(style);
        namePane.add(styleBox);
        namePane = GUICoreUtils.createFlowPane(new Component[]{
                        new UILabel("  " + Toolkit.i18nText("Fine-Design_Report_Event_Name") + ":"),
                        nameText,
                        new UILabel("    " + Toolkit.i18nText("Fine-Design_Report_Event_Type") + ":"),
                        styleBox},
                FlowLayout.LEFT);
        namePane.setBorder(BorderFactory.createTitledBorder(Toolkit.i18nText("Fine-Design_Report_Event_Name_Type")));
        this.add(namePane, BorderLayout.NORTH);
        card = new CardLayout();
        hyperlinkPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        hyperlinkPane.setLayout(card);
        JavaScriptImplPane javaScriptPane = new JavaScriptImplPane(defaultArgs);
        hyperlinkPane.add(JS, javaScriptPane);
        // 提交入库
        List dbManiList = new ArrayList();
        dbManiList.add(autoCreateDBManipulationPane());
        Commit2DBJavaScriptPane commit2DBJavaScriptPane = new Commit2DBJavaScriptPane(JavaScriptActionPane.defaultJavaScriptActionPane,
                dbManiList);
        hyperlinkPane.add(DBCOMMIT, commit2DBJavaScriptPane);
        // 自定义事件
        CustomActionPane customActionPane = new CustomActionPane();
        hyperlinkPane.add(CUSTOMACTION, customActionPane);
        // 发送邮件
        EmailPane emailPane = new EmailPane();
        hyperlinkPane.add(EMAIL, emailPane);
        cards.add(javaScriptPane);
        cards.add(commit2DBJavaScriptPane);
        cards.add(customActionPane);
        cards.add(emailPane);
        //其他事件
        addOtherEvent();
        hyperlinkPane.setBorder(BorderFactory.createTitledBorder(Toolkit.i18nText("Fine-Design_Report_JavaScript_Set")));
        this.add(hyperlinkPane);
        styleBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object selected = styleBox.getSelectedItem();
                if (selected == null) {
                    return;
                }
                card.show(hyperlinkPane, selected.toString());
            }
        });
    }

    private void addOtherEvent() {
        Set<JavaScriptActionProvider> javaScriptActionProviders = ExtraDesignClassManager.getInstance().getArray(JavaScriptActionProvider.XML_TAG);
        if (javaScriptActionProviders != null) {
            for (JavaScriptActionProvider jsp : javaScriptActionProviders) {
                FurtherBasicBeanPane pane = jsp.getJavaScriptActionPane();
                String title = pane.title4PopupWindow();
                styleBox.addItem(title);
                hyperlinkPane.add(title, pane);
                cards.add(pane);
            }
        }
    }

    /**
     * 根据有无单元格创建 DBManipulationPane
     *
     * @return 有单元格。有智能添加单元格等按钮，返回 SmartInsertDBManipulationPane
     */
    private DBManipulationPane autoCreateDBManipulationPane() {
        JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        return jTemplate.createDBManipulationPane();
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Report_Event_Set");
    }

    @Override
    public void populateBean(Listener listener) {
        if (listener == null){
            this.listener = new Listener();
            return;
        }
        this.listener = listener;
        this.nameText.setText(listener.getEventName());
        JavaScript js = listener.getAction();
        for (int i = 0; i < this.cards.size(); i++) {
            FurtherBasicBeanPane pane = cards.get(i);
            if (pane.accept(js)) {
                styleBox.setSelectedItem(pane.title4PopupWindow());
                card.show(hyperlinkPane, pane.title4PopupWindow());
                pane.populateBean(js);
                return;
            }
        }
    }

    public void checkValid() throws Exception {
        this.cards.get(this.styleBox.getSelectedIndex()).checkValid();
    }

    @Override
    public Listener updateBean() {
        this.listener.setEventName(this.nameText.getText());
        FurtherBasicBeanPane<? extends JavaScript> pane = this.cards.get(this.styleBox.getSelectedIndex());
        this.listener.setAction(pane.updateBean());
        return this.listener;
    }
}