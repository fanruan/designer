package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.CellExpandAttrPanelProvider;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.event.EventDispatcher;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.report.cell.TemplateCellElement;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.fr.plugin.observer.PluginEventType.AfterRun;
import static com.fr.plugin.observer.PluginEventType.AfterStop;

/**
 * @author yaohwu
 * created by yaohwu at 2020/4/28 10:01
 */
public class CellExpandExtraAttrPane extends JPanel {

    private static final double V_GAP = 5;
    private static final double H_GAP = 0;
    private static final int DEFAULT_COMPONENT_SIZE = 2;

    private List<BasicBeanPane<TemplateCellElement>> extras = null;
    private TemplateCellElement cellElement = null;


    private static final class Holder {
        private static final CellExpandExtraAttrPane INSTANCE = new CellExpandExtraAttrPane();
    }

    static {
        PluginFilter filter = new PluginFilter() {
            @Override
            public boolean accept(PluginContext pluginContext) {
                return pluginContext.contain(PluginModule.ExtraDesign, CellExpandAttrPanelProvider.MARK_STRING);
            }
        };
        PluginEventListener listener = new PluginEventListener() {
            @Override
            public void on(PluginEvent event) {
                CellExpandExtraAttrPane.getInstance().refresh();
            }
        };
        EventDispatcher.listen(AfterRun, listener, filter);
        EventDispatcher.listen(AfterStop, listener, filter);
    }

    public static CellExpandExtraAttrPane getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    private CellExpandExtraAttrPane() {
        this.setBorder(null);
        this.setLayout(new BorderLayout());
    }

    public void populate(TemplateCellElement cellElement) {
        this.cellElement = cellElement;
        if (extras != null) {
            for (BasicBeanPane<TemplateCellElement> extra : extras) {
                extra.populateBean(cellElement);
            }
        }
    }

    public void update(TemplateCellElement cellElement) {
        this.cellElement = cellElement;
        if (extras != null) {
            for (BasicBeanPane<TemplateCellElement> extra : extras) {
                extra.updateBean(cellElement);
            }
        }
    }

    private void refresh() {
        this.removeAll();
        if (extras == null) {
            extras = new ArrayList<>();
        }
        extras.clear();
        Set<CellExpandAttrPanelProvider> attrProviders = ExtraDesignClassManager.getInstance().getArray(CellExpandAttrPanelProvider.MARK_STRING);
        if (attrProviders != null) {
            for (CellExpandAttrPanelProvider attrProvider : attrProviders) {
                BasicBeanPane<TemplateCellElement> extra = attrProvider.createPanel();
                if (extra != null) {
                    extras.add(extra);
                }
            }
        }
        Component[][] components = new Component[extras.size()][DEFAULT_COMPONENT_SIZE];

        for (int i = 0; i < extras.size(); i++) {
            components[i] = new Component[]{extras.get(i), null};
        }
        double[] rowSize = new double[extras.size()];
        Arrays.fill(rowSize, TableLayout.PREFERRED);
        double[] columnSize = {TableLayout.PREFERRED, TableLayout.FILL};
        JPanel content = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, H_GAP, V_GAP);
        this.add(content, BorderLayout.CENTER);
        if (this.cellElement != null) {
            this.populate(cellElement);
        }
        this.validate();
        this.repaint();
    }

}
