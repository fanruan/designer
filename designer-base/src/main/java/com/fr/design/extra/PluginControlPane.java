package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.UIList;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.license.Licensed;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskCallback;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<PluginView> plugins;
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
        add(GUICoreUtils.createFlowPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Search") + ":"), searchTextField, FlowLayout.LEFT), BorderLayout.NORTH);

        pluginList = new UIList();
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PluginView) {
                    Licensed context = PluginManager.getContext(PluginMarker.read((PluginView) value));
                    if (context == null) {
                        return this;
                    }
                    String extraInfo = "";
                    if (context.isLicDamaged()) {
                        extraInfo = "(" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Plugin_Damaged") + ")";
                    } else if (!context.isFree()) {
                        if (context.isAvailable()) {
                            extraInfo = "(" + (context.isOnTrial() ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Trial") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Authorized")) + context.getLeftDays() + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Left") + ")";
                        } else {
                            extraInfo = "(" + (context.isOnTrial() ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Trial") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Authorized")) + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Designer_Expired") + ")";
                        }
                    }
                    setText(((PluginView) value).getName() + extraInfo);
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
        label.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Plugin"));
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
                PluginView plugin = (PluginView) pluginList.getSelectedValue();
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

    public void loadPlugins(java.util.List<PluginView> plugins) {
        this.plugins = plugins;
        for (PluginView plugin : plugins) {
            listModel.addElement(plugin);
        }
    }

    private void doSearch(String text) {
        if (StringUtils.isNotBlank(text)) {
            listModel.clear();
            for (PluginView plugin : plugins) {
                if (PluginUtils.isPluginMatch(plugin, text)) {
                    listModel.addElement(plugin);
                }
            }
        }
    }

    @Nullable
    public PluginView getSelectedPlugin() {
        return (PluginView) pluginList.getSelectedValue();
    }

    public void deletePlugin(PluginView plugin) {
        listModel.removeElement(plugin);
        String id = plugin.getID();
        String version = plugin.getVersion();
        PluginManager.getController().uninstall(PluginMarker.create(id, version), true, new PluginTaskCallback() {
            @Override
            public void done(PluginTaskResult result) {
                // do nothing
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return "Plugin";
    }

}