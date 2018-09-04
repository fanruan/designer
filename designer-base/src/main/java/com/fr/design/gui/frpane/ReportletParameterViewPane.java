/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.frpane;

import com.fr.base.Parameter;
import com.fr.base.core.KV;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;

import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.List;

/**
 * Defin hyperlink.
 * in fact,this is a TablEditorPane
 *
 * @editor zhou
 * @since 2012-3-23下午3:48:10
 */
public class ReportletParameterViewPane extends BasicPane {
    private UITableEditorPane<ParameterProvider> editorPane;

    public ReportletParameterViewPane() {
        this(null, ParameterTableModel.NO_CHART_USE);
    }

    // kunsnat: 控制是否用Chart的热点链接actions
    public ReportletParameterViewPane(int useParaType) {
        this(null, useParaType);
    }

    public ReportletParameterViewPane(UITableEditAction[] actions, int useParaType) {
        this(actions, useParaType, ValueEditorPaneFactory.createVallueEditorPaneWithUseType(useParaType),
                ValueEditorPaneFactory.createVallueEditorPaneWithUseType(useParaType));
    }

    public ReportletParameterViewPane(int useParaType, ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane) {
        this(null, useParaType, valueEditorPane, valueRenderPane);
    }

    public ReportletParameterViewPane(UITableEditAction[] actions, int useParaType, ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane) {
        this.initComponent(actions, useParaType, valueEditorPane, valueRenderPane);
    }

    /**
     * 初始化组件
     *
     * @param actions     Chart的热点链接actions
     * @param useParaType 类型
     */
    public void initComponent(final UITableEditAction[] actions, int useParaType, ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        if (useParaType != ParameterTableModel.NO_CHART_USE) {
            ParameterTableModel model = new ParameterTableModel(valueEditorPane, valueRenderPane, this) {
                @Override
                public UITableEditAction[] createAction() {
                    UITableEditAction[] tableEditActions = new UITableEditAction[]{new AddChartParameterAction(), new DeleteAction(this.component),
                            new MoveUpAction(), new MoveDownAction()};
                    return (UITableEditAction[]) ArrayUtils.addAll(tableEditActions, actions);
                }
            };
            editorPane = new UITableEditorPane<ParameterProvider>(model);
        } else {
            editorPane = new UITableEditorPane<ParameterProvider>(new ParameterTableModel() {
                @Override
                public UITableEditAction[] createAction() {
                    return (UITableEditAction[]) ArrayUtils.addAll(super.createAction(), actions);
                }
            });
        }

        this.add(editorPane, BorderLayout.CENTER);
    }


    /**
     * 增加事件监听
     *
     * @param l 加的东东
     */
    public void addTableEditorListener(TableModelListener l) {
        editorPane.addTableListener(l);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameters");
    }

    public void populate(ParameterProvider[] parameters) {
        if (parameters == null) {
            return;
        }
        editorPane.populate(parameters);
    }

    public void populate(KV[] kv) {
        if (kv == null) {
            return;
        }
        Parameter[] parameters = new Parameter[kv.length];
        for (int i = 0; i < kv.length; i++) {
            parameters[i] = new Parameter(kv[i].getKey(), kv[i].getValue());
        }
        this.populate(parameters);
    }

    public List<ParameterProvider> update() {
        return editorPane.update();
    }

    public void update(List list) {
        editorPane.update(list);
    }


    /**
     * 更新
     *
     * @return 数组
     */
    public KV[] updateKV() {
        List<ParameterProvider> list = this.update();
        int length = list.size();
        KV[] kv = new KV[length];
        for (int i = 0; i < length; i++) {
            kv[i] = new KV();
            kv[i].setKey(list.get(i).getName());
            kv[i].setValue(list.get(i).getValue());
        }
        return kv;
    }


}