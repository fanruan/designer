package com.fr.design.dialog;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 封装了"为该模版单独设置、采用服务器设置"选项功能的设置面板
 * Created by plough on 2018/11/7.
 */
public abstract class AbstractTemplateServerSettingPane extends BasicPane {
    private static final String[] CHOOSEITEM = new String[] {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_I_Want_To_Set_Single"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Using_Server_Report_View_Settings")
    };

    protected static final int SINGLE_SET = 0;
    protected static final int SERVER_SET = 1;

    protected UIComboBox chooseComboBox;
    protected JPanel buttonPane;
    private JPanel contentPane;

    protected AbstractTemplateServerSettingPane() {
        initComponents();
    }

    private void initComponents() {
        chooseComboBox = new UIComboBox(CHOOSEITEM);
        chooseComboBox.addItemListener(itemListener);
        UILabel belowSetLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Blow_Set"));
        belowSetLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        buttonPane = GUICoreUtils.createFlowPane(new Component[] {
                belowSetLabel, chooseComboBox}, FlowLayout.LEFT, 0, 0);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        this.setLayout(new BorderLayout());
        this.add(buttonPane, BorderLayout.NORTH);
        this.contentPane = getContentPane();
        this.add(contentPane, BorderLayout.CENTER);
    }

    /**
     * 包含设置项的主面板
     */
    protected abstract JPanel getContentPane();

    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (isUsingServerSettings()) {
                    populateServerSettings();
                    setSettingPaneEnabled(false);
                } else {
                    setSettingPaneEnabled(true);
                }
            }
        }
    };

    private void setSettingPaneEnabled(boolean enabled) {
        // GUICoreUtils.setEnabled 会遍历所有 Component。所以要先设置外层，如果是生效的，再设置内层
        GUICoreUtils.setEnabled(contentPane, enabled);
        if (enabled) {
            checkContentPaneEnabled();
        }
    }

    protected boolean isUsingServerSettings() {
        return chooseComboBox.getSelectedIndex() == SERVER_SET;
    }

    /**
     * 整个配置面板设置为可用后，可能还需要检测面板中部分区域的可用性
     */
    protected void checkContentPaneEnabled() {
        // do nothing
    }

    /**
     * 读取服务器配置并 populate 到主面板中
     */
    protected abstract void populateServerSettings();
}
