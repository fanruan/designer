package com.fr.plugin.chart.custom.component;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UIListControlPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.other.HyperlinkMapFactory;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;
import com.fr.stable.bridge.StableFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartHyperLinkPane extends UIListControlPane {

    public VanChartHyperLinkPane() {
        super();
    }

    @Override
    public NameableCreator[] createNameableCreators() {

        //面板初始化，需要在populate的时候更新
        Map<String, NameableCreator> nameCreators = new ListMap<>();
        NameableCreator[] creators = DesignModuleFactory.getHyperlinkGroupType().getHyperlinkCreators();
        for (NameableCreator creator : creators) {
            nameCreators.put(creator.menuName(), creator);
        }
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            nameCreators.put(nc.menuName(), nc);
        }
        return nameCreators.values().toArray(new NameableCreator[nameCreators.size()]);
    }


    /**
     * 弹出列表的标题.
     *
     * @return 返回标题字符串.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Hyperlink");
    }

    @Override
    protected String getAddItemText() {
        return Inter.getLocText("FR-Designer_Add_Hyperlink");
    }

    public void populate(NameJavaScriptGroup nameHyperlink_array) {
        java.util.List<NameObject> list = new ArrayList<NameObject>();
        if (nameHyperlink_array != null) {
            for (int i = 0; i < nameHyperlink_array.size(); i++) {
                list.add(new NameObject(nameHyperlink_array.getNameHyperlink(i).getName(), nameHyperlink_array.getNameHyperlink(i).getJavaScript()));
            }
        }

        this.populate(list.toArray(new NameObject[list.size()]));
    }

    public void populate(TargetComponent elementCasePane) {
    }

    /**
     * updateJs的Group
     *
     * @return 返回NameJavaScriptGroup
     */
    public NameJavaScriptGroup updateJSGroup() {
        Nameable[] res = this.update();
        NameJavaScript[] res_array = new NameJavaScript[res.length];
        for (int i = 0; i < res.length; i++) {
            NameObject no = (NameObject) res[i];
            res_array[i] = new NameJavaScript(no.getName(), (JavaScript) no.getObject());
        }

        return new NameJavaScriptGroup(res_array);
    }

    @Override
    public void saveSettings() {
        if (isPopulating) {
            return;
        }
        update((VanChartPlot) plot);
    }

    public void populate(Plot plot) {
        //特殊处理，使用instanceof判断，处理连续弹窗问题
        if (SwingUtilities.getWindowAncestor(this) instanceof JDialog) {
            popupEditDialog = new HyperDialog(cardPane);
        }
        this.plot = plot;
        HashMap paneMap = getHyperlinkMap(plot);

        //安装平台内打开插件时,添加相应按钮
        Set<HyperlinkProvider> providers = ExtraDesignClassManager.getInstance().getArray(HyperlinkProvider.XML_TAG);
        for (HyperlinkProvider provider : providers) {
            NameableCreator nc = provider.createHyperlinkCreator();
            //todo@shine9.0
            // paneMap.put(nc.getHyperlink(), nc.getUpdatePane());
        }

        java.util.List<UIMenuNameableCreator> list = refreshList(paneMap);
        NameObjectCreator[] creators = new NameObjectCreator[list.size()];
        for (int i = 0; list != null && i < list.size(); i++) {
            UIMenuNameableCreator uiMenuNameableCreator = list.get(i);
            creators[i] = new NameObjectCreator(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj().getClass(), uiMenuNameableCreator.getPaneClazz());

        }

        refreshNameableCreator(creators);

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();

        NameJavaScriptGroup nameGroup = populateHotHyperLink(plot);
        for (int i = 0; nameGroup != null && i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if (javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                UIMenuNameableCreator uiMenuNameableCreator = new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass()));
                nameObjects.add(new NameObject(uiMenuNameableCreator.getName(), uiMenuNameableCreator.getObj()));

            }
        }

        this.populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
        return plot.getHotHyperLink();
    }

    protected HashMap getHyperlinkMap(Plot plot) {
        return HyperlinkMapFactory.getHyperlinkMap(plot);
    }

    public void update(Plot plot) {

        NameJavaScriptGroup nameGroup = updateNameGroup();

        updateHotHyperLink(plot, nameGroup);
    }

    protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
        plot.setHotHyperLink(nameGroup);
    }

    private NameJavaScriptGroup updateNameGroup() {
        Nameable[] nameables = update();

        NameJavaScriptGroup nameGroup = new NameJavaScriptGroup();
        nameGroup.clear();

        for (int i = 0; i < nameables.length; i++) {
            JavaScript javaScript = (JavaScript) ((NameObject) nameables[i]).getObject();
            String name = nameables[i].getName();
            NameJavaScript nameJava = new NameJavaScript(name, javaScript);
            nameGroup.addNameHyperlink(nameJava);
        }

        return nameGroup;
    }


    protected java.util.List<UIMenuNameableCreator> refreshList(HashMap map) {
        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), ChartEmailPane.class));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Web"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Dynamic_Parameters"),
                new ParameterJavaScript(), getUseMap(map, ParameterJavaScript.class)));
        list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Float_Chart"),
                new ChartHyperPoplink(), getUseMap(map, ChartHyperPoplink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Cell"),
                new ChartHyperRelateCellLink(), getUseMap(map, ChartHyperRelateCellLink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Float"),
                new ChartHyperRelateFloatLink(), getUseMap(map, ChartHyperRelateFloatLink.class)));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Form"),
                hyperlink, getUseMap(map, FormHyperlinkProvider.class)));

        return list;
    }

    protected Class<? extends BasicBeanPane> getUseMap(HashMap map, Object key) {
        if (map.get(key) != null) {
            return (Class<? extends BasicBeanPane>) map.get(key);
        }
        //引擎在这边放了个provider,当前表单对象
        for (Object tempKey : map.keySet()) {
            if (((Class) tempKey).isAssignableFrom((Class) key)) {
                return (Class<? extends BasicBeanPane>) map.get(tempKey);
            }
        }
        return null;
    }


    protected void popupEditDialog(Point mousePos) {
        //特殊处理，处理连续弹窗情况
        if (SwingUtilities.getWindowAncestor(this) instanceof JDialog) {
            GUICoreUtils.centerWindow(popupEditDialog);
            popupEditDialog.setVisible(true);
            return;
        }
        super.popupEditDialog(mousePos);
    }



    // 点击"编辑"按钮，弹出面板
    protected class HyperDialog extends JDialog {
        private JComponent editPane;
        private static final int WIDTH = 570;
        private static final int HEIGHT = 490;

        private UIButton okButton, cancelButton;


        HyperDialog(JComponent pane) {
            super(DesignerContext.getDesignerFrame(), true);
            pane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            this.editPane = pane;
            JPanel editPaneWrapper = new JPanel(new BorderLayout());
            editPaneWrapper.add(editPane, BorderLayout.CENTER);
            this.getContentPane().add(editPaneWrapper, BorderLayout.CENTER);
            this.getContentPane().add(createControlButtonPane(), BorderLayout.SOUTH);
            setSize(WIDTH, HEIGHT);
            this.setVisible(false);
        }

        private JPanel createControlButtonPane() {
            JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

            JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            controlPane.add(buttonsPane, BorderLayout.EAST);

            //确定
            addOkButton(buttonsPane);
            //取消
            addCancelButton(buttonsPane);

            controlPane.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

            return controlPane;
        }

        private void addCancelButton(JPanel buttonsPane) {
            cancelButton = new UIButton(Inter.getLocText("Cancel"));
            buttonsPane.add(cancelButton);
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    doCancel();
                }
            });
        }

        private void addOkButton(JPanel buttonsPane) {
            okButton = new UIButton(Inter.getLocText("OK"));
            buttonsPane.add(okButton);
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    doOK();
                }
            });
        }

        public void doOK() {
            saveSettings();
            setVisible(false);
        }

        public void doCancel() {
            setVisible(false);
        }
    }

}
