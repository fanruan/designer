package com.fr.design.extra;

import com.fr.design.RestartHelper;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ibutton.UIButton;

import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.view.PluginView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginInstalledPane extends PluginAbstractViewPane {

    private int num;
    private UIButton disableButton;
    private UIButton deleteButton;
    private PluginControlPane controlPane;


    public PluginInstalledPane() {
        setLayout(new BorderLayout());
        controlPane = new PluginControlPane();
        add(controlPane, BorderLayout.CENTER);

        JPanel panel = createOperationPane();

        add(panel, BorderLayout.SOUTH);

        disableButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Disable"));
        disableButton.setEnabled(false);
        deleteButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Delete"));
        deleteButton.setEnabled(false);
        panel.add(disableButton);
        panel.add(deleteButton);
        controlPane.addPluginSelectionListener(new PluginSelectListener() {
            @Override
            public void valueChanged(PluginView plugin) {
                disableButton.setEnabled(true);
                deleteButton.setEnabled(true);
                changeTextForButton(plugin);
            }
        });
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PluginView plugin = controlPane.getSelectedPlugin();
                if (plugin != null) {
                    boolean isActive = plugin.isActive();
                    PluginMarker pluginMarker = PluginMarker.create(plugin.getID(), plugin.getVersion());
                    final String modifyMessage = isActive ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Has_Been_Actived") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Has_Been_Disabled");
                    if (isActive) {
                        PluginManager.getController().forbid(pluginMarker, new PluginTaskCallback() {
                            @Override
                            public void done(PluginTaskResult result) {
                                if (result.isSuccess()) {
                                    FineJOptionPane.showMessageDialog(null, modifyMessage);
                                } else {
                                    FineJOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    } else {
                        PluginManager.getController().enable(pluginMarker, new PluginTaskCallback() {
                            @Override
                            public void done(PluginTaskResult result) {
                                if (result.isSuccess()) {
                                    FineJOptionPane.showMessageDialog(null, modifyMessage);
                                } else {
                                    FineJOptionPane.showMessageDialog(null,PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    }
                    changeTextForButton(plugin);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDelete(controlPane.getSelectedPlugin());
            }
        });

        List<PluginContext> plugins = PluginManager.getContexts();
        List<PluginView> pluginViews = new ArrayList<>();
        for (PluginContext plugin : plugins) {
            pluginViews.add((PluginView) plugin);
        }
        controlPane.loadPlugins(pluginViews);
        num = plugins.size();
    }

    /**
     * tab标题
     *
     * @return 同上
     */
    public String tabTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Installed") + "(" + num + ")";
    }

    private void doDelete(PluginView plugin) {
        int rv = JOptionPane.showOptionDialog(
                PluginInstalledPane.this,
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Will_Be_Delete"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Restart_Designer"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Restart_Designer_Later"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cancel")
                },
                null
        );
        if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
            return;
        }

        try {
            controlPane.deletePlugin(plugin);
        } catch (Exception e) {
            FineJOptionPane.showMessageDialog(PluginInstalledPane.this, e.getMessage(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }

        if (rv == JOptionPane.OK_OPTION) {
            RestartHelper.restart();
        }
    }

    private void changeTextForButton(PluginView plugin) {
        if (plugin.isActive()) {
            disableButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Disable"));
        } else {
            disableButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Active"));
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "Installed";
    }
}
