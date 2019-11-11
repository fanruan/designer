package com.fr.design.module;

import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.BaseHyperlinkGroup;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-8-20
 * Time: 下午5:15
 */
public class FormHyperlinkGroup extends BaseHyperlinkGroup {

    /**
     * 返回支持的超级链接类型
     *
     * @return NameableCreator[]
     */
    @NotNull
    @Override
    public NameableCreator[] getHyperlinkCreators() {
        FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        NameableCreator[] related4ChartHyper = {
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Float_Chart"), ChartHyperPoplink.class, ChartHyperPoplinkPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Cell"), ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Hyperlink_Form_Link"), formHyperlink.getClass(), FormHyperlinkPane.class)};
        return ArrayUtils.addAll(super.getHyperlinkCreators(), related4ChartHyper);

    }
}
