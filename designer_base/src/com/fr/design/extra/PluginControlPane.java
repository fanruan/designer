package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.UIList;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLicense;
import com.fr.plugin.PluginLicenseManager;
import com.fr.plugin.PluginLoader;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author richie
 * @date 2015-03-09
 * @since 8.0
 */
public class PluginControlPane extends BasicPane {
    private UIList pluginList;
    private DefaultListModel listModel;
    private PluginDetailPane detailPane;
    private java.util.List<PluginSelectListener> listeners = new ArrayList<PluginSelectListener>();
    private Plugin[] plugins;
    private UITextField searchTextField;

    public PluginControlPane() {
        setLayout(new BorderLayout());
        searchTextField = new UITextField();
        searchTextField.setColumns(20);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                doSearch(searchTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doSearch(searchTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                doSearch(searchTextField.getText());
            }
        });
        add(GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("FR-Designer-Plugin_Search") + ":"), searchTextField, FlowLayout.LEFT), BorderLayout.NORTH);

        pluginList = new UIList();
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Plugin) {
                    PluginLicense pluginLicense = PluginLicenseManager.getInstance().getPluginLicenseByID(((Plugin) value).getId());
                    String extraInfo = "";
                    if (pluginLicense.isJarDamage()) {
                        extraInfo = "(" + Inter.getLocText("FR-Plugin-Plugin_Damaged") + ")";
                    } else if (pluginLicense.getLeftTime() != -1) {
                        if (pluginLicense.isAvailable()) {
                            extraInfo = "(" + (pluginLicense.isTrial() ? Inter.getLocText("FR-Plugin-Designer_Trial") : Inter.getLocText("FR-Plugin-Designer_Authorized")) + pluginLicense.getLeftTime() + Inter.getLocText("FR-Plugin-Designer_Left") + ")";
                        } else {
                            extraInfo = "(" + (pluginLicense.isTrial() ? Inter.getLocText("FR-Plugin-Designer_Trial") : Inter.getLocText("FR-Plugin-Designer_Authorized")) + Inter.getLocText("FR-Plugin-Designer_Expired") + ")";
                        }
                    }
                    setText(((Plugin) value).getName() + extraInfo);
                    setIcon(IOUtils.readIcon("/com/fr/design/images/server/plugin.png"));
                }
                return this;
            }
        };
        pluginList.setCellRenderer(renderer);
        listModel = new DefaultListModel();
        pluginList.setModel(listModel);
        JScrollPane jScrollPane = new JScrollPane(pluginList);


        PluginDescriptionLabel label = new PluginDescriptionLabel();
        label.setText(Inter.getLocText("FR-Designer-Plugin_Plugin"));
        JPanel leftPane = GUICoreUtils.createBorderLayoutPane(
                jScrollPane, BorderLayout.CENTER,
                label, BorderLayout.NORTH
        );
        detailPane = new PluginDetailPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, detailPane);
        splitPane.setDividerLocation(200);
        add(new UIScrollPane(splitPane));

        pluginList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Plugin plugin = (Plugin) pluginList.getSelectedValue();
                if (plugin != null) {
                    detailPane.populate(plugin);
                    for (PluginSelectListener l : listeners) {
                        l.valueChanged(plugin);
                    }
                } else {
                    detailPane.reset();
                }
            }
        });
    }

    public void addPluginSelectionListener(PluginSelectListener l) {
        listeners.add(l);
    }

    public void loadPlugins(Plugin[] plugins) {
        this.plugins = plugins;
        for (Plugin plugin : plugins) {
            listModel.addElement(plugin);
        }
    }

    private void doSearch(String text) {
        if (StringUtils.isNotBlank(text)) {
            listModel.clear();
            for (Plugin plugin : plugins) {
                if (plugin.match(text)) {
                    listModel.addElement(plugin);
                }
            }
        }
    }

    public Plugin getSelectedPlugin() {
        return (Plugin) pluginList.getSelectedValue();
    }

    public void deletePlugin(Plugin plugin) {
        listModel.removeElement(plugin);
        PluginLoader.getLoader().deletePlugin(plugin);
    }

    @Override
    protected String title4PopupWindow() {
        return "Plugin";
    }

}