package com.fr.van.chart.custom.component;

import com.fr.design.gui.HyperlinkFilterHelper;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.shortcutfactory.ShortCutFactory;
import com.fr.design.gui.controlpane.ShortCutListenerProvider;
import com.fr.design.menu.ShortCut;

/**
 * Created by plough on 2018/8/13.
 */
public class VanChartShortCutFactory extends ShortCutFactory {
    private VanChartShortCutFactory(ShortCutListenerProvider listenerProvider) {
        super(listenerProvider);
    }

    @Override
    public ShortCut createAddItemMenuDef(NameableCreator[] creators) {
        return new AddVanChartItemMenuDef(creators);
    }

    private class AddVanChartItemMenuDef extends AddItemMenuDef {

        AddVanChartItemMenuDef(NameableCreator[] creators) {
            super(creators);
        }

        @Override
        protected boolean whetherAdd(String itemName) {
            return HyperlinkFilterHelper.whetherAddHyperlink4Chart(itemName);
        }
    }
}
