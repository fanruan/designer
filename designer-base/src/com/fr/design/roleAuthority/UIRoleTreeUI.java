package com.fr.design.roleAuthority;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.gui.itree.UITreeUI;

import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-1-17
 * Time: 下午4:28
 */
public class UIRoleTreeUI extends UITreeUI {

    protected void selectPathForEvent(TreePath path, MouseEvent event) {    /* Adjust from the anchor point. */
        if (InputEventBaseOnOS.isControlDown(event) && tree.isPathSelected(path)) {
            tree.removeSelectionPath(path);
        } else if (event.isShiftDown()) {
            tree.setAnchorSelectionPath(null);
        }
        super.selectPathForEvent(path, event);
    }
}