package com.fr.design.designer;

import javax.swing.*;

import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.DesignState;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.stable.StringUtils;
import com.fr.design.mainframe.*;

/**
 * 模板设计界面
 */
public abstract class TargetComponent<T> extends JComponent {
	private T target;

	public TargetComponent(T t) {
		this.target = t;
	}

	public TargetComponent() {}

	// TODO ALEX_SEP JWorkBook不想有copy, paste, cut的操作,怎么办?
	public abstract void copy();

	public abstract boolean paste();

	public abstract boolean cut();

	public abstract void stopEditing();

	public final T getTarget() {
		return target;
	}


	public abstract AuthorityEditPane createAuthorityEditPane();

	public abstract ToolBarMenuDockPlus getToolBarMenuDockPlus();

    public abstract int getMenuState();

	public abstract JPanel getEastUpPane();

	public abstract JPanel getEastDownPane();

	public abstract void cancelFormat();

	public void setTarget(T t) {
		if (t != null) {
			this.target = t;
		}
	}

	///////////////////////////////////////Event Listener//////////////

	/**
	 * Adds a <code>TargetModifiedListener</code> to the listener list.
	 */
	public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
		this.listenerList.add(TargetModifiedListener.class, targetModifiedListener);
	}

	/**
	 * Removes a <code>TargetModifiedListener</code> from the listener list.
	 */
	public void removeTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
		this.listenerList.remove(TargetModifiedListener.class, targetModifiedListener);
	}

	/**
	 * Fire template modified listeners.
	 */
	public void fireTargetModified() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TargetModifiedListener.class) {
				((TargetModifiedListener) listeners[i + 1]).targetModified(new TargetModifiedEvent(this));
			}
		}

		this.repaint(30);
	}

	///////////////////////////////////////ToolBarMenuDock//////////////
	public abstract ToolBarDef[] toolbars4Target();

	public abstract MenuDef[] menus4Target();

	public abstract ShortCut[] shortcut4TemplateMenu();

	public abstract ShortCut[] shortCuts4Authority();

	public abstract JComponent[] toolBarButton4Form();

	///////////////////////////////////////EditingState//////////////

	public EditingState createEditingState() {
		return EditingState.NULL;
	}

	public static final TargetComponent<String> NULLAVOID = new TargetComponent<String>(StringUtils.EMPTY) {

		@Override
		public void copy() {
		}

		@Override
		public boolean paste() {
			return false;
		}

        public  int getMenuState(){
            return DesignState.WORK_SHEET;
        }

		public void cancelFormat() {
			return;
		}

		@Override
		public boolean cut() {
			return false;
		}

		@Override
		public void stopEditing() {
		}

		public AuthorityEditPane createAuthorityEditPane() {
			return null;
		}

		public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
			return null;
		}

		@Override
		public ToolBarDef[] toolbars4Target() {
			return new ToolBarDef[0];
		}

		@Override
		public MenuDef[] menus4Target() {
			return new MenuDef[0];
		}

		@Override
		public ShortCut[] shortcut4TemplateMenu() {
			return new ShortCut[0];
		}

		public ShortCut[] shortCuts4Authority() {
			return new ShortCut[0];

		}

		public JComponent[] toolBarButton4Form() {
			return new JComponent[0];
		}

		public JPanel getEastUpPane() {
			return new JPanel();
		}

		public JPanel getEastDownPane() {
			return new JPanel();
		}

	};
}