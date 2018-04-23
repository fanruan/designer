package com.fr.grid;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.stable.ReportConstants;

public class GridKeyAction extends AbstractAction {
	private Grid grid;
	private String actionKey;
	private boolean isShift;

	public GridKeyAction(Grid grid, String actionKey, boolean isShift) {
		this.grid = grid;
		this.actionKey = actionKey;
		this.isShift = isShift;
	}

	/**
	 * 注册默认的InputMap和ActionMap.
	 */
	protected static void initGridInputActionMap(Grid grid) {
		InputMap inputMap = grid.getInputMap();
		ActionMap actionMap = grid.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		actionMap.put("left", new GridKeyAction(grid, "left", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK), "left_shift");
		actionMap.put("left_shift", new GridKeyAction(grid, "left", true));

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		actionMap.put("right", new GridKeyAction(grid, "right", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK), "right_shift");
		actionMap.put("right_shift", new GridKeyAction(grid, "right", true));

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		actionMap.put("up", new GridKeyAction(grid, "up", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK), "up_shift");
		actionMap.put("up_shift", new GridKeyAction(grid, "up", true));

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		actionMap.put("down", new GridKeyAction(grid, "down", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK), "down_shift");
		actionMap.put("down_shift", new GridKeyAction(grid, "down", true));

		// Enter + Tab
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
		actionMap.put("enter", new GridKeyAction(grid, "enter", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK), "enter_shift");
		actionMap.put("enter_shift", new GridKeyAction(grid, "enter", true));

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "tab");
		actionMap.put("tab", new GridKeyAction(grid, "tab", false));
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK), "tab_shift");
		actionMap.put("tab_shift", new GridKeyAction(grid, "tab", true));
	}

	public void actionPerformed(ActionEvent e) {
		if ("left".equals(actionKey)) {
			selectionMove(ReportConstants.MOVE_DIRECTION_LEFT);
		} else if ("right".equals(actionKey)) {
			selectionMove(ReportConstants.MOVE_DIRECTION_RIGHT);
		} else if ("up".equals(actionKey)) {
			selectionMove(ReportConstants.MOVE_DIRECTION_UP);
		} else if ("down".equals(actionKey)) {
			selectionMove(ReportConstants.MOVE_DIRECTION_DOWN);
		} else if ("enter".equals(actionKey)) {
			enterKeyPressed();
		} else if ("tab".equals(actionKey)) {
			tabKeyPressed();
		}
	}

	private void enterKeyPressed() {
		if (grid.isCellEditing()) {
			grid.stopEditing();
		}
		selectionMove(ReportConstants.MOVE_DIRECTION_DOWN);
	}

	private void tabKeyPressed() {
		if (grid.isCellEditing()) {
			grid.stopEditing();
		}
		selectionMove(ReportConstants.MOVE_DIRECTION_RIGHT);
	}

	/**
	 * Tab, Enter Move
	 */
	private void selectionMove(int direction) {
		ElementCasePane ePane = grid.getElementCasePane();

		if (direction == ReportConstants.MOVE_DIRECTION_DOWN) {
			ePane.getSelection().moveDown(ePane);
		} else if (direction == ReportConstants.MOVE_DIRECTION_RIGHT) {
			ePane.getSelection().moveRight(ePane);
		} else if (direction == ReportConstants.MOVE_DIRECTION_UP) {
			ePane.getSelection().moveUp(ePane);
		} else if (direction == ReportConstants.MOVE_DIRECTION_LEFT) {
			ePane.getSelection().moveLeft(ePane);
		}

		ePane.repaint();
	}

	private static abstract class Helper {
		public abstract int getCoordinate(Rectangle rect);

		public abstract int getSize(Rectangle rect);

		public abstract int getOppoCoordinate(Rectangle rect);

		public abstract int getOppoSize(Rectangle rect);

		public abstract Rectangle asRect(int coordinate, int oppoCoordinate, int size, int oppoSize);
	}

	private static abstract class VisibleHelper {
		public abstract void ensureVisible(ElementCasePane reportPane, Rectangle rect);
	}

	private static final Helper UNION_LEFT = new Helper() {
		@Override
		public int getCoordinate(Rectangle rect) {
			return rect.x;
		}

		@Override
		public int getSize(Rectangle rect) {
			return rect.width;
		}

		@Override
		public int getOppoCoordinate(Rectangle rect) {
			return rect.y;
		}

		@Override
		public int getOppoSize(Rectangle rect) {
			return rect.height;
		}

		@Override
		public Rectangle asRect(int coordinate, int oppoCoordinate, int size, int oppoSize) {
			return new Rectangle(coordinate, oppoCoordinate, size, oppoSize);
		}
	};
	private static final VisibleHelper LEFT = new VisibleHelper() {
		@Override
		public void ensureVisible(ElementCasePane reportPane, Rectangle rect) {
			reportPane.ensureColumnRowVisible(rect.x + rect.width - 1, rect.y);
		}
	};

	private static final Helper UNION_UP = new Helper() {
		@Override
		public int getCoordinate(Rectangle rect) {
			return UNION_LEFT.getOppoCoordinate(rect);
		}

		@Override
		public int getSize(Rectangle rect) {
			return UNION_LEFT.getOppoSize(rect);
		}

		@Override
		public int getOppoCoordinate(Rectangle rect) {
			return UNION_LEFT.getCoordinate(rect);
		}

		@Override
		public int getOppoSize(Rectangle rect) {
			return UNION_LEFT.getSize(rect);
		}

		@Override
		public Rectangle asRect(int coordinate, int oppoCoordinate, int size, int oppoSize) {
			return new Rectangle(oppoCoordinate, coordinate, oppoSize, size);
		}
	};
	private static final VisibleHelper UP = new VisibleHelper() {
		@Override
		public void ensureVisible(ElementCasePane reportPane, Rectangle rect) {
			reportPane.ensureColumnRowVisible(rect.x, rect.y + rect.height - 1);
		}
	};
}