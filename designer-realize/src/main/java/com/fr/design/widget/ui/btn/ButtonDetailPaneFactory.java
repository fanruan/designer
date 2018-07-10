package com.fr.design.widget.ui.btn;

import com.fr.base.FRContext;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:29
 */
public class ButtonDetailPaneFactory {
    private static Map<String, Class> detailMap = new HashMap<String, Class>();

    static {
        detailMap.put(Button.class.getName(), DefaultButtonDetailPane.class);
        detailMap.put(FreeButton.class.getName(), FreeButtonDetailPane.class);
        if (StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class) != null) {
            detailMap.put(StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class).getName(), DesignModuleFactory.getButtonDetailPaneClass());
        }
        detailMap.put(TreeNodeToggleButton.class.getName(), TreeNodeToogleButtonDefinePane.class);
        detailMap.put(AppendRowButton.class.getName(), AppendRowButtonDefinePane.class);
        detailMap.put(DeleteRowButton.class.getName(), DeleteRowButtonDefinePane.class);
    }

    public static ButtonDetailPane<Button> createButtonDetailPane(Button button) {
        Class cls = detailMap.get(button.getClass().getName());
        ButtonDetailPane<Button> detailPane = null;
        if (cls != null) {
            try {
                detailPane = (ButtonDetailPane) cls.newInstance();
                detailPane.populate(button);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return detailPane;
    }

    public static ButtonDetailPane<Button> createButtonDetailPane(Class cls, Button button) {
        if (cls == null) {
            return createButtonDetailPane(button);
        }
        Class aa = detailMap.get(cls.getName());
        ButtonDetailPane<Button> detailPane = null;
        if (aa != null) {
            try {
                detailPane = (ButtonDetailPane) aa.newInstance();
                detailPane.populate(button == null ? detailPane.createButton() : button);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return detailPane;
    }
}