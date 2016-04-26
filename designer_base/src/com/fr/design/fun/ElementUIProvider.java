package com.fr.design.fun;

import com.fr.design.actions.UpdateAction;
import com.fr.design.selection.QuickEditor;
import com.fr.stable.fun.Level;


/**
 * Created by richie on 16/4/25.
 */
public interface ElementUIProvider extends Level {

    String MARK_STRING = "ElementUIProvider";

    int CURRENT_LEVEL = 1;

    Class<?> targetCellEditorClass();

    Class<?> targetObjectClass();

    QuickEditor<?> quickEditor();

    Class<? extends UpdateAction> actionForInsertCellElement();

    Class<? extends UpdateAction> actionForInsertFloatElement();
}
