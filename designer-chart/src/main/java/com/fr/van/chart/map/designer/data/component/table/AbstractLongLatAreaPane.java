package com.fr.van.chart.map.designer.data.component.table;

import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;

import javax.swing.JPanel;
import java.util.List;

/**
 * Created by hufan on 2016/12/21.
 */
public abstract class AbstractLongLatAreaPane extends JPanel {

    public abstract boolean isSelectedItem();

    public abstract void populate(VanMapTableDefinitionProvider mapTableDefinitionProvider);

    public abstract void update(VanMapTableDefinitionProvider mapTableDefinitionProvider);

    public abstract void refreshBoxListWithSelectTableData(List list);

    public abstract void checkBoxUse(boolean hasUse);

    public abstract void clearAllBoxList();


}
