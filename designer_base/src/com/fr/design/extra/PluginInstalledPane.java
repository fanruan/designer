package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        disableButton = new UIButton(Inter.getLocText("FR-Designer-Plugin_Disable"));
        disableButton.setEnabled(false);
        deleteButton = new UIButton(Inter.getLocText("FR-Designer-Plugin_Delete"));
        deleteButton.setEnabled(false);
        panel.add(disableButton);
        panel.add(deleteButton);
        controlPane.addPluginSelectionListener(new PluginSelectListener() {
            @Override
            public void valueChanged(Plugin plugin) {
                disableButton.setEnabled(true);
                deleteButton.setEnabled(true);
                changeTextForButton(plugin);
            }
        });
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Plugin plugin = controlPane.getSelectedPlugin();
                if (plugin != null) {
                    plugin.setActive(!plugin.isActive());
                    changeTextForButton(plugin);
                    try {
                        FRContext.getCurrentEnv().writePlugin(plugin);
                        int rv = JOptionPane.showOptionDialog(
                                PluginInstalledPane.this,
                                plugin.isActive() ? Inter.getLocText("FR-Designer-Plugin_Has_Been_Actived") : Inter.getLocText("FR-Designer-Plugin_Has_Been_Disabled"),
                                Inter.getLocText("FR-Designer-Plugin_Warning"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"), Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")},
                                null
                        );
                        if (rv == JOptionPane.OK_OPTION) {
                            RestartHelper.restart();
                        }
                    } catch (Exception e1) {
                        FRContext.getLogger().error(e1.getMessage(), e1);
                    }
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDelete(controlPane.getSelectedPlugin());
            }
        });

        PluginLoader loader = PluginLoader.getLoader();
        Plugin[] plugins = loader.getInstalled();
        controlPane.loadPlugins(plugins);
        num = plugins.length;
    }

    /**
     * tab标题
     * @return 同上
     */
    public String tabTitle() {
        return Inter.getLocText("FR-Designer-Plugin_Installed") + "(" + num + ")";
    }

    private void doDelete(Plugin plugin) {
        int rv = JOptionPane.showOptionDialog(
                PluginInstalledPane.this,
                Inter.getLocText("FR-Designer-Plugin_Will_Be_Delete"),
                Inter.getLocText("FR-Designer-Plugin_Warning"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                        Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later"),
                        Inter.getLocText("FR-Designer-Basic_Cancel")
                },
                null
        );
        if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
            return;
        }
        
        try {
            String[] filesToBeDelete = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), plugin);
            controlPane.deletePlugin(plugin);
            RestartHelper.saveFilesWhichToDelete(filesToBeDelete);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PluginInstalledPane.this, e.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
        
        if (rv == JOptionPane.OK_OPTION) {
            RestartHelper.restart();
        }
    }

    private void changeTextForButton(Plugin plugin) {
        if (plugin.isActive()) {
            disableButton.setText(Inter.getLocText("FR-Designer-Plugin_Disable"));
        } else {
            disableButton.setText(Inter.getLocText("FR-Designer-Plugin_Active"));
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "Installed";
    }
}