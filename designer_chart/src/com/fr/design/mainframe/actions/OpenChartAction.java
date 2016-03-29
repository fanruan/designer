package com.fr.design.mainframe.actions;

import com.fr.design.actions.file.OpenTemplateAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.FILEChooserPane4Chart;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-20
 * Time: 下午7:35
 */
public class OpenChartAction extends OpenTemplateAction {
    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
        FILEChooserPane fileChooser = FILEChooserPane4Chart.getInstance(true, true);

        if (fileChooser.showOpenDialog(DesignerContext.getDesignerFrame(),".crt")
                == FILEChooserPane.OK_OPTION) {
            final FILE file = fileChooser.getSelectedFILE();
            if (file == null) {//选择的文件不能是 null
                return;
            }
            DesignerContext.getDesignerFrame().openTemplate(file);
        }
    }
}