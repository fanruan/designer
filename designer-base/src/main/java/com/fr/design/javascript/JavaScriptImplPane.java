package com.fr.design.javascript;

import com.fr.base.Parameter;
import com.fr.design.data.tabledata.tabledatapane.OneListTableModel;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.js.JavaScriptImpl;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class JavaScriptImplPane extends AbstractHyperLinkPane<JavaScriptImpl> {
    private static final int BOTTOM_BORDER = 12;
    private UITextField itemNameTextField;
    private JSContentPane jsPane;
    private UITableEditorPane<String> importedJsPane;
    private ReportletParameterViewPane parameterPane;
    private String[] defaultArgs;


    public JavaScriptImplPane() {
        this(new String[0]);
    }

    public JavaScriptImplPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.defaultArgs = new String[0];
        initComponents();
    }


    public JavaScriptImplPane(String[] args) {
        this.defaultArgs = args;
        initComponents();
    }

    protected void initComponents() {
        parameterPane = new ReportletParameterViewPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        parameterPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter")));
        parameterPane.addTableEditorListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                List<ParameterProvider> list = parameterPane.update();
                HashSet tempSet = new HashSet();
                for (int i = 0; i < list.size(); i++) {
                    if (StringUtils.isEmpty(list.get(i).getName())) {
                        continue;
                    }
                    if (tempSet.contains(list.get(i).toString())) {
                        list.remove(i);
                        FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Duplicate_Name") + "!");
                        parameterChanger(list);
                        return;
                    }
                    tempSet.add(list.get(i).toString());
                }
                parameterChanger(list);
            }
        });

        OneListTableModel<String> model = new OneListTableModel<String>(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript"), this) {

            public UITableEditAction[] createAction() {
                return new UITableEditAction[]{getAddAction(), new DeleteAction(this.component), new MoveUpAction(), new MoveDownAction()};
            }

            @Override
            public UITableEditAction getAddAction() {
                return new AddJsAction();
            }
        };
        importedJsPane = new UITableEditorPane<String>(model);
        importedJsPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportServerP_Import_JavaScript")));
        importedJsPane.setPreferredSize(new Dimension(265, 150));
        jsPane = new JSContentPane(defaultArgs);
        jsPane.setBorder(BorderFactory.createTitledBorder(new ModLineBorder(ModLineBorder.TOP), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JavaScript")));

        parameterPane.setPreferredSize(new Dimension(265, 150));
        JPanel topPane = GUICoreUtils.createBorderLayoutPane(
                importedJsPane, BorderLayout.CENTER,
                parameterPane, BorderLayout.EAST
        );
        topPane.setPreferredSize(new Dimension(300, 150));
        topPane.setBorder(BorderFactory.createEmptyBorder(0, 0, BOTTOM_BORDER, 0));

        this.setLayout(new BorderLayout());
        this.add(topPane, BorderLayout.NORTH);
        this.add(jsPane, BorderLayout.CENTER);

        this.reLayoutForChart();
    }

    /**
     * 参数改变
     *
     * @param list 参数列表.
     */
    public void parameterChanger(List<ParameterProvider> list) {
        String[] name = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Parameter) {
                name[i] = list.get(i).getName();
            }
        }
        jsPane.setFunctionTitle(name, defaultArgs);
    }

    /**
     * title for popup window 弹出界面标题
     *
     * @return 标题.
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JavaScript");
    }

    /**
     * 重置
     */
    public void reset() {
        populateBean(null);
    }

    public void populateBean(JavaScriptImpl javaScriptImpl) {
        if (javaScriptImpl == null) {
            javaScriptImpl = new JavaScriptImpl();
            jsPane.reset();
        }else{
            jsPane.populate(javaScriptImpl.getContent());
        }

        int rowCount = javaScriptImpl.getJSImportSize();
        String[] value = new String[rowCount];
        for (int i = 0; i < rowCount; i++) {
            value[i] = javaScriptImpl.getJSImport(i);
        }
        importedJsPane.populate(value);
        parameterPane.populate(javaScriptImpl.getParameters());

        if (itemNameTextField != null) {
            itemNameTextField.setText(javaScriptImpl.getItemName());
        }
    }

    public JavaScriptImpl updateBean() {
        JavaScriptImpl javaScript = new JavaScriptImpl();
        updateBean(javaScript);
        return javaScript;
    }

    public void updateBean(JavaScriptImpl javaScript) {
        List<String> list = importedJsPane.update();
        javaScript.clearJSImports();
        for (int i = 0; i < list.size(); i++) {
            String a = list.get(i);
            javaScript.addJSImort(a);
        }

        List<ParameterProvider> parameterList = parameterPane.update();
        javaScript.setParameters(parameterList.toArray(new Parameter[parameterList.size()]));
        javaScript.setContent(jsPane.update());

        if (this.itemNameTextField != null) {
            javaScript.setItemName(itemNameTextField.getText());
        }
    }

    private GridBagConstraints setConstraints(int x, int y, int w, int h, double wx, double wy, GridBagConstraints c) {
        if (c == null) {
            return null;
        }
        c.gridx = x;
        c.gridy = y;
        c.gridheight = h;
        c.gridwidth = w;
        c.weightx = wx;
        c.weighty = wy;
        return c;
    }

    protected void reLayoutForChart() {
        if (needRenamePane()) {
            this.removeAll();
            itemNameTextField = new UITextField();

            JPanel topPane = GUICoreUtils.createBorderLayoutPane(
                    GUICoreUtils.createNamedPane(itemNameTextField, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Name") + ":"), BorderLayout.NORTH,
                    importedJsPane, BorderLayout.CENTER,
                    parameterPane, BorderLayout.EAST
            );
            topPane.setPreferredSize(new Dimension(300, 150));

            this.setLayout(new BorderLayout());
            this.add(topPane, BorderLayout.NORTH);
            this.add(jsPane, BorderLayout.CENTER);
            this.repaint();
        }
    }

    public static class ChartNoRename extends JavaScriptImplPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }

        protected boolean needRenamePane() {
            return false;
        }
    }

    /**
     * 判断类型
     *
     * @param ob 判断目标
     * @return 返回是否符合类型.
     */
    public boolean accept(Object ob) {
        return ob instanceof JavaScriptImpl;
    }
}
