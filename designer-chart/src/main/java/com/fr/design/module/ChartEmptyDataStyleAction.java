package com.fr.design.module;

import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.IOUtils;


import java.awt.event.ActionEvent;

/**
 * Created by mengao on 2017/11/23.
 * 空数据配置action
 */
public class ChartEmptyDataStyleAction extends UpdateAction {

    public ChartEmptyDataStyleAction() {
        this.setSmallIcon(IOUtils.readIcon("com/fr/design/images/EmptyChart.png"));
        this.setName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Chart_Empty_Data"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final ChartEmptyDataStylePane pane = new ChartEmptyDataStylePane();
        BasicDialog dialog = pane.showWindow(designerFrame);
        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                pane.updateBean();

            }

            @Override
            public void doCancel() {
                //直接关闭弹出框
            }
        });

        pane.populateBean();
        dialog.setVisible(true);
    }

}
