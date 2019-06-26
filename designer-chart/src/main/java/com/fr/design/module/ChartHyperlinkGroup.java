package com.fr.design.module;

import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.BaseHyperlinkGroup;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScript;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Filter;
import com.fr.stable.bridge.StableFactory;
import org.jetbrains.annotations.NotNull;

/**
 * 图表的超级链接界面. 比一般的HyperlinkGroupPane多了图表的相关超级链接
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-26 下午04:55:10
 */
public class ChartHyperlinkGroup extends BaseHyperlinkGroup {

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
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Float"), ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Hyperlink_Form_Link"), formHyperlink.getClass(), FormHyperlinkPane.class)};
        return ArrayUtils.addAll(super.getHyperlinkCreators(), related4ChartHyper);

    }

    @Override
    public Filter<Class<? extends JavaScript>> getFilter() {
        return new Filter<Class<? extends JavaScript>>() {
            @Override
            public boolean accept(Class<? extends JavaScript> clazz) {
                JTemplate template = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
                if (template == null) {
                    return false;
                }

                if (template.isJWorkBook()) {
                    // 如果是普通报表单元格，那么没有 FormHyperlink 选项
                    FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
                    //返回true表示可用，返回false表示不可用
                    return !ComparatorUtils.equals(clazz, formHyperlink.getClass());

                } else {
                    // 如果是决策报表
                    Class[] classes = new Class[]{ChartHyperRelateCellLink.class, ChartHyperRelateFloatLink.class};
                    for (Class aClass : classes) {
                        if (template.getEditingReportIndex() == BaseJForm.FORM_TAB) {
                            // 编辑的是决策报表中的图表，那么没有ChartHyperRelateFloatLink 和 ChartHyperRelateCellLink 选项，有FormHyperlink 选项
                            if (ComparatorUtils.equals(aClass, clazz)) {
                                return false;
                            }
                        } else if (template.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB) {
                            // 编辑的是决策报表中的报表块，那么没有 ChartHyperRelateFloatLink，有ChartHyperRelateCellLink 和 FormHyperlink 选项
                            return !ComparatorUtils.equals(clazz, ChartHyperRelateFloatLink.class);
                        }
                    }
                }
                return true;
            }
        };
    }

    @Override
    public Filter<Object> getOldFilter() {
        return new Filter<Object>() {
            @Override
            public boolean accept(Object object) {
                JTemplate template = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
                if (template == null) {
                    return false;
                }
                if (template.isJWorkBook()) {
                    // 如果是普通报表单元格，那么没有 FormHyperlink 选项
                    FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
                    //返回true表示可用，返回false表示不可用
                    return !ComparatorUtils.equals(object.getClass(), formHyperlink.getClass());
                } else {
                    // 如果是决策报表
                    Class[] classes = new Class[]{ChartHyperRelateCellLink.class, ChartHyperRelateFloatLink.class};
                    for (Class aClass : classes) {
                        if (template.getEditingReportIndex() == BaseJForm.FORM_TAB) {
                            // 编辑的是决策报表中的图表，那么没有ChartHyperRelateFloatLink 和 ChartHyperRelateCellLink 选项，有FormHyperlink 选项
                            if (ComparatorUtils.equals(aClass, object.getClass())) {
                                return false;
                            }
                        } else if (template.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB) {
                            // 编辑的是决策报表中的报表块，那么没有 ChartHyperRelateFloatLink，有ChartHyperRelateCellLink 和 FormHyperlink 选项
                            return !ComparatorUtils.equals(object.getClass(), ChartHyperRelateFloatLink.class);
                        }
                    }
                }
                return true;
            }
        };
    }
}
