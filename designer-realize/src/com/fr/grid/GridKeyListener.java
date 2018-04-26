package com.fr.grid;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @editor zhou
 * @since 2012-3-23上午10:55:36
 */
public class GridKeyListener implements KeyListener {
    private static final int DIFF = 48; // 103 - 55 = 48, 小键盘和大键盘数字的差值 48
    private static final int DELAY = 32;
    private Grid grid;
    // Keypressed last time
    private long keyPressedLastTime = 0;
    private boolean isKeyPressedContentChanged = false;

    public GridKeyListener(Grid grid) {
        this.grid = grid;
    }

    public void keyPressed(KeyEvent evt) {
        if (!grid.isEnabled() || evt.isConsumed()) {// 如果用户在自己的KyeListener里面consume了.就不执行下面的代码了.
            return;
        }
        if (KeyEventWork.processKeyEvent(evt) == null) {
            return;
        }
        long systemCurrentTime = System.currentTimeMillis();
        int code = evt.getKeyCode();
        boolean isNeedRepaint = false;
        ElementCasePane reportPane = grid.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        if (reportPane.getSelection() instanceof FloatSelection) {
            if (systemCurrentTime - keyPressedLastTime <= 2) {
                return;
            } else {
                keyPressedLastTime = systemCurrentTime;
            }
            dealWithFloatSelection(reportPane, code);
        } else {
            if (systemCurrentTime - keyPressedLastTime <= DELAY) {
                return;
            } else {
                keyPressedLastTime = systemCurrentTime;
            }
            dealWithCellSelection(evt, code);
        }
        switch (code) {
            case KeyEvent.VK_PAGE_UP: {// page up
                reportPane.getVerticalScrollBar().setValue(Math.max(0, grid.getVerticalValue() - grid.getVerticalExtent()));
                isNeedRepaint = true;
                break;
            }
            case KeyEvent.VK_PAGE_DOWN: {// page down
                reportPane.getVerticalScrollBar().setValue(grid.getVerticalValue() + grid.getVerticalExtent());
                isNeedRepaint = true;
                break;
            }
            // Richie:Ctrl + A全选单元格
            case KeyEvent.VK_A:
                if (InputEventBaseOnOS.isControlDown(evt)) {
                    reportPane.setSelection(new CellSelection(0, 0, report.getColumnCount(), report.getRowCount()));
                }
                isNeedRepaint = true;
                break;
        }
        if (isNeedRepaint) {
            reportPane.repaint();
        }
    }

    /**
     * 单选中悬浮元素时，只处理4个方向键
     *
     * @param reportPane
     * @param code
     */
    private void dealWithFloatSelection(ElementCasePane reportPane, int code) {
        boolean isContentChanged = false;
        FloatSelection floatselection = (FloatSelection) reportPane.getSelection();

        switch (code) {
            case KeyEvent.VK_LEFT: {// left
                floatselection.moveLeft(reportPane);
                isContentChanged = true;
                break;
            }
            case KeyEvent.VK_RIGHT: {// right
                floatselection.moveRight(reportPane);
                isContentChanged = true;
                break;
            }
            case KeyEvent.VK_UP: {// up
                floatselection.moveUp(reportPane);
                isContentChanged = true;
                break;
            }
            case KeyEvent.VK_DOWN: {// down
                floatselection.moveDown(reportPane);
                isContentChanged = true;
                break;
            }
        }

        if (isContentChanged) {
            grid.getElementCasePane().repaint();
            this.isKeyPressedContentChanged = true;
        }
    }

    private void dealWithCellSelection(KeyEvent evt, int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE: {
                if (grid.isCellEditing()) {
                    grid.cancelEditing();
                }
                break;
            }
            case KeyEvent.VK_F2: {
                if (!grid.isCellEditing()) {
                    grid.startEditing();
                }

                break;
            }
        }

        // 支持小键盘
        if (IS_NUM_PAD_KEY(code)) {
            keyTyped(evt);
        }
    }

    public void keyReleased(KeyEvent evt) {
        if (!grid.isEnabled() || evt.isConsumed()) {
            return;
        }
        KeyEvent newEvt = KeyEventWork.processKeyEvent(evt);
        if (newEvt == null) {
            return;
        }

        if (this.isKeyPressedContentChanged) {
            grid.getElementCasePane().fireTargetModified();

            this.isKeyPressedContentChanged = false;
        }
    }

    public void keyTyped(KeyEvent evt) {
        if (!grid.isEnabled() || evt.isConsumed()) {
            return;
        }
        KeyEvent newEvt = KeyEventWork.processKeyEvent(evt);
        if (newEvt == null || InputEventBaseOnOS.isControlDown(evt)) {// uneditable.
            return;
        }
        char ch = evt.getKeyChar();
        if (ch == KeyEvent.VK_TAB) {// 禁止Tab键.
            return;
        }
        int code = evt.getKeyCode();
        if (Character.isDefined(ch)) {// VK_SUBTRACT小键盘的减号
            Selection s = grid.getElementCasePane().getSelection();
            if (s instanceof CellSelection) {
                if (!grid.getElementCasePane().isSelectedOneCell()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if (!grid.isCellEditing()) {
                    grid.startEditing(true);
                }

                if (grid.getCellEditor() != null && grid.editorComponent != null) {
                    if (IS_NUM_PAD_KEY(code)) {
                        KeyEvent ke = new KeyEvent(grid, KeyEvent.KEY_PRESSED, 0, 0, code - DIFF, ch);
                        grid.editorComponent.dispatchEvent(ke);
                        ke.consume();
                    } else {
                        if (!evt.isConsumed()) {
                            grid.editorComponent.dispatchEvent(evt);
                        }
                    }
                }
            }
        }
    }

    /**
     * 小键盘
     *
     * @param code
     * @return
     */
    private static boolean IS_NUM_PAD_KEY(int code) {
        return code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_NUMPAD1
                || code == KeyEvent.VK_NUMPAD2
                || code == KeyEvent.VK_NUMPAD3
                || code == KeyEvent.VK_NUMPAD4
                || code == KeyEvent.VK_NUMPAD5
                || code == KeyEvent.VK_NUMPAD6
                || code == KeyEvent.VK_NUMPAD7
                || code == KeyEvent.VK_NUMPAD8
                || code == KeyEvent.VK_NUMPAD9
                || code == KeyEvent.VK_MULTIPLY
                || code == KeyEvent.VK_ADD
                || code == KeyEvent.VK_SUBTRACT
                || code == KeyEvent.VK_DECIMAL
                || code == KeyEvent.VK_DIVIDE;
    }
}